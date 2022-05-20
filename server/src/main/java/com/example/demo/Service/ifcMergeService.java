package com.example.demo.Service;
import com.example.demo.Controller.IfcController;
import org.apache.commons.io.IOUtils;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.UUID;

@Service
public class ifcMergeService {
    public static void mergeIfc(long mergeFile1, long mergeFile2,String ifcSchema,String scriptPath, String tempFolderPath){

        Runtime rt = Runtime.getRuntime();
        File dir = new File(scriptPath);

        /**
         * TODO
         * Put in SpringFileUploadController function here
         * Make the Id for the file UUID as temporary
         * Replace firstMergeFile with the path of the uploaded file
         * wait for it to finish
         * Take uploaded with bimserver and merge
         * TODO send the outputfile back as response
         * Remove all the temp files
         * */

        try {
            SProject firstMergeFile = IfcController.client.getServiceInterface().getProjectByPoid(mergeFile1);
            SProject secondMergeFile = IfcController.client.getServiceInterface().getProjectByPoid(mergeFile2);

            SSerializerPluginConfiguration serializer = IfcController.client.getServiceInterface().getSerializerByName(ifcSchema);

            long firstTopicId = IfcController.client.getServiceInterface().download(Collections.singleton(firstMergeFile.getLastRevisionId()),"{}", serializer.getOid(),false);
            long secondTopicId = IfcController.client.getServiceInterface().download(Collections.singleton(secondMergeFile.getLastRevisionId()), "{}", serializer.getOid(), false);

            InputStream firstInputStream = IfcController.client.getServiceInterface().getDownloadData(firstTopicId).getFile().getInputStream();
            InputStream secondInputStream = IfcController.client.getServiceInterface().getDownloadData(secondTopicId).getFile().getInputStream();

            UUID uuidFirst = UUID.randomUUID();
            UUID uuidSecond = UUID.randomUUID();
            UUID uuidProduct = UUID.randomUUID();

            String PathForFirstFile = tempFolderPath+uuidFirst+".ifc";
            String PathForSecondFile = tempFolderPath+uuidSecond+".ifc";
            String PathForProductFile = tempFolderPath+uuidProduct.toString() + ".ifc";

            System.out.println(uuidProduct + " this is the output file.");

            File createFirstFile = new File(PathForFirstFile);
            File createSecondFile = new File(PathForSecondFile);

            java.nio.file.Files.copy(
                    firstInputStream,
                    createFirstFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            java.nio.file.Files.copy(
                    secondInputStream,
                    createSecondFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            IOUtils.closeQuietly(firstInputStream);
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