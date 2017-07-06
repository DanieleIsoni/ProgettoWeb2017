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
import java.text.SimpleDateFormat;
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
                //not logged, so show link to login and signup
                out.println(generateLoginAndSingupLink(request));

            } else {
                //logged, show user name and notification (counter + list)

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
                //print notification ad user info
                out.print(generateRightCornerUserInfo(userNotifications, request, user));

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

    private String generateLoginAndSingupLink(HttpServletRequest request) {
        return "<p class=\"navbar-text navbar-right\">\n"
                + "<a href=\"" + String.format("%s/login.jsp", request.getContextPath()) + "\" class=\"site-header-link\">" + getLocalizedString("login") + "</a>  " + getLocalizedString("or") + "  <a href=\"" + String.format("%s/signup.jsp", request.getContextPath()) + "\" class=\"site-header-link\">" + getLocalizedString("sign_up") + "</a>\n"
                + "</p>";
    }

    private String generateRightCornerUserInfo(List<Notification> userNotifications, HttpServletRequest request, User currentUser) {
        String out = "<ul class=\"nav navbar-nav navbar-right\">\n"
                + "<li data-html=\"true\" data-placement=\"bottom\"  title=\"" + getLocalizedString("notifications") + "\"  tabindex=\"0\" data-toggle=\"popover\" data-content=\"" + generateNotificationList(userNotifications, request) + "\">\n"
                + "<a href=\"#\"> \n"
                + "<div>\n";
        if (userNotifications.size() > 0) {
            out += "<div class=\"numberCircle\">" + userNotifications.size() + "</div>\n";
        }
        out += "<span class=\"glyphicon glyphicon-bell\" id=\"logIcon\" ></span>\n"
                + "</div>\n"
                + "</a>\n"
                + "</li>\n"
                + "<li><a href=\"#\" data-placement=\"bottom\" data-trigger=\"focus\" tabindex=\"0\" title=\"" + getLocalizedString("user") + "\" data-toggle=\"popover\">" + currentUser.getFirstName() + "</a></li>\n"
                + "</ul>";
        return out;
    }

    private String generateNotificationList(List<Notification> notifications, HttpServletRequest request) {

        //Generation all notification list
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String out = "";
        for (Notification n : notifications) {
            out += "<hr><div class='notification-element'>\n"
                    + "            <table  align='center'>\n"
                    + "                <tr>\n"
                    + "                    <th id='image' rowspan='2'>\n"
                    + "                        <img src='" + String.format("%s/images/icon.png", request.getContextPath()) + "' />\n"
                    + "                    </th>\n"
                    + "                    <th>" + n.getDescription() + "</th>\n"
                    + "                </tr>\n"
                    + "                <tr>\n"
                    + "                    <td>" + dateFormat.format(n.getDateCreation()) + "</td>\n"
                    + "                </tr>\n"
                    + "            </table>\n"
                    + "        </div>";
        }
        return out;
    }

}
