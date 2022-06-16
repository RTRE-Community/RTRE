package com.example.demo.Service;

import com.example.demo.Controller.IfcController;
import com.example.demo.Object.Notification;
import com.example.demo.Service.Firebase.FirebaseService;
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
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@Service
public class ifcPostService {
    public static ResponseEntity<String> postIfc(MultipartFile file, String schema, Long parentPoid){

        try {
            String relativeFolder = "src\\main\\resources\\BimServerInstallTempFolder\\";
            UUID uniqueId = UUID.randomUUID();
            String uniqueName = file.getName()+ "-"+ uniqueId;

            SProject newProject = IfcController.client.getServiceInterface().addProjectAsSubProject(uniqueName, parentPoid,schema);
            long poid = newProject.getOid();
            File fileOfSubject = new File(relativeFolder + uniqueName + ".ifc");
            try {
                file.transferTo(fileOfSubject.getAbsoluteFile());
            } catch (IOException e) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            // This method is an easy way to find a compatible deserializer for the combination of the "ifc" file extension and this project. You can also get a specific deserializer if you want to.
            SDeserializerPluginConfiguration deserializer = IfcController.client.getServiceInterface().getSuggestedDeserializerForExtension("ifc", poid);

            // Make sure you change this to a path to a local IFC file
            Path demoIfcFile = Paths.get(relativeFolder+uniqueName +".ifc");

            // Here we actually checkin the IFC file. Flow.SYNC indicates that we only want to continue the code-flow after the checkin has been completed
            SLongActionState state = IfcController.client.checkinSync(poid,"",deserializer.getOid(),false,demoIfcFile);
            Files.delete(fileOfSubject.toPath());

            List<SUser> allUsers = IfcController.client.getServiceInterface().getAllUsers();

            for (SUser sUser : allUsers) {
                Notification newPostNotification = new Notification(newProject.getOid(), false, sUser.getOid() + sUser.getUuid().toString());
                FirebaseService.postNotification(newPostNotification);
            }
            if(!state.getErrors().isEmpty()){
                return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (ServiceException | PublicInterfaceNotFoundException | IOException e) {
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
            IfcController.client.getServiceInterface().deleteProject(oid);
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Success", HttpStatus.valueOf(200));
    }
}
