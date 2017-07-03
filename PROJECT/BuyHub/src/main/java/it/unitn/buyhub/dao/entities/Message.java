/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author matteo
 */
public class Message implements Serializable {

    private int id;
    private Review review;
    private User owner;
    private Date date;
    private Date validationDate;
    private User validation;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    public void setValidation(User validation) {
        this.validation = validation;
    }

    public int getId() {
        return id;
    }

    public Review getReview() {
        return review;
    }

    public User getOwner() {
        return owner;
    }

    public Date getDate() {
        return date;
    }

    public Date getValidationDate() {
        return validationDate;
    }

    public User getValidation() {
        return validation;
    }

}
