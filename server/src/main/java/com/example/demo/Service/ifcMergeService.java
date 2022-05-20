package com.example.demo.Service;
import com.example.demo.Controller.IfcController;
import org.apache.commons.io.IOUtils;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.UUID;

@Service
public class ifcMergeService {
    public static void mergeIfc(MultipartFile file, long mergeFile2, String ifcSchema, String scriptPath, String tempFolderPath){

        Runtime rt = Runtime.getRuntime();
        File dir = new File(scriptPath);

        UUID uuidFirst = UUID.randomUUID();
        UUID uuidSecond = UUID.randomUUID();
        UUID uuidProduct = UUID.randomUUID();

        String PathForFirstFile = "C:\\Users\\Dennis\\Desktop\\Program\\RTRE\\Server\\src\\main\\resources\\MergeTemporaryFolder\\" + uuidFirst +".ifc";
        String PathForSecondFile = tempFolderPath+uuidSecond+".ifc";
        String PathForProductFile = tempFolderPath+uuidProduct.toString() + ".ifc";

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
            SSerializerPluginConfiguration serializer = IfcController.client.getServiceInterface().getSerializerByName(ifcSchema);
            long secondTopicId = IfcController.client.getServiceInterface().download(Collections.singleton(secondMergeFile.getLastRevisionId()), "{}", serializer.getOid(), false);
            InputStream secondInputStream = IfcController.client.getServiceInterface().getDownloadData(secondTopicId).getFile().getInputStream();

            System.out.println(uuidProduct + " this is the output file.");

            File createSecondFile = new File(PathForSecondFile);

            try {
                file.transferTo( new File(PathForFirstFile));
            } catch (IOException e) {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            java.nio.file.Files.copy(
                    secondInputStream,
                    createSecondFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            IOUtils.closeQuietly(secondInputStream);

            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(PathForProductFile)
            );
            bw.write("ISO-10303-21;\n" +
                    "HEADER;\n" +
                    "FILE_DESCRIPTION ((''), '2;1');\n" +
                    "FILE_NAME ('', '2022-05-16T14:35:59', (''), (''), '', 'BIMserver', '');\n" +
                    "FILE_SCHEMA (('IFC4'));\n" +
                    "ENDSEC;");
            bw.close();

            Process pr = rt.exec(" python -m f.py "
                    +PathForFirstFile+
                    " "+PathForSecondFile+
                    " "+ PathForProductFile, null, dir);

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (UserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}