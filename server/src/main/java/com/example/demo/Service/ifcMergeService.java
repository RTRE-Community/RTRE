package com.example.demo.Service;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
@Service
public class ifcMergeService {
    public static void mergeIfc(String mergeFile1, String mergeFile2, String outputFile, String scriptPath, String ifcPath){

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