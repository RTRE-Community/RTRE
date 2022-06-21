package com.example.demo.Service.Firebase;

import com.example.demo.DemoApplication;
import com.example.demo.Object.Message;
import com.example.demo.Object.Notification;
import com.example.demo.Object.User;
import com.example.demo.Controller.IfcController;
import com.example.demo.Object.Notification;
import com.google.api.client.json.Json;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.protobuf.ServiceException;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.TokenAuthentication;
import org.bimserver.shared.exceptions.BimServerClientException;

import org.bimserver.shared.Token;
import org.bimserver.shared.TokenAuthentication;
import org.bimserver.shared.exceptions.PublicInterfaceNotFoundException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    //hello

    private static String collectionName = "projectNotifications";
    public static ResponseEntity<String> getAllNotification(String username, String uuid) {
        try{
            Firestore dbFirestore = FirestoreClient.getFirestore();
            String userOid = String.valueOf(IfcController.client.getServiceInterface().getUserByUserName(username).getOid());
            String completeId = userOid+ uuid;
            Query query = dbFirestore.collection(collectionName).whereEqualTo("userId", completeId);
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            JsonArray jsonArray = new JsonArray();
            JsonObject jsonObject = new JsonObject();
            for (int document = 0; document < documents.size(); document++) {
                jsonArray.add( documents.get(document).get("postId").toString());
            }

            jsonObject.add("postId", jsonArray);
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.valueOf(200));
        } catch (ExecutionException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            return new ResponseEntity<String>("Internal server error, timed Out", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ServerException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (PublicInterfaceNotFoundException e) {
            return new ResponseEntity<String>("Not Found", HttpStatus.valueOf(404));
        }

    }
  public static String postNotification(Notification notification) throws ExecutionException, InterruptedException {

            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collectionName).document().set(notification);
            return collectionApiFuture.get().getUpdateTime().toString();

    }

    public static ResponseEntity<String> sendMessage(String token, String message, Long from, Long to, String date) {
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(DemoApplication.BimPort);
                    client = factory.create(new TokenAuthentication(token));

                    Message m = new Message(message, to, from, date);
                    Firestore dbFirestore = FirestoreClient.getFirestore();


                    ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("Message").document().set(m);
                    String result = collectionApiFuture.get().getUpdateTime().toString();


                    System.out.println(m + " " + result);

                    return new ResponseEntity<String>(result,HttpStatus.valueOf(200));
                                   
                    
        }  catch (UserException e) {
            return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
        } catch (BimServerClientException e) {
            e.printStackTrace();
        }  catch (ChannelConnectionException e) {
            e.printStackTrace();
        } catch (org.bimserver.shared.exceptions.ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
    }


/*
    public static Notification getNotification(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection(collectionName).document(id);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        Notification notification = null;

        if(document.exists()){
            notification = document.toObject(Notification.class);
            return notification;
        }else{
            return null;
        }
    }

    public static String updateNotification(Notification notification) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collectionName).document(String.valueOf(notification.getUserId())).set(notification);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

 */
    public static ResponseEntity<String> deleteNotification(String uuid,String username, Long postId) {

            String userOid;
            try {
                userOid = String.valueOf(IfcController.client.getServiceInterface().getUserByUserName(username).getOid());
                String completeId = userOid + uuid;
                Firestore dbFirestore = FirestoreClient.getFirestore();
                CollectionReference notifications = dbFirestore.collection(collectionName);
                ApiFuture<QuerySnapshot> querySnapshot = notifications.whereEqualTo("userId",completeId)
                .whereEqualTo("postId", postId)
                .get();
                DocumentSnapshot document = querySnapshot.get().getDocuments().get(0);
                
                ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collectionName).document(document.getId()).delete();
                String results = collectionApiFuture.get().getUpdateTime().toString();
                return new ResponseEntity<String>(results, HttpStatus.OK);
            } catch (ServerException e) {
                return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (UserException e) {
                return new ResponseEntity<String>("Bad Request", HttpStatus.BAD_REQUEST);
            } catch (PublicInterfaceNotFoundException e) {
                return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (InterruptedException e) {
                return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (ExecutionException e) {
                return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }
}
