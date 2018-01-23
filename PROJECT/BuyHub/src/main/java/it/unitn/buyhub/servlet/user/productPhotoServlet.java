package it.unitn.buyhub.servlet.user;

import it.unitn.buyhub.dao.OrderDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
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
 * Sertvlet that allow a shop to upload product's photos.
 * This creates the data used by productPhoto.jsp
 * @author massimo
 */
public class productPhotoServlet extends HttpServlet {


    private ProductDAO productDAO;
    private PictureDAO pictureDAO;
     public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            pictureDAO = daoFactory.getDAO(PictureDAO.class);

        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for shop storage system");
            throw new ServletException("Impossible to get dao factory for shop storage system", ex);
        }
//        Log.info("EditProductServlet init done");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        response.setContentType("text/html;charset=UTF-8");
            try {

                if(request.getParameter("id")==null)
                    throw new Exception("Missing id");

                int id = Integer.parseInt(request.getParameter("id"));

                String contextPath = getServletContext().getContextPath();
                if (!contextPath.endsWith("/")) {
                    contextPath += "/";
                }
                    Product product = productDAO.getByPrimaryKey(id);
                    request.setAttribute("product", product);
                    List<Picture> pictures = pictureDAO.getByProduct(product);

                    request.setAttribute("pictures", pictures);
                    request.getRequestDispatcher("productPhoto.jsp").forward(request, response);


        } catch (Exception ex) {
            Logger.getLogger(productPhotoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

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
