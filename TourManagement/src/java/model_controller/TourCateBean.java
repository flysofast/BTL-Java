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
@Named(value = "tourCateBean")
@SessionScoped
public class TourCateBean implements Serializable {
    private int tourCateID;
    private String cateName;
    private String description;

    public TourCateBean(String cateName, String description) {
        this.cateName = cateName;
        this.description = description;
    }

    public TourCateBean(int tourCateID, String cateName, String description) {
        this.tourCateID = tourCateID;
        this.cateName = cateName;
        this.description = description;
    }
    /**
     * Creates a new instance of TourCateBean
     */
    public TourCateBean() {
    }

    public int getTourCateID() {
        return tourCateID;
    }

    public void setTourCateID(int tourCateID) {
        this.tourCateID = tourCateID;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    /*----------------------------------------------------------------------------------------------------*/
    private final String sqlCreate = "INSERT INTO TourCate VALUES(?, ?)";
    private final String sqlRead = "SELECT * FROM TourCate";
    private final String sqlReadById = "SELECT * FROM TourCate WHERE TourCateID = ?";
    private final String sqlUpdate = "UPDATE TourCate SET CateName = ?, Description = ? WHERE TourCateID = ?";
    private final String sqlDelete = "DELETE FROM TourCate WHERE TourCateID = ?";
    private Connection connect;
    private ConfigDB db = new ConfigDB();
    private ResultSet rs;
    private PreparedStatement pst;
    
    /**
     * Create new Tour Category
     */
    public boolean create() {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlCreate);
            pst.setString(1, this.getCateName());
            pst.setString(2, this.getDescription());
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
                Logger.getLogger(TourCateBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * read all Tour Category in database
     */
    public ArrayList<TourCateBean> readAll() {
        try {
            connect = db.getConection();
            ArrayList<TourCateBean> tourCateList = new ArrayList<TourCateBean>();
            rs = connect.createStatement().executeQuery(sqlRead);
            while (rs.next()) {
                TourCateBean tourCate = new TourCateBean();
                tourCate.setTourCateID(rs.getInt("TourCateID"));
                tourCate.setCateName(rs.getString("CateName"));
                tourCate.setDescription(rs.getString("Description"));
                tourCateList.add(tourCate);
            }
            return tourCateList;
        } catch (SQLException ex) {
            Logger.getLogger(TourCateBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourCateBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    /**
     * Read Tour Category base id
     */
    public TourCateBean readById(int id) {
        connect = db.getConection();
        try {
            pst = connect.prepareStatement(sqlReadById, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.first()) {
                TourCateBean tourCate = new TourCateBean();
                tourCate.setTourCateID(rs.getInt("TourCateID"));
                tourCate.setCateName(rs.getString("CateName"));
                tourCate.setDescription(rs.getString("Description"));
                return tourCate;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TourCateBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourCateBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public void editRedirect() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        int id = Integer.valueOf(request.getParameter("id"));
        TourCateBean data = this.readById(id);
        this.tourCateID = data.getTourCateID();
        this.cateName = data.getCateName();
        this.description = data.getDescription();
    }
    
    /**
     * Update Tour Category base id
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
                rs.updateString("CateName", this.getCateName());
                rs.updateString("Description", this.getDescription());
                rs.updateRow();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(TourCateBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                rs.close();
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourCateBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * Delete Tour Category base id
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
            Logger.getLogger(TourCateBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pst.close();
                db.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(TourCateBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
