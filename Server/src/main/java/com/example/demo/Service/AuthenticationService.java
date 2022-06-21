package com.example.demo.Service;

import com.example.demo.Controller.IfcController;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
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


import java.util.UUID;

@Service
public class AuthenticationService {

    public static ResponseEntity<String> login(String username, String password){

        // Take Email And password
        JsonBimServerClientFactory factory;
        BimServerClient client;
            try{
            factory = new JsonBimServerClientFactory("http://localhost:8082");
            client = factory.create(new UsernamePasswordAuthenticationInfo(username, password));
            SUser user = client.getServiceInterface().getUserByUserName(username);
            Long oid = user.getOid();
            String id = Long.toString(oid);
            JsonObject result = new JsonObject();
      
            result.addProperty("Token", client.getToken());
            result.addProperty("UserType", user.getUserType().toString());
            result.addProperty("oid", id);
          
            result.addProperty("uuid", user.getUuid().toString());
            return new ResponseEntity<String>(result.toString(),HttpStatus.valueOf(200));
        } catch (BimServerClientException e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ServiceException e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.valueOf(404));
        } catch (ChannelConnectionException e) {
            return new ResponseEntity<>("Couldn't connect to server", HttpStatus.valueOf(502));
        }

        // Use service interface to check if exist in database

        /*
        * If True --> send auth token to be stored in session storage
        * Else respond with status code
        * */
    }


    public static ResponseEntity<String> register(String emailUsername, String password, String name){
        // Take all the necessary data for register
        try {
            IfcController.client.getServiceInterface().addUserWithPassword(emailUsername, password, name, SUserType.USER, false, String.valueOf(UUID.randomUUID()));
            return new ResponseEntity<String>("ok", HttpStatus.valueOf(200));
        } catch (ServerException e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        }
        // send to bimserver for storing
        // send back httpstatus depending on status
    }

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

    }


