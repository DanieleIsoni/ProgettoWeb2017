/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matteo
 */
public class Cart implements Serializable {

    private List<CartElement> products = new ArrayList<>();

    public Cart() {
        products.add(new CartElement(3, 4));
        products.add(new CartElement(2, 1));
    }

    public void addProduct(int id, int number) {
        boolean found = false;
        for (CartElement ce : products) {
            if (ce.getId() == id) {
                found = true;
                ce.setNumber(ce.getNumber() + number);
            }
        }

        if (!found) {
            products.add(new CartElement(id, number));
        }
    }

    public int getCount() {
        return products.size();
    }

    public void removeProduct(int id) {
        CartElement temp = null;
        for (CartElement ce : products) {
            if (ce.getId() == id) {
                temp = ce;
            }
        }
        products.remove(temp);
    }

    public void removeProduct(CartElement c) {
        products.remove(c);
    }

    public void removeProductAt(int position) {
        products.remove(position);
    }

    public List<CartElement> getProducts() {
        return products;
    }

    public void setProducts(List<CartElement> products) {
        this.products = products;
    }

    public void removeAllProducts() {
        products.clear();
    }

}
