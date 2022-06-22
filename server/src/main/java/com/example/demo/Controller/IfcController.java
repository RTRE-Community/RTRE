package com.example.demo.Controller;

import com.example.demo.Service.Firebase.FirebaseService;
import com.example.demo.Service.AdminManagement;
import com.example.demo.Service.AuthenticationService;
import com.example.demo.Service.IfcPostService;
import com.example.demo.Service.IfcGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class IfcController {

    private final IfcPostService HelloService;
    private final com.example.demo.Service.IfcMergeService IfcMergeService;


    @Autowired
    public IfcController(IfcPostService IfcGetService, com.example.demo.Service.IfcMergeService ifcMergeService) {
        this.HelloService = IfcGetService;
        IfcMergeService = ifcMergeService;
    }

    @PostMapping("/postIfcAsSubProject")
    @ResponseBody
    public ResponseEntity<String> postIfc(@RequestParam("file") MultipartFile file, String schema, Long parentPoid){
        return IfcPostService.postIfc(file, schema, parentPoid);
    }

    @GetMapping("/getIfc")
    @ResponseBody
    public ResponseEntity<String> getIfc(@RequestParam Long fileName, HttpServletResponse response){
        return IfcGetService.downloadIfc(fileName,response);}

    @GetMapping("/getProjectList")
    @ResponseBody
    public ResponseEntity<String> getProjectList(@RequestParam String token){
        return IfcGetService.authGetAllProjects(token);
    }

    @GetMapping("/deleteProject")
    @ResponseBody
    public ResponseEntity<String> deleteProject(@RequestParam Long oid){
        return IfcPostService.deleteProject(oid);
    }

    @PostMapping("/merge")
    @ResponseBody
    public ResponseEntity<String> merge(@RequestParam("file") MultipartFile file, long mergeFile2) {
        return com.example.demo.Service.IfcMergeService.mergeIfc(file, mergeFile2);}

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestParam String username,String password){
        return AuthenticationService.login(username,password);
    }

    @GetMapping("/AddUserToProject")
    @ResponseBody
    public ResponseEntity<String> AddUserToProject(@RequestParam Long parent0Id,String username, String token){
        return AdminManagement.addUser(parent0Id,username, token);
    }

    @GetMapping("/RemoveUserFromProject")
    @ResponseBody
    public ResponseEntity<String> RemoveUserFromProject(@RequestParam Long parent0Id,String username, String token){
        return AdminManagement.removeUserProject(parent0Id,username, token);
    }

    @GetMapping("/ViewUsers")
    @ResponseBody
    public ResponseEntity<String> ViewUsers(@RequestParam Long parent0Id, String token){
        return AdminManagement.ViewUsers(parent0Id, token);
    }

    @GetMapping("/getAllUsers")
    @ResponseBody
    public ResponseEntity<String> getAllUsers(@RequestParam String token){
        return AdminManagement.getAllUsers(token);
    }

    @GetMapping("/getAllUsers!Admin")
    @ResponseBody
    public ResponseEntity<String> getAllUsers_noAdmin(@RequestParam String token){
        return AdminManagement.getAllUsers_noAdmin(token);
    }

    @GetMapping("/CreateProject")
    @ResponseBody
    public ResponseEntity<String> createProject(@RequestParam String projectName, String schema, String token){
        return AdminManagement.createProject(projectName, schema, token);
    }


    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register (@RequestParam String emailUsername, String password, String name){
        return AuthenticationService.register(emailUsername, password,name);
    }

    @PostMapping("/sendMessage")
    @ResponseBody
    public ResponseEntity<String> sendMessage (@RequestParam String token, String message, Long from, Long to, String date){
        return FirebaseService.sendMessage(token, message, from, to, date);
    }

    @GetMapping("/getAllNotification")
    public ResponseEntity<String> getAllNotification(@RequestParam String username, String uuid){
        return FirebaseService.getAllNotification(username,uuid);
    }

    @GetMapping("/getUserMessages")
    public ResponseEntity<String> getUserMessages(@RequestParam String token, String username){
        return FirebaseService.getUserMessages(token, username);
    }

    @GetMapping("/readMessages")
    public ResponseEntity<String> readMessages(@RequestParam String token, String username, Long sender){
        return FirebaseService.readMessages(token, username, sender);
    }

    @DeleteMapping("/deleteNotification")
    public ResponseEntity<String> deleteNotification(@RequestParam String uuid,String username, Long postId){
        return FirebaseService.deleteNotification(uuid, username,postId);
    }

}

