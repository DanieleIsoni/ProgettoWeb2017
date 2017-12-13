/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet.order;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.OrderDAO;
import it.unitn.buyhub.dao.OrderedProductDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Mailer;
import it.unitn.buyhub.utils.PropertyHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author massimo
 */
public class PayedServlet extends HttpServlet {

    private OrderDAO orderDAO;


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        try{
        
            HttpSession session = request.getSession(false);
            if (session != null && request.getParameter("orderid")!=null ) {
                Order o = orderDAO.getByPrimaryKey(Integer.valueOf(request.getParameter("orderid")));
                
                if(o.getUser().getId()!= ((User) request.getSession().getAttribute("authenticatedUser")).getId() || o.isPaid())
                        throw new Exception("Wrong user or payment already done");
                
                
                o.setPaid(true);
                if(request.getParameter("address")!= null &&request.getParameter("address").length()!=0)
                    o.setShipment(o.getShipment()+" @ "+request.getParameter("address"));
                
                orderDAO.update(o);
                User u=o.getUser();
                
                PropertyHandler ph=PropertyHandler.getInstance();

                String text="Hi "+u.getFirstName()+",\n<br/>"
                        + " Your order #"+o.getId()+" has been payed succesfully to "+o.getShop().getName()+"!"
                        + " You will receive an email when your order will be shipped or it's ready for pickup.";
                Mailer.mail(ph.getValue("noreplyMail"), u.getEmail(), "Your order has been payed", text,ph.getValue("baseUrl"),"Take a look on BuyHub");
                response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/user/myorders"));
            }
            else
            {
                throw new Exception("Missing parameter");
            }
        }catch(Exception ex)
        {
            Log.error("There was a problem during payment:"+ ex );
            Logger.getLogger("test").log(Level.SEVERE, null, ex);
            
            throw new ServletException();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
    @Override
    public void init() throws ServletException
    {DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for Order storage system");
            throw new ServletException("Impossible to get dao factory for order storage system");
        }
        try {
     
            orderDAO=daoFactory.getDAO(OrderDAO.class);
             } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for order storage system");
            throw new ServletException("Impossible to get dao factory for order storage system", ex);
        }
        
    }
}
