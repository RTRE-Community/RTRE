package com.example.demo.Controller;

import com.example.demo.Service.helloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class helloController {

    private final helloService HelloService;

    @Autowired
    public helloController(helloService HelloService) {
        this.HelloService = HelloService;
    }

    @GetMapping("/hello")
    public void getHello(){
        helloService.getHello();
    }
}
