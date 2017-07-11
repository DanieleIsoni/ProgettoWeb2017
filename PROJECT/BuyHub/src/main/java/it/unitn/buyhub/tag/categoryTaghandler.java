/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.entities.Product;
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
public class categoryTaghandler extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        
        JspWriter out = getJspContext().getOut();
        PageContext pageContext= (PageContext) getJspContext();
        try{
        int category=((Product)pageContext.getAttribute("product",PageContext.REQUEST_SCOPE)).getCategory();
        
            out.println("<a href=\""+Utility.getUrl(pageContext, "search?category="+category)+"\">");
            out.println(Utility.getCategory(pageContext,category));
            out.println("</a>");
            
        } catch (java.io.IOException ex) {
            throw new JspException("Error in categoryTaghandler tag", ex);
        }
    }
    
}
