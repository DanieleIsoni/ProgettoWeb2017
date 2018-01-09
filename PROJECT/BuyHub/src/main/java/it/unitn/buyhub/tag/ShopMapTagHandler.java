package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag used in product and shop page to print a map with all shops and the
 * details
 *
 * @author Massimo Girondi
 */
public class ShopMapTagHandler extends SimpleTagSupport {

    private int shopId;
    private boolean owner = false;

    List<Coordinate> coordinates;
    private ShopDAO shopDAO;
    private CoordinateDAO coordinateDAO;
    PageContext pageContext;

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    @Override

    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        pageContext = (PageContext) getJspContext();

        try {
            init();
        } catch (Exception e) {
            Log.error(e);
        }

        try {
            if (owner) {
                try {
                    coordinates = coordinateDAO.getByShop(shopDAO.getByPrimaryKey(shopId));
                } catch (DAOException ex) {
                    Log.error(ex);
                }
            } else {
                coordinates = ((List<Coordinate>) pageContext.getAttribute("coordinates", PageContext.REQUEST_SCOPE));
            }
            String markers = "var markers=[";
            String details = "var details=[";
            if (coordinates != null && coordinates.size() != 0) {
                out.println("<div class=\"row maps\">");
                out.println("<div class=\"row where_we_are\">" + Utility.getLocalizedString(pageContext, "where_we_are") + "</div>");

                out.println("<div class=\"row\">");

                if (owner) {
                    out.println("<div class=\" map-wrapper \" >");
                    out.println("<div class=\" map \" id=\"map\"></div>");

                    out.println("</div>");
                    out.println("<div class=\"row\">\n"
                            + "                <table class=\"table table-striped table-bordered\" id=\"coordinates_table\">\n"
                            + "                    <thead>\n"
                            + "                    <td> " + Utility.getLocalizedString(pageContext, "address") + "</td>\n"
                            + "                    <td> " + Utility.getLocalizedString(pageContext, "opening_hours") + "</td>\n"
                            + "                    <td> <a href=\"addCoordinate.jsp?shopId=" + shopId + "\" title=\"" + Utility.getLocalizedString(pageContext, "add") + "\" class=\"btn btn-primary a-btn-slide-text mybtn\">\n"
                            + "                            <span class=\"glyphicon myglyph glyphicon-plus\" aria-hidden=\"true\"></span>          \n"
                            + "                         </a>"
                            + "</td>\n");
                    out.println("</thead>");
                    for (Coordinate coordinate : coordinates) {
                        out.println("<tr>\n"
                                + "                            <td>" + coordinate.getAddress().replace("\n", "\n<br/>\n") + "</td>\n"
                                + "                            <td>");
                        if (coordinate.getOpening_hours() != null && !coordinate.getOpening_hours().equals("")) {
                            System.err.println("ciao");
                            out.println(coordinate.getOpening_hours());
                        } else {
                            System.err.println("culo");
                            out.println(Utility.getLocalizedString(pageContext, "no_opening_hours"));
                        }
                        out.println(" </td>\n");

                        out.println("<td>\n"
                                + "                                <a href=\"../EditCoordinateServlet?code=1&coordinateId=" + coordinate.getId() + "\" title=\"" + Utility.getLocalizedString(pageContext, "edit_coordinate") + "\" class=\"btn btn-primary a-btn-slide-text mybtn\">\n"
                                + "                                    <span class=\"glyphicon myglyph glyphicon-edit\" aria-hidden=\"true\"></span>          \n"
                                + "                                </a>\n"
                                + "                                <a href=\"../DeleteCoordinateServlet?coordinateId=" + coordinate.getId() + "\" title=\"" + Utility.getLocalizedString(pageContext, "delete_coordinate") + "\" class=\"btn btn-danger a-btn-slide-text mybtn\" onclick=\"return confirm('Are you sure you want to continue')\">\n"
                                + "                                    <span class=\"glyphicon myglyph glyphicon-remove\" aria-hidden=\"true\"></span>          \n"
                                + "                                </a>\n"
                                + "                            </td>\n");
                        out.println("</tr>");
                        markers += "[''," + coordinate.getLatitude() + "," + coordinate.getLongitude() + "],\n";
                        details += "`" + coordinate.getAddress() + "`, ";
                    }
                    out.println("</table></div>");

                } else {
                    out.println("<div class=\" map-wrapper col-md-9\" >");
                    out.println("<div class=\" map col-md-9\" id=\"map\"></div>");

                    out.println("</div>");
                    out.println("<div class=\"col-md-3 shop_address\">");
                    for (Coordinate coordinate : coordinates) {

                        out.println("<div class=\"row\">");
                        out.println(coordinate.getAddress().replace("\n", "\n<br/>\n"));
                        if (coordinate.getOpening_hours() != null && !coordinate.getOpening_hours().equals("")) {
                            out.println("<br/><em>" + Utility.getLocalizedString(pageContext, "opening_hours") + "<br/>" + coordinate.getOpening_hours().replace("\n", "\n<br/>\n") + "</em>");

                        } else {
                            out.println("<br/><em>" + Utility.getLocalizedString(pageContext, "no_opening_hours") + "</em>");
                        }
                        out.println("</div><hr/>");

                        markers += "[''," + coordinate.getLatitude() + "," + coordinate.getLongitude() + "],\n";
                        details += "`" + coordinate.getAddress() + "`, ";

                    }
                    out.println("</div>");
                }

                out.println("</div>");
                out.println("</div>");//fine div globale

                out.println("<script>\n" + markers + "];\n" + details + "];</script>");
                out.println("<script src=\"" + Utility.getUrl(pageContext, "js/ShopMaps.js") + "\"></script>");

            }

        } catch (java.io.IOException ex) {
            throw new JspException("Error in ShopMapTagHandler tag", ex);
        }
    }

    private void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) pageContext.getAttribute("daoFactory", PageContext.APPLICATION_SCOPE);
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for product table printer");
        }
        try {
            shopDAO = daoFactory.getDAO(ShopDAO.class);
            coordinateDAO = daoFactory.getDAO(CoordinateDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for product table printer", ex);
        }
    }

}
