package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.utils.Utility;
import static java.lang.Math.round;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag used to print stars into product page
 * @author massimo
 */
public class ReviewStarsTagHandler extends SimpleTagSupport {

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     */

    int value=0;
    int count=0;
    boolean label=false;
    @Override
    public void doTag() throws JspException {
        try {

             JspWriter out = getJspContext().getOut();
             PageContext pageContext= (PageContext) getJspContext();

             out.println("<div class=\"stars\">");
             for(int i=1;i<6;i++)
             {
                 out.print("<span class=\"glyphicon glyphicon-star");
                 if(value<=i)
                     out.print("-empty");
                 out.println("\" aria-hidden=\"true\"></span>");
             }
             if(label)
                out.println("<span class=\"label label-success\">"+count+"</span>");
            out.println("</div>");


        } catch (java.io.IOException ex) {
            throw new JspException("Error in ReviewStarstagHandler tag", ex);
        }
    }

    public void setValue(double value)
    {
        this.value=(int) round(value);
    }
    public void setCount(int count)
    {
        this.count=count;
    }
    public void setLabel(boolean label)
    {
        this.label=label;
    }
}
