package com.example.demo.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/hello")
public class helloController {

    private final helloService HelloService;

    @Autowired
    public helloController(helloService HelloService) {
        this.HelloService = HelloService;
    }

    @GetMapping
    public String getHello(){
        return helloService.getHello();
    }
}
