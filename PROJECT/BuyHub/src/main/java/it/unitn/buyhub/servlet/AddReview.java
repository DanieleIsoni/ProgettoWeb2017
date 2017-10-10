/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import java.io.PrintWriter;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Daniso
 */
public class AddReview extends HttpServlet {
    
    private ReviewDAO reviewDao;
    private UserDAO userDao;
    
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            reviewDao = daoFactory.getDAO(ReviewDAO.class);
            userDao = daoFactory.getDAO(UserDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
        Log.info("CreateNewShopServlet init done");
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String name = (String) request.getAttribute("shopName");
        String description = (String) request.getAttribute("description");
        String website = (String) request.getAttribute("website");
        String shipment = (String) request.getAttribute("shipment");
        User owner = (User) request.getSession().getAttribute("authenticatedUser");
        String address = (String) request.getSession().getAttribute("autocomplete_address");
        String openingHours = (String) request.getSession().getAttribute("opening_hours");
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        try {
            if(name != null && !name.equals("") && 
                    description != null && !description.equals("") &&
                    website != null && !website.equals("") &&
                    shipment != null && !shipment.equals("") &&
                    owner != null &&
                    address != null && !address.equals("") &&
                    openingHours != null && !openingHours.equals("")){
                Shop newShop = new Shop();
                newShop.setDescription(description);
                newShop.setName(name);
                newShop.setOwner(owner);
                newShop.setWebsite(website);
                Long id = shopDao.insert(newShop);
                if (id == 0){
                    Log.warn("Shop name already used");
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "createNewShop.jsp?error=1"));
                }
            }
        } catch (DAOException ex) {
            
            Log.error("Error creating shop");
        }
    }

    
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
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }    

}
