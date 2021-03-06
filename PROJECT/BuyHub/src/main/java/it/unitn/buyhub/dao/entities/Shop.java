/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.entities;

import java.io.Serializable;

/**
 *
 * @author Matteo Battilana
 */
public class Shop implements Serializable {

    private int id;
    private String name;
    private String description;
    private String website;
    private User owner;
    private String shipment;
    private int validity;
    private double shipment_cost;

    public double getShipment_cost() {
        return shipment_cost;
    }

    public void setShipment_cost(double shipment_cost) {
        this.shipment_cost = shipment_cost;
    }

    public int getValidity() {
        return validity;
    }

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

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

}
