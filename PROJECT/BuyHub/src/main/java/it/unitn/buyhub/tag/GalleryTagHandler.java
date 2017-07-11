/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.util.ArrayList;
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
public class GalleryTagHandler extends SimpleTagSupport {

    PageContext pageContext;
    @Override
    public void doTag() throws JspException, IOException {

        pageContext=(PageContext) getJspContext();
        List<Picture> pictures=(List<Picture>) pageContext.getAttribute("pictures",PageContext.REQUEST_SCOPE);
        JspWriter out = pageContext.getOut();

        if(pictures == null || pictures.size()==0)
            out.println(noImage());
        else
        {
            out.println("<div class=\"gallery\">");
            out.println("<div class=\"easyzoom easyzoom--overlay easyzoom--with-thumbnails\">");
            out.println(printMain(pictures.get(0)));
            out.println("</div>");
            if(pictures.size()>1)
            {
                out.println("<ul class=\"thumbnails\">");

                for (Picture picture : pictures) {
                           out.println(printThumbnail(picture));
                }
            }
            out.println("</div>");
        }
    }

    private String noImage() {
            return "<img class=\"noimage\" src="+Utility.getUrl(pageContext,"/images/noimage.png")+" alt=\""+Utility.getLocalizedString(pageContext, "no_image")+"\"></img>";
    }
    

    private String printMain(Picture p)
    {
        String s="<a href=\""+Utility.getUrl(pageContext, p.getPath())+"\">";
        s+="<img src=\""+Utility.getUrl(pageContext, p.getPath())+"\"";
        s+=" alt=\""+p.getDescription()+"\"";
        s+=" title=\""+p.getName()+"\"";
        s+="/></a>";
          return s;
    }
    private String printThumbnail(Picture p)
    {
        String s="<li><a ";
        s+=" href=\""+Utility.getUrl(pageContext, p.getPath())+"\"";
        s+=" data-standard=\""+Utility.getUrl(pageContext, p.getPath())+"\">";
        s+="<img src=\""+Utility.getUrl(pageContext, p.getPath())+"\"";
        s+=" alt=\""+p.getDescription()+"\"";
        s+=" title=\""+p.getName()+"\"";
        s+="/></a></li>";
        
                
        return s;
    }
}