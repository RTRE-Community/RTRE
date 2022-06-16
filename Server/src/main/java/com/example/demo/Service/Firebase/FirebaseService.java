package com.example.demo.Service.Firebase;

import com.example.demo.Object.Notification;
import com.google.api.client.json.Json;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    //hello

    private static String collectionName = "projectNotifications";
    public static ResponseEntity<String> getAllNotification(Long id) {
        try{
            Firestore dbFirestore = FirestoreClient.getFirestore();
            Query query = dbFirestore.collection(collectionName).whereEqualTo("userId", id);
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            JsonArray jsonArray = new JsonArray();
            JsonObject jsonObject = new JsonObject();
            ArrayList<String> notificationList = new ArrayList<>();
            for (int document = 0; document < documents.size(); document++) {
                jsonArray.add( documents.get(document).get("postId").toString());
            }

            jsonObject.add("postId", jsonArray);
            return new ResponseEntity<String>(jsonObject.toString(),HttpStatus.valueOf(200));
        } catch (ExecutionException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            return new ResponseEntity<String>("Internal server error, timed Out", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
  public static String postNotification(Notification notification) throws ExecutionException, InterruptedException {

            Firestore dbFirestore = FirestoreClient.getFirestore();
            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collectionName).document().set(notification);
            return collectionApiFuture.get().getUpdateTime().toString();

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

    public static String deleteNotification(String id) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection(collectionName).document(id).delete();
        return collectionApiFuture.get().getUpdateTime().toString();
    }

 */
}
