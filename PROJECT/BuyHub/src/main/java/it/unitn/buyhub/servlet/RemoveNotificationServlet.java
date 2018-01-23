
package it.unitn.buyhub.servlet;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.NotificationDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allow the user to delete a notification (invoked by the X on any notification)
 * @author Matteo Battilana
 */
public class RemoveNotificationServlet extends HttpServlet {

    private NotificationDAO notificationDao;
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for notification storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            notificationDao = daoFactory.getDAO(NotificationDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for notification storage system");
            throw new ServletException("Impossible to get dao factory for notification storage system", ex);
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

        String notificationId = (String) request.getParameter("id_notification");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            if(notificationId!=null && !notificationId.equals("")){
                Notification not = notificationDao.getByPrimaryKey(Integer.valueOf(notificationId));
                not.setStatus(true);
                notificationDao.update(not);
            }


        } catch (Exception ex) {
            Log.error("Error remove notification" + ex.getMessage().toString());
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
