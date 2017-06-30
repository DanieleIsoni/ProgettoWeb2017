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
public class Message {

    private int id;
    private User review;
    private User owner;
    private Date date;
    private Date validationDate;
    private int idValidation;

    public void setId(int id) {
        this.id = id;
    }

    public void setReview(User review) {
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

    public void setIdValidation(int idValidation) {
        this.idValidation = idValidation;
    }

    public int getId() {
        return id;
    }

    public User getReview() {
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

    public int getIdValidation() {
        return idValidation;
    }

    public Message(int id, User review, User owner, Date date, Date validationDate, int idValidation) {
        this.id = id;
        this.review = review;
        this.owner = owner;
        this.date = date;
        this.validationDate = validationDate;
        this.idValidation = idValidation;
    }


}
