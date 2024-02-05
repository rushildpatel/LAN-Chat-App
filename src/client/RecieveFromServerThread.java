/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import core.JSONRead;
import core.JSONWrite;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author Keval Sanghvi
 */
public class RecieveFromServerThread extends Thread {
    
    // Default Fields and References
    Socket socket;
    ChatFrame chatFrame;
    JSONWrite jsonWrite;
    RecieveFromServerThread(Socket socket, ChatFrame chatFrame) {
        this.socket = socket;
        this.chatFrame = chatFrame;
        jsonWrite = new JSONWrite();
    }
    
    @Override
    public void run() {
        while(true) {
            try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String input = dis.readUTF();
                if(input.startsWith("((") && input.endsWith("))")) {
                    HashMap<String, Integer> users = new HashMap<>();
                    input = input.replace("((", "");
                    input = input.replace("))", "");
                    String[] arr = input.split(",");
                    for(String a: arr) {
                        String aa[] = a.split(":");
                        users.put(aa[0], Integer.parseInt(aa[1]));
                    }
                    chatFrame.changeUsersPanel(users);
                    if(!chatFrame.opened.isEmpty()) {
                        chatFrame.updateChatPanel(chatFrame.opened, users.get(chatFrame.opened));
                        JSONRead jsonRead = new JSONRead(chatFrame.getUsername(), chatFrame.opened, "client");
                        chatFrame.updateMessagePanel(jsonRead.getMessageList());
                    }
                } else if(input.contains("==")) {
                    String arr[] = input.split("==");
                    String otherUser = arr[0];
                    String message = arr[1];
                    jsonWrite.write(message, chatFrame.getUsername(), otherUser, "client", "recieve");
                    if(!chatFrame.opened.isEmpty()) {
                        JSONRead jsonRead = new JSONRead(chatFrame.getUsername(), chatFrame.opened, "client");
                        chatFrame.updateMessagePanel(jsonRead.getMessageList());
                    }
                }
            } catch(Exception e) {
                System.out.println("An Error Occured At Client Side! " + e);
            }
        }
    }
}
