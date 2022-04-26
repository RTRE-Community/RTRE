package com.example.demo.Service;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.ServiceException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

@Service
public class ifcGetService {
    
    static JsonBimServerClientFactory factory;
    static BimServerClient client;

    {
        try {
            factory = new JsonBimServerClientFactory("http://localhost:8082");
            client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));
        } catch (BimServerClientException | ServiceException | ChannelConnectionException e) {
            e.printStackTrace();
        }
    }


    public static void installIfcFile(Long fileName){
        try{
            // initialize "BimServer" client and authentication


            // Pre-requisite steps - Get all Projects (obs! further implementation needed to give user a list of all projects)
            List<SProject> projectList = client.getServiceInterface().getAllProjects(true,true);
            // Select one project
            SProject project = client.getServiceInterface().getProjectByPoid(fileName);
            // get the latest revision id from the project
            System.out.println(project.getLastRevisionId());
            // Create a serializer for our configuration/Schema
            SSerializerPluginConfiguration serializer = client.getServiceInterface().getSerializerByName("Ifc2x3tc1");
            // Start the download process and receive a topic id

            //Installation process
            long topicId =  client.getServiceInterface().download(Collections.singleton(project.getLastRevisionId()),"{}",serializer.getOid(),false);
            // Use the topic id from "BimServer" which contains the file data to download it
            InputStream is = client.getServiceInterface().getDownloadData(topicId).getFile().getInputStream();
            File targetFile = new File("C:\\Users\\Levan\\Documents\\RTRECOM\\ " + fileName +".ifc");
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

    public static String authGetAllProjects (){
        try {

           List<SProject> data = client.getServiceInterface().getAllProjects(true,true);
           String result = new Gson().toJson(data);
           return result;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (UserException e) {
            e.printStackTrace();
        }
        return null;
    }
}

