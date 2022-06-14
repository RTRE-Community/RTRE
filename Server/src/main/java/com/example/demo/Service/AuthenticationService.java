package com.example.demo.Service;

import com.example.demo.Controller.IfcController;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SUserType;
import org.bimserver.shared.ChannelConnectionException;
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
            String result = client.getToken();

            return new ResponseEntity<String>(result,HttpStatus.valueOf(200));
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

    }


