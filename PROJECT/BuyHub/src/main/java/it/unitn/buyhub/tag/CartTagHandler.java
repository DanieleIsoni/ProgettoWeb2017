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
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Cart;
import it.unitn.buyhub.dao.entities.CartElement;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.jdbc.JDBCProductDAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Pair;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    CoordinateDAO coordinateDAO;
    ShopDAO shopDAO;

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        pageContext = (PageContext) getJspContext();

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) format).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("&euro;");
        ((DecimalFormat) format).setDecimalFormatSymbols(decimalFormatSymbols);

        try {
            init();
        } catch (Exception e) {
        }
        Cart cart = (Cart) pageContext.getAttribute("userCart", PageContext.SESSION_SCOPE);

        String shopName = "";
        try {
            out.println("<div id=\"products\">");
            for (Map.Entry<Integer, ArrayList<CartElement>> entry : cart.getProducts().entrySet()) {
                double total = 0f;
                Shop shop = shopDAO.getByPrimaryKey(entry.getKey());

                //controllo qunado chiudere il pannello
                if (!shopName.equals(shop.getName())) {
                    out.println("</div>");
                    shopName = shop.getName();
                }
                out.println("<div class=\"panel panel-default panel-default-cart\">");
                out.println("<h4>" + shop.getName() + "</h4>");

                for (CartElement ce : entry.getValue()) {
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
                                + "                     <p>" + format.format(product.getPrice()) + "</p>\n"
                                + "                     <div id=\"cart-quantity\" class=\"input-group input-group-sm\"> <span class=\"input-group-addon\" id=\"sizing-addon3\">" + Utility.getLocalizedString(pageContext, "element_quantity") + "</span> <input onkeyup=\"changeItemNumber(" + ce.getId() + ", this.value)\" class=\"form-control\" placeholder=\"" + Utility.getLocalizedString(pageContext, "element_quantity") + "\" value=\"" + ce.getNumber() + "\" aria-describedby=\"sizing-addon3\"></div>\n"
                                + "                  </div>\n"
                                + "            </div>\n"
                                + "         </th>\n"
                                + "         <th>\n"
                                + "            <p class=\"total-element-cart\">" + format.format(product.getPrice() * ce.getNumber()) + "</p>\n"
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

                
                String select="";
                
                List<Coordinate> coordinates = coordinateDAO.getByShop(shop);

                if(shop.getShipment()!=null && shop.getShipment().length()!=0)
                {
                    select+="<option id='-1' >"+format.format(shop.getShipment_cost())+" - "+shop.getShipment()+"</option>\n";
                }

                for (Coordinate coordinate : coordinates) {
                    String opening=coordinate.getOpening_hours();
                    if(opening!="")
                    {
                        select+="\n<option id="+coordinate.getId()+" >";
                        select+=format.format(0)+" - "+Utility.getLocalizedString(pageContext, "pickup_in_store")+" ("+coordinate.getAddress()+")</option>";
                        
                    }
                }

                // COUNT TOTAL
                out.println("<table class=\"row search_item\">\n"
                        + "   <tbody>\n"
                        + "      <tr>\n"
                        + "         <th>\n"
                        + "             <h2 class=\"media-body\">" + Utility.getLocalizedString(pageContext, "cart_total") + "</h2>"
                        + "         </th>\n"
                        + "         <th>\n"
                        + "            <p class=\"total-element-cart\" id=total"+shop.getId()+">" + format.format(total) + "</p>\n"
                        + "         </th>\n"
                        + "      </tr>\n"
                        + "   </tbody>\n"
                        + "</table>"
                        + "<div class=\"to-right-cart\">"
                        + "   <div id=\"cart-shipment\" >"+Utility.getLocalizedString(pageContext, "shipment_mode_singular")+": </span> <select id=\"shipment"+shop.getId()+"\" onchange=changeShipment("+shop.getId()+") class=\"btn btn-default dropdown-toggle\">"+select+"</select></div>"
                        + "   <button type=\"button\" class=\"btn btn-info\" onclick=\"location.href = 'cart.jsp'\">" + Utility.getLocalizedString(pageContext, "recalculate_cart") + "</button>"
                        + "   <button type=\"button\" class=\"btn btn-success\" onclick=\"placeOrderCart("+shop.getId()+")\">" + Utility.getLocalizedString(pageContext, "pay") + "</button>"
                        + "</div>");
                out.println("<input type=\"hidden\" id=\"shipment_cost"+shop.getId()+"\" value=\""+shop.getShipment_cost()+"\">");
                out.println("<input type=\"hidden\" id=\"originalTotal"+shop.getId()+"\" value=\""+total+"\">");
                out.println("<script> changeShipment("+shop.getId()+")</script>");
            }
            
            out.println("</div>");

        } catch (Exception ec) {
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
            shopDAO = daoFactory.getDAO(ShopDAO.class);
            coordinateDAO=daoFactory.getDAO(CoordinateDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for product storage system", ex);
        }
    }

}
