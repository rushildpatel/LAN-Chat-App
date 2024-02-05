/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Keval Sanghvi
 */
public class MyFile {
    
    // Default Fields and References
    Socket socket;
    
    public MyFile(Socket socket) {
        this.socket = socket;
    }
    
    public void receiveFileFormat(String username) {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String fileSize = dis.readUTF();
            String fileName = dis.readUTF();
            System.out.println(fileName);
            File file = new File("src\\client\\backup\\" + Helper.getNewFileName(username, fileName));
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[8192];
            int filesize = Integer.parseInt(fileSize);
            int read = 0;
            int totalRead = 0;
            int remaining = filesize;
            while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                totalRead += read;
                remaining -= read;
                fos.write(buffer, 0, read);
            }
            System.out.println("Recieved");
            fos.close();
        } catch(IOException e) {
            System.out.println("Exception while receiving file! " + e);
        }
    }
    
    public void sendFileFormat(String fileName) {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            File file = new File("src\\server\\backup\\" + fileName);
            FileInputStream fis = new FileInputStream(file);
            long fileSize = file.length();
            dos.writeUTF(fileSize + "");
            dos.writeUTF(fileName);
            byte[] buffer = new byte[8192];
            int read;
            while((read=fis.read(buffer)) > 0) {
                dos.write(buffer,0,read);
            }
            fis.close();
        } catch(IOException e) {
            System.out.println("Exception while sending file! " + e);
        }
    }
    
    public void sendFile(String fileName) {
        File file = new File("src\\server\\backup\\" + fileName);
        if(file.exists()) {
            MyFileReader fileReader = new MyFileReader(file);
            String content = fileReader.fileRead();
            try {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(fileName);
                dos.writeUTF(content);
            } catch(IOException e) {
                System.out.println("Exception while sending file! " + e);
            }
        }
    }
    
    public void receiveFile(String username) {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String fileName = dis.readUTF();
            String content = dis.readUTF();
            fileName = Helper.getNewFileName(username, fileName);
            File file = new File("src\\client\\backup\\" + fileName);
            file.createNewFile();
            MyFileWriter fileWriter = new MyFileWriter(file, content);
            fileWriter.fileWrite();
        } catch(IOException e) {
            System.out.println("Exception while receiving file! " + e);
        }
    }
}
