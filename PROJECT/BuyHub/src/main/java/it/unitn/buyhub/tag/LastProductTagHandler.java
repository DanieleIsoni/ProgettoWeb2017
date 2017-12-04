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

        try {
            List<Product> allProducts;
            try {
                allProducts = productDAO.getAllLimit(12);
                /* out.println("<div class=\"panel panel-default\">");
                out.println("<div class=\"panel-heading\"> <h3 class=\"panel-title\">Panel title</h3> </div>");
                out.println("<div class=\"panel-body\">");
                
                for (Product p : allProducts) {
                    out.println("<div class=\"col-sm-6 col-md-4\">\n"
                            + "   <div class=\"thumbnail\">\n"
                            + "      <img alt=\"100%x200\" data-src=\"holder.js/100%x200\" style=\"height: 200px; width: 100%; display: block;\" src=\"data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxzdmcgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB3aWR0aD0iMzE5IiBoZWlnaHQ9IjIwMCIgdmlld0JveD0iMCAwIDMxOSAyMDAiIHByZXNlcnZlQXNwZWN0UmF0aW89Im5vbmUiPjwhLS0KU291cmNlIFVSTDogaG9sZGVyLmpzLzEwMCV4MjAwCkNyZWF0ZWQgd2l0aCBIb2xkZXIuanMgMi42LjAuCkxlYXJuIG1vcmUgYXQgaHR0cDovL2hvbGRlcmpzLmNvbQooYykgMjAxMi0yMDE1IEl2YW4gTWFsb3BpbnNreSAtIGh0dHA6Ly9pbXNreS5jbwotLT48ZGVmcz48c3R5bGUgdHlwZT0idGV4dC9jc3MiPjwhW0NEQVRBWyNob2xkZXJfMTVmZTRhMjMyN2QgdGV4dCB7IGZpbGw6I0FBQUFBQTtmb250LXdlaWdodDpib2xkO2ZvbnQtZmFtaWx5OkFyaWFsLCBIZWx2ZXRpY2EsIE9wZW4gU2Fucywgc2Fucy1zZXJpZiwgbW9ub3NwYWNlO2ZvbnQtc2l6ZToxNnB0IH0gXV0+PC9zdHlsZT48L2RlZnM+PGcgaWQ9ImhvbGRlcl8xNWZlNGEyMzI3ZCI+PHJlY3Qgd2lkdGg9IjMxOSIgaGVpZ2h0PSIyMDAiIGZpbGw9IiNFRUVFRUUiLz48Zz48dGV4dCB4PSIxMTcuMTA5Mzc1IiB5PSIxMDcuMDg3NSI+MzE5eDIwMDwvdGV4dD48L2c+PC9nPjwvc3ZnPg==\" data-holder-rendered=\"true\"> \n"
                            + "      <div class=\"caption\">\n"
                            + "         <h3>Thumbnail label</h3>\n"
                            + "         <p>Cras justo odio, dapibus ac facilisis in, egestas eget quam. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>\n"
                            + "         <p><a href=\"#\" class=\"btn btn-primary\" role=\"button\">Button</a> <a href=\"#\" class=\"btn btn-default\" role=\"button\">Button</a></p>\n"
                            + "      </div>\n"
                            + "   </div>\n"
                            + "</div>");
                }
                
                out.println("</div>");*/
                out.println("<div class=\"well\">\n"
                        + "            <div id=\"myCarousel\" class=\"carousel slide\">\n"
                        + "                \n"
                        + "                <!-- Carousel items -->\n"
                        + "                <div class=\"carousel-inner\">");
                boolean first= true;
                for (int i = 0; i <= allProducts.size(); i += 4) {
                    if (i + 3 < allProducts.size()) {
                        out.println("<div class=\"item "+(first?"active":"")+"\">\n"
                                + "                        <div class=\"row\">\n"
                                + "                            <div class=\"col-sm-3\"><a href=\"#x\"><img src=\""+allProducts.get(i).getMainPicture().getPath()+"\" alt=\"Image\" class=\"img-responsive\"></a>\n"
                                + "                            </div>\n"
                                + "                            <div class=\"col-sm-3\"><a href=\"#x\"><img src=\""+allProducts.get(i+1).getMainPicture().getPath()+"\" alt=\"Image\" class=\"img-responsive\"></a>\n"
                                + "                            </div>\n"
                                + "                            <div class=\"col-sm-3\"><a href=\"#x\"><img src=\""+allProducts.get(i+2).getMainPicture().getPath()+"\" alt=\"Image\" class=\"img-responsive\"></a>\n"
                                + "                            </div>\n"
                                + "                            <div class=\"col-sm-3\"><a href=\"#x\"><img src=\""+allProducts.get(i+3).getMainPicture().getPath()+"\" alt=\"Image\" class=\"img-responsive\"></a>\n"
                                + "                            </div>\n"
                                + "                        </div>\n"
                                + "                        <!--/row-->\n"
                                + "                    </div>\n"
                                + "                    <!--/item-->");
                    }
                    first = false;

                }
                out.println("</div>\n"
                        + "                <!--/carousel-inner--> <a class=\"left carousel-control\" href=\"#myCarousel\" data-slide=\"prev\">‹</a>\n"
                        + "\n"
                        + "                <a class=\"right carousel-control\" href=\"#myCarousel\" data-slide=\"next\">›</a>\n"
                        + "            </div>\n"
                        + "            <!--/myCarousel-->\n"
                        + "        </div>\n"
                        + "        <!--/well-->");
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
