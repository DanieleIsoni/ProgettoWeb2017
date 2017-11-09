/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Mailer;
import it.unitn.buyhub.utils.PropertyHandler;
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
public class CreateNewShopServlet extends HttpServlet {
    
    private ShopDAO shopDao;
    private CoordinateDAO coordinateDao;
    
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            shopDao = daoFactory.getDAO(ShopDAO.class);
            coordinateDao = daoFactory.getDAO(CoordinateDAO.class);
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
        String name = (String) request.getParameter("shopName");
        String description = (String) request.getParameter("description");
        String website = (String) request.getParameter("website");
        String shipment = (String) request.getParameter("shipment");
        User owner = (User) request.getSession().getAttribute("authenticatedUser");
        String address = (String) request.getParameter("autocomplete_address");
        String openingHours = (String) request.getParameter("opening_hours");
        String longitude = (String) request.getParameter("longitude");
        String latitude = (String) request.getParameter("latitude");
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        try {
            if(name != null && !name.equals("") && 
                    description != null && !description.equals("") &&
                    website != null && !website.equals("") &&
                    shipment != null && !shipment.equals("") &&
                    owner != null ){
                Shop newShop = new Shop();
                newShop.setDescription(description);
                newShop.setName(name);
                newShop.setOwner(owner);
                newShop.setWebsite(website);
                newShop.setValidity(0);
                newShop.setShipment(shipment);
                Long shop_id = shopDao.insert(newShop);
                if (shop_id == 0){
                    Log.warn("Shop name already used");
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "createNewShop.jsp?error=1"));
                } else {
                    Log.info("Shop inserted correctly");
                    newShop.setId(shop_id.intValue());
                    if(address != null && !address.equals("") &&
                            openingHours != null && !openingHours.equals("")){
                        Coordinate newcoordinate = new Coordinate();
                        newcoordinate.setAddress(address);
                        newcoordinate.setOpening_hours(openingHours);
                        newcoordinate.setShop(newShop);
                        newcoordinate.setLatitude(Double.valueOf(latitude));
                        newcoordinate.setLongitude(Double.valueOf(longitude));
                        Long coordinate_id = coordinateDao.insert(newcoordinate);
                        if (coordinate_id == 0){
                            Log.warn("Coordinate not inserted in Database");
                        } else {
                            Log.info("Coordinates inserted correctly");
                        }
                    }
                }
                String msg="Dear admin,\n<br/> a new shop has been created and need to be validated";
                String linkMail = PropertyHandler.getInstance().getValue("baseUrl")+"restricted/admin/shops";
                Mailer.mailToAdmins(PropertyHandler.getInstance().getValue("noreplyMail"), "New shop created", msg, linkMail,"Go to Shops Manager", super.getServletContext());
                
                response.sendRedirect(contextPath + "restricted/myself.jsp");
            }
        } catch (DAOException ex) {
            
            Log.error("Error creating shop "+ ex);
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
