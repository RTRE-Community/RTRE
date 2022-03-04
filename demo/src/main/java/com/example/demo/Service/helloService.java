package com.example.demo.Service;

import org.springframework.stereotype.Service;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.PublicInterfaceNotFoundException;
import org.bimserver.shared.exceptions.ServiceException;


@Service
public class helloService {
    public static void getHello(){
        try {
            JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:8082");
            BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo("Email", "Password"));

            SProject newProject = client.getServiceInterface().addProject("TestFolder", "ifc2x3tc1");
            System.out.println(newProject.getOid());
        } catch (BimServerClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ChannelConnectionException e) {
            e.printStackTrace();
        } catch (PublicInterfaceNotFoundException e) {
            e.printStackTrace();
        }
    }
}
