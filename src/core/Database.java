/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.util.List;

/**
 *
 * @author Keval Sanghvi
 */
public class Database {
    // Connection reference
    private static final Connection conn;
    // static block that is called when Database class is first loaded in memory
    static {
        conn = DBConnect.connectDB();
    }
    
    // This method returns ResultSet object, the query passed as parameter is executed in DB
    public static ResultSet select(String customQuery) {
        try {
            PreparedStatement ps = conn.prepareStatement(customQuery);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Exception : " + e, "Error", JOptionPane.OK_OPTION);
            return null;
        }
    }
    
    // This method returns ResultSet object, the query passed as parameter is executed in DB and WHERE clause is used as field, operator & value is passed
    public static ResultSet select(String customQuery, String field, String operator, String value) {
        customQuery += " WHERE " + field + operator + value;
        try {
            PreparedStatement ps = conn.prepareStatement(customQuery);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Exception : " + e, "Error", JOptionPane.OK_OPTION);
            return null;
        }
    }
    
    // This method inserts into database, it takes column names and column values as List and also takes table name as string 
    public static int insert(List<String> colName, List<String> colVal, String tableName) throws NullPointerException, IllegalArgumentException {
        int id = -1;
        if(colName.isEmpty()) {
            throw new NullPointerException("Column Names cannot be empty!");
        }
        if(colVal.isEmpty()) {
            throw new NullPointerException("Column Values cannot be empty!");
        }
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + "(");
        for(int i = 0; i < colName.size(); i++) {
            query.append(colName.get(i)).append((i < colName.size() - 1) ? ", " : "");
        }
        query.append(") VALUES (");
        for(int i = 0; i < colVal.size(); i++) {
            query.append("?").append((i < colVal.size() - 1) ? ", " : "");
        }
        query.append(")");
        String finalQuery = query.toString();
        try {
            PreparedStatement ps = conn.prepareStatement(finalQuery, Statement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < colVal.size(); i++) {
                ps.setString((i+1), colVal.get(i));
            }
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id = rs.getInt(1);
            }
            return id;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Exception : " + e, "Error", JOptionPane.OK_OPTION);
            return -1;
        }
    }
    
    // This method deletes from database, it takes table name and column name and id of the row to delete
    public static boolean delete(String tableName, String conditionalColumnName, int id) {
        if(conditionalColumnName == null){
            throw new NullPointerException("Conditional Column Cannot be NULL");
        }
        if(id <= 0){
            throw new IllegalArgumentException("ID cannot be 0 or less!");
        }
        String query = "DELETE FROM " + tableName + " WHERE " + conditionalColumnName + " = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            int numRows = ps.executeUpdate();
            return numRows > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Exception : " + e, "Error", JOptionPane.OK_OPTION);
            return false;
        }
    }
    
    // This method updates a row in database
    public static boolean update(List<String> colName, List<String> colVal, String tableName, String conditionalColumnName, int id){
        if(conditionalColumnName == null){
            throw new NullPointerException("Conditional Column Cannot be NULL");
        }
        if(id <= 0){
            throw new IllegalArgumentException("ID cannot be 0 or less!");
        }
        if(colName.isEmpty()) {
            throw new NullPointerException("Column Names cannot be empty!");
        }
        if(colVal.isEmpty()) {
            throw new NullPointerException("Column Values cannot be empty!");
        }
        if(colName.size() != colVal.size()){
            throw new IllegalArgumentException("Column Count and Value Count Does Not Match!");
        }
        StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
        for(int i = 0; i < colName.size(); i++){
            query.append(colName.get(i)).append(" = ?").append((i < colVal.size() - 1) ? ", " : "");
        }
        query.append(" WHERE ").append(conditionalColumnName).append(" = ").append(id);
        String finalQuery = query.toString();
        try {
            PreparedStatement ps = conn.prepareStatement(finalQuery, Statement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < colVal.size(); i++){
                ps.setString((i+1), colVal.get(i));
            }
            int numRows = ps.executeUpdate();
            return numRows > 0;
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Exception : " + e, "Error", JOptionPane.OK_OPTION);
            return false;
        }
    }
 }