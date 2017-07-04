/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class NavbarTagHandler extends SimpleTagSupport {

    public void doTag() throws JspException {
        try {
            JspWriter out = getJspContext().getOut();
            out.println("This is my own custom tag");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
