/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.entities.User;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import static javax.servlet.jsp.tagext.Tag.EVAL_PAGE;
import org.apache.taglibs.standard.tag.common.fmt.BundleSupport;

public class NavbarTagHandler extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        User user = null;
        user = (User) getJspContext().getAttribute("authenticatedUser", PageContext.SESSION_SCOPE);

        JspWriter out = getJspContext().getOut();

        try {
            if (user == null) {
                //not logged
                out.println("<h1>asdsad</h1> <fmt:message key='search'/>");
                out.flush();
            } else {
                //logged
                out.println("<h1>asdsad</h1> <fmt:message key='search'/>");
            }
        } catch (IOException es) {
            es.printStackTrace();
        }

    }

    private String getLocalizedString(final String key) {
        PageContext pageContext = (PageContext) getJspContext();
        String value = LocaleSupport.getLocalizedMessage(pageContext, key, "bundle.buyhubBundle");

        return value;
    }

}
/**
 *
 * out.println("<p class=\"navbar-text navbar-right\">\n" + "
 * <a href=\"/login.jsp\" class=\"site-header-link\">"+getLocalizedString("login")+"</a>
 * "+getLocalizedString("or")+"
 * <a href=\"/signup.jsp\" class=\"site-header-link\">"+getLocalizedString("sign_up")+"</a>\n"
 * + "                    </p>");
 */
