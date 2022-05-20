package com.example.demo.UploadExample;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.io.File;
import java.io.IOException;

@Controller
public class SpringFileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload( @RequestParam("file") MultipartFile file){
        String fileName = file.getOriginalFilename();

        try {

            file.transferTo( new File( "C:\\Users\\Dennis\\Desktop\\Program\\RTRE\\Server\\src\\main\\resources\\MergeTemporaryFolder\\" + fileName));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok("File uploaded Successfully");
    }
}
