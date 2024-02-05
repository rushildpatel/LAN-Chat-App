/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.*;

/**
 *
 * @author Keval Sanghvi
 */
public class JSONRead {
    
    // Default Fields and References
    String client1;
    String client2;
    String fileName;
    String wholeFileContents;
    String folder;
    ArrayList<String> messages;
    
    public JSONRead(String client1, String client2, String folder) {
        this.client1 = client1;
        this.client2 = client2;
        this.fileName = Helper.getFileName(client1, client2);
        this.folder = folder;
    }
    
    // This method is used to read from a JSON file.
    public String read() {
        String result = "";
        try {
            File file = new File("src\\" + folder + "\\backup\\" + fileName);
            if(!file.exists()) {
                return "";
            }
            FileReader in = new FileReader(file);
            int b;
            while((b=in.read())!=-1) {
                result += (char)b;
            }
        } catch(FileNotFoundException e) {
            System.out.println("Exception occured while reading a file! " + e);
        } catch(IOException e) {
            System.out.println("Exception occured while reading a file! " + e);
        } catch(Exception e) {
            System.out.println("Exception occured while reading a file! " + e);
        }
        return result;
    }
    
    public ArrayList<String> getMessageList() {
        String content = read();
        messages = new ArrayList<>();
        if(content.isEmpty()) {
            return messages;
        }
        String patternString = "(message:(.*?);)(sender:(.*?);)(reciever:(.*?);)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
            messages.add(matcher.group(2) + "-" + matcher.group(4));
        }
        return messages;
    }
}
