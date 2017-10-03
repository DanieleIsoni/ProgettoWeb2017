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
public class Picture implements Serializable {

    private int id;
    private String name;
    private String description;
    private String path;
    private User owner;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

    public String getPath() {
        return path;
    }

    public User getOwner() {
        return owner;
    }

    public static Picture NONE()
    {
        Picture P=new Picture();
        P.setId(-1);
        P.setName("No picture");
        P.setPath("images/noimage.png");
        return P;
    }
}
