package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.utils.Utility;
import java.util.List;
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

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();

        PageContext pageContext = (PageContext) getJspContext();
        try {

            List<Coordinate> coordinates = ((List<Coordinate>) pageContext.getAttribute("coordinates", PageContext.REQUEST_SCOPE));
            String markers = "var markers=[";
            String details = "var details=[";
            if (coordinates != null && coordinates.size() != 0) {
                out.println("<div class=\"row maps\">");
                out.println("<div class=\"row where_we_are\">" + Utility.getLocalizedString(pageContext, "where_we_are") + "</div>");

                out.println("<div class=\"row\">");
                out.println("<div class=\" map-wrapper col-md-9\" >");
                out.println("<div class=\" map col-md-9\" id=\"map\"></div>");

                out.println("</div>");
                out.println("<div class=\"col-md-3 shop_address\">");

                for (Coordinate coordinate : coordinates) {

                    out.println("<div class=\"row\">");
                    out.println(coordinate.getAddress().replace("\n", "\n<br/>\n"));
                    if (coordinate.getOpening_hours() != null && coordinate.getOpening_hours() != "") {
                        out.println("<br/><em>" + Utility.getLocalizedString(pageContext, "opening_hours") + "<br/>" + coordinate.getOpening_hours().replace("\n", "\n<br/>\n") + "</em>");

                    } else {
                        out.println("<br/><em>" + Utility.getLocalizedString(pageContext, "no_opening_hours") + "</em>");
                    }
                    out.println("</div><hr/>");

                    markers += "[''," + coordinate.getLatitude() + "," + coordinate.getLongitude() + "],\n";
                    details += "`" + coordinate.getAddress() + "`, ";

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

}
