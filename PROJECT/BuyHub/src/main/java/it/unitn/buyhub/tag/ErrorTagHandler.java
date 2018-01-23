package it.unitn.buyhub.tag;

import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag to print an error message (used in forms around the site)
 * @author maxgiro96
 */
public class ErrorTagHandler extends SimpleTagSupport {


    @Override
    public void doTag() {

        JspWriter out = getJspContext().getOut();

        PageContext pageContext= (PageContext) getJspContext();
        //System.out.println(pageContext.getRequest().getParameter("error"));
        if(pageContext.getRequest().getParameter("error")!=null)
        {

           setCode(Integer.parseInt(pageContext.getRequest().getParameter("error").toString()));
           if(code!=0)
           {

                try {
                       out.println("<div class=\"alert alert-danger fade in\">\n" +
                               "                <a href=\"#\" class=\"close\" data-dismiss=\"alert\">&times;</a>\n" +
                               "                <strong>"
                               + Utility.getLocalizedString(pageContext,"error")
                               + "!</strong><br/>"
                               + Utility.getLocalizedString(pageContext, page+"_"+code) +
                               "            </div>");
                   } catch (IOException ex) {
                       Log.warn("Error writing in error tag");
                   }
            }
        }


    }
    private String page;
    private int code;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
