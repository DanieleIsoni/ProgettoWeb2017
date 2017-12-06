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
 * @author matteo
 */
public class Cart implements Serializable {

    private HashMap<Integer, ArrayList<CartElement>> products = new HashMap<Integer, ArrayList<CartElement>>();

    public Cart() {
       /* insert(1, new CartElement(2, 1));
        insert(1, new CartElement(1, 2));
        insert(2, new CartElement(3, 3));
    */}

    private void insert(int shopid, CartElement ce) {
        if (products.get(shopid) == null) {
            products.put(shopid, new ArrayList<CartElement>());
        }
        
        addProductIfAlready(products.get(shopid), ce);
    }
    private void addProductIfAlready(ArrayList<CartElement> products,CartElement newce) {
        boolean found = false;
        for (CartElement ce : products) {
            if (ce.getId() == newce.getId()) {
                found = true;
                ce.setNumber(ce.getNumber() + newce.getNumber());
            }
        }

        if (!found) {
            products.add(new CartElement(newce.getId(), newce.getNumber()));
        }
    }


    public void addProduct(int shopid, int id, int number) {
        insert(shopid, new CartElement(id, number));
    }

    public int getCount() {
        int count = 0;
        for (Map.Entry<Integer, ArrayList<CartElement>> entry : products.entrySet()) {
            count += entry.getValue().size();
        }

        return count;
    }

    public void removeProduct(int id) {
        CartElement temp = null;
        int tempkey = -1;
        for (Map.Entry<Integer, ArrayList<CartElement>> entry : products.entrySet()) {
            for (CartElement ce : entry.getValue()) {
                if (ce.getId() == id) {
                    //MUST TEST
                    temp = ce;
                    tempkey = entry.getKey();

                }
            }
        }
        if (temp != null) {
            products.get(tempkey).remove(temp);
        }
        if (products.get(tempkey).size() == 0) {
            products.remove(tempkey);
        }

    }

    
    public void removeIf(List<CartElement> list)
    {
    /*     for (CartElement cartElement : list) {
            removeProduct(cartElement);
        }   
    */
    
    }
    public void removeProduct(CartElement c) {
        removeProduct(c.getId());
    }

    public void removeProductAt(int position) {
        products.remove(position);
    }

    public HashMap<Integer, ArrayList<CartElement>> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Integer, ArrayList<CartElement>> products) {
        this.products = products;
    }

    public void removeAllProducts() {
        products.clear();
    }

    public void setProduct(int id, int count) {
        for (Map.Entry<Integer, ArrayList<CartElement>> entry : products.entrySet()) {
            for (CartElement ce : entry.getValue()) {
                if (ce.getId() == id) {
                    //MUST TEST
                    ce.setNumber(count);;
                }
            }
        }

    }

}
