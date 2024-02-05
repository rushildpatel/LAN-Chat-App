package client;

import core.JSONRead;
import core.JSONWrite;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Keval Sanghvi
 */
public class SendToServerThread extends Thread {
    
    // Default Fields and References
    Socket socket;
    String username;
    String message;
    JSONWrite jsonWrite;
    
    public SendToServerThread(Socket socket, String username, String message) {
        this.socket = socket;
        this.username = username;
        this.message = message;
        jsonWrite = new JSONWrite();
    }
    
    @Override
    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(username + "==" + message);
            if(!message.contains("EXIT")) {
                jsonWrite.write(message, UserClient.chatFrame.getUsername(), username, "client", "send");
                JSONRead jsonRead = new JSONRead(UserClient.chatFrame.getUsername(), username, "client");
                UserClient.chatFrame.updateMessagePanel(jsonRead.getMessageList());
            }
        } catch(IOException e) {
            System.out.println("An Error Occured While Sending Message! " + e);
        }
    }
}
