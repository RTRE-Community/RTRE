package com.example.demo.config;

import com.example.demo.DemoApplication;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class BimserverConfig {

    static public JsonBimServerClientFactory factory;
    static public BimServerClient client;


    @PostConstruct
    public void BimserverInit(){
        {
            try {
                factory = new JsonBimServerClientFactory(DemoApplication.BimPort);
                client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));
            } catch (BimServerClientException | ServiceException | ChannelConnectionException e) {
                e.printStackTrace();
            }
        }
    }
}
