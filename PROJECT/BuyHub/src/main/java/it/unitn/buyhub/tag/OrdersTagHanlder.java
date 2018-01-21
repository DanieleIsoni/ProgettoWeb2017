/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.OrderDAO;
import it.unitn.buyhub.dao.OrderedProductDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.entities.OrderedProduct;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * This tags print the orders list in myshop page
 * @author massimo
 */
public class OrdersTagHanlder extends SimpleTagSupport {

    private String shop_id;
    private OrderDAO orderDAO;
    private OrderedProductDAO orderedProductDAO;
    private PageContext pageContext;
   
    
    
    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */
    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        pageContext = (PageContext) getJspContext();
        try{
            init();
        } catch(Exception e){
            Log.error(e);
            throw new JspException("Error creating DAOs:", e);
        }
        try {
            
            
           String modals="";
           out.println("<div class=\"row\">\n" +
"                <div class=\"row shop_page_shipment_info\">\n" +
                        Utility.getLocalizedString(pageContext,"myorders_title")+
"                </div>\n" +
"                <table class=\"table table-striped table-bordered\" id=\"orders_table\">\n" +
"                    <thead>\n" +
"                    <td> "+Utility.getLocalizedString(pageContext,"order_id")+"</td>\n" +
"                    <td> "+Utility.getLocalizedString(pageContext,"full_name")+"</td>\n" +
"                    <td> "+Utility.getLocalizedString(pageContext,"total")+"</td>\n" +
"                    <td> </td>\n</thead>");
            List<Order> orders = orderDAO.getByShop(Integer.parseInt(shop_id));
            if(orders!=null && orders.size()>0)
            {
                for(Order o: orders)
                {
                    if(o.isPaid())
                    {
                        List<OrderedProduct> byOrder = orderedProductDAO.getByOrder(o.getId());
                        float total=0;
                        for (OrderedProduct op:byOrder)
                            total+= op.getQuantity()*op.getPrice();
                        total+=o.getShipment_cost();

                        out.println("<tr>\n" +
                                    "<td> #"+o.getId()+"</td>"+
                                    "<td>"+o.getUser().getFirstName() + " "+o.getUser().getLastName()+"</td>"+
                                    "<td>"+total+"</td>"+
                                    
                                    "<td><button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#Modal"+o.getId()+"\">\n" +
                                    Utility.getLocalizedString(pageContext, "order_details")+"  \n" +
                                    "</button></td></tr>"
                        );
                        modals+=createModal(o, byOrder)+"\n";
                    }
                }
                 
            }
           
           out.println("</table>");
           
            out.println("<!--------MOADLS CONTENT--------------->");

            out.println(modals);
            out.println("<!--------MOADLS CONTENT END--------------->");

           
            
            
            out.println("</div>");
        } catch (Exception ex) {
            
            Log.error(ex);
            throw new JspException("Error in OrdersTagHanlder tag: ", ex);
        }
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }
    
    private void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) pageContext.getAttribute("daoFactory", PageContext.APPLICATION_SCOPE);
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for orders table printer");
        }
        try {
            orderDAO = daoFactory.getDAO(OrderDAO.class);
            orderedProductDAO = daoFactory.getDAO(OrderedProductDAO.class);
            
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for orders table printer", ex);
        }
    }
    
    private String createModal(Order o, List<OrderedProduct> orederedProduct)
    {
        
            float total=0;
                for (OrderedProduct op: orederedProduct)
                   total+= op.getQuantity()*op.getPrice();
            total+=o.getShipment_cost();

            String content="<!-- Modal -->\n" +
            "<div class=\"modal fade\" id=\"Modal"+o.getId()+"\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalCenterTitle\" aria-hidden=\"true\">\n" +
            "  <div class=\"modal-dialog modal-dialog-centered\" role=\"document\">\n" +
            "    <div class=\"modal-content\">\n" +
            "      <div class=\"modal-header\">\n" +
            "        <h5 class=\"modal-title\" id=\"exampleModalLongTitle\">"+Utility.getLocalizedString(pageContext, "detailsfororder")+""+o.getId()+"</h5>\n" +
            "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n" +
            "          <span aria-hidden=\"true\">&times;</span>\n" +
            "        </button>\n" +
            "      </div>\n" +
            "      <div class=\"modal-body\">\n"
                    + "<div class=\"container-fluid\">";
            
            content+="<div class='row'>";
            content+=Utility.getLocalizedString(pageContext, "name")+": "+o.getUser().getFirstName()+" "+o.getUser().getLastName()+" (#"+o.getUser().getId()+")<br/>\n";
            content+=Utility.getLocalizedString(pageContext, "email_address")+": "+o.getUser().getEmail()+"<br/>\n";
            content+=Utility.getLocalizedString(pageContext, "shipment_mode_singular")+": "+o.getShipment()+"<br/>\n";
            content+=Utility.getLocalizedString(pageContext, "total")+": €"+String.format("%.2f", total)+"<br/>\n";
            content+="</div><div class='row'>\n";
            
            
            content+="<table class='table-striped table-bordered'>";
            content+="<thead><td>"+"ID"+"</td><td>"+Utility.getLocalizedString(pageContext, "product_name")
                    +"</td><td>"+Utility.getLocalizedString(pageContext, "element_quantity")+"</td><td>"
                    +Utility.getLocalizedString(pageContext, "price")+"</td></thead>\n";
            for(OrderedProduct op: orederedProduct)
            {
                content+="<tr>";
                content+="<td>"+op.getId()+"</td>";
                content+="<td>"+op.getName()+"</td>";
                content+="<td>"+op.getQuantity()+"</td>";
                content+="<td>"+String.format("%.2f", op.getPrice())+"</td>";
                content+="</tr>\n";
            }
                
            content+="</table>\n";
            
                    
            content+=    
                    "</div>\n"+
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>"+
            "</div>";

        
        
        return content;
    }
    
}
