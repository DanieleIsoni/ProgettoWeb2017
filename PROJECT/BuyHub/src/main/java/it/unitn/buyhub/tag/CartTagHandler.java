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
import java.util.Locale;
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
        try {
            init();
        } catch (Exception e) {
        }
        Cart cart = (Cart) pageContext.getAttribute("userCart", PageContext.SESSION_SCOPE);
        double total = 0f;
        try {
            out.println("<div id=\"products\">");

            for (CartElement ce : cart.getProducts()) {
                try {
                    Product product = productDAO.getByPrimaryKey(ce.getId());
                    total += ce.getNumber() * product.getPrice();

                    out.println("<table class=\"row search_item\">\n"
                            + "   <tbody>\n"
                            + "      <tr>\n"
                            + "         <th valign=\"center\">\n"
                            + "               <div class=\"media\">\n"
                            + "                  <div class=\"media-left\">\n"
                            + "                     <div class=\"search_img_box\"><a href=\"product?id=3\"><img class=\"media-object img-rounded img-responsive\" src=\"" + product.getMainPicture().getPath() + "\" alt=\"" + product.getName() + "\"></a></div>\n"
                            + "                  </div>\n"
                            + "                  <div class=\"media-body\">\n"
                            + "                     <h4 class=\"media-heading\"><a href=\"product?id=" + ce.getId() + "\">" + product.getName() + "</a></h4>\n"
                            + "                     <p>" + product.getPrice() + " EUR</p>\n"
                            + "                     <div id=\"cart-quantity\" class=\"input-group input-group-sm\"> <span class=\"input-group-addon\" id=\"sizing-addon3\">" + Utility.getLocalizedString(pageContext, "element_quantity") + "</span> <input class=\"form-control\" placeholder=\"" + Utility.getLocalizedString(pageContext, "element_quantity") + "\" value=\"" + ce.getNumber() + "\" aria-describedby=\"sizing-addon3\"></div>\n"
                            + "                  </div>\n"
                            + "            </div>\n"
                            + "         </th>\n"
                            + "         <th>\n"
                            + "            <p class=\"total-element-cart\">" + product.getPrice() * ce.getNumber() + " EUR</p>\n"
                            + "         </th>\n"
                            + "         <th>"
                            + "         <a href=\"#\"> \n"
                            + "             <div>\n"
                            + "                 <span class=\"glyphicon glyphicon-remove\" id=\"logIcon\" onclick=\"location.href = 'removefromcart?id=" + ce.getId() + "'\"></span>\n"
                            + "             </div>\n"
                            + "         </a>"
                            + "     </th>"
                            + "      </tr>\n"
                            + "   </tbody>\n"
                            + "</table><hr>");

                } catch (DAOException ex) {
                    Log.error("Error using ProductDAO");
                }

            }
            // COUNT TOTAL
            out.println("<table class=\"row search_item\">\n"
                    + "   <tbody>\n"
                    + "      <tr>\n"
                    + "         <th>\n"
                    + "             <h2 class=\"media-body\">" + Utility.getLocalizedString(pageContext, "total") + "</h2>"
                    + "         </th>\n"
                    + "         <th>\n"
                    + "            <p class=\"total-element-cart\">" + total + " EUR</p>\n"
                    + "         </th>\n"
                    + "      </tr>\n"
                    + "   </tbody>\n"
                    + "</table>"
                    + "<div class=\"to-right\">"
                    + "   <button type=\"button\" class=\"btn btn btn-danger\" onclick=\"location.href = 'emptycart'\">" + Utility.getLocalizedString(pageContext, "empty_cart") + "</button>"
                    + "   <button type=\"button\" class=\"btn btn-info\">" + Utility.getLocalizedString(pageContext, "recalculate_cart") + "</button>"
                    + "   <button type=\"button\" class=\"btn btn-success\" onclick=\"location.href = 'restricted/payment.jsp'\">" + Utility.getLocalizedString(pageContext, "pay") + "</button>"
                    + "</div>");
            out.println("</div>");
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
