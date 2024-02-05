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
public class JSONWrite {
    
    // Default Fields and References
    String message;
    String reciever;
    String sender;
    
    // This method is used to write to a JSON file.
    public void write(String message, String client1, String client2, String folder, String sendOrRecieve) {
        try {
            String fileName = "server".equals(folder) ? Helper.getOldFileName(client1, client2) : Helper.getFileName(client1, client2);
            File file = new File("src\\" + folder + "\\backup\\" + fileName);
            file.createNewFile();
            String content = "";
            if(sendOrRecieve.equals("send")) {
                content = "{message:" + message + ";sender:" + client1 + ";reciever:" + client2 + ";}\n";
            } else if(sendOrRecieve.equals("recieve")) {
                content = "{message:" + message + ";sender:" + client2 + ";reciever:" + client1 + ";}\n";
            }
            FileWriter out = new FileWriter(file, true);
            out.append(content);
            out.flush();
        } catch (IOException e) {
            System.out.println("Exception occured while writing to a file! " + e);
        }
    }
}
