/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.entities;

/**
 *
 * @author massimo
 */
public class OrderedProduct extends Product{
    int quantity=1;
    int order;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderedProduct(Product p, int q, int o)
    {
        this.setAvgReview(p.getAvgReview());
        this.setCategory(p.getCategory());
        this.setDescription(p.getDescription());
        this.setId(p.getId());
        this.setMainPicture(p.getMainPicture());
        this.setName(p.getName());
        this.setReviewCount(p.getReviewCount());
        this.setShop(p.getShop());
        this.setPrice(p.getPrice());
        this.setQuantity(q);
        this.setOrder(o);
        
    }
    public OrderedProduct(){}

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
