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
public class Ticket implements Serializable {

    private int id;
    private Order order;

    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
