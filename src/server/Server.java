package server;

import core.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javax.swing.JOptionPane;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Keval Sanghvi
 */
public class Server {
    // PORT NUMBER
    private static final int PORT = 4567;
    // HashMap for storing all online users
    private static final HashMap<String, Socket> onlineUsers = new HashMap<>();
    // HashMap for storing all users in database
    private static final HashMap<String, Integer> allUsers = new HashMap<>();
    // ArrayList for storing senders and receivers that got message while they were offline
    private static final ArrayList<String> messageOffline = new ArrayList<>();
    
    //  main method
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            while(true) {
                Socket socket = server.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                String name = dis.readUTF();
                getAllUsers();
                allUsers.replace(name, 1);
                sendConnectionMessageToOtherUsers(name, 1);
                onlineUsers.put(name, socket);
                ObjectOutputStream oos = new ObjectOutputStream(dos);
                oos.writeObject(allUsers);
                int count = 0;
                Iterator itr = messageOffline.iterator();
                while(itr.hasNext()) {
                    String each = (String)itr.next();
                    if(each.contains(name)) {
                        count++;
                    }
                }
                dos.writeUTF(count + "");
                if(count > 0) {
                    itr = messageOffline.iterator();
                    while(itr.hasNext()) {
                        String each = (String)itr.next();
                        String []users = each.split("==");
                        if(each.contains(name)) {
                            String fileName = Helper.getOldFileName(users[0], users[1]);
                            File file = new File("src\\server\\backup\\" + fileName);
                            if(file.exists()) {
                                MyFile myFile = new MyFile(socket);
                                myFile.sendFile(fileName);
                                itr.remove();
                            }
                        }
                    }
                }
                new RecieveFromClientThread(socket, name, onlineUsers, messageOffline).start();
            }
        } catch(IOException e) {
            System.out.println("An Error Occured At Server Side! " + e);
        }
    }
    
    // This method sends message to other users about status of this user.
    public static void sendConnectionMessageToOtherUsers(String user, int active) {
        for(Map.Entry<String, Socket> username : onlineUsers.entrySet()) {
            String hashmap = "((";
            for(Map.Entry<String, Integer> singleUser : allUsers.entrySet()) {
                hashmap += (singleUser.getKey() + ":" + singleUser.getValue() + ",");
            }
            hashmap += "))";
            new SendToClientThread(username.getValue(), hashmap).start();
        }
    }
    
    // This method selects all users from database and stores in allUsers hashmap and if the user is online then the corresponding value is stored as 1 or else 0
    private static void getAllUsers() {
        ResultSet rs = Database.select("SELECT username from users");
        try {
            allUsers.clear();
            while(rs.next()) {
                String name = rs.getString(1);
                if(onlineUsers.containsKey(name)) {
                    allUsers.put(name, 1);
                } else {
                    allUsers.put(name, 0);
                }
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Exception : " + e, "Error", JOptionPane.OK_OPTION);
        }
    }
    
    // This method removes a specific username from onlineUsers hashmap and changes allUsers value to 0.
    public static void removeUserFromHashMap(String username) {
        onlineUsers.remove(username);
        allUsers.replace(username, 0);
    }
}
