/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Hungkhung
 */
public class ConfigDB {
    
    private Connection connect;
    private String url;
    private String user;
    private String pass;
    private String driver;

    public ConfigDB() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(System.getProperty("user.dir") + "\\ConfigDB.properties"));
            url = "jdbc:sqlserver://" + prop.getProperty("server") + ":" + prop.getProperty("port") + ";databaseName=" + prop.getProperty("databaseName");
            user = prop.getProperty("sqlUser");
            pass = prop.getProperty("sqlPass");
            driver = prop.getProperty("sqlDriver");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ConfigDB(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }
    
    public Connection getConection() {
        url = "jdbc:sqlserver://localhost:1433;databaseName=TourManagement";
        user = "sa";
        pass = "123456";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connect = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Không tải được driver của Microsoft SQL Server", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return connect;
    }
    
    public void closeConnection() {
        try {
            if(!connect.isClosed()) {
                connect.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfigDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
