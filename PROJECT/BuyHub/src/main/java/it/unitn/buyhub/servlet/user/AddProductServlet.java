package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.PropertyHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet allow the shop to add a new product
 * @author Daniso
 */
public class AddProductServlet extends HttpServlet {

    private ShopDAO shopDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            shopDAO = daoFactory.getDAO(ShopDAO.class);
            productDAO = daoFactory.getDAO(ProductDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
        Log.info("AddProductServlet init done");
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

        String productName = (String) request.getParameter("productName");
        int category = Integer.parseInt(request.getParameter("product_category"));
        Double price = Double.parseDouble(request.getParameter("price"));
        String description = (String) request.getParameter("description");
        int shopId = Integer.parseInt(request.getParameter("shopId"));

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        try {
            if(productName != null && !productName.equals("") &&
                    category >= 0 && category < Integer.parseInt(PropertyHandler.getInstance().getValue("categoriesNumber")) &&
                    price >= 0 &&
                    description != null && !description.equals("") &&
                    shopId != 0){
                Product newProduct = new Product();
                newProduct.setCategory(category);
                newProduct.setDescription(description);
                newProduct.setName(productName);
                newProduct.setShop(shopDAO.getByPrimaryKey(shopId));
                newProduct.setPrice(price);
                Long prod_id = productDAO.insert(newProduct);
                if(prod_id == 0){
                    Log.warn("Shop " + shopId + " already has a product with this name");
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "addProduct.jsp?error=1"));
                } else {
                    Log.info("Product inserted correctly");
                    ((List<Product>) request.getSession().getAttribute("myproducts")).add(newProduct);
                }

                response.sendRedirect(contextPath + "restricted/myshop.jsp");
            }
        } catch (DAOException ex) {
            Log.error("Error creating product, "+ex);
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
