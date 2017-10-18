/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import static java.lang.System.out;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 *
 * @author massimo
 */
public class CategoryTaghandler extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        
        JspWriter out = getJspContext().getOut();
        PageContext pageContext= (PageContext) getJspContext();
        try{
            int category=0;
            if(this.category!=-1)
                category=this.category;
            else
                category=((Product)pageContext.getAttribute("product",PageContext.REQUEST_SCOPE)).getCategory();
            out.println("<a href=\""+Utility.getUrl(pageContext, "search.jsp?q=*&category="+category)+"\">");
            out.println(Utility.getCategory(pageContext,category));
            out.println("</a>");
            
        } catch (java.io.IOException ex) {
            throw new JspException("Error in categoryTaghandler tag", ex);
        }
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        //Log.info(category);
        this.category = category;
    }
    private int category=-1;
    
}
