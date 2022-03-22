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

    public static void installIfcFile(){
        try{
            JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:8082");
            BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));
            List<SProject> projectList = client.getServiceInterface().getAllProjects(true,true);
            SProject projectid = client.getServiceInterface().getProjectByPoid(projectList.get(0).getOid());
            System.out.println(projectid.getLastRevisionId());
            SSerializerPluginConfiguration serializer = client.getServiceInterface().getSerializerByName("Ifc2x3tc1");
            System.out.println(serializer.getOid());
            long id = 1966083;
            long topicId;
           topicId=  client.getServiceInterface().download(Collections.singleton(projectid.getLastRevisionId()),"{}",serializer.getOid(),false);
            System.out.println(serializer.getOid() + " "+ projectid.getLastRevisionId()+ " "+ projectid.getOid());
            System.out.println(topicId);
            InputStream is = client.getServiceInterface().getDownloadData(topicId).getFile().getInputStream();
            File targetFile = new File("Save directory + filename and .ifc");

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

