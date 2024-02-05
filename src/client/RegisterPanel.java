package client;

import client.ChatFrame;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import core.*;
import java.util.ArrayList;
import java.util.List;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Keval Sanghvi
 */
public class RegisterPanel extends javax.swing.JPanel {

    /**
     * Creates new form RegisterPanell
     */
    MainFrame mainFrame;
    public RegisterPanel(MainFrame mainFrame) {
        initComponents();
        this.mainFrame = mainFrame;
    }
    
    // This method sets the username and password field.
    public void setBlankText() {
        usernameField.setText("");
        passwordField.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        register = new javax.swing.JButton();
        usernameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();

        setBackground(new java.awt.Color(52, 170, 177));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Register");

        register.setBackground(new java.awt.Color(255, 255, 255));
        register.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        register.setText("Submit");
        register.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerActionPerformed(evt);
            }
        });

        usernameField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Enter Username: ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Enter Password: ");

        passwordField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(usernameField, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                            .addComponent(passwordField)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(register)))))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel1)
                .addGap(92, 92, 92)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(67, 67, 67)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                .addComponent(register)
                .addGap(40, 40, 40))
        );
    }// </editor-fold>//GEN-END:initComponents

    // This method checks if the username entered is unique or not.
    private boolean checkIfUsernameIsUnique(String username) {
        ResultSet rs = Database.select("SELECT username FROM users");
        try {
            while(rs.next()) {
                if(rs.getString(1).equals(username)) {
                    return false;
                }
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Exception : " + e, "Error", JOptionPane.OK_OPTION);
        }
        return true;
    }
    
    private void registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerActionPerformed
        // TODO add your handling code here:
        String username = usernameField.getText();
        char passwordArray[] = passwordField.getPassword();
        String password = String.valueOf(passwordArray);
        if(username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!");
            return;
        }
        if(password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty!");
            return;
        }
        if(password.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password should be of atleast 8 characters!");
            return;
        }
        boolean isUsernameUnique = checkIfUsernameIsUnique(username);
        if(!isUsernameUnique) {
            JOptionPane.showMessageDialog(this, "Username already taken!");
            return;
        }
        String hashedPassword = Hash.getHashValue(password);
        List<String> colName = new ArrayList<>();
        colName.add("username");
        colName.add("password");
        List<String> colVal = new ArrayList<>();
        colVal.add(username);
        colVal.add(hashedPassword);
        int result = Database.insert(colName, colVal, "users");
        if(result != -1) {
            JOptionPane.showMessageDialog(this, "User Registered Successfully!");
            mainFrame.dispose();
            new ChatFrame(username).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "User Registration Failed!");
        }
        setBlankText();
    }//GEN-LAST:event_registerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JButton register;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables
}
