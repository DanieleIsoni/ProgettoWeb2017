package it.unitn.buyhub.servlet.admin;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Mailer;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
/**
 *

  * Servlet that retrieves the data for the administrative users page
 * @author massimo
 */
public class UsersServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private UserDAO userDAO;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {




    }
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            userDAO = daoFactory.getDAO(UserDAO.class);

        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
//        Log.info("UsersServlet init done");
    }


    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }


        try {

           List<User> users=userDAO.getAll();
           request.setAttribute("users", users);

           request.getRequestDispatcher("users.jsp").forward(request, response);

        } catch (DAOException ex) {
            Log.error("Error getting product "+ ex.toString());
            response.sendRedirect(response.encodeRedirectURL(contextPath + "../../common/error.jsp"));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      process(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      process(req,resp);
    }

}
