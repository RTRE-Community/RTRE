package com.example.demo.Service;

import com.example.demo.Controller.IfcController;
import com.example.demo.Helper_Classes.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.interfaces.objects.SUser;
import org.bimserver.interfaces.objects.SUserType;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.TokenAuthentication;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.ServiceException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminManagement {


    public static ResponseEntity<String> addUser(Long parent0Id, String username, String token){
        try {
            JsonBimServerClientFactory factory;
            BimServerClient client;
            factory = new JsonBimServerClientFactory("http://localhost:8082");
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
            factory = new JsonBimServerClientFactory("http://localhost:8082");
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
                    factory = new JsonBimServerClientFactory("http://localhost:8082");
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
                    factory = new JsonBimServerClientFactory("http://localhost:8082");
                    client = factory.create(new TokenAuthentication(token));

                    List<SUser> resultList = client.getServiceInterface().getAllUsers();
                    ArrayList<User> users = new ArrayList<User>();
                    for(int i = 0; i < resultList.size(); i++){
                        users.add(new User(resultList.get(i).getName(), resultList.get(i).getUsername(), resultList.get(i).getOid())); 
                    }
                    String result = new Gson().toJson(users);
                    System.out.println(result);

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

    public static ResponseEntity<String> createProject(String projectName, String schema, String token){
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory("http://localhost:8082");
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
