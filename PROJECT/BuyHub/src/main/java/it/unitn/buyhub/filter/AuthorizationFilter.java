/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.filter;

import it.unitn.buyhub.dao.CoordinateDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Daniele Isoni
 */
public class AuthorizationFilter implements Filter {
    
    private static final boolean DEBUG = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    public AuthorizationFilter() {
    }    
    
    private boolean doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (DEBUG) {
            log("AuthorizationFilter:DoBeforeProcessing");
        }
        if(request instanceof HttpServletRequest){
            ServletContext servletContext = ((HttpServletRequest) request).getServletContext();
            String link = ((HttpServletRequest) request).getRequestURI();
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            User user = null;
            if (session != null){
                user = (User) session.getAttribute("authenticatedUser");
            }
            
            DAOFactory daoFactory = (DAOFactory) servletContext.getAttribute("daoFactory");
            if(daoFactory == null){
                Log.error("Impossible to get dao factory for AuthorizationFilter");
                throw new ServletException("Impossible to get dao factory for AuthorizationFilter");
            }
            ShopDAO shopDAO;
            ProductDAO productDAO;
            CoordinateDAO coordinateDAO;
            try {
                shopDAO = daoFactory.getDAO(ShopDAO.class);
                productDAO = daoFactory.getDAO(ProductDAO.class);
                coordinateDAO = daoFactory.getDAO(CoordinateDAO.class);
            } catch (DAOFactoryException ex) {
                Log.error("Impossible to get dao factory for product storage system");
                throw new ServletException("Impossible to get dao factory for product storage system", ex);
            }
            
            User shopOwner = null;
            int shopId = -1;
            int prodId = -1;
            int coordinateId = -1;
            switch(link){
                case "/BuyHub/restricted/addProduct.jsp":
                case "/BuyHub/restricted/addCoordinate.jsp":
                    shopId = Integer.parseInt(request.getParameter("shopId"));
                    try {
                        shopOwner = shopDAO.getByPrimaryKey(shopId).getOwner();
                    } catch (DAOException ex) {
                        Logger.getLogger(AuthorizationFilter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(shopOwner != null && user != null && user.getId() != shopOwner.getId()){
                        String contextPath = servletContext.getContextPath();
                        if (!contextPath.endsWith("/")) {
                            contextPath += "/";
                        }
                        ((HttpServletResponse) response).sendRedirect(contextPath + "common/error.jsp");
                        return false;
                    }
                    break;
                case "/BuyHub/EditProductServlet":
                case "/BuyHub/restricted/editProduct.jsp":
                case "/BuyHub/DeleteProductServlet":
                    prodId = Integer.parseInt(request.getParameter("prodId"));
                    try {
                        Product product = productDAO.getByPrimaryKey(prodId);
                        if(product != null){
                            shopOwner = product.getShop().getOwner();
                        }
                    } catch (DAOException ex) {
                        Logger.getLogger(AuthorizationFilter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(shopOwner != null && user != null && user.getId() != shopOwner.getId()){
                        String contextPath = servletContext.getContextPath();
                        if (!contextPath.endsWith("/")) {
                            contextPath += "/";
                        }
                        ((HttpServletResponse) response).sendRedirect(contextPath + "common/error.jsp");
                        return false;
                    }
                    break;
                case "/BuyHub/restricted/productPhoto":
                    prodId = Integer.parseInt(request.getParameter("id"));
                    try {
                        Product product = productDAO.getByPrimaryKey(prodId);
                        if(product != null){
                            shopOwner = product.getShop().getOwner();
                        }
                    } catch (DAOException ex) {
                        Logger.getLogger(AuthorizationFilter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(shopOwner != null && user != null && user.getId() != shopOwner.getId()){
                        String contextPath = servletContext.getContextPath();
                        if (!contextPath.endsWith("/")) {
                            contextPath += "/";
                        }
                        ((HttpServletResponse) response).sendRedirect(contextPath + "common/error.jsp");
                        return false;
                    }
                    break;
                case "/BuyHub/EditCoordinateServlet":
                case "/BuyHub/restricted/editCoordinate.jsp":
                case "/BuyHub/DeleteCoordinateServlet":
                    coordinateId = Integer.parseInt(request.getParameter("coordinateId"));
                    try {
                        Coordinate coordinate = coordinateDAO.getByPrimaryKey(coordinateId);
                        if(coordinate != null){
                            shopOwner = coordinate.getShop().getOwner();
                        }
                    } catch (DAOException ex) {
                        Logger.getLogger(AuthorizationFilter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(shopOwner != null && user != null && user.getId() != shopOwner.getId()){
                        String contextPath = servletContext.getContextPath();
                        if (!contextPath.endsWith("/")) {
                            contextPath += "/";
                        }
                        ((HttpServletResponse) response).sendRedirect(contextPath + "common/error.jsp");
                        return false;
                    }
                    break;
            }
                
        }
        return true;
    }    
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (DEBUG) {
            log("AuthorizationFilter:DoAfterProcessing");
        }        
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        if (DEBUG) {
            log("AuthorizationFilter:doFilter()");
        }
        
        doBeforeProcessing(request, response);
        
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            log("Some problem in filter chain", t);
        }
        
        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (DEBUG) {                
                log("AuthorizationFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthorizationFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthorizationFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);        
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);                
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");                
                pw.print(stackTrace);                
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                log("Error during exception notification", ex);
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
                log("Error during exception notification", ex);
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
        Log.info(msg);
    }

    public void log(String msg, Throwable throwable) {
        filterConfig.getServletContext().log(msg, throwable);
        Log.error(msg+":"+throwable.getMessage());
    }
    
}
