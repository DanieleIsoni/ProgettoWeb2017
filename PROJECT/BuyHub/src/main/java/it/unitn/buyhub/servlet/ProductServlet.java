/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.servlet;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.UserDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
/**
 *
 * @author massimo
 */
public class ProductServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private ProductDAO productDAO;
    private PictureDAO pictureDAO;
    private ReviewDAO reviewDAO;
    private CoordinateDAO coordinateDAO;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
         
        
        
    }
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            pictureDAO = daoFactory.getDAO(PictureDAO.class);
            reviewDAO=daoFactory.getDAO(ReviewDAO.class);
            coordinateDAO=daoFactory.getDAO(CoordinateDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for product storage system", ex);
        }
    }
    
    
    protected void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
       
       
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        
        try {
            
            int id = Integer.parseInt(request.getParameter("id"));
            Product product=productDAO.getByPrimaryKey(id);
            if (product == null) {
                response.sendRedirect(response.encodeRedirectURL(contextPath + "home.jsp"));
            } else {
                request.setAttribute("product", product);
                
                List<Picture> pictures=pictureDAO.getByProduct(product);
                List<Review> reviews=reviewDAO.getByProduct(product);
                List<Coordinate> coordinates=coordinateDAO.getByShop(product.getShop());
                
                request.setAttribute("pictures", pictures);
                request.setAttribute("reviews", reviews);
                request.setAttribute("coordinates", coordinates);
                
                double qualityAvg=0;
                double serviceAvg=0;
                double moneyAvg=0;
                for(Review r:reviews)
                {
                    qualityAvg+=r.getQuality();
                    moneyAvg+=r.getValueForMoney();
                    serviceAvg+=r.getService();
                }
                qualityAvg/=reviews.size();
                serviceAvg/=reviews.size();
                moneyAvg/=reviews.size();
                
                request.setAttribute("globalValue", (double)(qualityAvg+serviceAvg+moneyAvg)/3);
                request.setAttribute("reviewsCount", reviews.size());

                
                                   
                request.getRequestDispatcher("product.jsp").forward(request, response);
            }
        } catch (DAOException|NumberFormatException ex) {
            //TODO: log exception
            System.out.println("Error getting product");
            response.sendRedirect(response.encodeRedirectURL(contextPath + "common/error.jsp"));
            System.out.println(ex.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      process(req,resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      process(req,resp);
    }
    
}