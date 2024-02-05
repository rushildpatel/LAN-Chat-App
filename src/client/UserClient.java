package client;

import core.MyFile;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Keval Sanghvi
 */
public class UserClient {
    
    // Default Fields and References
    String username;
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    HashMap<String, Integer> users;
    static ChatFrame chatFrame;
    
    UserClient(String username, ChatFrame chatFrame) {
        this.username = username;
        UserClient.chatFrame = chatFrame;
        try {
            socket = new Socket("localhost", 4567);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF(username);
            ObjectInputStream ois = new ObjectInputStream(dis);
            users = (HashMap<String, Integer>)ois.readObject();
            String count = dis.readUTF();
            int countInt = Integer.parseInt(count);
            while(countInt > 0) {
                MyFile myFile = new MyFile(socket);
                myFile.receiveFile(username);
                countInt--;
            }
            new RecieveFromServerThread(socket, chatFrame).start();
        } catch(Exception e) {
            System.out.println("An Error Occured At Client Side! " + e);
        }
    }
    
    public void sendDisconnectionMessage() {
        new SendToServerThread(socket, username, "EXIT").start();
    } 
    
    public HashMap<String, Integer> getAllUsers() {
        return users;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
}
