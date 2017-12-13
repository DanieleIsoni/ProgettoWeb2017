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
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.jdbc.JDBCProductDAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
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
public class LastProductTagHandler extends SimpleTagSupport {

    PageContext pageContext;
    ProductDAO productDAO;
    PictureDAO pictureDAO;
    ReviewDAO reviewDao;

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        pageContext = (PageContext) getJspContext();
        try {
            init();
        } catch (Exception e) {
        }
        /*https://codepen.io/bkainteractive/pen/VLxLYp*/
        try {
            List<Product> allProducts;
            try {
                allProducts = productDAO.getAllLimit(10);
                out.println(" <div class=\"container\">\n"
                        + "            <h2>" + Utility.getLocalizedString(pageContext, "last_added") + "</h2>\n"
                        + "            <div class=\"row\">\n"
                        + "                <div class=\"col-md-12 heroSlider-fixed\">\n"
                        + "                    <div class=\"overlay\">\n"
                        + "                    </div>\n"
                        + "                    <!-- Slider -->\n"
                        + "                    <div class=\"slider responsive\">\n");

                for (Product p : allProducts) {
                    out.println("                        <div>\n"
                            + "                            <a href=\"product?id=" + p.getId() + "\"><img src=\"" + p.getMainPicture().getPath() + "\" alt=\"\" /></a>\n"
                            + "                        </div>\n");

                }
                out.println("                    </div>\n"
                        + "                    <!-- control arrows -->\n"
                        + "                    <div class=\"prev\">\n"
                        + "                        <span class=\"glyphicon glyphicon-chevron-left\" aria-hidden=\"true\"></span>\n"
                        + "                    </div>\n"
                        + "                    <div class=\"next-car\">\n"
                        + "                        <span class=\"glyphicon glyphicon-chevron-right\" aria-hidden=\"true\"></span>\n"
                        + "                    </div>\n"
                        + "\n"
                        + "                </div>\n"
                        + "            </div>\n"
                        + "        </div>\n"
                        + "        <script src='https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.5.5/slick.min.js'></script>\n"
                        + "\n"
                        + "        <script  src=\"js/carouselHome.js\"></script>");
            } catch (DAOException ex) {
                Logger.getLogger(LastProductTagHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ec) {
            Log.error("Error writing from LastProductTagHandler");
        }

    }

    private void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) pageContext.getAttribute("daoFactory", PageContext.APPLICATION_SCOPE);
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            reviewDao = daoFactory.getDAO(ReviewDAO.class);
            pictureDAO = daoFactory.getDAO(PictureDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for product storage system", ex);
        }
    }

}
