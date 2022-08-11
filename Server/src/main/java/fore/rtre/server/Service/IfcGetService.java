package fore.rtre.server.Service;

import fore.rtre.server.Main;
import fore.rtre.server.Object.Helper.sortByDate;
import fore.rtre.server.Object.SubProjectMeta;
import fore.rtre.server.config.BimserverConfig;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SSerializerPluginConfiguration;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.TokenAuthentication;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.ServiceException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class IfcGetService {



    public static ResponseEntity<String> downloadIfc(Long fileName, HttpServletResponse response, String query){
        try{
            // initialize "BimServer" client and authentication
            String relativePath = "src\\main\\resources\\BimServerInstallTempFolder\\";
            UUID uniqueName = UUID.randomUUID();
            // Select one project
            SProject project = BimserverConfig.client.getServiceInterface().getProjectByPoid(fileName);
            // get the latest revision id from the project
            // Create a serializer for our configuration/Schema
            String schema = project.getSchema().substring(0, 1).toUpperCase() + project.getSchema().substring(1);
            SSerializerPluginConfiguration serializer = BimserverConfig.client.getServiceInterface().getSerializerByName(schema); //Ifc2x3tc1 or Ifc4
            // Start the download process and receive a topic id

            //Installation process
            String downloadQuery = "";
            if(query.length() < 1){
                downloadQuery = "{}";
            }else {
                downloadQuery = query;
            }
            long topicId =  BimserverConfig.client.getServiceInterface().download(Collections.singleton(project.getLastRevisionId()),downloadQuery,serializer.getOid(),false);
            // Use the topic id from "BimServer" which contains the file data to download it
            InputStream is = BimserverConfig.client.getServiceInterface().getDownloadData(topicId).getFile().getInputStream();
            File targetFile = new File(relativePath + uniqueName +".ifc");
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
            fis.close();
            Files.delete(targetFile.toPath());
            return new ResponseEntity<String>("Success", HttpStatus.valueOf(200));


        } catch (ServerException | IOException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

    public static ResponseEntity<String> authGetAllProjects(String token){
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(Main.BimPort);
                    client = factory.create(new TokenAuthentication(token));
           List<SProject> data = client.getServiceInterface().getAllProjects(false,true);
           String result = new Gson().toJson(data);
            return new ResponseEntity<String>(result, HttpStatus.valueOf(200));
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (ChannelConnectionException | ServiceException | BimServerClientException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<String> authGetDateAndSubProject(String token, Long id){
        try {
            ArrayList<SubProjectMeta> subProjectMetaList = new ArrayList<>();
            JsonBimServerClientFactory factory;
            BimServerClient client;

            factory = new JsonBimServerClientFactory(Main.BimPort);
            client = factory.create(new TokenAuthentication(token));

            long parentId = client.getServiceInterface().getProjectByPoid(id).getParentId();
            List<SProject> allProjects = client.getServiceInterface().getAllProjects(false,true);

            for (int project = 0; project < allProjects.size(); project++) {
                if(client.getServiceInterface().getProjectByPoid(allProjects.get(project).getOid()).getParentId() == parentId){
                 subProjectMetaList.add(
                         new SubProjectMeta(allProjects.get(project).getOid(),
                                 allProjects.get(project).getCreatedDate(),
                                 allProjects.get(project).getName(),
                                 allProjects.get(project).getDescription()
                         )
                 );
                }
            }
            subProjectMetaList.sort(new sortByDate());
            String result = new Gson().toJson(subProjectMetaList);
            return new ResponseEntity<String>(result, HttpStatus.valueOf(200));


        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (BimServerClientException | ServiceException | ChannelConnectionException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

