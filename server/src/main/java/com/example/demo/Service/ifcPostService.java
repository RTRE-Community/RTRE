package com.example.demo.Service;

import com.example.demo.Controller.IfcController;
import org.bimserver.interfaces.objects.SDeserializerPluginConfiguration;
import org.bimserver.shared.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.bimserver.interfaces.objects.SProject;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Random;


@Service
public class ifcPostService {
    public static ResponseEntity.BodyBuilder postIfc(String fileName, String ifcPath, String schema, Long parentPoid, String projectName){
        try {

            String randomName = projectName + new Random().nextLong();

            SProject newProject = IfcController.client.getServiceInterface().addProjectAsSubProject(randomName, parentPoid,schema);

            long poid = newProject.getOid();
            String comment = "This is a comment";

            // This method is an easy way to find a compatible deserializer for the combination of the "ifc" file extension and this project. You can also get a specific deserializer if you want to.
            SDeserializerPluginConfiguration deserializer = IfcController.client.getServiceInterface().getSuggestedDeserializerForExtension("ifc", poid);

            // Make sure you change this to a path to a local IFC file
            System.out.println(fileName);
            Path demoIfcFile = Paths.get(ifcPath+fileName);
            System.out.println(demoIfcFile);

            // Here we actually checkin the IFC file. Flow.SYNC indicates that we only want to continue the code-flow after the checkin has been completed
            IfcController.client.checkinSync(poid,comment,deserializer.getOid(),false,demoIfcFile);
        } catch (ServerException e) {
           return ResponseEntity.internalServerError();
        } catch (UserException e) {
            return ResponseEntity.badRequest();
        } catch (IOException e) {
            return ResponseEntity.internalServerError();
        }
        return ResponseEntity.ok();
    }

    public static ResponseEntity.BodyBuilder deleteProject(Long oid){

        try {
             IfcController.client.getServiceInterface().deleteProject(oid);
        } catch (ServerException e) {
            return ResponseEntity.internalServerError();
        } catch (UserException e) {
            return ResponseEntity.badRequest();
        }
        return ResponseEntity.ok();
    }
}
