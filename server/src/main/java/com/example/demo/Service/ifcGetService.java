package com.example.demo.Service;

import com.example.demo.Controller.IfcController;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.bimserver.client.BimServerClient;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ifcGetService {



    public static ResponseEntity<String> downloadIfc(Long fileName, HttpServletResponse response){
        try{
            // initialize "BimServer" client and authentication
            String relativePath = "src\\main\\resources\\BimServerInstallTempFolder\\";
            UUID uniqueName = UUID.randomUUID();
            // Select one project
            SProject project = IfcController.client.getServiceInterface().getProjectByPoid(fileName);
            // get the latest revision id from the project
            // Create a serializer for our configuration/Schema
            String schema = project.getSchema().substring(0, 1).toUpperCase() + project.getSchema().substring(1);
            SSerializerPluginConfiguration serializer = IfcController.client.getServiceInterface().getSerializerByName(schema); //Ifc2x3tc1 or Ifc4
            // Start the download process and receive a topic id

            //Installation process
            long topicId =  IfcController.client.getServiceInterface().download(Collections.singleton(project.getLastRevisionId()),"{}",serializer.getOid(),false);
            // Use the topic id from "BimServer" which contains the file data to download it
            InputStream is = IfcController.client.getServiceInterface().getDownloadData(topicId).getFile().getInputStream();
            File targetFile = new File(relativePath + uniqueName +".ifc");
            java.nio.file.Files.copy(
                    is,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            IOUtils.closeQuietly(is);
            System.out.println("Success");

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename="+ "\""+targetFile.getName() + "\"");
            response.setHeader("Content-Transfer-Encoding", "binary");

            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(targetFile.getAbsoluteFile());
            int len;
            byte[] buf = new byte[1024];
            while((len = fis.read(buf))> 0){
                bos.write(buf,0,len);
            }
            bos.close();
            response.flushBuffer();
            fis.close();
            Files.delete(targetFile.toPath());
            return new ResponseEntity<String>("Success", HttpStatus.valueOf(200));


        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<String> authGetAllProjects(BimServerClient client){
        try {
           List<SProject> data = client.getServiceInterface().getAllProjects(false,true);
           String result = new Gson().toJson(data);
            return new ResponseEntity<String>(result, HttpStatus.valueOf(200));
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

}

