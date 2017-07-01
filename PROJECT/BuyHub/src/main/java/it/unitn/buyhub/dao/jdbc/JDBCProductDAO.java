/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import info.debatty.java.stringsimilarity.JaroWinkler;
import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Product}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCProductDAO extends JDBCDAO<Product, Integer> implements ProductDAO {

    public JDBCProductDAO(Connection con) {
        super(con);

        FRIEND_DAOS.put(ShopDAO.class, new JDBCShopDAO(CON));
    }

    /**
     * Persists the new {@link Product products} passed as parameter to the
     * storage system.
     *
     * @param products the new {@code products} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Product products) throws DAOException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO products(name, description, price, id_shop) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, products.getName());
            ps.setString(2, products.getDescription());
            ps.setDouble(3, products.getPrice());
            ps.setInt(4, products.getShop().getId());

            if (ps.executeUpdate() == 1) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    try {
                        CON.rollback();
                    } catch (SQLException ex) {
                        //TODO: log the exception
                    }
                    throw new DAOException("Impossible to persist the new product");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    //TODO: log the exception
                }
                throw new DAOException("Impossible to persist the new product");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                //TODO: log the exception
            }
            throw new DAOException("Impossible to persist the new notification", ex);
        }
    }

    /**
     * Returns the number of {@link Product products} stored on the persistence
     * system of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     */
    @Override
    public Long getCount() throws DAOException {
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM products");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count products", ex);
        }

        return 0L;
    }

    /**
     * Returns the {@link Product product} with the primary key equals to the
     * one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code product} to get.
     * @return the {@code product} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Product getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM review WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                Product product = new Product();
                product.setId(primaryKey);
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));

                //Get user associate
                ShopDAO shopDao = getDAO(ShopDAO.class);
                product.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                return product;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
    }

    /**
     * Returns the list of the {@link Product product} with the shop passed as
     * parameter.
     *
     * @param shop the {@code shop} of the {@code products} to get.
     * @return the list of the {@link Product product} with the shop the one
     * passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Product> getByShop(Shop shop) throws DAOException {
        if (shop == null) {
            throw new DAOException("shop is null");
        }
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM products WHERE id_shop = ?")) {
            stm.setInt(1, shop.getId());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));

                    //Get user associate
                    product.setShop(shop);

                    products.add(product);
                }

            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the shops for the passed shop", ex);
        }
        return products;
    }

    /**
     * Returns the list of all the valid {@link Product products} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code products}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Product> getAll() throws DAOException {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM products")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));

                    //Get user associate
                    ShopDAO shopDao = getDAO(ShopDAO.class);
                    product.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                    products.add(product);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
        return products;
    }

    /**
     * Returns the list of all the valid {@link Product products} stored by the
     * storage system with the price in the range.
     *
     * @param min the min price
     * @param max the max price
     * @return the list of all the valid {@code products}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Product> getAllWithPriceRange(double min, double max) throws DAOException {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM products WHERE price >= ? AND price <= ?")) {
            stm.setDouble(1, min);
            stm.setDouble(2, max);
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));

                    //Get user associate
                    ShopDAO shopDao = getDAO(ShopDAO.class);
                    product.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                    products.add(product);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
        return products;
    }

    /**
     * Update the product passed as parameter and returns it.
     *
     * @param product the product used to update the persistence system.
     * @return the updated product.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Product update(Product product) throws DAOException {
        if (product == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed product is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("UPDATE products SET name = ?, description = ?, price = ?, id_shop = ? WHERE id = ?")) {
            std.setString(1, product.getName());
            std.setString(2, product.getDescription());
            std.setDouble(3, product.getPrice());
            std.setInt(4, product.getShop().getId());

            if (std.executeUpdate() == 1) {
                return product;
            } else {
                throw new DAOException("Impossible to update the product");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the product", ex);
        }
    }

    /**
     * Returns the list of the {@link Product product} with the name passed as
     * parameter.
     *
     * @param name the {@code name} of the {@code products} to get.
     * @return the list of the {@link Product product} with the name that
     * contains the one passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Product> getByName(String name) throws DAOException {
        //MUST IMPLEMENT string-similarity -> https://github.com/tdebatty/java-string-similarity
        List<Product> all = getAll();
        List<Product> filtered = new ArrayList<>();

        //Start JaroWinkler filter
        JaroWinkler jw = new JaroWinkler();
        for (Product p : all) {
            if (jw.similarity(name, p.getName()) > 0.85) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    /**
     * Returns the list of the {@link Product product} with the name that
     * contains the one passed as parametera and the price in the selected gap.
     *
     * @param name the {@code name} of the {@code products} to get.
     * @param min the {@code min} price of the {@code products} to get.
     * @param max the {@code max} price of the {@code products} to get.
     * @return the list of the {@link Product product} with the name that
     * contains the one passed as parametera and the price in the selected gap.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Product> getByNameAndPriceRange(String name, double min, double max) throws DAOException {
        //MUST IMPLEMENT string-similarity -> https://github.com/tdebatty/java-string-similarity
        List<Product> all = getAllWithPriceRange(min,max);
        List<Product> filtered = new ArrayList<>();

        //Start JaroWinkler filter
        JaroWinkler jw = new JaroWinkler();
        for (Product p : all) {
            if (jw.similarity(name, p.getName()) > 0.85) {
                filtered.add(p);
            }
        }
        return filtered;
    }
}
