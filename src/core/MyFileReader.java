/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author Keval Sanghvi
 */
public class MyFileReader {
    
    // Default Fields and References
    File file;

    MyFileReader(File file) {
        this.file = file;
    }

    String fileRead() {
        try(FileInputStream in = new FileInputStream(file)) {
            String result = "";
            int b;
            while((b=in.read())!=-1) {
                result += (char)b;
            }
            return result;
        } catch(Exception e) {
            System.out.println("Error! " + e);
        }
        return "";
    }
}

