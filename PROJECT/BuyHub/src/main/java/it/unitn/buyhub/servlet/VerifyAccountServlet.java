/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.AES;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.PropertyHandler;
import it.unitn.buyhub.utils.Utility;
import java.util.Base64;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is the servlet where the user come pressing the link in the verification mail
 * 
 * @author massimo
 */
public class VerifyAccountServlet extends HttpServlet {

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
        Log.info("VerifyAccountServlet init done");
    }
    
    
    /** Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        if(request.getParameter("key")!=null && request.getParameter("key")!="")
        {
            //decode the URL, decode the BASE64 reppresentation and decrypt with AES
            String encoded=(String) request.getParameter("key");
            String key=URLDecoder.decode(encoded, "UTF-8").replace(" ", "+");
            AES aes=new AES(PropertyHandler.getInstance().getValue("encodeKey"));
            key=aes.decrypt(new String(Base64.getUrlDecoder().decode(key)));
            
            
            String[] params=key.split("\\$");//split on `$` (this symbol is a special char for regex, so I escape it before)
            
            
            int id=Integer.parseInt(params[0]);
            String password=params[1];
            
            //check if the credentials are correct
            User u= userDao.getByPrimaryKey(id);
            if(u==null || u.getPassword().compareTo(password)!=0)
                throw new Exception("Error retrieving user");
            
            //allow the user to login
            u.setCapability(Utility.CAPABILITY.USER.ordinal());
            userDao.update(u);
            Log.info("User "+u.getId()+" successfully verified: ");
            
            //authenticate the user
            request.getSession().setAttribute("authenticatedUser", u);
            
            //forward request to homepage
            String contextPath = getServletContext().getContextPath();
            if (!contextPath.endsWith("/")) {
                contextPath += "/";
            }
            response.sendRedirect(response.encodeRedirectURL(contextPath + "home.jsp"));
        }
       
        } catch (Exception ex) {
            Log.info("Error in verifing user: "+ex.toString());
            throw new ServletException("Error verifing user");
        }
                
                
                
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
