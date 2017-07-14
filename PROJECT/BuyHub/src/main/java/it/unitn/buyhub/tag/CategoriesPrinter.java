/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.utils.Utility;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author massimo
 */
public class CategoriesPrinter extends SimpleTagSupport {

    private String style;

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        PageContext pageContext= (PageContext) getJspContext();
        int categoriesNumber=Integer.parseInt(pageContext.getServletContext().getInitParameter("categoriesNumber"));
        //int categoriesNumber=42;
        try {
            for(int i=0;i<categoriesNumber;i++)
              switch(style)
                    {

                        case "select": out.println("<option value=\""+i+"\">"+Utility.getCategory(pageContext, i)+"</option>"); break;
                    //    case "list":   out.println("<li><a href=\"category"+i+"\">"++Utility.getCategory(pageContext, i)); break;
                        default:out.println(Utility.getCategory(pageContext, i));
              }
           
            
            
        } catch (java.io.IOException ex) {
            throw new JspException("Error in CategoriesPrinter tag", ex);
        }
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
}
