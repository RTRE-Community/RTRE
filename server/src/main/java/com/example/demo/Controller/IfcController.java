package com.example.demo.Controller;

import com.example.demo.Service.ifcMergeService;
import com.example.demo.Service.ifcPostService;
import com.example.demo.Service.ifcGetService;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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

    static public JsonBimServerClientFactory factory;
    static public BimServerClient client;

    {
        try {
            factory = new JsonBimServerClientFactory("http://localhost:8082");
            client = factory.create(new UsernamePasswordAuthenticationInfo("admin@admin.com", "password"));
        } catch (BimServerClientException | ServiceException | ChannelConnectionException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/postIfcAsSubProject")
    @ResponseBody
    public void postIfc(@RequestParam("file") MultipartFile file,String schema, Long parentPoid){
        ifcPostService.postIfc(file,schema, parentPoid);
    }

    @GetMapping("/getIfc")
    @ResponseBody
    public void getIfc(@RequestParam Long fileName, HttpServletResponse response){
        ifcGetService.downloadIfc(fileName,response);}

    @GetMapping("/getProjectList")
    @ResponseBody
    public String getProjectList(){
        System.out.println(ifcGetService.authGetAllProjects(client));
        return ifcGetService.authGetAllProjects(client);
    }

    @GetMapping("/deleteProject")
    @ResponseBody
    public void deleteProject(@RequestParam Long oid){
        ifcPostService.deleteProject(oid);
    }

    @PostMapping("/merge")
    @ResponseBody
    public  void merge(@RequestParam("file") MultipartFile file, long mergeFile2) {
        ifcMergeService.mergeIfc(file, mergeFile2);}




}

