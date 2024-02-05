/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Keval Sanghvi
 */
public class SendToClientThread extends Thread {
    
    // Default Fields and References
    Socket socket;
    String message;
    
    public SendToClientThread(Socket socket, String message) {
        this.socket = socket;
        this.message = message;
    }
    
    @Override
    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(message);
        } catch(IOException e) {
            System.out.println("An Error Occured While Sending Message! " + e);
        }
    }
}
