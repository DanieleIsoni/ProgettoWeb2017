package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.ShopDAO;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to allow the creation of new shops
 *
 * @author Daniele Isoni
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
//        Log.info("CreateNewShopServlet init done");
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
        Double shipment_costs = 0.0;
        if (!request.getParameter("shipment_costs").equals("")) {

            shipment_costs = Double.parseDouble(request.getParameter("shipment_costs"));
        }
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
            if (name != null && !name.equals("")
                    && description != null && !description.equals("")
                    && website != null && !website.equals("")
                    && owner != null) {

                Shop newShop = new Shop();
                newShop.setDescription(description);
                newShop.setName(name);
                newShop.setOwner(owner);
                newShop.setWebsite(website);
                newShop.setValidity(0);
                if ((shipment != null && !shipment.equals("")) || (address != null && !address.equals(""))) {
                    if (shipment != null && !shipment.equals("")) {
                        newShop.setShipment(shipment);
                        newShop.setShipment_cost(shipment_costs);
                    }
                    Long shop_id = shopDao.insert(newShop);
                    if (shop_id == 0) {
                        Log.warn("Error creating the shop");
                        response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/createNewShop.jsp?error=1"));
                    } else {
                        Log.info("Shop inserted correctly");
                        newShop.setId(shop_id.intValue());
                        if (address != null && !address.equals("")) {
                            Coordinate newCoordinate = new Coordinate();
                            newCoordinate.setAddress(address);
                            if (openingHours != null) {
                                newCoordinate.setOpening_hours(openingHours);
                            } else {
                                newCoordinate.setOpening_hours("");
                            }
                            newCoordinate.setShop(newShop);
                            newCoordinate.setLatitude(Double.valueOf(latitude));
                            newCoordinate.setLongitude(Double.valueOf(longitude));
                            Long coordinate_id = coordinateDao.insert(newCoordinate);
                            if (coordinate_id == 0) {
                                Log.warn("Coordinate not inserted in Database");
                                String msg = "Dear admin,\n<br/> a new shop has been created and need to be validated";
                                String linkMail = PropertyHandler.getInstance().getValue("baseUrl") + "restricted/admin/shops";
                                Mailer.mailToAdmins(PropertyHandler.getInstance().getValue("noreplyMail"), "New shop created", msg, linkMail, "Go to Shops Manager", super.getServletContext());

                                response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/myself.jsp?error=1"));
                            } else {
                                Log.info("Coordinates inserted correctly");
                                String msg = "Dear admin,\n<br/> a new shop has been created and need to be validated";
                                String linkMail = PropertyHandler.getInstance().getValue("baseUrl") + "restricted/admin/shops";
                                Mailer.mailToAdmins(PropertyHandler.getInstance().getValue("noreplyMail"), "New shop created", msg, linkMail, "Go to Shops Manager", super.getServletContext());

                                response.sendRedirect(contextPath + "restricted/myself.jsp");
                            }
                        } else {
                            String msg = "Dear admin,\n<br/> a new shop has been created and need to be validated";
                            String linkMail = PropertyHandler.getInstance().getValue("baseUrl") + "restricted/admin/shops";
                            Mailer.mailToAdmins(PropertyHandler.getInstance().getValue("noreplyMail"), "New shop created", msg, linkMail, "Go to Shops Manager", super.getServletContext());

                            response.sendRedirect(contextPath + "restricted/myself.jsp");
                        }
                    }
                } else {
                    Log.warn("Neither the shipment nor the address have been filled in the createNewShop form");

                    response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/createNewShop.jsp?error=2"));
                }
            }
        } catch (DAOException ex) {

            Log.error("Error creating shop " + ex);
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
