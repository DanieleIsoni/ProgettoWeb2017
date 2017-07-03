/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.entities;

import java.io.Serializable;

/**
 *
 * @author matteo
 */
public class Shop implements Serializable {

    private int id;
    private String name;
    private String description;
    private String website;
    private User owner;
    private User creator;
    private int globalValue;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getWebsite() {
        return website;
    }

    public User getOwner() {
        return owner;
    }

    public User getCreator() {
        return creator;
    }

    public int getGlobalValue() {
        return globalValue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setGlobalValue(int globalValue) {
        this.globalValue = globalValue;
    }

}
