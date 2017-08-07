/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.listeners;

import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.dao.persistence.factories.jdbc.JDBCDAOFactory;
import it.unitn.buyhub.servlet.AutoCompleteServlet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author matteo
 */
public class WebAppContextListener implements ServletContextListener {

    
    
    //Componenti runnable per l'esecuzione automatica dei servizi di aggiornamento del DB dei termini di ricerca
    private volatile ScheduledExecutorService executor;

    
    final Runnable autoCompleteUpdate = new Runnable() {
        public void run() { 
               try {
               new AutoCompleteServlet().rigenera();
                } catch (DAOException ex) {
                    Logger.getLogger(WebAppContextListener.class.getName()).log(Level.SEVERE, null, ex);
                }
           // System.out.println("EXECUTION");
        }
    };
    /**
     * The serlvet container call this method when initializes the application
     * for the first time.
     *
     * @param sce the event fired by the servlet container when initializes the
     * application
     *
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String dburl = sce.getServletContext().getInitParameter("dburl");
        try {
            JDBCDAOFactory.configure(dburl,"root","");
            DAOFactory daoFactory = JDBCDAOFactory.getInstance();
            sce.getServletContext().setAttribute("daoFactory", daoFactory);
            
            
            //Inizializzo un esecutore automatico ogni 30 minuti per aggiornare il DB dei termini di ricerca
            executor = Executors.newScheduledThreadPool(2);
            executor.scheduleAtFixedRate(autoCompleteUpdate, 0, 30, TimeUnit.MINUTES);

        } catch (DAOFactoryException ex) {
            Logger.getLogger(WebAppContextListener.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * The servlet container call this method when destroyes the application.
     *
     * @param sce the event generated by the servlet container when destroyes
     * the application.
     *
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DAOFactory daoFactory = (DAOFactory) sce.getServletContext().getAttribute("daoFactory");
        if (daoFactory != null) {
            daoFactory.shutdown();
        }
        daoFactory = null;
        
        
        final ScheduledExecutorService executor = this.executor;

        if (executor != null)
        {
            executor.shutdown();
            this.executor = null;
        }
    }
}
