package com.example.demo.hello;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class helloService {
    public static List<String> getHello(){
        return List.of("Hello","World");
    }
}
