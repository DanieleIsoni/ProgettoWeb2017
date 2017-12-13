/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.NotificationDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Cart;
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
import it.unitn.buyhub.utils.Utility;

public class NavbarTagHandler extends SimpleTagSupport {

    PageContext pageContext;

    @Override

    public void doTag() throws JspException {
        User user = null;
        user = (User) getJspContext().getAttribute("authenticatedUser", PageContext.SESSION_SCOPE);

        JspWriter out = getJspContext().getOut();
        pageContext = (PageContext) getJspContext();
        Cart cart = (Cart) getJspContext().getAttribute("userCart", PageContext.SESSION_SCOPE);
        try {
            if (user == null) {
                //not logged, so show link to login and signup
                out.println(generateLoginAndSingupLink());
                //Getting user cart

                if (cart == null) {
                    throw new JspTagException("Impossible to get cart for user storage system");
                } else {
                    out.println(generateCart(cart));
                }

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
                if (cart == null) {
                    throw new JspTagException("Impossible to get cart for user storage system");
                } else {
                    out.println(generateCart(cart));
                }
                out.print(generateRightCornerUserInfo(userNotifications, user));

            }
        } catch (IOException es) {
            es.printStackTrace();
        }

    }

    private String generateLoginAndSingupLink() {
        return "<p class=\"navbar-text navbar-right\">\n"
                + "<a href=\"" + Utility.getUrl(pageContext, "login.jsp") + "\" class=\"site-header-link\">" + Utility.getLocalizedString(pageContext, "login") + "</a>  " + Utility.getLocalizedString(pageContext, "or") + "  <a href=\"" + Utility.getUrl(pageContext, "signup.jsp") + "\" class=\"site-header-link\">" + Utility.getLocalizedString(pageContext, "sign_up") + "</a>\n"
                + "</p>";
    }

    private String generateRightCornerUserInfo(List<Notification> userNotifications, User currentUser) {
        String out = "<ul class=\"nav navbar-nav navbar-right\">\n"
                + "<li data-html=\"true\" data-trigger=\"focus\" data-placement=\"bottom\"  title=\"" + Utility.getLocalizedString(pageContext, "notifications") + "\"  tabindex=\"0\" data-toggle=\"popover\" data-content=\"" + generateNotificationList(userNotifications) + "\">\n"
                + "<a href=\"#\"> \n"
                + "<div>\n";
        if (userNotifications.size() > 0) {
            out += "<div class=\"numberCircle\">" + userNotifications.size() + "</div>\n";
        }
        out += "<span class=\"glyphicon glyphicon-bell\" id=\"logIcon\" ></span>\n"
                + "</div>\n"
                + "</a>\n"
                + "</li>\n"
                + "<li class=\"user-popover\" ><a href=\"#\" data-html=\"true\" data-placement=\"bottom\" data-content=\"" + generateUserList(currentUser) + "\" data-trigger=\"focus\" tabindex=\"0\" title=\"" + Utility.getLocalizedString(pageContext, "user") + "\" data-toggle=\"popover\">" + currentUser.getFirstName() + "</a></li>\n"
                + "</ul>";
        return out;
    }

    private String generateCart(Cart cart) {
        String out = "<ul class=\"nav navbar-nav navbar-right\">\n";
        out += "<li>\n"
                + "<a href=\"" + Utility.getUrl(pageContext, "cart.jsp") + "\"> \n"
                + "<div>\n";
        if (cart.getCount() > 0) {
            out += "<div class=\"numberCircle\">" + cart.getCount() + "</div>\n";
        }
        out += "<span class=\"glyphicon glyphicon-shopping-cart\" id=\"logIcon\" ></span>\n"
                + "</div>\n"
                + "</a>\n"
                + "</li>\n"
                + "</ul>";
        return out;
    }

    private String generateUserList(User currentUser) {
        String dropdown = "<a href='" + Utility.getUrl(pageContext, "restricted/myself.jsp") + "' class='btn btn-primary user' role='button'>" + Utility.getLocalizedString(pageContext, "user_page") + "</a>";
        if (currentUser.getCapability() >= Utility.CAPABILITY.SHOP.ordinal()) {
            dropdown += "<a href='" + Utility.getUrl(pageContext, "restricted/myshop.jsp") + "' class='btn btn-success user' role='button'>" + Utility.getLocalizedString(pageContext, "myshop_page") + "</a>";
        }
        if (currentUser.getCapability() >= Utility.CAPABILITY.ADMIN.ordinal()) {
            dropdown += "<a href='" + Utility.getUrl(pageContext, "restricted/admin/shops") + "' class='btn btn-warning user' role='button'>" + Utility.getLocalizedString(pageContext, "shops_management") + "</a>";
            dropdown += "<a href='" + Utility.getUrl(pageContext, "restricted/admin/users") + "' class='btn btn-warning user' role='button'>" + Utility.getLocalizedString(pageContext, "users_management") + "</a>";
        }
        dropdown += "<a href='" + Utility.getUrl(pageContext, "logout") + "' class='btn btn-danger user' role='button'>" + Utility.getLocalizedString(pageContext, "logout") + "</a>";

        return dropdown;
    }

    private String generateNotificationList(List<Notification> notifications) {

        //Generation all notification list
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String out = "";
        if (notifications.size() == 0) {
            out += "<div class='no-notification'>" + Utility.getLocalizedString(pageContext, "no_notification") + "</div>";
        }
        for (Notification n : notifications) {
            out += "<hr><div class='notification-element'>\n"
                    + "            <table  align='center'>\n"
                    + "                <tr>\n"
                    + "                    <th id='image' rowspan='2'>\n"
                    + "                        <img src='" + Utility.getUrl(pageContext, "images/icon.png") + "' />\n"
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
