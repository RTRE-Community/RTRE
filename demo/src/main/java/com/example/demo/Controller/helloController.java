package com.example.demo.Controller;

import com.example.demo.Service.helloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class helloController {

    private final helloService HelloService;

    @Autowired
    public helloController(helloService HelloService) {
        this.HelloService = HelloService;
    }

    @GetMapping("/hello")
    @ResponseBody
    public void getHello(@RequestParam String fileName){
        helloService.getHello(fileName);
    }
}
