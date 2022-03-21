package com.example.demo.Controller;

import com.example.demo.Service.ifcPostService;
import com.example.demo.Service.ifcGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class IfcController {

    private final ifcPostService HelloService;

    @Autowired
    public IfcController(ifcPostService IfcGetService) {
        this.HelloService = IfcGetService;
    }

    @GetMapping("/postIfc")
    @ResponseBody
    public void getHello(@RequestParam String fileName){
        ifcPostService.postIfc(fileName);
    }

    @GetMapping("/getIfc")
    public void getIfc(){
        ifcGetService.installIfcFile();}
}
