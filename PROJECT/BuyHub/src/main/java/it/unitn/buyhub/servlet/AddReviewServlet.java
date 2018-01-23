package it.unitn.buyhub.servlet;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
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
 * Allow a user to send a review
 * @author Matteo Battilana
 */
public class AddReviewServlet extends HttpServlet {

    private ReviewDAO reviewDao;
    private UserDAO userDao;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            reviewDao = daoFactory.getDAO(ReviewDAO.class);
            userDao = daoFactory.getDAO(UserDAO.class);
            productDAO = daoFactory.getDAO(ProductDAO.class);
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
        String description = (String) request.getParameter("description");
        String total = (String) request.getParameter("total");

        String productId = (String) request.getParameter("prod_id");
        String quality = (String) request.getParameter("quality");
        String service = (String) request.getParameter("service");
        String money = (String) request.getParameter("money");
        String title = (String) request.getParameter("title");
        User owner = (User) request.getSession().getAttribute("authenticatedUser");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            if (productId != null) {
                Product prod = productDAO.getByPrimaryKey(Integer.valueOf(productId));

                if (title != null && !title.equals("") && owner != null && description != null && total != null && quality != null && service != null && money != null
                        && !description.equals("") && !total.equals("") && !quality.equals("") && !service.equals("") && !money.equals("")) {

                    Review review = new Review();
                    review.setCreator(owner);
                    review.setDateCreation(new Date());
                    review.setDescription(description);
                    review.setGlobalValue(Integer.valueOf(total));
                    review.setProduct(prod);
                    review.setQuality(Integer.valueOf(quality));
                    review.setService(Integer.valueOf(service));
                    review.setTitle(title);
                    review.setValueForMoney(Integer.valueOf(money));
                    Long id = 0l;
                    try {
                        id = reviewDao.insert(review);
                    } catch (DAOException ex) {
                        Log.warn("Error inserting review, maybe already in the db");
                    }
                    System.out.println(id + "TEST");
                    if (id == 0) {
                        response.sendRedirect(response.encodeRedirectURL(contextPath + "product?id=" + productId + "&error=1"));

                        Log.error("Error adding review, already used");
                    } else {
                        response.sendRedirect(response.encodeRedirectURL(contextPath + "product?id=" + productId));
                    }

                } else {
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "product?id=" + productId));
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL(contextPath + "product?id=-1"));
            }
        } catch (Exception ex) {
            Log.error("Error adding review" + ex.getMessage().toString());
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
