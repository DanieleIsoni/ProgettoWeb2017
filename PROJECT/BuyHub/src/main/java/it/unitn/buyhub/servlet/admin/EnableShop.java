/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet.admin;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Mailer;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
/**
 *
 * @author massimo
 */
public class EnableShop extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private ShopDAO shopDAO;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {




    }
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            shopDAO = daoFactory.getDAO(ShopDAO.class);
            
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
        Log.info("EnableShop init done");
    }


    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        try {
        
            if(request.getParameter("id")!=null && request.getParameter("status")!=null)
            {
                int id=Integer.parseInt(request.getParameter("id"));
                char status=request.getParameter("status").charAt(0);

            
                Shop shop = shopDAO.getByPrimaryKey(id);
                shop.setValidity(status=='1'? 1 : 0);//if not 1 is false-> don't enable shop
                shopDAO.update(shop);
                Log.info("Shop "+id+": validity set to "+status);
                
                response.sendRedirect(response.encodeRedirectURL("shops"));
            }
         
            
        } catch (DAOException ex) {
            Log.error("Error getting product "+ ex.toString());
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
