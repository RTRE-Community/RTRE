package com.example.demo.Service;
import com.example.demo.Controller.IfcController;
import org.apache.commons.io.IOUtils;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
public class ifcMergeService {

    public static ResponseEntity.BodyBuilder mergeIfc(MultipartFile file, Long mergeFile2, String scriptPath, String tempFolderPath){
        if(file == null || Objects.isNull(mergeFile2)){
            return ResponseEntity.badRequest();
        }
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
            file.transferTo( new File(pathForFirstFile));


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
            ResponseEntity.internalServerError();
        } catch (UserException e) {
            ResponseEntity.badRequest();
        } catch (IOException e) {
            ResponseEntity.internalServerError();
        } catch (InterruptedException e) {
            ResponseEntity.status(408);
        } catch (MultipartException e) {
            ResponseEntity.badRequest();
        }

        return ResponseEntity.ok();
    }
}