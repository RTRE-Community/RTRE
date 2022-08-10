package fore.rtre.server.Service;

import fore.rtre.server.Object.Notification;
import fore.rtre.server.Service.Firebase.FirebaseService;
import fore.rtre.server.config.BimserverConfig;
import org.bimserver.interfaces.objects.SDeserializerPluginConfiguration;
import org.bimserver.interfaces.objects.SLongActionState;
import org.bimserver.interfaces.objects.SUser;
import org.bimserver.shared.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.bimserver.interfaces.objects.SProject;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@Service
public class IfcPostService {
    public static ResponseEntity<String> postIfc(MultipartFile file, String schema, Long parentPoid, String queryName, String queryDescription){

        String name;
        String ifcSchema;

        try {

            if(schema.equals("empty")){
                String temp = BimserverConfig.client.getServiceInterface().getProjectByPoid(parentPoid).getSchema();
                ifcSchema = temp.substring(0,1).toUpperCase() + temp.substring(1);
            } else {
                ifcSchema = schema;
            }

            if (queryName.equals("empty")){
                UUID uniqueId = UUID.randomUUID();
                name = file.getName()+ "-"+ uniqueId;
            } else {
                UUID uniqueId = UUID.randomUUID();
                name = queryName + "-" + uniqueId;
            }

            if(!(ifcSchema.toLowerCase().equals(BimserverConfig.client.getServiceInterface().getProjectByPoid(parentPoid).getSchema()))){
                throw new IllegalArgumentException();
            }

            String relativeFolder = "src\\main\\resources\\BimServerInstallTempFolder\\";

            SProject newProject = BimserverConfig.client.getServiceInterface().addProjectAsSubProject(name, parentPoid, ifcSchema);
            long poid = newProject.getOid();
            File fileOfSubject = new File(relativeFolder + name + ".ifc");
            try {
                file.transferTo(fileOfSubject.getAbsoluteFile());
            } catch (IOException e) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            // This method is an easy way to find a compatible deserializer for the combination of the "ifc" file extension and this project. You can also get a specific deserializer if you want to.
            SDeserializerPluginConfiguration deserializer = BimserverConfig.client.getServiceInterface().getSuggestedDeserializerForExtension("ifc", poid);

            // Make sure you change this to a path to a local IFC file
            Path demoIfcFile = Paths.get(relativeFolder+name +".ifc");

            // Here we actually checkin the IFC file. Flow.SYNC indicates that we only want to continue the code-flow after the checkin has been completed
            SLongActionState state = BimserverConfig.client.checkinSync(poid,"",deserializer.getOid(),false,demoIfcFile);
            Files.delete(fileOfSubject.toPath());
            newProject.setDescription(queryDescription);
            BimserverConfig.client.getServiceInterface().updateProject(newProject);

            List<SUser> allUsers = BimserverConfig.client.getServiceInterface().getAllAuthorizedUsersOfProject(parentPoid);

            for(SUser sUser: allUsers){
                BimserverConfig.client.getServiceInterface().addUserToProject(sUser.getOid(),  newProject.getOid());
            }
            
            allUsers = BimserverConfig.client.getServiceInterface().getAllAuthorizedUsersOfProject(newProject.getOid());
        
            for (SUser sUser : allUsers) {
                String uuid = BimserverConfig.client.getServiceInterface().getUserByUoid(sUser.getOid()).getUuid().toString();
                Notification newPostNotification = new Notification(newProject.getOid(), false, String.valueOf(sUser.getOid()) + uuid);
                FirebaseService.postNotification(newPostNotification);
            }
            if(!state.getErrors().isEmpty()){
                return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (ServiceException | PublicInterfaceNotFoundException | IOException | IllegalArgumentException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);

        } catch (ExecutionException e) {
            return new ResponseEntity<String>("Internal server error, Firebase", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            return new ResponseEntity<String>("Internal server error, Firebase", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Success", HttpStatus.valueOf(200));
    }

    public static ResponseEntity<String> deleteProject(Long oid){

        try {
            BimserverConfig.client.getServiceInterface().deleteProject(oid);
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Success", HttpStatus.valueOf(200));
    }
}
