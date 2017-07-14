package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.utils.Utility;
import java.util.List;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author massimo
 */
public class ShopMapTagHandler extends SimpleTagSupport {
  @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        
        PageContext pageContext= (PageContext) getJspContext();
        try {
            
            List<Coordinate> coordinates=((List<Coordinate>)pageContext.getAttribute("coordinates",PageContext.REQUEST_SCOPE));
            String script="var markers=[";
            if(coordinates!=null && coordinates.size()!=0)
            {
                out.println("<div class=\"row maps\">");
                out.println("<div class=\"row\">"+Utility.getLocalizedString(pageContext, "where_we_are")+"</div>");
                
                out.println("<div class=\"row\">");
                out.println("<div class=\" map col-md-9\" id=\"map\">");
                //mappa
                out.println("</div>");
                out.println("<div class=\"col-md-3 shop_address\">");
                
                for (Coordinate coordinate : coordinates) {
                    
                    out.println("<div class=\"row\"");
                    out.println(coordinate.getAddress().replace("\n","\n<br/>\n"));
                    out.println("</div>");
                    
                    script+= "\nnew google.maps.Marker({ position:"+
                             "\nnew google.maps.LatLng("+coordinate.getLatitude()+","+coordinate.getLongitude()+"),"+
                             "\nmap: googlemap,"+ 
                             "\ntitle: 'Questo è un testo di suggerimento' }),\n"; 
                }
                //script.substring(0, script.length()-2);//cancello ultima virgola
                out.println("</div>");
                out.println("</div>");//fine div globale
                
                script="function myMap(){var googlemap = new google.maps.Map(document.getElementById('map'), {\n" +
                        "      zoom: 10,\n" +
                        "      mapTypeId: google.maps.MapTypeId.ROADMAP\n" +
                        "    });\n"+script+"];}";
                
                out.println("<script>"+script+"</script>");
                out.println("<script src=\"http://maps.google.com/maps/api/js?key=AIzaSyAjVcIi8WUN_UNmyn8JG1FncjBQUn6qk_g&callback=myMap\" type=\"text/javascript\"></script>");
                
               
                
                
            }
            
        } catch (java.io.IOException ex) {
            throw new JspException("Error in ShopMapTagHandler tag", ex);
        }
    }
    
}
