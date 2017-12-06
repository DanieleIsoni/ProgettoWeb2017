/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet.order;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.OrderDAO;
import it.unitn.buyhub.dao.OrderedProductDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Cart;
import it.unitn.buyhub.dao.entities.CartElement;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.entities.OrderedProduct;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
public class PlaceOrderServlet extends HttpServlet {

    
    
    private ProductDAO productDAO;
    private CoordinateDAO coordinateDAO;
    private ShopDAO shopDAO;
    private OrderDAO orderDAO;
    private OrderedProductDAO orderedProductDAO;

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

        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        try{
        
        HttpSession session = request.getSession(false);
        if (session != null && request.getParameter("shopid")!=null && request.getParameter("shipment")!=null ) {
            
            Order o=new Order();
            o.setPaid(false);
            o.setUser_id( ((User) request.getSession().getAttribute("authenticatedUser")).getId());
            
            Shop s= shopDAO.getByPrimaryKey(Integer.valueOf(request.getParameter("shopid")));
            if(Integer.valueOf(request.getParameter("shipment"))==-1 && s.getShipment().length()>0)
            {
                o.setShipment(s.getShipment());
                o.setShipment_cost(s.getShipment_cost());
            }
            else
            {
                int c= Integer.valueOf(request.getParameter("shipment"));
                Coordinate coordinate = coordinateDAO.getByPrimaryKey(c);
                if(coordinate.getOpening_hours().length()==0 || coordinate.getShop().getId()!=s.getId())
                    throw  new Exception("Erorr: wrong shipment mode");
                else
                    o.setShipment("Pickup @ "+ coordinate.getAddress());
            }
            o.setShop(s);
            Cart cart = (Cart) session.getAttribute("userCart");
            
            HashMap<Integer, ArrayList<CartElement>> cartMap = cart.getProducts();
            ArrayList<CartElement> cartElems= cartMap.get(s.getId());
            if(cartElems!= null && cartElems.size()!=0)
            {
                for(CartElement ce: cartElems){
                    System.out.println("ID: "+ce.getId());
                    o.add(productDAO.getByPrimaryKey(ce.getId()),ce.getNumber());
                }

                int oid = orderDAO.insert(o).intValue();
                o.setId(oid);
                //INSERT PRODUCTS FROM HERE (ORDERDAO DOESN'T INSERT-> it generated errors) 
                for (OrderedProduct op: o.getProducts()) {
                    orderedProductDAO.insert(op);
                }

                cart.removeIf(cartElems);
                session.setAttribute("userCart", cart);

                request.setAttribute("order", o);
                
                
           
                request.getRequestDispatcher("../payment.jsp").forward(request, response);
                //response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/payment.jsp?orderid="+oid));
            }
            else
                throw  new Exception("Erorr: no product to buy");    
        }           
        response.sendRedirect(response.encodeRedirectURL(contextPath + "cart.jsp"));
        }catch (Exception ex) {
            System.out.println(ex);
        
            //Logger.getLogger(PlaceOrderServlet.class.getName()).log(Level.SEVERE, null, ex);
            Log.error("Error placing order: "+ex);
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
    }// </editor-fold>// </editor-fold>// </editor-fold>// </editor-fold>
    
    
    public void init() throws ServletException {
      DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for Order storage system");
            throw new ServletException("Impossible to get dao factory for order storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            coordinateDAO=daoFactory.getDAO(CoordinateDAO.class);
            orderDAO=daoFactory.getDAO(OrderDAO.class);
            orderedProductDAO=daoFactory.getDAO(OrderedProductDAO.class);
            shopDAO=daoFactory.getDAO(ShopDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for order storage system");
            throw new ServletException("Impossible to get dao factory for order storage system", ex);
        }
        
    }
}
