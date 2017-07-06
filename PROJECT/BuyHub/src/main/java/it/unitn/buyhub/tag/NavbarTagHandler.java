/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.NotificationDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
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
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        try {
            if (user == null) {
                //not logged
                out.println("<p class=\"navbar-text navbar-right\">\n"
                        + "                        <a href=\"" + String.format("%s/login.jsp", request.getContextPath()) + "\" class=\"site-header-link\">" + getLocalizedString("login") + "</a>  " + getLocalizedString("or") + "  <a href=\"" + String.format("%s/signup.jsp", request.getContextPath()) + "\" class=\"site-header-link\">" + getLocalizedString("sign_up") + "</a>\n"
                        + "                    </p>");

            } else {
                //logged
                NotificationDAO notificationDao;
                List<Notification> userNotifications = new ArrayList<>();

                DAOFactory daoFactory = (DAOFactory) getJspContext().getAttribute("daoFactory", PageContext.APPLICATION_SCOPE);
                if (daoFactory == null) {
                    throw new JspTagException("Impossible to get dao factory for user storage system");
                }
                try {
                    notificationDao = daoFactory.getDAO(NotificationDAO.class);
                } catch (DAOFactoryException ex) {
                    throw new JspTagException("Impossible to get notificationDao for user storage system", ex);
                }

                try {
                    userNotifications = notificationDao.getUnreadByUser(user);
                } catch (DAOException ex) {
                    throw new JspTagException("Impossible to get unread notification user storage system", ex);
                }

                out.println("<ul class=\"nav navbar-nav navbar-right\">                        \n"
                        + "                        <li>\n"
                        + "                            <a href=\"#\"> \n"
                        + "                                <div>\n");
                if (userNotifications.size() > 0) {
                    out.println("                                    <div class=\"numberCircle\">" + userNotifications.size() + "</div>\n");
                }
                out.println("                                    <span class=\"glyphicon glyphicon-bell\" id=\"logIcon\"></span>\n"
                        + "                                </div>\n"
                        + "                            </a>\n"
                        + "\n"
                        + "                        </li>\n"
                        + "                        <li><a href=\"#\">"+user.getFirstName()+"</a></li>\n"
                        + "                    </ul>");
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
