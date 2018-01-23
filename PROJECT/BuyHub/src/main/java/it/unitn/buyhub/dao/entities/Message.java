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
public class Message implements Serializable {

    private int id;
    private Ticket ticket;
    private User owner;
    private String content;

    public int getId() {
        return id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public User getOwner() {
        return owner;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
