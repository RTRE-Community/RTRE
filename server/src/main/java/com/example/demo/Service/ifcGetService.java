package com.example.demo.Service;

import com.example.demo.Controller.IfcController;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.bimserver.client.BimServerClient;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ifcGetService {



    public static void downloadIfc(Long fileName, String schema, HttpServletResponse response){
        try{
            // initialize "BimServer" client and authentication
            String tempPath = "C:\\Users\\Dennis\\Desktop\\Program\\RTRE\\Server\\src\\main\\resources\\BimServerInstallTempFolder\\";
            UUID name = UUID.randomUUID();
            // Select one project
            SProject project = IfcController.client.getServiceInterface().getProjectByPoid(fileName);
            // get the latest revision id from the project
            // Create a serializer for our configuration/Schema
            SSerializerPluginConfiguration serializer = IfcController.client.getServiceInterface().getSerializerByName(schema); //Ifc2x3tc1 or Ifc4
            // Start the download process and receive a topic id

            //Installation process
            long topicId =  IfcController.client.getServiceInterface().download(Collections.singleton(project.getLastRevisionId()),"{}",serializer.getOid(),false);
            // Use the topic id from "BimServer" which contains the file data to download it
            InputStream is = IfcController.client.getServiceInterface().getDownloadData(topicId).getFile().getInputStream();
            File targetFile = new File(tempPath+ name +".ifc");
            java.nio.file.Files.copy(
                    is,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            IOUtils.closeQuietly(is);
            System.out.println("Success");

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename="+ "\""+targetFile.getName() + "\"");
            response.setHeader("Content-Transfer-Encoding", "binary");

            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(targetFile.getAbsoluteFile());
            int len;
            byte[] buf = new byte[1024];
            while((len = fis.read(buf))> 0){
                bos.write(buf,0,len);
            }
            bos.close();
            response.flushBuffer();

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static String authGetAllProjects(BimServerClient client){
        try {

           List<SProject> data = client.getServiceInterface().getAllProjects(false,true);
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

