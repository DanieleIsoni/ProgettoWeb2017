/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.utils.Utility;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * This tag provide the images for the slider homepage,
 * listing the images under images/slider_images folder
 * @author massimo
 */
public class HomepageSlider extends SimpleTagSupport {

 
    @Override
    public void doTag() throws JspException {
        PageContext pageContext = (PageContext)getJspContext();
        
        JspWriter out = getJspContext().getOut();
        String path=pageContext.getServletContext().getRealPath("/images/slider_images")+"/";
        String lang = Utility.getLocalizedString(pageContext, "language");
        path+=lang+"/";
        File folder = new File(path);
        //System.out.println(path+" ->"+folder.getAbsolutePath());
        File[] listOfFiles = folder.listFiles();
        boolean first=true;
        for (File file: listOfFiles) {
            try {
                out.print("<div class=\"item");
                if(first)
                {
                    first=false;
                    out.print(" active");
                }
                out.println("\">\n" +
                        "                        <img src=\"images/slider_images/"+lang+"/"+file.getName()+"\" alt=\"SliderImage\" style=\"width:100%;\">\n</img>" +
                        "                        <div class=\"carousel-caption\">\n" +
                        "                        </div>\n" +
                        "                    </div>");
            } catch (IOException ex) {
                Logger.getLogger(HomepageSlider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
