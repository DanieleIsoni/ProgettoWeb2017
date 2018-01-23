package it.unitn.buyhub.servlet;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.MessageDAO;
import it.unitn.buyhub.dao.NotificationDAO;
import it.unitn.buyhub.dao.OrderDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.TicketDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Message;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.Ticket;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Mailer;
import it.unitn.buyhub.utils.PropertyHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.Session;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet to send a message inside a ticket.
 * @author Matteo Battilana
 */
public class sendMessageServlet extends HttpServlet {

    private UserDAO userDao;
    private TicketDAO ticketDao;
    private MessageDAO messageDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            ticketDao = daoFactory.getDAO(TicketDAO.class);
            userDao = daoFactory.getDAO(UserDAO.class);
            messageDao = daoFactory.getDAO(MessageDAO.class);

        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
//        Log.info("CreateNewShopServlet init done");
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String textMessage = (String) request.getParameter("text");

        String ticketId = (String) request.getParameter("ticket_id");
        User owner = (User) request.getSession().getAttribute("authenticatedUser");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            if (owner != null && textMessage != null && !textMessage.equals("") && ticketId != null && !ticketId.equals("")) {
                Ticket t = ticketDao.getByPrimaryKey(Integer.valueOf(ticketId));
                Message newMessage = new Message();
                newMessage.setContent(textMessage);
                newMessage.setOwner(owner);
                newMessage.setTicket(t);
                messageDao.insert(newMessage);

            }
        } catch (Exception ex) {
            Log.error("Error sending message" + ex.getMessage().toString());
        }
        response.sendRedirect(request.getHeader("referer"));
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
