/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author massimo
 */
public class Order implements Serializable {

   ArrayList<OrderedProduct> products=new ArrayList<>();
   int id;
   boolean paid;
   String shipment;
   int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

   
   double shipment_cost;

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public double getShipment_cost() {
        return shipment_cost;
    }

    public void setShipment_cost(double shipment_cost) {
        this.shipment_cost = shipment_cost;
    }
   
   
   public void add(Product p, int quantity)
   {
       OrderedProduct op=new OrderedProduct(p,quantity,id);
       products.add(op);
   }
   public void add(OrderedProduct p)
   {
       products.add(p);
   }
   public double sum()
   {
       double s=0;
       for(OrderedProduct o : products)
           s+=o.getPrice()*o.getQuantity();
       return s;
   }
   public int count()
   {
       return products.size();
   }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
   

}
