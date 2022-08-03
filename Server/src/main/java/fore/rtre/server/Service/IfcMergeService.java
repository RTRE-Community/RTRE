package fore.rtre.server.Service;
import fore.rtre.server.config.BimserverConfig;
import org.apache.commons.io.IOUtils;
import org.bimserver.interfaces.objects.SDeserializerPluginConfiguration;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.UUID;

@Service
public class IfcMergeService {

    public static ResponseEntity<String> mergeIfc(MultipartFile file, long mergeFile2){
        String scriptPath = "src/main/resources/script/";
        String tempFolderPath = "src/main/resources/MergeTemporaryFolder/";
        Runtime rt = Runtime.getRuntime();
        File dir = new File(scriptPath);

        UUID uuidFirst = UUID.randomUUID();
        UUID uuidSecond = UUID.randomUUID();
        UUID uuidProduct = UUID.randomUUID();

        String pathForFirstFile = tempFolderPath+uuidFirst +".ifc";
        String pathForSecondFile = tempFolderPath+uuidSecond+".ifc";
        String pathForProductFile = tempFolderPath+uuidProduct + ".ifc";

        try {
            /*INSTALL FILE THAT GOT UPLOADED*/
                File postedFile = new File(pathForFirstFile);
                file.transferTo(postedFile.getAbsoluteFile());
            /* GET THE SECOND FILE FROM BIMSERVER*/
            SProject secondMergeFile = BimserverConfig.client.getServiceInterface().getProjectByPoid(mergeFile2);
            String schema = secondMergeFile.getSchema().substring(0, 1).toUpperCase() + secondMergeFile.getSchema().substring(1);
            SSerializerPluginConfiguration serializer = BimserverConfig.client.getServiceInterface().getSerializerByName(schema);
            long secondTopicId = BimserverConfig.client.getServiceInterface().download(Collections.singleton(secondMergeFile.getLastRevisionId()), "{}", serializer.getOid(), false);
            InputStream secondInputStream = BimserverConfig.client.getServiceInterface().getDownloadData(secondTopicId).getFile().getInputStream();

            File createSecondFile = new File(pathForSecondFile);
            java.nio.file.Files.copy(
                    secondInputStream,
                    createSecondFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            IOUtils.closeQuietly(secondInputStream);
            /* CREATE OUTPUT FILE*/
            File outputFile = new File(pathForProductFile);
            outputFile.getAbsoluteFile().createNewFile();
            /* EXECUTE MERGE */
            Process pr = rt.exec(" python -m f"
                    +postedFile.getAbsoluteFile()+
                    " "+createSecondFile.getAbsoluteFile()+
                    " "+ outputFile.getAbsoluteFile(), null, dir);

            System.out.println("waiting...");
            pr.waitFor();
            System.out.println("done!");
            /* CREATE A CHECK-IN FOR THE NEW MERGED PRODUCT FILE*/
            long mergeParentOid = secondMergeFile.getParentId();
            SDeserializerPluginConfiguration deserializer = BimserverConfig.client.getServiceInterface().getSuggestedDeserializerForExtension("ifc", mergeParentOid);
            String nameOfDeserializer = deserializer.getName().replace(" (Streaming)","");
            SProject newProject = BimserverConfig.client.getServiceInterface().addProjectAsSubProject(uuidProduct.toString(), mergeParentOid,nameOfDeserializer );
            System.out.println(newProject.getOid() + newProject.getName()+ newProject.getSchema());
            BimserverConfig.client.checkinSync(newProject.getOid(), "", deserializer.getOid(), false, outputFile.toPath());
            System.out.println("done Checking in");


            /*DELETE FILES*/
            File uploaded = new File(pathForFirstFile);
            File local = new File(pathForSecondFile);
            File output = new File(pathForProductFile);
            Files.delete(uploaded.toPath());
            Files.delete(local.toPath());
            Files.delete(output.toPath());
            return new ResponseEntity<String>("Success", HttpStatus.valueOf(200));

        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            return new ResponseEntity<String>(errors.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}