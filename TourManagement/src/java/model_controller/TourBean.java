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
@Named(value = "tourBean")
@SessionScoped
public class TourBean implements Serializable {
    private int tourID;
    private TourCateBean tourCateID;
    private String name;
    private String imageUrl;
    private float price;
    private String description;

    public TourBean(TourCateBean tourCateID, String name, String imageUrl, float price, String description) {
        this.tourCateID = tourCateID;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
    }

    public TourBean(int tourID, TourCateBean tourCateID, String name, String imageUrl, float price, String description) {
        this.tourID = tourID;
        this.tourCateID = tourCateID;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
    }
    /**
     * Creates a new instance of TourBean
     */
    public TourBean() {
    }

    public int getTourID() {
        return tourID;
    }

    public void setTourID(int tourID) {
        this.tourID = tourID;
    }

    public TourCateBean getTourCateID() {
        return tourCateID;
    }

    public void setTourCateID(TourCateBean tourCateID) {
        this.tourCateID = tourCateID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    /*----------------------------------------------------------------------------------------------------*/
    private final String sqlCreate = "INSERT INTO Tours VALUES(?, ?, ?, ?, ?)";
    private final String sqlRead = "SELECT * FROM Tours";
    private final String sqlReadById = "SELECT * FROM Tours WHERE TourID = ?";
    private final String sqlUpdate = "UPDATE Tours SET TourCateID = ?, Name = ?, ImageUrl = ?, Pricate = ?, Description = ? WHERE TourID = ?";
    private final String sqlDelete = "DELETE FROM Tours WHERE TourID = ?";
    private Connection connect;
    private ConfigDB db = new ConfigDB();
    private ResultSet rs;
    private PreparedStatement pst;
    
    /**
     * Create new Tour
     */
    public boolean create() {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlCreate);
            pst.setInt(1, this.getTourCateID().getTourCateID());
            pst.setString(2, this.getName());
            pst.setString(3, this.getImageUrl());
            pst.setFloat(4, this.getPrice());
            pst.setString(5, this.getDescription());
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
                Logger.getLogger(TourBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * read all Tour in database
     */
    public ArrayList<TourBean> readAll() {
        try {
            connect = db.getConection();
            ArrayList<TourBean> tourList = new ArrayList<TourBean>();
            rs = connect.createStatement().executeQuery(sqlRead);
            while (rs.next()) {
                TourBean tour = new TourBean();
                tour.setTourID(rs.getInt("TourID"));
                tour.setTourCateID(new TourCateBean().readById(rs.getInt("TourCateID")));
                tour.setName(rs.getString("Name"));
                tour.setImageUrl(rs.getString("ImageUrl"));
                tour.setPrice(rs.getFloat("Price"));
                tour.setDescription(rs.getString("Description"));
                tourList.add(tour);
            }
            return tourList;
        } catch (SQLException ex) {
            Logger.getLogger(TourBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * Read Tour base id
     */
    public TourBean readById(int id) {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlReadById, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.first()) {
                TourBean tour = new TourBean();
                tour.setTourID(rs.getInt("TourID"));
                tour.setTourCateID(new TourCateBean().readById(rs.getInt("TourCateID")));
                tour.setName(rs.getString("Name"));
                tour.setImageUrl(rs.getString("ImageUrl"));
                tour.setPrice(rs.getFloat("Price"));
                tour.setDescription(rs.getString("Description"));
                return tour;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TourBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    public void editRedirect() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        TourBean data = this.readById(id);
        this.tourID = id;
        this.tourCateID = data.getTourCateID();
        this.name = data.getName();
        this.imageUrl = data.getImageUrl();
        this.price = data.getPrice();
        this.description = data.getDescription();
    }
    
    /**
     * Update Tour base id
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
                rs.updateInt("TourCateID", this.getTourCateID().getTourCateID());
                rs.updateString("Name", this.getName());
                rs.updateString("ImageUrl", this.getImageUrl());
                rs.updateFloat("Price", this.getPrice());
                rs.updateString("Description", this.getDescription());
                rs.updateRow();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TourBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * Delete Tour base id
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
            Logger.getLogger(TourBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
