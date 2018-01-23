package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.MD5;
import static it.unitn.buyhub.utils.MD5.getMD5Hex;
import it.unitn.buyhub.utils.Utility.CAPABILITY;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to login.
 * If successful set authenticatedUser session attribute,
 * if it is a shop user also set myshop, myPictures and mycoordinates
 * @author matteo
 */
public class LoginServlet extends HttpServlet {

    private UserDAO userDao;
    private ShopDAO shopDao;
    private PictureDAO pictureDAO;
    private ProductDAO productDAO;
    private CoordinateDAO coordinateDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UserDAO.class);
            shopDao = daoFactory.getDAO(ShopDAO.class);
            coordinateDAO=daoFactory.getDAO(CoordinateDAO.class);
            pictureDAO=daoFactory.getDAO(PictureDAO.class);
            productDAO=daoFactory.getDAO(ProductDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }

        Log.info("LoginServlet init done");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     * @author Matteo Battilana
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        //already in MD5 hash
        String password = request.getParameter("password");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            User user = userDao.getByUsernameAndPassord(username, MD5.getMD5Hex(password));
            if (user == null) {
                Log.warn("User not found");

                response.sendRedirect(response.encodeRedirectURL(contextPath + "login.jsp?error=1"));
            } else if (user.getCapability() == CAPABILITY.INVALID.ordinal()) {
                Log.warn("User must validate");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "login.jsp?error=2"));
            } else {
                Log.info("User " + user.getId() + " logged in");
                request.getSession().setAttribute("authenticatedUser", user);
                if (user.getCapability() == CAPABILITY.SHOP.ordinal()) {
                    Shop shop = (Shop) shopDao.getByOwner(user);
                    if (shop != null) {
                        request.getSession().setAttribute("myshop", shop);
                        List<Picture> pictures=pictureDAO.getByShop(shop);
                        List<Coordinate> coordinates=coordinateDAO.getByShop(shop);
                        List<Product> products=productDAO.getByShop(shop);
                        request.getSession().setAttribute("mypictures", pictures);
                        request.getSession().setAttribute("mycoordinates", coordinates);
                        request.getSession().setAttribute("myproducts", products);
                    }
                }
                String target = contextPath + "home.jsp";
                if (request.getSession().getAttribute("origin") != null) {
                    target = (String) request.getSession().getAttribute("origin");
                }
                request.getSession().removeAttribute("origin");
                response.sendRedirect(response.encodeRedirectURL(target));
            }

        } catch (DAOException ex) {
            Log.error("Error login");
        }
    }
}
