/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import com.mysql.cj.core.conf.url.ConnectionUrlParser;
import info.debatty.java.stringsimilarity.JaroWinkler;
import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Pair;
import it.unitn.buyhub.utils.Utility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        FRIEND_DAOS.put(PictureDAO.class, new JDBCPictureDAO(CON));
        // FRIEND_DAOS.put(ReviewDAO.class, new JDBCReviewDAO(CON));
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
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO products(name, description, price, id_shop,category) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, products.getName());
            ps.setString(2, products.getDescription());
            ps.setDouble(3, products.getPrice());
            ps.setInt(4, products.getShop().getId());
            ps.setInt(5, products.getCategory());

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
     * Returns the list of the {@link Product product} with the category passed
     * as parameter.
     *
     * @param categoryId the {@code category id} of the {@code products} to get.
     * @return the list of the {@link Product product} with the category the one
     * passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Product> getByCategory(int categoryId) throws DAOException {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT p.* FROM products p WHERE p.category = ?")) {
            stm.setInt(1, categoryId);
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setCategory(rs.getInt("category"));
                    //Get user associate
                    ShopDAO shopDao = getDAO(ShopDAO.class);
                    product.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                    PictureDAO pictureDao = getDAO(PictureDAO.class);
                    List<Picture> pictures = pictureDao.getByProduct(product);
                    product.setMainPicture(pictures.size() > 0 ? pictures.get(0) : Picture.NONE());

                    products.add(product);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the shops for the passed shop", ex);
        }
        return products;
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
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM products WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                Product product = new Product();
                product.setId(primaryKey);
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setCategory(rs.getInt("category"));
                //Get shop associate
                ShopDAO shopDao = getDAO(ShopDAO.class);
                product.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                //get review infos
                Pair<Double, Integer> p = getAvgreview(product.getId());
                product.setAvgReview(p.Left);
                product.setReviewCount(p.Right);

                PictureDAO pictureDao = getDAO(PictureDAO.class);
                List<Picture> pictures = pictureDao.getByProduct(product);
                product.setMainPicture(pictures.size() > 0 ? pictures.get(0) : Picture.NONE());

                return product;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the product for the passed primary key", ex);
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
                    product.setCategory(rs.getInt("category"));
                    //Get user associate
                    product.setShop(shop);

                    //get review infos
                    Pair<Double, Integer> p = getAvgreview(product.getId());
                    product.setAvgReview(p.Left);
                    product.setReviewCount(p.Right);

                    PictureDAO pictureDao = getDAO(PictureDAO.class);
                    List<Picture> pictures = pictureDao.getByProduct(product);
                    product.setMainPicture(pictures.size() > 0 ? pictures.get(0) : Picture.NONE());

                    products.add(product);
                }

            } catch (DAOFactoryException ex) {
                throw new DAOException("Impossible to get the shops for the passed shop", ex);
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
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM products ORDER BY name")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));

                    product.setCategory(rs.getInt("category"));
                    //Get user associate
                    ShopDAO shopDao = getDAO(ShopDAO.class);
                    product.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                    //get review infos
                    Pair<Double, Integer> p = getAvgreview(product.getId());
                    product.setAvgReview(p.Left);
                    product.setReviewCount(p.Right);

                    PictureDAO pictureDao = getDAO(PictureDAO.class);
                    List<Picture> pictures = pictureDao.getByProduct(product);
                    product.setMainPicture(pictures.size() > 0 ? pictures.get(0) : Picture.NONE());

                    //test di funzionamento
                    /*     Picture picture=new Picture(); 
                picture.setPath("images/noimage.png");
                product.setMainPicture(picture);*/
                    products.add(product);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the products for the passed primary key", ex);
        }
        return products;
    }

    /**
     * Returns the list of all the valid {@link Product products} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code products}.
     *
     * @param number max number of elements
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Product> getAllLimit(int number) throws DAOException {

        List<Product> products = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM products LIMIT ?")) {
            stm.setInt(1, number);
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));

                    product.setCategory(rs.getInt("category"));
                    //Get user associate
                    ShopDAO shopDao = getDAO(ShopDAO.class);
                    product.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                    //get review infos
                    Pair<Double, Integer> p = getAvgreview(product.getId());
                    product.setAvgReview(p.Left);
                    product.setReviewCount(p.Right);

                    PictureDAO pictureDao = getDAO(PictureDAO.class);
                    List<Picture> pictures = pictureDao.getByProduct(product);
                    product.setMainPicture(pictures.size() > 0 ? pictures.get(0) : Picture.NONE());

                    //test di funzionamento
                    /*     Picture picture=new Picture(); 
                picture.setPath("images/noimage.png");
                product.setMainPicture(picture);*/
                    products.add(product);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the products", ex);
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

                    product.setCategory(rs.getInt("category"));
                    //Get user associate
                    ShopDAO shopDao = getDAO(ShopDAO.class);
                    product.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                    //get review infos
                    Pair<Double, Integer> p = getAvgreview(product.getId());
                    product.setAvgReview(p.Left);
                    product.setReviewCount(p.Right);

                    PictureDAO pictureDao = getDAO(PictureDAO.class);
                    List<Picture> pictures = pictureDao.getByProduct(product);
                    product.setMainPicture(pictures.size() > 0 ? pictures.get(0) : Picture.NONE());

                    products.add(product);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the products for the passed primary key", ex);
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

        try (PreparedStatement std = CON.prepareStatement("UPDATE products SET name = ?, description = ?, price = ?, id_shop = ?, category = ?  WHERE id = ?")) {
            std.setString(1, product.getName());
            std.setString(2, product.getDescription());
            std.setDouble(3, product.getPrice());
            std.setInt(4, product.getCategory());
            std.setInt(5, product.getShop().getId());
            std.setInt(6, product.getId());

            if (std.executeUpdate() == 1) {
                return product;
            } else {
                throw new DAOException("Impossible to update the product ");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the product ", ex);
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

        if (name.equals(""))//carattere jolly
        {
            filtered = all;
        } else {
            //Start JaroWinkler filter
            JaroWinkler jw = new JaroWinkler();
            for (Product p : all) {
                if (jw.similarity(name, p.getName()) > 0.5) {
                    filtered.add(p);
                }
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
        List<Product> all = getAllWithPriceRange(min, max);
        List<Product> filtered = new ArrayList<>();

        if (name.equals(""))//carattere jolly
        {
            filtered = all;
        } else {
            //Start JaroWinkler filter
            JaroWinkler jw = new JaroWinkler();
            for (Product p : all) {
                if (jw.similarity(name, p.getName()) > 0.5) {
                    filtered.add(p);
                }
            }
        }
        return filtered;
    }
    public Pair getAvgreview(int id) throws DAOException
    {
        Pair<Double,Integer> p=null;
        try (PreparedStatement stm = CON.prepareStatement
                (//"SELECT AVG((r.global_value+r.quality+r.service+r.value_for_money)/4) AS avg, COUNT(r.id) AS c " +
                "SELECT AVG(r.global_value) AS avg, COUNT(r.id) AS c "+
                "FROM reviews r " +
                "JOIN products p " +
                "ON p.id=r.id_product " +
                "WHERE p.id = ?")) {
            stm.setInt(1, id);

            try (ResultSet rs = stm.executeQuery()) {
                rs.next();
                p = new Pair<>(rs.getDouble("avg"), rs.getInt("c"));

            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the reviews for the passed primary key", ex);
        }
        return p;

    }

    public void remove(Product product) throws DAOException {
        try (PreparedStatement stm = CON.prepareStatement("DELETE FROM products WHERE products.id = ?")) {
            stm.setInt(1, product.getId());
            if (stm.executeUpdate() == 1) {
                Log.info("Prod " + product.getId() + " deleted");
            } else {
                Log.error("Error in executing delete");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to delete the product", ex);
        }
    }

}
