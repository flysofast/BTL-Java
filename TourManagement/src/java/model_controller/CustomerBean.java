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
@Named(value = "customerBean")
@SessionScoped
public class CustomerBean implements Serializable {
    private int customerID;
    private UserBean userID;
    private String name;
    private String email;
    private String mobile;
    private String address;
    private String personalNumber;

    public CustomerBean(int customerID, UserBean userID, String name, String email, String mobile, String address, String personalNumber) {
        this.customerID = customerID;
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.personalNumber = personalNumber;
    }

    public CustomerBean(UserBean userID, String name, String email, String mobile, String address, String personalNumber) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.personalNumber = personalNumber;
    }
    /**
     * Creates a new instance of CustomerBean
     */
    public CustomerBean() {
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public UserBean getUserID() {
        return userID;
    }

    public void setUserID(UserBean userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }
    
    /*----------------------------------------------------------------------------------------------------*/
    private final String sqlCreate = "INSERT INTO Customers VALUES(?, ?, ?, ?, ?, ?)";
    private final String sqlRead = "SELECT * FROM Customers";
    private final String sqlReadById = "SELECT * FROM Users WHERE CustomerID = ?";
    private final String sqlUpdate = "UPDATE Customers SET UserID = ?, Name = ?, Email = ?, Mobile = ?, Address = ?, PersonalNumber = ? WHERE CustomerID = ?";
    private final String sqlDelete = "DELETE FROM Customers WHERE CustomerID = ?";
    private Connection connect;
    private ConfigDB db = new ConfigDB();;
    private ResultSet rs;
    private PreparedStatement pst;
    
    /**
     * Create new Customer
     */
    public boolean create() {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlCreate);
            pst.setInt(1, this.getUserID().getUserID());
            pst.setString(2, this.getName());
            pst.setString(3, this.getEmail());
            pst.setString(4, this.getMobile());
            pst.setString(5, this.getAddress());
            pst.setString(6, this.getPersonalNumber());
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
                Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * read all Customer in database
     */
    public ArrayList<CustomerBean> readAll() {
        try {
            connect = db.getConection();
            ArrayList<CustomerBean> cusList = new ArrayList<CustomerBean>();
            rs = connect.createStatement().executeQuery(sqlRead);
            while (rs.next()) {
                CustomerBean cus = new CustomerBean();
                cus.setCustomerID(rs.getInt("CustomerID"));
                cus.setUserID(new UserBean().readById(rs.getInt("UserID")));
                cus.setName(rs.getString("Name"));
                cus.setEmail(email);
                cus.setMobile(mobile);
                cus.setAddress(address);
                cus.setPersonalNumber(personalNumber);
                cusList.add(cus);
            }
            return cusList;
        } catch (SQLException ex) {
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * Read Customer base id
     */
    public CustomerBean readById(int id) {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlReadById, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.first()) {
                CustomerBean customer = new CustomerBean();
                customer.setCustomerID(rs.getInt("CustomerID"));
                customer.setUserID(new UserBean().readById(rs.getInt("UserID")));
                customer.setName(rs.getString("Name"));
                customer.setEmail(rs.getString("Email"));
                customer.setMobile(rs.getString("Mobile"));
                customer.setAddress(rs.getString("Address"));
                customer.setPersonalNumber(rs.getString("PersonalNumber"));
                return customer;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    public void editRedirect() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        CustomerBean data = this.readById(id);
        this.customerID = id;
        this.userID = data.getUserID();
        this.name = data.getName();
        this.email = data.getEmail();
        this.mobile = data.getMobile();
        this.address = data.getAddress();
        this.personalNumber = data.getPersonalNumber();
    }
    
    /**
     * Update Customer base id
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
                rs.updateInt("UserID", this.getUserID().getUserID());
                rs.updateString("Name", this.getName());
                rs.updateString("Email", this.getEmail());
                rs.updateString("Mobile", this.getMobile());
                rs.updateString("Address", this.getAddress());
                rs.updateString("PersonalNumber", this.getPersonalNumber());
                rs.updateRow();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * Delete TourBean base id
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
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
