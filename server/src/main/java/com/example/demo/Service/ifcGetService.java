package com.example.demo.Service;

import com.example.demo.Controller.IfcController;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.bimserver.client.BimServerClient;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@Service
public class ifcGetService {



    public static ResponseEntity.BodyBuilder downloadIfc(Long fileName, String ifcPATH, String schema, String localName){
        try{
            // initialize "BimServer" client and authentication

            // Select one project
            SProject project = IfcController.client.getServiceInterface().getProjectByPoid(fileName);
            // get the latest revision id from the project
            // Create a serializer for our configuration/Schema
            SSerializerPluginConfiguration serializer = IfcController.client.getServiceInterface().getSerializerByName(schema); //Ifc2x3tc1 or Ifc4
            // Start the download process and receive a topic id

            //Installation process
            long topicId =  IfcController.client.getServiceInterface().download(Collections.singleton(project.getLastRevisionId()),"{}",serializer.getOid(),false);
            // Use the topic id from "BimServer" which contains the file data to download it
            InputStream is = IfcController.client.getServiceInterface().getDownloadData(topicId).getFile().getInputStream();
            File targetFile = new File(ifcPATH+ localName +".ifc");
            java.nio.file.Files.copy(
                    is,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            IOUtils.closeQuietly(is);
            System.out.println("Success");


        } catch (ServerException e) {
            return ResponseEntity.internalServerError();
        } catch (UserException e) {
            return ResponseEntity.badRequest();
        } catch (IOException e) {
            return ResponseEntity.internalServerError();
        } catch (NullPointerException e){
            return ResponseEntity.internalServerError();
        }
        return ResponseEntity.ok();
    }

    public static String authGetAllProjects(BimServerClient client){
        try {

           List<SProject> data = client.getServiceInterface().getAllProjects(false,true);
           String result = new Gson().toJson(data);
           return result;
        } catch (ServerException e) {
            return String.valueOf(ResponseEntity.internalServerError());
        } catch (UserException e) {
            return String.valueOf(ResponseEntity.badRequest());
        }
    }

}

