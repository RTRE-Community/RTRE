package com.example.demo.Service;
import com.example.demo.Controller.IfcController;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.UUID;

@Service
public class ifcMergeService {

    public static void mergeIfc(MultipartFile file, long mergeFile2, String scriptPath, String tempFolderPath){

        Runtime rt = Runtime.getRuntime();
        File dir = new File(scriptPath);

        UUID uuidFirst = UUID.randomUUID();
        UUID uuidSecond = UUID.randomUUID();
        UUID uuidProduct = UUID.randomUUID();

        String pathForFirstFile = tempFolderPath+uuidFirst +".ifc";
        String pathForSecondFile = tempFolderPath+uuidSecond+".ifc";
        String pathForProductFile = tempFolderPath+uuidProduct + ".ifc";

        /**
         * TODO
         * Take the upload service into merge service
         * Make the upload service change the name of the filename to a UUID
         * remove all inputs for mergeFile1 aka
         *          - firstmergefile, mergefile1, createdFirstFile, firstinputstream, pathforfirstfile
         *
         * TODO next step
         * is to have the respose be the outputfile
         * */

        try {
            SProject secondMergeFile = IfcController.client.getServiceInterface().getProjectByPoid(mergeFile2);
            String schema = secondMergeFile.getSchema().substring(0, 1).toUpperCase() + secondMergeFile.getSchema().substring(1);
            SSerializerPluginConfiguration serializer = IfcController.client.getServiceInterface().getSerializerByName(schema);
            long secondTopicId = IfcController.client.getServiceInterface().download(Collections.singleton(secondMergeFile.getLastRevisionId()), "{}", serializer.getOid(), false);
            InputStream secondInputStream = IfcController.client.getServiceInterface().getDownloadData(secondTopicId).getFile().getInputStream();

            System.out.println(uuidProduct + " this is the output file.");

            File createSecondFile = new File(pathForSecondFile);

            try {
                file.transferTo( new File(pathForFirstFile));
            } catch (IOException e) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            java.nio.file.Files.copy(
                    secondInputStream,
                    createSecondFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            IOUtils.closeQuietly(secondInputStream);


            Process pr = rt.exec(" python -m f.py "
                    +pathForFirstFile+
                    " "+pathForSecondFile+
                    " "+ pathForProductFile, null, dir);

            System.out.println("waiting...");
            pr.waitFor();
            System.out.println("done!");
            /* CREATE A CHECK-IN FOR THE NEW MERGED PRODUCT FILE*/
            long mergeParentOid = secondMergeFile.getParentId();
            UUID newProjectName = UUID.randomUUID();
            ifcPostService.postIfc(uuidProduct+".ifc",tempFolderPath,schema , mergeParentOid, String.valueOf(newProjectName));

            File uploaded = new File(pathForFirstFile);
            File local = new File(pathForSecondFile);
            File output = new File(pathForProductFile);

            Files.delete(uploaded.toPath());
            Files.delete(local.toPath());
            Files.delete(output.toPath());

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (UserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}