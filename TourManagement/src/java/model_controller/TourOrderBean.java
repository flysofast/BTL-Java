/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import utils.ConfigDB;

/**
 *
 * @author USER
 */
@Named(value = "tourOrderBean")
@Dependent
public class TourOrderBean {
    private int orderID;
    private TourBean tourID;
    private CustomerBean customerID;
    private Date startDate;
    private Date orderDate;
    private String notes;
    private int status;

    public TourOrderBean(TourBean tourID, CustomerBean customerID, Date startDate, Date orderDate, String notes, int status) {
        this.tourID = tourID;
        this.customerID = customerID;
        this.startDate = startDate;
        this.orderDate = orderDate;
        this.notes = notes;
        this.status = status;
    }

    public TourOrderBean(int orderID, TourBean tourID, CustomerBean customerID, Date startDate, Date orderDate, String notes, int status) {
        this.orderID = orderID;
        this.tourID = tourID;
        this.customerID = customerID;
        this.startDate = startDate;
        this.orderDate = orderDate;
        this.notes = notes;
        this.status = status;
    }
    /**
     * Creates a new instance of TourOrderBean
     */
    public TourOrderBean() {
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public TourBean getTourID() {
        return tourID;
    }

    public void setTourID(TourBean tourID) {
        this.tourID = tourID;
    }

    public CustomerBean getCustomerID() {
        return customerID;
    }

    public void setCustomerID(CustomerBean customerID) {
        this.customerID = customerID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    /*----------------------------------------------------------------------------------------------------*/
    private final String sqlCreate = "INSERT INTO TourOrders VALUES(?, ?, ?, ?, ?, ?)";
    private final String sqlRead = "SELECT * FROM TourOrders";
    private final String sqlReadById = "SELECT * FROM TourOrders WHERE TourOrderID = ?";
    private final String sqlUpdate = "UPDATE TourOrders SET TourID = ?, CustomerID = ?, StartDate = ?, OrderDate = ?, Notes = ?, Status = ? WHERE TourOrderID = ?";
    private final String sqlDelete = "DELETE FROM TourOrders WHERE TourOrderID = ?";
    private Connection connect;
    private ConfigDB db = new ConfigDB();
    private ResultSet rs;
    private PreparedStatement pst;
    
    /**
     * Create new Tour Order
     */
    public boolean create() {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlCreate);
            pst.setInt(1, this.getTourID().getTourID());
            pst.setInt(2, this.getCustomerID().getCustomerID());
            pst.setDate(3, this.getStartDate());
            pst.setDate(4, this.getOrderDate());
            pst.setString(5, this.getNotes());
            pst.setInt(6, this.getStatus());
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
                Logger.getLogger(TourOrderBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * read all Tour Order in database
     */
    public ArrayList<TourOrderBean> readAll() {
        try {
            connect = db.getConection();
            ArrayList<TourOrderBean> tourOrderList = new ArrayList<TourOrderBean>();
            rs = connect.createStatement().executeQuery(sqlRead);
            while (rs.next()) {
                TourOrderBean tourOrder = new TourOrderBean();
                tourOrder.setOrderID(rs.getInt("OrderID"));
                tourOrder.setTourID(new TourBean().readById(rs.getInt("TourID")));
                tourOrder.setCustomerID(new CustomerBean().readById(rs.getInt("CustomerID")));
                tourOrder.setStartDate(rs.getDate("StartDate"));
                tourOrder.setOrderDate(rs.getDate("OrderDate"));
                tourOrder.setNotes(rs.getString("Notes"));
                tourOrder.setStatus(rs.getInt("Status"));
                tourOrderList.add(tourOrder);
            }
            return tourOrderList;
        } catch (SQLException ex) {
            Logger.getLogger(TourOrderBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourOrderBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * Read Tour Order base id
     */
    public TourOrderBean readById(int id) {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlReadById, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.first()) {
                TourOrderBean tourOrder = new TourOrderBean();
                tourOrder.setOrderID(rs.getInt("OrderID"));
                tourOrder.setTourID(new TourBean().readById(rs.getInt("TourID")));
                tourOrder.setCustomerID(new CustomerBean().readById(rs.getInt("CustomerID")));
                tourOrder.setStartDate(rs.getDate("StartDate"));
                tourOrder.setOrderDate(rs.getDate("OrderDate"));
                tourOrder.setNotes(rs.getString("Notes"));
                tourOrder.setStatus(rs.getInt("Status"));
                return tourOrder;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TourOrderBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourOrderBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public void editRedirect() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        TourOrderBean data = this.readById(id);
        this.orderID = id;
        this.tourID = data.getTourID();
        this.customerID = data.getCustomerID();
        this.startDate = data.getStartDate();
        this.orderDate = data.getOrderDate();
        this.status = data.getStatus();
    }
    
    /**
     * Update Tour Order base id
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
                rs.updateInt("TourID", this.getTourID().getTourID());
                rs.updateInt("CustomerID", this.getCustomerID().getCustomerID());
                rs.updateDate("StartDate", this.getStartDate());
                rs.updateDate("OrderDate", this.getOrderDate());
                rs.updateString("Notes", this.getNotes());
                rs.updateInt("Status", this.getStatus());
                rs.updateRow();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TourOrderBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourOrderBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * Delete Tour Order base id
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
            Logger.getLogger(TourOrderBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourOrderBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
