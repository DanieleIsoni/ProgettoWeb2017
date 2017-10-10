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
import it.unitn.buyhub.utils.AES;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.MD5;
import static it.unitn.buyhub.utils.MD5.getMD5Hex;
import it.unitn.buyhub.utils.Mailer;
import it.unitn.buyhub.utils.PropertyHandler;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for user storage system");
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
        Log.info("SignupServlet init done");
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
                newUser.setCapability(Utility.CAPABILITY.INVALID.ordinal());
                
                Long id = 0l;
                try{
                id= userDao.insert(newUser);
                }
                catch(Exception ex)
                {
                    Log.warn("Error inserting user, maybe username or mail not unique?");
                }
                
                if (id == 0) {
                    Log.warn("Username already used");
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "signup.jsp?error=1"));
                } else {
                    
                    /*
                    Send a mail to the user to activate the account with a link composed of:
                    Encrypted id$MD5(password) with AES, 
                    then encoded  to reppresent it in url
                    */
                    PropertyHandler ph=PropertyHandler.getInstance();

                    String linkMail=ph.getValue("baseUrl")+"verifyAccount?key=";
                    
                    //create the key with aes, Base64 and URLencode
                    AES aes=new AES(ph.getValue("encodeKey"));
                    String crypt=aes.encrypt(id+"$"+MD5.getMD5Hex(password));
                    linkMail+= Base64.getUrlEncoder().encodeToString(crypt.getBytes());
                    
                    //build the message
                    String msg="Welcome to BuyHub, "+first_name+"\n<br/>";
                    msg+="you need to verify your email to activate your account.\n<br>";
                    msg+="Click on the button below to proceed, or click on this link: \n<br>";
                    msg+="<a href='"+linkMail+"'>"+linkMail+"</a><br/>\n";
                    
                    //send the message
                    Mailer.mail(ph.getValue("noreplyMail"),email,"Verify your account on BuyHub",msg,linkMail,"Verify account");
                    Log.info("LINK: "+linkMail);
                    
                    Log.info("User "+ id +" correctly signed up" );
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "home.jsp"));
                }
            } else if (!password.equals(password2)) {
                //Wrong password
                Log.warn("Passwords does not match in sign up");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "signup.jsp?error=2"));

            } else {
                //Missing parameter
                Log.warn("Missing paramenters during sign up");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "signup.jsp?error=3"));

            }
        } catch (Exception ex) {
            
            Log.error("Error signupServlet: "+ex.getMessage());
        
        }
    }
}
