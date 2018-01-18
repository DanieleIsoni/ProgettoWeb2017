/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Daniso
 */
public class OpenTicketServlet extends HttpServlet {

    private UserDAO userDao;
    private OrderDAO orderDao;
    private TicketDAO ticketDao;
    private NotificationDAO notificationDao;
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
            orderDao = daoFactory.getDAO(OrderDAO.class);
            notificationDao = daoFactory.getDAO(NotificationDAO.class);
            
            messageDao = daoFactory.getDAO(MessageDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
        Log.info("CreateNewShopServlet init done");
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

        String productId = (String) request.getParameter("id_order");
        User owner = (User) request.getSession().getAttribute("authenticatedUser");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            if (owner != null && productId != null && !productId.equals("")) {
                Order order = orderDao.getByPrimaryKey(Integer.valueOf(productId));
                if (order.getUser() == owner || owner.getCapability() == 3 || order.getShop().getOwner() == owner) {
                    Ticket tnew;
                    try {
                        tnew = ticketDao.getByOrder(order);
                    } catch (Exception ex2) {
                        //MUST CREATE NEW
                        tnew = new Ticket();
                        tnew.setOrder(order);
                        ticketDao.insert(tnew);
                        
                        

                        //SEND MAIL NEW TICKET
                        PropertyHandler ph = PropertyHandler.getInstance();
                        Mailer.mail(ph.getValue("noreplyMail"), owner.getEmail(), "Opened ticket", "Opened ticket", "http://localhost:8080/BuyHub/restricted/ticket.jsp?id=" + tnew.getId(), "Take a look on BuyHub");
                        Mailer.mail(ph.getValue("noreplyMail"), order.getShop().getOwner().getEmail(), "Opened ticket", "Opened ticket", "http://localhost:8080/BuyHub/restricted/ticket.jsp?id=" + tnew.getId(), "Take a look on BuyHub");
                        Mailer.mailToAdmins(ph.getValue("noreplyMail"), "Opened ticket", "Opened ticket", "http://localhost:8080/BuyHub/restricted/ticket.jsp?id=" + tnew.getId(), "Take a look on BuyHub", getServletContext());
                        //ADD notification to shop

                        Notification not = new Notification();
                        not.setUser(owner);
                        not.setStatus(false);
                        not.setDateCreation(new Date());
                        not.setDescription("Opened new ticket, check your email");
                        notificationDao.insert(not);
                        not.setUser(order.getShop().getOwner());
                        notificationDao.insert(not);

                    }
                    
                
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/ticket.jsp?id="+tnew.getId()));
                }
                response.sendRedirect(request.getHeader("referer"));
            }
        } catch (Exception ex) {
            Log.error("Error adding ticket" + ex.getMessage().toString());
        }

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
