package fore.rtre.server.Service;

import fore.rtre.server.DemoApplication;
import fore.rtre.server.Helper_Classes.User;
import com.google.gson.Gson;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SUser;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.TokenAuthentication;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.ServiceException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminManagement {


    public static ResponseEntity<String> addUser(Long parent0Id, String username, String token){
        try {
            JsonBimServerClientFactory factory;
            BimServerClient client;
            factory = new JsonBimServerClientFactory(DemoApplication.BimPort);
            client = factory.create(new TokenAuthentication(token));
            SUser user = client.getServiceInterface().getUserByUserName(username);
            Long oid = user.getOid();
            boolean added = client.getServiceInterface().addUserToProject(oid, parent0Id);
            if(added){
                return new ResponseEntity<String>(HttpStatus.valueOf(200));
            } else {
                System.out.println("could not add user");
                return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
            }
                    
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (BimServerClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ChannelConnectionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<String> removeUserProject(Long parent0Id, String username, String token){
        try {
            JsonBimServerClientFactory factory;
            BimServerClient client;
            factory = new JsonBimServerClientFactory(DemoApplication.BimPort);
            client = factory.create(new TokenAuthentication(token));
            SUser user = client.getServiceInterface().getUserByUserName(username);
            Long oid = user.getOid();
            boolean added = client.getServiceInterface().removeUserFromProject(oid, parent0Id);
            if(added){
                return new ResponseEntity<String>(HttpStatus.valueOf(200));
            } else {
                System.out.println("could not add user");
                return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
            }
                    
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (BimServerClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ChannelConnectionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

    public static ResponseEntity<String> ViewUsers(Long parent0Id, String token){
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(DemoApplication.BimPort);
                    client = factory.create(new TokenAuthentication(token));

                    List<SUser> resultList = client.getServiceInterface().getAllAuthorizedUsersOfProject(parent0Id);
                    ArrayList<User> users = new ArrayList<User>();
                    for(int i = 0; i < resultList.size(); i++){
                        users.add(new User(resultList.get(i).getName(), resultList.get(i).getUsername(), resultList.get(i).getOid())); 
                    }
                    String result = new Gson().toJson(users);

                    if(result.length() > 0){
                        return new ResponseEntity<String>(result, HttpStatus.valueOf(200));
                    } else {
                        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
                    }                   
                    
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (BimServerClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ChannelConnectionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

    public static ResponseEntity<String> getAllUsers(String token){
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(DemoApplication.BimPort);
                    client = factory.create(new TokenAuthentication(token));

                    List<SUser> resultList = client.getServiceInterface().getAllUsers();
                    ArrayList<User> users = new ArrayList<User>();
                    if(!(resultList.size() > 0)){
                        System.out.println("Empty User list");
                        return new ResponseEntity<>("no users" ,HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    for(int i = 0; i < resultList.size(); i++){
                        users.add(new User(resultList.get(i).getName(), resultList.get(i).getUsername(), resultList.get(i).getOid())); 
                    }
                    String result = new Gson().toJson(users);

                    if(result.length() > 0){
                        return new ResponseEntity<String>(result, HttpStatus.valueOf(200));
                    } else {
                        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
                    }                   
                    
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (BimServerClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ChannelConnectionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

    public static ResponseEntity<String> getAllUsers_noAdmin(String token){
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(DemoApplication.BimPort);
                    client = factory.create(new TokenAuthentication(token));

                    List<SProject> projects = client.getServiceInterface().getAllProjects(false, false);
                    Long[] projectId = new Long[projects.size()];
                    for(int j = 0; j < projects.size(); j++){
                        projectId[j] =  projects.get(j).getOid();

                    }
                    ArrayList<List<SUser>> userList = new ArrayList<List<SUser>>();
                    for(int k = 0; k < projectId.length; k++){
                       userList.add(client.getServiceInterface().getAllAuthorizedUsersOfProject(projectId[k]));

                    }
                    if( !(userList.size() > 0) ){
                        System.out.println("Empty User list");
                        return new ResponseEntity<>("no users" ,HttpStatus.valueOf(204));
                    }else {
                        Set<User> users = new HashSet<>();
                        for(int i = 0; i < userList.size(); i++){
                            for(int s = 0; s < userList.get(i).size(); s++){
                                User newUser = new User(userList.get(i).get(s).getName(), 
                                userList.get(i).get(s).getUsername(), 
                                userList.get(i).get(s).getOid());
                                users.add(newUser);
                            }

                        }
                     
                        String result = new Gson().toJson(users);
                        System.out.println(result);
                        return new ResponseEntity<String>(result, HttpStatus.valueOf(200));
            
                    }
             
                    
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (BimServerClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ChannelConnectionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

    public static ResponseEntity<String> createProject(String projectName, String schema, String token){
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(DemoApplication.BimPort);
                    client = factory.create(new TokenAuthentication(token));

                    SProject newProject = client.getServiceInterface().addProject(projectName, schema);

                    String result = new Gson().toJson(newProject.toString());
                    System.out.println(result);

                    
                    return new ResponseEntity<String>(result, HttpStatus.valueOf(200));
                                   
                    
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (BimServerClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ChannelConnectionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
        
    }









}
