package com.example.demo.Controller;

import com.example.demo.Service.ifcMergeService;
import com.example.demo.Service.ifcPostService;
import com.example.demo.Service.ifcGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class IfcController {

    private final ifcPostService HelloService;
    private final ifcMergeService IfcMergeService;

    @Autowired
    public IfcController(ifcPostService IfcGetService, ifcMergeService ifcMergeService) {
        this.HelloService = IfcGetService;
        IfcMergeService = ifcMergeService;
    }

    @GetMapping("/postIfc")
    @ResponseBody
    public void postIfc(@RequestParam String fileName){
        ifcPostService.postIfc(fileName);
    }

    @GetMapping("/getIfc")
    @ResponseBody
    public void getIfc(@RequestParam Long fileName){
        ifcGetService.installIfcFile(fileName);}

    @GetMapping("/getProjectList")
    @ResponseBody
    public String getProjectList(){
        System.out.println(ifcGetService.authGetAllProjects());
        return ifcGetService.authGetAllProjects();
    }
    @GetMapping("/merge")
    @ResponseBody
    public  void merge(@RequestParam String mergeFile1, String mergeFile2, String outputFile){ ifcMergeService.mergeIfc(mergeFile1,mergeFile2,outputFile);}
}
