package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.AES;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Mailer;
import it.unitn.buyhub.utils.PropertyHandler;
import java.io.IOException;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet is called when a user request password recovery A link is sent
 * by email, see on the documentation to learn more on its construction in the
 * user validation section
 *
 * @author Massimo Girondi
 */
public class RequestPasswordChangeServlet extends HttpServlet {

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
//        Log.info("SignupServlet init done");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

        String email = request.getParameter("email");

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            if (email != null) {
                User user = userDao.getByEmail(email);
                if (user == null) {
                    Log.warn("User is not present in DB");
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "requestpassword.jsp?error=1"));
                }

                /*
                    Send a mail to the user to activate the account with a link composed of:
                    Encrypted id$MD5(password) with AES,
                    then encoded  to reppresent it in url
                 */
                PropertyHandler ph = PropertyHandler.getInstance();

                String linkMail = ph.getValue("baseUrl") + "changepassword.jsp?token=";

                //create the key with aes, Base64 and URLencode
                AES aes = new AES(ph.getValue("encodeKey"));
                String crypt = aes.encrypt(user.getId() + "$" + user.getPassword());
                linkMail += Base64.getUrlEncoder().encodeToString(crypt.getBytes());

                //build the message
                String msg = "Hi " + user.getFirstName() + ",\n<br/>";
                msg += "if you have required a password change, click on the button below.\n<br>";
                msg += "or click on this link: \n<br>";
                msg += "<a href='" + linkMail + "'>" + linkMail + "</a><br/>\n";
                msg += "If you have not requested a password change, just ignore this mail.\n<br>";

                //send the message
                Mailer.mail(ph.getValue("noreplyMail"), email, "Change your password on BuyHub", msg, linkMail, "Change your password");
                //Log.info("LINK: "+linkMail);
                Log.info("User" + user.getId() + " has requested password change");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "passwordchangerequested.jsp"));
            }
        } catch (Exception ex) {

            Log.error("Error changepassword: " + ex.getMessage());
            response.sendRedirect(response.encodeRedirectURL(contextPath + "requestpassword.jsp?error=2"));
        }
    }

    /**
     *
     * /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
