package com.example.demo.hello;

import org.springframework.stereotype.Service;

@Service
public class helloService {
    public static String getHello(){
        return "Hello World!";
    }
}
