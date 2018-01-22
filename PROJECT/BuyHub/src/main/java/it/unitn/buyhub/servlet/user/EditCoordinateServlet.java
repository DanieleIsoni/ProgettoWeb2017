/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.CoordinateDAO;
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
public class EditCoordinateServlet extends HttpServlet {

    private CoordinateDAO coordinateDAO;

    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            coordinateDAO = daoFactory.getDAO(CoordinateDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
        Log.info("EditCoordinateServlet init done");
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
        int code = Integer.parseInt(request.getParameter("code"));
        int coordinateId = Integer.parseInt(request.getParameter("coordinateId"));

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        try {
            Coordinate coordinate = coordinateDAO.getByPrimaryKey(coordinateId);

            if (code == 1) {
                request.setAttribute("coordinate", coordinate);
                request.getRequestDispatcher("restricted/editCoordinate.jsp").forward(request, response);
            } else if (code == 2) {
                String address = (String) request.getParameter("autocomplete_address");
                String openingHours = (String) request.getParameter("opening_hours");
                String longitude = (String) request.getParameter("longitude");
                String latitude = (String) request.getParameter("latitude");
                if (address != null && !address.equals("")
                        && openingHours != null && !openingHours.equals("")
                        && longitude != null && !longitude.equals("")
                        && latitude != null && !latitude.equals("")) {

                    if (!address.equals(coordinate.getAddress())) {
                        coordinate.setAddress(address);
                    }
                    if (!openingHours.equals(coordinate.getOpening_hours())) {
                        coordinate.setOpening_hours(openingHours);
                    }

                    if (longitude != null && !longitude.equals("") && Double.valueOf(longitude) != coordinate.getLongitude()) {
                        coordinate.setLongitude(Double.valueOf(longitude));
                    }
                    if (latitude != null && !latitude.equals("") && Double.valueOf(latitude) != coordinate.getLatitude()) {
                        coordinate.setLatitude(Double.valueOf(latitude));
                    }
                    coordinateDAO.update(coordinate);
                    List<Coordinate> coordinates = coordinateDAO.getByShop(coordinate.getShop());
                    request.getSession().setAttribute("mycoordinates", coordinates);

                }
                response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/myshop.jsp"));
            }
        } catch (DAOException ex) {
            Log.error("Error editing coordinate, " + ex);
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
