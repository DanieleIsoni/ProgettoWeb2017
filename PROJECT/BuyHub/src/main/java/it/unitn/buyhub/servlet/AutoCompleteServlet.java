
package it.unitn.buyhub.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import info.debatty.java.stringsimilarity.JaroWinkler;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.utils.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutoCompleteServlet extends HttpServlet {

    private ProductDAO productDAO;

    static Map<Integer,ArrayList<String> > titles=new HashMap<>();
    
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        
        if (daoFactory == null) {
            Log.error("Impossible to get dao factory for product storage system");
            throw new ServletException("Impossible to get dao factory for product storage system");
        }
        try {
            productDAO = daoFactory.getDAO(ProductDAO.class);
            rigenera();
        } catch (DAOFactoryException ex) {
            Log.error("Impossible to get dao factory for prduct storage system");
            throw new ServletException("Impossible to get dao factory for prduct storage system", ex);
        } catch (DAOException ex) {
            Log.error("Error in autocomplete generation");
           throw new ServletException("Error in autocomplete generation");
        }
        Log.info("AutoCompleteServlet init done");
        
    }

    public void rigenera() throws DAOException
    {
        Map<Integer,ArrayList<String> > newTitles=new HashMap<>();
        
        List<Product> products=productDAO.getAll();
        JaroWinkler jw= new JaroWinkler();
        
        for(Product p : products)
        {
            if(newTitles.get(p.getCategory())==null)
               newTitles.put(p.getCategory(), new ArrayList<String>());
            //escludo troppo corti e troppo simili
            if(p.getName().length()>3 && nearest(p.getName(),newTitles.get(p.getCategory()),jw)<0.9)
                newTitles.get(p.getCategory()).add(p.getName());
        }
        
        titles.clear();
        titles.putAll(newTitles);
        Log.info("AutoComplete Updated");
        
        
        
    }
    /**
     * Get the nearest string from titles (the one with the smallest distance/greatest similarity)
     */
    public double nearest(String s,ArrayList<String> titles,JaroWinkler jw)
    {
        double val=0;
        
        for(String t : titles)
        {
            double similarity=jw.similarity(s, t);
            if(similarity>val)
                val=similarity;
        }
        return val;
    }
    
    
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("Application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            
            if(request.getParameter("term")!=null && request.getParameter("term").length()>=3)
            {
                String term=request.getParameter("term");
                int cat=-1;
                
                if(request.getParameter("cat")!=null && request.getParameter("cat").length()>=1)
                {
                    cat=Integer.parseInt(request.getParameter("cat"));
                    
                }
                // da dove prendere i termini (controllo della categoria)
                ArrayList<String> from = new  ArrayList<>();
                
                if(cat==-1)
                    for (Map.Entry<Integer, ArrayList<String>> entry : titles.entrySet()) {
                        ArrayList<String> value = entry.getValue();
                        from.addAll(value);
                        
                    }
                else
                {
                    from.addAll(titles.get(cat));
                }
                
                /*
                To solve java.util.ConcurrentModificationException
                1) Add to toRemove arrayList the elements to remove
                2) Remove the elements
                */
                ArrayList<String> toRemove=new ArrayList<>();
                
                /*
                take only the similar titles
                */
                JaroWinkler jw=new JaroWinkler();
                for (String s: from) {
                    if(jw.similarity(term,s)<0.5)
                    {
                        toRemove.add(s);
                    }
                    
                }
                from.removeAll(toRemove);
                Gson gson=new Gson();
                out.println("{\"res\" : "+gson.toJson(from)+"}");
                
            }
            
            
            
            
            
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "AutoComplete Servlet";
    }

}
