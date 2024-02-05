/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Keval Sanghvi
 */
public class MyFileWriter {
    
    // Default Fields and References
    String content;
    File file;

    MyFileWriter(File file, String content) {
        this.content = content;
        this.file = file;
    }
    
    void fileWrite() {
        try(FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException e) {
            System.out.println("Error! " + e);
        }
    }
}
