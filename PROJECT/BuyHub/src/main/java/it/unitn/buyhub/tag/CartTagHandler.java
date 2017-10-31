/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.entities.Cart;
import it.unitn.buyhub.dao.entities.CartElement;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.jdbc.JDBCProductDAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author massimo
 */
public class CartTagHandler extends SimpleTagSupport {

    PageContext pageContext;
    ProductDAO productDAO;
    PictureDAO pictureDAO;

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        pageContext = (PageContext) getJspContext();
        TreeMap<Product, Integer> products = new TreeMap<Product, Integer>(new Comparator<Product>() {
            @Override
            public int compare(Product t, Product t1) {
                return t.getShop().getId() > t1.getShop().getId() ? 1 : -1;
            }
        });

        try {
            init();
        } catch (Exception e) {
        }

        Cart cart = (Cart) pageContext.getAttribute("userCart", PageContext.SESSION_SCOPE);
        try {
            for (CartElement ce : cart.getProducts()) {
                Product product = productDAO.getByPrimaryKey(ce.getId());
                products.put(product, ce.getNumber());
            }

        } catch (DAOException ex) {
            Logger.getLogger(CartTagHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        Map.Entry<Product,Integer> first=products.entrySet().iterator().next();
        int shopId = -1;
        double total = 0f;
        
        try {
            out.println("<div id=\"products\">");

            for (Map.Entry<Product, Integer> ce : products.entrySet()) {
                if(shopId!= ce.getKey().getShop().getId()){
                    if(shopId!=-1)
                        out.println("</div>");
                    out.println("<div class=\"panel panel-default\">");
                }
                total += ce.getValue() * ce.getKey().getPrice();

                out.println("<table class=\"row search_item\">\n"
                        + "   <tbody>\n"
                        + "      <tr>\n"
                        + "         <th valign=\"center\">\n"
                        + "               <div class=\"media\">\n"
                        + "                  <div class=\"media-left\">\n"
                        + "                     <div class=\"search_img_box\"><a href=\"product?id=3\"><img class=\"media-object img-rounded img-responsive\" src=\"" + ce.getKey().getMainPicture().getPath() + "\" alt=\"" + ce.getKey().getName() + "\"></a></div>\n"
                        + "                  </div>\n"
                        + "                  <div class=\"media-body\">\n"
                        + "                     <h4 class=\"media-heading\"><a href=\"product?id=" + ce.getKey().getId() + "\">" + ce.getKey().getName() + "</a></h4>\n"
                        + "                     <p>" + ce.getKey().getPrice() + " EUR</p>\n"
                        + "                     <div id=\"cart-quantity\" class=\"input-group input-group-sm\"> <span class=\"input-group-addon\" id=\"sizing-addon3\">" + Utility.getLocalizedString(pageContext, "element_quantity") + "</span> <input onkeyup=\"changeItemNumber(" + ce.getKey().getId() + ", this.value)\" class=\"form-control\" placeholder=\"" + Utility.getLocalizedString(pageContext, "element_quantity") + "\" value=\"" + ce.getValue() + "\" aria-describedby=\"sizing-addon3\"></div>\n"
                        + "                  </div>\n"
                        + "            </div>\n"
                        + "         </th>\n"
                        + "         <th>\n"
                        + "            <p class=\"total-element-cart\">" + ce.getKey().getPrice() * ce.getValue() + " EUR</p>\n"
                        + "         </th>\n"
                        + "         <th>"
                        + "         <a href=\"#\"> \n"
                        + "             <div>\n"
                        + "                 <a class=\"glyphicon glyphicon-remove\" id=\"logIcon\" href='removefromcart?id=" + ce.getKey().getId() + "'\"></a>\n"
                        + "             </div>\n"
                        + "         </a>"
                        + "     </th>"
                        + "      </tr>\n"
                        + "   </tbody>\n"
                        + "</table><hr>");

                // COUNT TOTAL FOR EVERY SHOP
                out.println("<table class=\"row search_item\">\n"
                        + "   <tbody>\n"
                        + "      <tr>\n"
                        + "         <th>\n"
                        + "             <h2 class=\"media-body\">" + Utility.getLocalizedString(pageContext, "cart_total") + "</h2>"
                        + "         </th>\n"
                        + "         <th>\n"
                        + "            <p class=\"total-element-cart\">" + total + " EUR</p>\n"
                        + "         </th>\n"
                        + "      </tr>\n"
                        + "   </tbody>\n"
                        + "</table>"
                        + "<div class=\"to-right\">"
                        + "   <button type=\"button\" class=\"btn btn btn-danger\" onclick=\"location.href = 'emptycart'\">" + Utility.getLocalizedString(pageContext, "empty_cart") + "</button>"
                        + "   <button type=\"button\" class=\"btn btn-info\" onclick=\"location.href = 'cart.jsp'\">" + Utility.getLocalizedString(pageContext, "recalculate_cart") + "</button>"
                        + "   <button type=\"button\" class=\"btn btn-success\" onclick=\"location.href = 'restricted/payment.jsp'\">" + Utility.getLocalizedString(pageContext, "pay") + "</button>"
                        + "</div>");
                out.println("</div>");

            }

        } catch (IOException ec) {
            Log.error("Error writing from CartTagHandler");
        }

    }

    private void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) pageContext.getAttribute("daoFactory", PageContext.APPLICATION_SCOPE);
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            pictureDAO = daoFactory.getDAO(PictureDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for product storage system", ex);
        }
    }

}
