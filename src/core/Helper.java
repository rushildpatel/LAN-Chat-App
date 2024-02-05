/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Keval Sanghvi
 */
public class Helper {
    public static String getOldFileName(String client1, String client2) {
        String fileName;
        if(client1.compareToIgnoreCase(client2) > 0) {
            fileName = client2 + "-" + client1 + ".JSON";
        } else {
            fileName = client1 + "-" + client2 + ".JSON";
        }
        return fileName;
    }
    
    public static String getFileName(String client1, String client2) {
        String fileName = client1 + "-" + client2 + ".JSON";
        return fileName;
    }
    
    public static String getNewFileName(String username, String fileName) {
        fileName = fileName.replace(".JSON", "");
        fileName = fileName.replace(username, "");
        fileName = fileName.replace("-", "");
        fileName = username + "-" + fileName + ".JSON";
        return fileName;
    }
}
