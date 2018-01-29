package it.unitn.buyhub.tag;

import it.unitn.buyhub.dao.MessageDAO;
import it.unitn.buyhub.dao.TicketDAO;
import it.unitn.buyhub.dao.entities.Message;
import it.unitn.buyhub.dao.entities.Ticket;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Utility;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag used in ticket page to print messages
 *
 * @author Matteo Battilana
 */
public class MessagesTagHandler extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException {
        MessageDAO messageDao;
        TicketDAO ticketDao;

        JspWriter out = getJspContext().getOut();

        PageContext pageContext = (PageContext) getJspContext();
        //System.out.println(pageContext.getRequest().getParameter("error"));
        if (pageContext.getRequest().getParameter("id") != null) {
            User user = null;
            user = (User) getJspContext().getAttribute("authenticatedUser", PageContext.SESSION_SCOPE);
            int id = (Integer.parseInt(pageContext.getRequest().getParameter("id").toString()));

            DAOFactory daoFactory = (DAOFactory) getJspContext().getAttribute("daoFactory", PageContext.APPLICATION_SCOPE);
            if (daoFactory == null) {
                throw new JspTagException("Impossible to get dao factory for user storage system");
            }
            try {
                messageDao = daoFactory.getDAO(MessageDAO.class);
                ticketDao = daoFactory.getDAO(TicketDAO.class);

            } catch (DAOFactoryException ex) {
                throw new JspTagException("Impossible to get notificationDao for user storage system", ex);
            }
            //GET messages
            try {
                Ticket t = ticketDao.getByPrimaryKey(id);
               
                if (t.getOrder().getUser() == user || user.getCapability() == 3 || t.getOrder().getShop().getOwner().getId() == user.getId()) {
              
                    List<Message> messages = messageDao.getByTicket(t);
                    String list = "";

                    for (Message m : messages) {
                        list += "  <div class=\"container-chat " + ((m.getOwner().getId() == user.getId()) ? "darker-chat" : "") + "\">\n"
                                + "                    <img src=\"" + Utility.getUrl(pageContext, m.getOwner().getAvatar()) + "\" alt=\"Avatar\" " + ((m.getOwner().getId() == user.getId()) ? "class=\"right\"" : "") + " style=\"width:100%;\">\n"
                                + "                    <p>" + m.getContent() + "</p>\n"
                                + "                </div>";
                    }
                    out.println(list);
                    if (messages.size() == 0) {
                        out.println(Utility.getLocalizedString(pageContext, "ask"));
                    }
                } else {
                    //NESSUN PERMESSO
                    out.println(Utility.getLocalizedString(pageContext, "not_permission"));
                }
            } catch (Exception ex) {
            }
        }

    }

}
