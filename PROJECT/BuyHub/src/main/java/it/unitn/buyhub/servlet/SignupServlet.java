/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet;

import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.MD5;
import static it.unitn.buyhub.utils.MD5.getMD5Hex;
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
public class SignupServlet extends HttpServlet {

    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
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
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String email = request.getParameter("email");
       
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            if (username != null && password != null && password2 != null && first_name != null && last_name != null && email != null && password.equals(password2)) {
                User newUser = new User();
                newUser.setCapability(0);
                newUser.setEmail(email);
                newUser.setFirstName(first_name);
                newUser.setLastName(last_name);
                newUser.setPassword(MD5.getMD5Hex(password));
                newUser.setUsername(username);

                Long id = userDao.insert(newUser);
                if (id == 0) {
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "signup.jsp?error=1"));
                } else {
                    request.getSession().setAttribute("authenticatedUser", newUser);
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "home.jsp"));
                }
            } else if (!password.equals(password2)) {
                //Wrong password
                response.sendRedirect(response.encodeRedirectURL(contextPath + "signup.jsp?error=2"));

            } else {
                //Missing parameter
                response.sendRedirect(response.encodeRedirectURL(contextPath + "signup.jsp?error=3"));

            }
        } catch (DAOException ex) {
            //TODO: log exception
            System.out.println("Error login");
        }
    }
}
