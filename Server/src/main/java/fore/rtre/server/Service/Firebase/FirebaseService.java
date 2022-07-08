package fore.rtre.server.Service.Firebase;

import fore.rtre.server.Main;
import fore.rtre.server.Object.Message;
import fore.rtre.server.Object.Notification;
import fore.rtre.server.config.BimserverConfig;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SUser;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.TokenAuthentication;
import org.bimserver.shared.exceptions.BimServerClientException;

import org.bimserver.shared.exceptions.PublicInterfaceNotFoundException;
import org.bimserver.shared.exceptions.ServerException;
import org.bimserver.shared.exceptions.ServiceException;
import org.bimserver.shared.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    //hello

    private static String collectionName = "projectNotifications";
    public static ResponseEntity<String> getAllNotification(String username, String uuid) {
        try{
            Firestore dbFirestore = FirestoreClient.getFirestore();
            String userOid = String.valueOf(BimserverConfig.client.getServiceInterface().getUserByUserName(username).getOid());
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
            Message m = new Message(message, String.valueOf(to), String.valueOf(from), date, false);
            Firestore dbFirestore = FirestoreClient.getFirestore();

            ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("Message").document().set(m);
            while(!(collectionApiFuture.isDone())){}

            return new ResponseEntity<String>(HttpStatus.valueOf(200));
            
        } catch (Exception e) {
            return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
    }


    public static ResponseEntity<String> getUserMessages(String token, String username) {

        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(Main.BimPort);
                    client = factory.create(new TokenAuthentication(token));
                    SUser user = client.getServiceInterface().getUserByUserName(username);
                    Long oid = user.getOid();

                    Firestore dbFirestore = FirestoreClient.getFirestore();

                    Query query = dbFirestore.collection("Message").whereEqualTo("to", String.valueOf(oid));
                    ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
                    QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
                    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
                    
                    ArrayList<Message> messages = new ArrayList<Message>();

                    for(int i = 0; i < documents.size(); i++){
                        if (documents.get(i).get("readMessage").toString().equals("false")) {
                            messages.add(new Message(documents.get(i).get("message").toString(), 
                                    documents.get(i).get("to").toString(), 
                                    documents.get(i).get("from").toString(),
                                    documents.get(i).get("date").toString(),
                                    false
                                    ));
                        }
                    }
                    String result = new Gson().toJson(messages);

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
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<>("error" ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<String> readMessages(String token, String username, Long sender) {
        
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(Main.BimPort);
                    client = factory.create(new TokenAuthentication(token));
                    SUser user = client.getServiceInterface().getUserByUserName(username);
                    Long oid = user.getOid();

                    Firestore dbFirestore = FirestoreClient.getFirestore();
                    Query query = dbFirestore.collection("Message").whereEqualTo("to", String.valueOf(oid))
                                                                        .whereEqualTo("from", String.valueOf(sender));
                    ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
                    QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
                    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
                    
                    for(int i = 0; i < documents.size(); i++){
                        
                        if (documents.get(i).get("readMessage").toString().equals("false")) {
                            dbFirestore.collection("Message").document(documents.get(i).getId()).update("readMessage", true);
                         }
                    }
                    return new ResponseEntity<String>(HttpStatus.valueOf(200));
                                   
                    
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
        } catch (Exception e) {
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
                userOid = String.valueOf(BimserverConfig.client.getServiceInterface().getUserByUserName(username).getOid());
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


    public static String readFileAsString(String file) {

        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (Exception e) {
            return "Error reading JSON";
        }
        
    }

    public static ResponseEntity<String> getQuerys(String token, String queryName){
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(Main.BimPort);
                    client = factory.create(new TokenAuthentication(token));

                    String filePath = "./src/main/resources/Querys/"+queryName+".json";
                    String json = readFileAsString(filePath);
                    if(json.contains("Error")){
                        return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

                    }else {
                        String result = json;
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

    public static ResponseEntity<String> sendQuery(String token, String recieveingUser, String queryTopic, String Query){
        try {
            JsonBimServerClientFactory factory;
                    BimServerClient client;
                    factory = new JsonBimServerClientFactory(Main.BimPort);
                    client = factory.create(new TokenAuthentication(token));
                    String postId = "0";
                    Notification notification = new Notification(postId, false, recieveingUser, true, Query, queryTopic);
                    Firestore dbFirestore = FirestoreClient.getFirestore();

                    ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("QueryNotification").document().set(notification);
                    while(!(collectionApiFuture.isDone())){}
                    System.out.println(notification.toString());

                    return new ResponseEntity<String>(HttpStatus.OK);

                    
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

    public static ResponseEntity<String> getUserQuerys(String username, String oid) {
        try{
            Firestore dbFirestore = FirestoreClient.getFirestore();

            //String userOid = String.valueOf(BimserverConfig.client.getServiceInterface().getUserByUserName(username).getOid());

            //String completeId = userOid+ uuid;
            Query query = dbFirestore.collection("QueryNotification").whereEqualTo("userId", oid);
                                                               
            ApiFuture<QuerySnapshot> querySnapshotApiFuture = query.get();
            QuerySnapshot querySnapshot = querySnapshotApiFuture.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            ArrayList<Notification> queryList = new ArrayList<>();
            ArrayList<String> querysList = new ArrayList<>();
            for (int i = 0; i < documents.size(); i++) {
                /* 
                queryList.add(new Notification(
                                            documents.get(i).get("postId").toString(),
                                            false,
                                            documents.get(i).get("userId").toString(),
                                            true,
                                            documents.get(i).get("queryName").toString(),
                                            documents.get(i).get("queryTopic").toString()
                                            ));*/
                querysList.add(documents.get(i).get("queryTopic").toString());
            }
            System.out.println(queryList.toString());
            String result = new Gson().toJson(querysList);

            return new ResponseEntity<String>(result,HttpStatus.valueOf(200));
        } catch (ExecutionException e) {
            return new ResponseEntity<String>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            return new ResponseEntity<String>("Internal server error, timed Out", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (PublicInterfaceNotFoundException e) {
            return new ResponseEntity<String>("Not Found", HttpStatus.valueOf(404));
        }

    }

}
