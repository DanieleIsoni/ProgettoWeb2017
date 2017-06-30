/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.entities;

/**
 *
 * @author matteo
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private Shop shop;

    public Product(int id, String name, String description, double price, Shop shop) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.shop = shop;
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

    public double getPrice() {
        return price;
    }

    public Shop getShop() {
        return shop;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }


}
