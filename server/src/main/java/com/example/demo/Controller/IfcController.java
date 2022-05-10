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
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${my.IFCPATH}")
    private String ifcPATH;

    @Value("${my.SCRIPTPATH}")
    private String scriptPATH;

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

    @GetMapping("/postIfcAsSubProject")
    @ResponseBody
    public void postIfc(@RequestParam String fileName,String schema, Long parentPoid, String projectName){
        ifcPostService.postIfc(fileName,ifcPATH,schema, parentPoid,projectName);
    }

    @GetMapping("/getIfc")
    @ResponseBody
    public void getIfc(@RequestParam Long fileName, String schema){
        ifcGetService.installIfcFile(fileName,ifcPATH,schema);}

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

    @GetMapping("/merge")
    @ResponseBody
    public  void merge(@RequestParam String mergeFile1, String mergeFile2, String outputFile){
        ifcMergeService.mergeIfc(mergeFile1,mergeFile2,outputFile, scriptPATH,ifcPATH);}
}

