package com.example.demo.Service;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ifcGetService {

    public static void installIfcFile(){
        try{
            JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:8082");
            BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));
            List<SProject> projectList = client.getServiceInterface().getAllProjects(true,true);


            for (SProject sProject : projectList) {
                System.out.println(sProject.getOid());
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
