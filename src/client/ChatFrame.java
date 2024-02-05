package client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Keval Sanghvi
 */
public class ChatFrame extends javax.swing.JFrame implements ActionListener {

    /**
     * Creates new form ChatFrame
     */
    
    // Default Fields and References
    String username;
    UserClient userClient;
    UsersPanel usersPanel;
    JTextField messageField;
    String otherUser;
    JPanel middlePanel;
    JScrollPane messagePanel;
    String opened;
    public ChatFrame(String username) {
        initComponents();
        this.username = username;
        userClient = new UserClient(username, this);
        usersPanel = new UsersPanel(username, userClient.getAllUsers(), this);
        usersPanel.setBounds(0, 0, 350, 700);
        add(usersPanel);
        
        // Window Closing Event, It sends messages to all other clients that it is disconnected.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                userClient.sendDisconnectionMessage();
                dispose();
                System.exit(0);
            }
        });
    }
    
    // This method repaints the UsersPanel.
    public void changeUsersPanel(HashMap<String, Integer> users) {
        remove(usersPanel);
        usersPanel = new UsersPanel(username, users, this);
        usersPanel.setBounds(0, 0, 350, 700);
        add(usersPanel);
    }
    
    // This method updates the ChatPanel.
    public void updateChatPanel(String username, int isActive) {
        opened = username;
        chatPanel.removeAll();
        otherUser = username;
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBorder(new EmptyBorder(15, 10, 10, 10));
        topPanel.setBackground(new Color(4, 89, 79));
        topPanel.setBounds(0, 0, 700, 75);
        JLabel userLabel = new JLabel("Chat with " + username);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Verdana", Font.PLAIN, 24));
        if(isActive == 1) {
            JPanel onlineStatus = new JPanel();
            onlineStatus.setBackground(new Color(37, 211, 102));
            onlineStatus.setPreferredSize(new Dimension(13, 13));
            topPanel.add(onlineStatus);
        }
        topPanel.add(userLabel);
        middlePanel = new JPanel();
        middlePanel.setBackground(new Color(18, 140, 126));
        messagePanel = new JScrollPane(middlePanel);
        messagePanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);  
        messagePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        messagePanel.setBounds(0, 75, 700, 525);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(4, 89, 79));
        bottomPanel.setBorder(new EmptyBorder(30, 25, 10, 10));
        bottomPanel.setBounds(0, 600, 700, 100);
        messageField = new JTextField(40);
        bottomPanel.add(messageField);
        JButton send = new JButton("Send");
        send.setCursor(new Cursor(Cursor.HAND_CURSOR));
        send.addActionListener(this);
        bottomPanel.add(send);
        chatPanel.add(topPanel);
        chatPanel.add(bottomPanel);
        chatPanel.add(messagePanel);
        chatPanel.updateUI();
    }
    
    public void updateMessagePanel(ArrayList<String> messages) {
        middlePanel.removeAll();
        if(messages.isEmpty()) {
            JLabel label = new JLabel("No Messages...");
            label.setFont(new Font("Verdana", Font.PLAIN, 24));
            label.setForeground(Color.WHITE);
            middlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 240));
            middlePanel.add(label);
            middlePanel.updateUI();
            return;
        }
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        middlePanel.setLayout(grid);
        int i = 0;
        for(String eachMessage: messages) {
            String clientAndMessage[] = eachMessage.split("-", 2);
            gbc.fill = GridBagConstraints.NONE;
            if(clientAndMessage[1].equals(username)) {
                gbc.anchor = GridBagConstraints.LINE_END;
                gbc.gridx = 1;
                gbc.gridy = i;
            } else {
                gbc.anchor = GridBagConstraints.LINE_START;
                gbc.gridx = 0;
                gbc.gridy = i;
            }
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.weightx = 1;
            i++;
            JPanel message = new JPanel();
            message.setBackground(Color.WHITE);
            message.add(new JLabel(clientAndMessage[0]));
            middlePanel.add(message, gbc);
        }
        middlePanel.updateUI();
        setScrollBarPosition();
    }
    
    // This method returns username of client.
    public String getUsername() {
        return username;
    }
    
    public void setScrollBarPosition() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = messagePanel.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }
    
    // This method sends message to server on click of the send button.
    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            String message = messageField.getText();
            if(message.equals("")) {
                return;
            }
            messageField.setText("");
            new SendToServerThread(userClient.getSocket(), otherUser, message).start();
        } catch (Exception e) {
            System.out.println("Exception while joining thread! " + e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chatPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("ChatApp");

        chatPanel.setBackground(new java.awt.Color(18, 140, 126));

        javax.swing.GroupLayout chatPanelLayout = new javax.swing.GroupLayout(chatPanel);
        chatPanel.setLayout(chatPanelLayout);
        chatPanelLayout.setHorizontalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        chatPanelLayout.setVerticalGroup(
            chatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 350, Short.MAX_VALUE)
                .addComponent(chatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel chatPanel;
    // End of variables declaration//GEN-END:variables
}