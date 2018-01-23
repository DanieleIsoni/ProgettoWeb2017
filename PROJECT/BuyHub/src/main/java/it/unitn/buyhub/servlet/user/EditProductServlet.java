package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Shops use this servlet to edit products: invoked both before and after the modifications.
 * @author Daniso
 */
public class EditProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
//        Log.info("EditProductServlet init done");
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
        int prodId = Integer.parseInt(request.getParameter("prodId"));

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        try {
            Product product = productDAO.getByPrimaryKey(prodId);
            if(code == 1){
                request.setAttribute("product", product);
                request.getRequestDispatcher("restricted/editProduct.jsp").forward(request, response);
            } else if (code == 2){
                String productName = request.getParameter("productName");
                int category = Integer.parseInt(request.getParameter("product_category"));
                double price = Double.parseDouble(request.getParameter("price"));
                String description = request.getParameter("description");
                if (productName != null && !productName.equals("") &&
                        category != -1 &&
                        price != 0 &&
                        description != null && !description.equals("")){
                    if(!productName.equals(product.getName()))
                        product.setName(productName);
                    if(category != product.getCategory())
                        product.setCategory(category);
                    if(price != product.getPrice())
                        product.setPrice(price);
                    if(!description.equals(product.getDescription()))
                        product.setDescription(description);
                    productDAO.update(product);
                    List<Product> products=productDAO.getByShop(product.getShop());
                    request.getSession().setAttribute("myproducts", products);
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/myshop.jsp"));
                }
            }
        } catch (DAOException ex){
            Log.error("Error editing product, "+ex);
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
