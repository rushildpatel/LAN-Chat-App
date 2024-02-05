package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Keval Sanghvi
 */
public class DBConnect {
    // Connection reference
    Connection conn;
    
    // returns a valid Connection object if it establishes connection with DB else returns null
    public static Connection connectDB() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lan_chat_app", "keval", "keval");
            return conn;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection Failed " + e);
            return null;
        }
    }
}