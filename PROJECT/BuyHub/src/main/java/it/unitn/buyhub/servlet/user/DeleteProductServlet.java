package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
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
 * With this the shops can remove products
 *
 * @author Daniele Isoni
 */
public class DeleteProductServlet extends HttpServlet {

    private ProductDAO productDAO;
    private PictureDAO pictureDAO;

    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for product storage system");
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            pictureDAO = daoFactory.getDAO(PictureDAO.class);
            productDAO = daoFactory.getDAO(ProductDAO.class);
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for product storage system");
            throw new ServletException("Impossible to get dao factory for product storage system", ex);
        }
//        Log.info("DeleteProductServlet init done");
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
        int prodId = Integer.parseInt(request.getParameter("prodId"));
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        try {
            boolean done = true;
            Product product = productDAO.getByPrimaryKey(prodId);
            List<Picture> pictures = pictureDAO.getByProduct(product);
            for(Picture p: pictures){
                done = done && pictureDAO.removeProductPicture(product, p);
            }
            if(done){
                productDAO.remove(product);
                response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/myshop.jsp"));
            } else {
                response.sendRedirect(response.encodeRedirectURL(contextPath + "common/error.jsp"));
            }
        } catch (DAOException ex) {
            Log.error("Error deleting product, " + ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
    }// </editor-fold>

}
