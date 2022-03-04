package com.example.demo.Service;

import org.bimserver.interfaces.objects.SDeserializerPluginConfiguration;
import org.springframework.stereotype.Service;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.PublicInterfaceNotFoundException;
import org.bimserver.shared.exceptions.ServiceException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;


@Service
public class helloService {
    public static void getHello(){
        try {

            String randomName = "Random " + new Random().nextLong();
            JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:8082");
            BimServerClient client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "admin"));

            SProject newProject = client.getServiceInterface().addProject(randomName, "ifc2x3tc1");

            long poid = newProject.getOid();
            String comment = "This is a comment";

            // This method is an easy way to find a compatible deserializer for the combination of the "ifc" file extension and this project. You can also get a specific deserializer if you want to.
            SDeserializerPluginConfiguration deserializer = client.getServiceInterface().getSuggestedDeserializerForExtension("ifc", poid);

            // Make sure you change this to a path to a local IFC file
            Path demoIfcFile = Paths.get("C:\\Users\\Levan\\Documents\\RTRECOM\\IFCfilerevite.ifc");

            // Here we actually checkin the IFC file. Flow.SYNC indicates that we only want to continue the code-flow after the checkin has been completed
            client.checkin(poid, comment, deserializer.getOid(), false, true, demoIfcFile);
        } catch (BimServerClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ChannelConnectionException e) {
            e.printStackTrace();
        } catch (PublicInterfaceNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
