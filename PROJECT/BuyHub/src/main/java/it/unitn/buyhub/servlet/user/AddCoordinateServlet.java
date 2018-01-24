/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Daniele Isoni
 */
public class AddCoordinateServlet extends HttpServlet {

    private ShopDAO shopDAO;
    private CoordinateDAO coordinateDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for coordinate storage system");
            throw new ServletException("Impossible to get dao factory for coordinate storage system");
        }
        try {
            shopDAO = daoFactory.getDAO(ShopDAO.class);
            coordinateDAO = daoFactory.getDAO(CoordinateDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for coordinate storage system");
            throw new ServletException("Impossible to get dao factory for coordinate storage system", ex);
        }
        Log.info("AddCoordinateServlet init done");
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

        String address = (String) request.getParameter("autocomplete_address");
        String openingHours = (String) request.getParameter("opening_hours");
        String longitude = (String) request.getParameter("longitude");
        String latitude = (String) request.getParameter("latitude");
        int shopId = Integer.parseInt(request.getParameter("shopId"));

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            if (address != null && !address.equals("")) {
                Coordinate newCoordinate = new Coordinate();
                newCoordinate.setAddress(address);
                if (openingHours != null) {
                    newCoordinate.setOpening_hours(openingHours);
                } else {
                    newCoordinate.setOpening_hours("");
                }
                newCoordinate.setShop(shopDAO.getByPrimaryKey(shopId));
                newCoordinate.setLatitude(Double.valueOf(latitude));
                newCoordinate.setLongitude(Double.valueOf(longitude));
                Long coordinate_id = coordinateDAO.insert(newCoordinate);
                if (coordinate_id == 0) {
                    Log.warn("Error inserting coordinate for shop " + shopId);
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/addCoordinate.jsp?error=1&shopId=" + shopId));
                } else {
                    Log.info("Coordinate inserted correctly");
                    ((List<Coordinate>) request.getSession().getAttribute("mycoordinates")).add(newCoordinate);
                    response.sendRedirect(contextPath + "restricted/myshop.jsp");
                }
            }
        } catch (DAOException ex) {
            Log.error("Error creating coordinate, " + ex);
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
