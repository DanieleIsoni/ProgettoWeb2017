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
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Daniele Isoni
 */
public class ModifyAccountServlet extends HttpServlet {

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
        
        Log.info("ModifyAccountServlet init done");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     * @author Daniele Isoni
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String oldPassword = request.getParameter("old_password");
        String newPassword = request.getParameter("new_password");
        String newPassword2 = request.getParameter("new_password2");
       
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        User user = (User) request.getSession().getAttribute("authenticatedUser");

        if (user != null && (MD5.getMD5Hex(oldPassword)).equals(user.getPassword()) && newPassword != null && newPassword2 != null && newPassword.equals(newPassword2)) {
            user.setPassword(MD5.getMD5Hex(newPassword));
            try {
                userDao.update(user);
                Log.info("User "+ user.getId() + " updated");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/myself.jsp"));
            } catch (DAOException ex) {
                Log.error("Error updating user");
            }
        } else if (newPassword!=null && newPassword2!=null && !newPassword.equals(newPassword2)) {
            //Wrong new password
            response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/modifyAccount.jsp?error=1"));

        } else {
            response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/modifyAccount.jsp?error=2"));
        }
    }
}
