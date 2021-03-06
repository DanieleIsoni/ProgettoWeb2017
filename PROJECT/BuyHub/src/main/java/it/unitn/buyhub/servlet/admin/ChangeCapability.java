package it.unitn.buyhub.servlet.admin;

import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is used to change users capability by admins
 *
 * @author Massimo Girondi
 */
public class ChangeCapability extends HttpServlet {

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
    private ShopDAO shopDAO;

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
            shopDAO = daoFactory.getDAO(ShopDAO.class);

        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
        //Log.info("UsersServlet init done");
    }

    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {

            if (request.getParameter("id") != null && request.getParameter("capability") != null
                    && Integer.parseInt(request.getParameter("id")) > 0
                    && Integer.parseInt(request.getParameter("capability")) >= 0
                    && Integer.parseInt(request.getParameter("capability")) < Utility.CAPABILITY.values().length) {

                User u = userDAO.getByPrimaryKey(Integer.parseInt(request.getParameter("id")));
                if (u.getId() != ((User) request.getSession().getAttribute("authenticatedUser")).getId()) {
                    if (u.getCapability() == Utility.CAPABILITY.SHOP.ordinal() && shopDAO.getByOwner(u) != null) {
                        throw new Exception("User has shops!");
                    }

                    u.setCapability(Integer.parseInt(request.getParameter("capability")));
                    userDAO.update(u);
                }

                response.sendRedirect(response.encodeRedirectURL("./users"));

            } else {
                throw new Exception("Error parameter");
            }

        } catch (Exception ex) {
            Log.error("Error changing user permission" + ex.toString());
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
