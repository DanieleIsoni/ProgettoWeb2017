package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to update the shop's informations
 *
 * @author Daniele Isoni
 */
public class EditShopServlet extends HttpServlet {

    private ShopDAO shopDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            shopDao = daoFactory.getDAO(ShopDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
//        Log.info("EditShopServlet init done");
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

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        String shopName = request.getParameter("shopName");
        String website = request.getParameter("website");
        String shipment = request.getParameter("shipment");
        String description = request.getParameter("description");

        try {
            Shop shop = (Shop) request.getSession().getAttribute("myshop");
            if (shop != null) {
                if (shopName != null && !shopName.equals("")) {
                    shop.setName(shopName);
                }
                if (website != null && !website.equals("")) {
                    shop.setWebsite(website);
                }
                if (shipment != null && !shipment.equals("")) {
                    shop.setShipment(shipment);
                }
                if (description != null && !description.equals("")) {
                    shop.setDescription(description);
                }
                shopDao.update(shop);
            } else {
                Log.error("Shop is null");
            }
            response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/myshop.jsp"));
        } catch (DAOException ex) {
            Log.error("Error editing shop, " + ex);
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
