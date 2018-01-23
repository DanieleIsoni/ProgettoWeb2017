/*
 * AA 2016-2017
 * Introduction to Web Programming
 * Common - DAO
 * UniTN
 */
package it.unitn.buyhub.dao.persistence.factories.jdbc;

import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.factories.DAOFactory;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * This JDBC implementation of {@code DAOFactory}.
 *
 * @author Stefano Chirico &lt;stefano dot chirico at unitn dot it&gt;
 * @since 2017.04.17
 */
public class JDBCDAOFactory implements DAOFactory {

    private final transient Connection CON;
    private final transient HashMap<Class, DAO> DAO_CACHE;

    private static JDBCDAOFactory instance;

    /**
     * Call this method before use the instance of this class.
     *
     * @param dbUrl the url to access to the database.
     * @param username the db username
     * @param password the db password for the given username
     *
     * @throws DAOFactoryException if an error occurred during dao factory
     * configuration.
     *
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    public static void configure(String dbUrl, String username, String password) throws DAOFactoryException {
        if (instance == null) {
            instance = new JDBCDAOFactory(dbUrl, username, password);
        } else {
            throw new DAOFactoryException("DAOFactory already configured. You can call configure only one time");
        }
    }

    /**
     * Returns the singleton instace of this {@link DAOFactory}.
     *
     * @return the singleton instance of this {@code DAOFactory}.
     * @throws DAOFactoryException if an error occurred if this dao factory is
     * not yet configured.
     *
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    public static JDBCDAOFactory getInstance() throws DAOFactoryException {
        if (instance == null) {
            throw new DAOFactoryException("DAOFactory not yet configured. Call DAOFactory.configure(String dbUrl) before use the class");
        }
        return instance;
    }

    /**
     * The private constructor used to creste the singleton instance of this
     * {@code DAOFactory}.
     *
     * @param dbUrl the url to access the database.
     * @param username the db username
     * @param password the db password for the given username
     *
     * @throws DAOFactoryException if an error occurred during
     * {@code DAOFactory} creation.
     *
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    private JDBCDAOFactory(String dbUrl, String username, String password) throws DAOFactoryException {
        super();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver", true, getClass().getClassLoader());
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(cnfe.getMessage(), cnfe.getCause());
        }

        try {
            //jdbc:mysql://localhost:3306/buyhub
            CON = DriverManager.getConnection(dbUrl + "?serverTimezone=UTC", username, password);
        } catch (SQLException sqle) {
            throw new DAOFactoryException("Cannot create connection", sqle);
        }

        DAO_CACHE = new HashMap<>();
    }

    /**
     * Shutdowns the access to the storage system.
     *
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    @Override
    public void shutdown() {
        /*  try {
                com.mysql.jdbc.AbandonedConnectionCleanupThread.shutdown();
            }
            catch (Exception e) {
                logger.error(e.toString() + System.lineSeparator() + StackTrace.getStringFromStackTrace(e));
            }*/

        //NOT WORKING
        /*  try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException sqle) {
            Logger.getLogger(JDBCDAOFactory.class.getName()).info(sqle.getMessage());
        }*/
    }

    /**
     * Returns the concrete {@link DAO dao} which type is the class passed as
     * parameter.
     *
     * @param <DAO_CLASS> the class name of the {@code dao} to get.
     * @param daoInterface the class instance of the {@code dao} to get.
     * @return the concrete {@code dao} which type is the class passed as
     * parameter.
     * @throws DAOFactoryException if an error occurred during the operation.
     *
     * @author Stefano Chirico
     * @since 1.0.170417
     */
    @Override
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoInterface) throws DAOFactoryException {
        DAO dao = DAO_CACHE.get(daoInterface);
        if (dao != null) {
            return (DAO_CLASS) dao;
        }

        Package pkg = daoInterface.getPackage();
        String prefix = pkg.getName() + ".jdbc.JDBC";

        try {
            Class daoClass = Class.forName(prefix + daoInterface.getSimpleName());

            Constructor<DAO_CLASS> constructor = daoClass.getConstructor(Connection.class);
            DAO_CLASS daoInstance = constructor.newInstance(CON);
            if (!(daoInstance instanceof JDBCDAO)) {
                throw new DAOFactoryException("The daoInterface passed as parameter doesn't extend JDBCDAO class");
            }
            DAO_CACHE.put(daoClass, daoInstance);
            return daoInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException ex) {
            throw new DAOFactoryException("Impossible to return the DAO", ex);
        }
    }
}
