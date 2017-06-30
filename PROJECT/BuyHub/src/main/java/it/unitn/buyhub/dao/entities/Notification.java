/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.entities;

import java.util.Date;

/**
 *
 * @author matteo
 */
public class Notification {

    private int id;
    private String description;
    private Date dateCreation;
    private boolean status;
    private User user;

    public Notification(int id, String description, Date dateCreation, boolean status, User user) {
        this.id = id;
        this.description = description;
        this.dateCreation = dateCreation;
        this.status = status;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public boolean isStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}