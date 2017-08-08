package it.unitn.buyhub.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ReviewDAO;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.max;
import static java.lang.Integer.min;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a webservice, implemented witha servlet
 * @author massimo
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private ProductDAO productDAO;
    private ReviewDAO reviewDAO;

    int ProductsPerPage=10;
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
        try (PrintWriter out = response.getWriter()) {
           
            
            if(request.getParameter("q")!=null && request.getParameter("q").length()>0)
            {
                String q=request.getParameter("q");
                
                double min=0;
                double max=Double.MAX_VALUE;
                
                int c=-1;
                int  minRev=0;
                //minimo e massimo
                if(request.getParameter("min")!=null && Double.parseDouble(request.getParameter("min"))>0)
                {
                    min=Double.parseDouble(request.getParameter("min"));
                }
                if(request.getParameter("max")!=null && Double.parseDouble(request.getParameter("max"))>min)
                {
                    max=Double.parseDouble(request.getParameter("max"));
                }
                
                //categoria
                if(request.getParameter("c")!=null && Integer.parseInt(request.getParameter("c"))>=0)
                {
                    c=Integer.parseInt(request.getParameter("c"));
                }
                
                //minimo della media delle recensioni
                if(request.getParameter("minRev")!=null && Integer.parseInt(request.getParameter("minRev"))>0)
                {
                    minRev=Integer.parseInt(request.getParameter("minRev"));
                }
                
                
                List<Product> products;
                try {
                   /* if(min!=0 || max!= Double.MAX_VALUE)
                        products = productDAO.getByNameAndPriceRange(q, min, max);
                    else*/
                        products = productDAO.getByName(q);
                    
                    //rimozioni non voluti
                    if(c!=-1 || minRev>0)
                        products.removeIf(new productRemovePredicate(c, minRev));
                    
                    
                } catch (DAOException ex) {
                    throw new ServletException("Error on retreving products: "+ex.toString());
                }
                
                
                
                
                int count = products.size();
                
                int pages = count/ProductsPerPage +1;
                
                int p=1;
                
                int s=0;
                
                //ordinamento, solo se il modo è >0, altrimenti è già ordinato alfabeticamente
                if(request.getParameter("s")!=null && Integer.parseInt(request.getParameter("s"))>0)
                {
                    s=Integer.parseInt(request.getParameter("s"));
                    productComparator comp=new productComparator(s);
                    products.sort(comp);
                }
                
               
                //paginazione
                if(request.getParameter("p")!=null && Integer.parseInt(request.getParameter("p"))>0)
                {
                    p=Integer.parseInt(request.getParameter("p"));
                }
                
                int from = Math.max(0,(p-1)*ProductsPerPage);
                int to = Math.min(products.size(),(p)*ProductsPerPage);
                
                products=products.subList(from,to);
                
                
                
                
                
                
                Gson gson=new Gson();
                String result="{\"s\":"+s+",\n\"p\":"+p+",\n\"pages\":"+pages+",\n";
                //non voglio le informazioni sugli utenti nel JSON
                for (Product product : products) {
                    product.getShop().setCreator(null);
                    product.getShop().setOwner(null);
                }
                result+="\"products\": "+gson.toJson(products);
                result+="}";
                
                response.setContentType("Application/json;charset=UTF-8");
       
                out.println(result);
                
            }
            else
            {
            
                response.setContentType("Application/json;charset=UTF-8");
                out.println("{\"msg\": \"No query specified\"}");
            }   
            
            
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

    @Override
    public void init() throws ServletException {
        super.init();
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            reviewDAO = daoFactory.getDAO(ReviewDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for prduct storage system", ex);
        }
        
    }

    
    
    class productComparator implements Comparator
    {

        int mode=0;
        @Override
        public int compare(Object o1, Object o2) {
     
            int retval=-1;
            Product p1=(Product) o1;
            Product p2=(Product) o2;
            
            switch(mode)
            {
                /*
                il metodo di ordinamento dei risultati, 
                di default alfabetico (0), 
                alfagetico inverso (1) 
                prezzo crescente (2), 
                prezzo decrescente(3), 
                valutazione decrescente(4). 
                numero recensioni decrescente(5). 
                */
                default:
                case 0: retval=p1.getName().compareTo(p2.getName()); break;
                case 1: retval=p2.getName().compareTo(p1.getName()); break;
                case 2: retval= ((Double)p1.getPrice()).compareTo(p2.getPrice()); break;
                case 3: retval= ((Double)p2.getPrice()).compareTo(p1.getPrice()); break;
                case 4: retval= ((Double)p2.getAvgReview()).compareTo(p1.getAvgReview()); break;
                case 5: retval= ((Integer)p2.getReviewCount()).compareTo(p1.getReviewCount()); break;
                
                
            }
        
            return retval;
        }
        public productComparator(int mode)
        {
            this.mode=mode;
        }
        
    }
    
    class productRemovePredicate implements Predicate<Product>
    {

        private final int minRev;
        private final int c;
        @Override
        public boolean test(Product t) {
            return t.getAvgReview()<minRev || (c!=-1 && t.getCategory()!=c);

        }

        public productRemovePredicate(int c, int minRev)
        {
            this.minRev=minRev;
            this.c=c;
        }

    }
}