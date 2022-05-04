package com.example.demo.Service;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
@Service
public class ifcMergeService {
    public static void mergeIfc(String mergeFile1, String mergeFile2, String outputFile, String scriptPath, String ifcPath){
        //http://localhost:8080/api/merge?mergeFile1=C:/Users/Dennis/Desktop/Program/BimServer/bolt.ifc&mergeFile2=C:/Users/Dennis/Desktop/Program/BimServer/stud.ifc&outputFile=C:/Users/Dennis/Desktop/Program/BimServer/default.ifc
        Runtime rt = Runtime.getRuntime();
        File dir = new File(scriptPath);


        try {
            Process pr = rt.exec(" python -m f.py "
                    +ifcPath+mergeFile1+
                    " "+ ifcPath+mergeFile2+
                    " "+ ifcPath+outputFile, null, dir);
            System.out.println("it worked!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}