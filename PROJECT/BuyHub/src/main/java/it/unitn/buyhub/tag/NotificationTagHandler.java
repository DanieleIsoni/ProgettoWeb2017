package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.NotificationDAO;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag to print the notification menu
 *
 * @author Matteo Battilana
 */
public class NotificationTagHandler extends SimpleTagSupport {

    PageContext pageContext;

    @Override

    public void doTag() throws JspException {
        User user = null;
        user = (User) getJspContext().getAttribute("authenticatedUser", PageContext.SESSION_SCOPE);

        JspWriter out = getJspContext().getOut();
        pageContext = (PageContext) getJspContext();

        try {
            if (user != null) {

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
                    userNotifications = notificationDao.getAllByUser(user);
                } catch (DAOException ex) {
                    throw new JspTagException("Impossible to get unread notification user storage system", ex);
                }

                out.print(generateNotificationList(userNotifications));

            }
        } catch (IOException es) {

            throw new JspException("There was an error during taghandler");
            //es.printStackTrace();
        }

    }

    private String generateNotificationList(List<Notification> notifications) {

        //Generation all notification list
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String out = "";
        if (notifications.size() == 0) {
            out += "<div class='no-notification'>" + Utility.getLocalizedString(pageContext, "no_notification") + "</div>";
        }
        for (Notification n : notifications) {
            out += "<tr><td><hr><div class='notification-element' style='word-break:break-all;'>\n"
                    + "            <table >\n"
                    + "                <tr >\n"
                    + "                    <th id='image' rowspan='2'>\n"
                    + "                        <img src='" + Utility.getUrl(pageContext, "images/icon.png") + "' />\n"
                    + "                    </th>\n"
                    + "                    <th>" + n.getDescription() + "</th>\n"
                    + (!(n.isStatus()) ? "                    <th><a class='glyphicon glyphicon-remove' id='logIcon' href = '" + Utility.getUrl(pageContext, "removenotification") + "?id_notification=" + n.getId() + "'></a></th>" : "")
                    + "                </tr>\n"
                    + "                <tr >\n"
                    + "                    <td style=\" text-align: left; \">" + dateFormat.format(n.getDateCreation()) + "</td>\n"
                    + "                </tr>\n"
                    + "            </table>\n"
                    + "        </div></td></tr>";
        }
        return out;
    }

}
