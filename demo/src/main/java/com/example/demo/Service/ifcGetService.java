package com.example.demo.Service;

import org.apache.commons.io.IOUtils;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.*;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@Service
public class ifcGetService {

    public static void installIfcFile(String fileName){
        try{
            // initialize "BimServer" client and authentication
            JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:8082");
            BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));

            // Pre-requisite steps - Get all Projects (obs! further implementation needed to give user a list of all projects)
            List<SProject> projectList = client.getServiceInterface().getAllProjects(true,true);
            // Select one project
            SProject project = client.getServiceInterface().getProjectByPoid(projectList.get(0).getOid());
            // get the latest revision id from the project
            System.out.println(project.getLastRevisionId());
            // Create a serializer for our configuration/Schema
            SSerializerPluginConfiguration serializer = client.getServiceInterface().getSerializerByName("Ifc2x3tc1");
            // Start the download process and receive a topic id

            //Installation process
            long topicId =  client.getServiceInterface().download(Collections.singleton(project.getLastRevisionId()),"{}",serializer.getOid(),false);
            // Use the topic id from "BimServer" which contains the file data to download it
            InputStream is = client.getServiceInterface().getDownloadData(topicId).getFile().getInputStream();
            File targetFile = new File("Save directory " + fileName +".ifc");
            java.nio.file.Files.copy(
                    is,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            IOUtils.closeQuietly(is);
            System.out.println("Success");


        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

