/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.UserDAO;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author matteo
 */
public class LoginServlet extends HttpServlet {

    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UserDAO.class);
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
            } else if(user.getCapability()==CAPABILITY.INVALID.ordinal()) {
             Log.warn("User must validate");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "login.jsp?error=2"));
            }
            else
            {
                Log.info("User " + user.getId() + " logged in");
                request.getSession().setAttribute("authenticatedUser", user);
                response.sendRedirect(response.encodeRedirectURL(contextPath + "home.jsp"));
            }
            
        } catch (DAOException ex) {
            Log.error("Error login");
        }
    }
}
