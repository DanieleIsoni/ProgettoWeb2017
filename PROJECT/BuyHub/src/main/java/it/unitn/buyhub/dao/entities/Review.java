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
public class Review {
    private int id;
    private Product product;
    private User creator;
    private int globalValue;
    private int quality;
    private int service;
    private int ValueForMoney;
    private String title;
    private String description;
    private Date dateCreation;

    public void setId(int id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setGlobalValue(int globalValue) {
        this.globalValue = globalValue;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public void setService(int service) {
        this.service = service;
    }

    public void setValueForMoney(int ValueForMoney) {
        this.ValueForMoney = ValueForMoney;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public User getCreator() {
        return creator;
    }

    public int getGlobalValue() {
        return globalValue;
    }

    public int getQuality() {
        return quality;
    }

    public int getService() {
        return service;
    }

    public int getValueForMoney() {
        return ValueForMoney;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    
    
}
