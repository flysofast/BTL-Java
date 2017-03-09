/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_controller;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import utils.ConfigDB;

/**
 *
 * @author USER
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    private int userID;
    private String username;
    private String password;
    private int RoleNo;
    private int status;
    private UserBean selectedUser;

    public UserBean getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserBean selectedUser) {
        this.selectedUser = selectedUser;
    }
    
    public UserBean(String username, String password, int RoleNo, int status) {
        this.username = username;
        this.password = password;
        this.RoleNo = RoleNo;
        this.status = status;
    }

    public UserBean(int userID, String username, String password, int RoleNo, int status) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.RoleNo = RoleNo;
        this.status = status;
    }
    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleNo() {
        return RoleNo;
    }

    public void setRoleNo(int RoleNo) {
        this.RoleNo = RoleNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    /*----------------------------------------------------------------------------------------------------*/
    private final String sqlCreate = "INSERT INTO Users VALUES(?, ?, ?, ?)";
    private final String sqlRead = "SELECT * FROM Users";
    private final String sqlReadById = "SELECT * FROM Users WHERE UserID = ?";
    private final String sqlUpdate = "SELECT * FROM Users WHERE UserID = ?";
    private final String sqlDelete = "DELETE FROM Users WHERE UserID = ?";
    private Connection connect;
    private ConfigDB db = new ConfigDB();
    private ResultSet rs;
    private PreparedStatement pst;
    
    /**
     * Create new User
     */
    public boolean create() {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlCreate);
            pst.setString(1, this.getUsername());
            pst.setString(2, this.getPassword());
            pst.setInt(3, this.getRoleNo());
            pst.setInt(4, this.getStatus());
            if (pst.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            return false;
        } finally {
            try {
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * read all User in database
     */
    public ArrayList<UserBean> readAll() {
        try {
            connect = db.getConection();
            ArrayList<UserBean> userList = new ArrayList<UserBean>();
            rs = connect.createStatement().executeQuery(sqlRead);
            while (rs.next()) {
                UserBean user = new UserBean();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setRoleNo(rs.getInt("RoleNo"));
                user.setStatus(rs.getInt("Status"));
                userList.add(user);
            }
            return userList;
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * Read User base id
     */
    public UserBean readById(int id) {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlReadById, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.first()) {
                UserBean user = new UserBean();
                user.setUserID(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setRoleNo(rs.getInt("RoleNo"));
                user.setStatus(rs.getInt("Status"));
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    public void editRedirect() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        UserBean data = this.readById(id);
        this.userID = id;
        this.username = data.getUsername();
        this.password = data.getPassword();
        this.RoleNo = data.getRoleNo();
        this.status = data.getStatus();
    }
    
    /**
     * Update User base id
     */
    public boolean update() {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlUpdate, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if(rs.first()) {
                rs.updateString("Username", this.getUsername());
                rs.updateString("Password", this.getPassword());
                rs.updateInt("RoleNo", this.getRoleNo());
                rs.updateInt("Status", this.getStatus());
                rs.updateRow();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * Delete User base id
     */
    public boolean delete(int id) {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlDelete, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1, id);
            if (pst.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
