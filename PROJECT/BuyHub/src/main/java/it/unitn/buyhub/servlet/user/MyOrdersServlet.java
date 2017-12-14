/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.servlet.admin.*;
import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.OrderDAO;
import it.unitn.buyhub.dao.OrderDAO;
import it.unitn.buyhub.dao.OrderedProductDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.entities.OrderedProduct;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Mailer;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
/**
 *
 * @author massimo
 */
public class MyOrdersServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private OrderDAO orderDAO;
    private OrderedProductDAO orderedProductDAO;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {




    }
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for order storage system");
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            orderDAO = daoFactory.getDAO(OrderDAO.class);
            orderedProductDAO = daoFactory.getDAO(OrderedProductDAO.class);
            
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for order storage system");
            throw new ServletException("Impossible to get dao factory for order storage system", ex);
        }
        Log.info("OrdersServlet init done");
    }


    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }


        try {

           User u=(User) request.getSession().getAttribute("authenticatedUser"); 
           
           List<Order> orders=orderDAO.getByUser(u.getId());
           
           request.setAttribute("orders", orders);
           List<Double> totals=new  ArrayList<Double>();
           for(Order o: orders)
           {
               List<OrderedProduct> byOrder = orderedProductDAO.getByOrder(o.getId());
               double sum=o.getShipment_cost();
               for (OrderedProduct orderedProduct : byOrder) {
                   sum+=orderedProduct.getQuantity()*orderedProduct.getPrice();
               }
               totals.add(sum);
           }
           request.setAttribute("totals", totals);
           
           request.getRequestDispatcher("myorders.jsp").forward(request, response);
            
        } catch (DAOException ex) {
            Log.error("Error getting orders: "+ ex.toString());
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
