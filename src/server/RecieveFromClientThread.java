/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import core.JSONWrite;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author Keval Sanghvi
 */
public class RecieveFromClientThread extends Thread {
    
    // Default Fields and References
    Socket socket;
    String username;
    JSONWrite jsonWrite;
    HashMap<String, Socket> onlineUsers;
    ArrayList<String> messageOffline;
    
    RecieveFromClientThread(Socket socket, String username, HashMap<String, Socket> onlineUsers, ArrayList<String> messageOffline) {
        this.socket = socket;
        this.username = username;
        this.onlineUsers = onlineUsers;
        this.messageOffline = messageOffline;
        jsonWrite = new JSONWrite();
    }
    
    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            while(true) {
                String input = dis.readUTF();
                if(input.contains("==EXIT")) {
                    input = input.replace("==EXIT", "");
                    Server.removeUserFromHashMap(input);
                    Server.sendConnectionMessageToOtherUsers(username, 0);
                    break;
                } else {
                    String arr[] = input.split("==");
                    String otherUser = arr[0];
                    String message = arr[1];
                    if(onlineUsers.containsKey(otherUser)) {
                        new SendToClientThread(onlineUsers.get(otherUser), username + "==" + message).start();
                    } else {
                        messageOffline.add(otherUser + "==" + username);
                    }
                    jsonWrite.write(message, otherUser, username, "server", "recieve");
                }
            }
        } catch(IOException e) {
            System.out.println("An Error Occured At Server Side! " + e);
        }
    }
}
