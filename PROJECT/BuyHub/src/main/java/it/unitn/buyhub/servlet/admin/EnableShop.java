package it.unitn.buyhub.servlet.admin;

import it.unitn.buyhub.dao.NotificationDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Mailer;
import it.unitn.buyhub.utils.PropertyHandler;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet changes the status of a shop from enabled to disable and
 * viceversa It changes also the capability of the associated user
 *
 * @author Massimo Girondi
 */
public class EnableShop extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private ShopDAO shopDAO;
    private UserDAO userDAO;
    private NotificationDAO notificationDAO;
    private ProductDAO productDAO;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            shopDAO = daoFactory.getDAO(ShopDAO.class);
            userDAO = daoFactory.getDAO(UserDAO.class);
            notificationDAO = daoFactory.getDAO(NotificationDAO.class);
            productDAO = daoFactory.getDAO(ProductDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
        //Log.info("EnableShop init done");
    }

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        try {

            if (request.getParameter("id") != null && request.getParameter("status") != null) {
                int id = Integer.parseInt(request.getParameter("id"));
                int status = Integer.parseInt(request.getParameter("status"));

                Shop shop = shopDAO.getByPrimaryKey(id);
                List<Product> products = productDAO.getByShop(shop);

                if (status == 1 && shop.getOwner().getCapability() < Utility.CAPABILITY.SHOP.ordinal()) {
                    shop.setValidity(status);
                    User u = shop.getOwner();
                    u.setCapability(Utility.CAPABILITY.SHOP.ordinal());
                    userDAO.update(u);
                    Log.info("User " + id + " promoted to shop user ");
                    
                    shopDAO.update(shop);
                    Log.info("Shop " + id + ": validity set to " + status);
                    
                    PropertyHandler ph = PropertyHandler.getInstance();
                    String noreply = ph.getValue("noreplyMail");
                    String baseurl = ph.getValue("baseUrl");
                    
                    String body = "Hi " + shop.getOwner().getFirstName() + ",\n<br/"
                            + "your shop on BuyHub has been enabled."
                            + "<br/> Now you can sell products!";
                    Mailer.mail(noreply, shop.getOwner().getEmail(), "Shop enabled", body, baseurl, "Take a look on BuyHub");

                    Notification not = new Notification();
                    not.setUser(shop.getOwner());
                    not.setStatus(false);
                    not.setDateCreation(new Date());
                    not.setDescription("Shop enabled!");
                    notificationDAO.insert(not);
                    
                    response.sendRedirect(response.encodeRedirectURL("shops"));

                } else if (status == 0 && shop.getOwner().getCapability() == Utility.CAPABILITY.SHOP.ordinal() && (products == null || products.size() == 0)){
                    shop.setValidity(status);
                    Log.info(shop.getValidity());
                    User u = shop.getOwner();
                    u.setCapability(Utility.CAPABILITY.USER.ordinal());
                    userDAO.update(u);
                    Log.info("User " + id + " degraded to normal user ");
                    
                    shopDAO.update(shop);
                    Log.info("Shop " + id + ": validity set to " + status);
                    
                    PropertyHandler ph = PropertyHandler.getInstance();
                    String noreply = ph.getValue("noreplyMail");
                    String baseurl = ph.getValue("baseUrl");
                    
                    String body = "Hi " + shop.getOwner().getFirstName() + ",\n<br/"
                            + "your shop on BuyHub has been disabled.";
                    Mailer.mail(noreply, shop.getOwner().getEmail(), "Shop disabled", body, baseurl, "Take a look on BuyHub");

                    Notification not = new Notification();
                    not.setUser(shop.getOwner());
                    not.setStatus(false);
                    not.setDateCreation(new Date());
                    not.setDescription("Shop disabled!");
                    notificationDAO.insert(not);
                    
                    response.sendRedirect(response.encodeRedirectURL("shops"));
                } else {
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "../../common/error.jsp"));
                }
            }
        } catch (DAOException ex) {
            Log.error("Error getting product " + ex.toString());
            response.sendRedirect(response.encodeRedirectURL(contextPath + "../../common/error.jsp"));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

}
