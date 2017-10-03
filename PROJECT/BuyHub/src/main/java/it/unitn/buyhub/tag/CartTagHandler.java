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
        try {

            for (CartElement ce : cart.getProducts()) {
                try {
                    Product product = productDAO.getByPrimaryKey(ce.getId());

                    out.println("<div class=\"row search_item\">\n"
                            + "                         <div class=\"media\"><div class=\"media-left\"><div class=\"search_img_box\"><a href=\"product?id=" + ce.getId() + "\"><img class=\"media-object img-rounded img-responsive\" src=\"" + pictureDAO.getByProduct(product).get(0).getPath() + "\" alt=\"" + product.getName() + "\"></a></div></div><div class=\"media-body\"><h4 class=\"media-heading\"><a href=\"product?id=" + ce.getId() + "\">" + product.getName() + "</a></h4>\n"
                            + "<p>" + product.getPrice() + " EUR</p>"
                            + "<div class=\"input-group input-group-sm\"> <span class=\"input-group-addon\" id=\"sizing-addon3\">@</span> <input class=\"form-control\" placeholder=\"Username\" aria-describedby=\"sizing-addon3\"> </div>"
                            + "                     </div></div></div><hr>\n"
                            + "                </div>");

                } catch (DAOException ex) {
                    Log.error("Error using ProductDAO");
                }
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
