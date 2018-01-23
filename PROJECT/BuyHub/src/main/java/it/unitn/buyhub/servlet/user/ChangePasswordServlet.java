package it.unitn.buyhub.servlet.user;

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
import java.net.URLDecoder;
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
 * Servlet to allow password recovery
 * @author Massimo Girondi
 */
public class ChangePasswordServlet extends HttpServlet {

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
        Log.info("RecoverPasswordServlet init done");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     * @author Massimo Girondi
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String token = request.getParameter("token");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            if (password != "" && password2!="" && password.equals(password2) && token!="") {

                Log.info("received "+token);
                String key=URLDecoder.decode(token, "UTF-8").replace(" ", "+");
                AES aes=new AES(PropertyHandler.getInstance().getValue("encodeKey"));
                key=aes.decrypt(new String(Base64.getUrlDecoder().decode(key)));

                String[] params=key.split("\\$");//split on `$` (this symbol is a special char for regex, so I escape it before)

                int id=Integer.parseInt(params[0]);
                String password_token=params[1];
                User u=userDao.getByPrimaryKey(id);
               // Log.info(id+ " "+MD5.getMD5Hex(password)+" "+token +" ->"+ u.getPassword().equals(password_token));
                if(u!=null && u.getPassword().equals(password_token))
                {
                    u.setPassword(MD5.getMD5Hex(password));
                    try{
                        userDao.update(u);

                        Log.info("User "+id+" successfully changed password");

                        response.sendRedirect(response.encodeRedirectURL(contextPath + "login.jsp"));
                    }
                    catch(Exception ex)
                    {
                        Log.warn("Error updating user, maybe username or mail not unique?");
                        response.sendRedirect(response.encodeRedirectURL(contextPath + "common/error.jsp"));

                    }
                }
                else
                {
                    throw new  Exception("Error in password change");
                }
            } else if (!password.equals(password2)) {
                //Wrong password
                Log.warn("Passwords does not match");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "changepassword.jsp?error=1&token="+token));

            } else {
                //Missing parameter or worng token
                Log.warn("Missing paramenters during change password");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "changepassword.jsp?error=2&token="+token));

            }
        } catch (Exception ex) {

            Log.error("Error changepassword: "+ex.getMessage());
            response.sendRedirect(response.encodeRedirectURL(contextPath + "changepassword.jsp?error=2&token="+token));
        }
    }
}
