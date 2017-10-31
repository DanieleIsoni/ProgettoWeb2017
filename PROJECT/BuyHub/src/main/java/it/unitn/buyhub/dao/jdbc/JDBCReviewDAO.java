/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
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
 * system that interact with {@code Review}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCReviewDAO extends JDBCDAO<Review, Integer> implements ReviewDAO {

    private int limit = 5;

    public JDBCReviewDAO(Connection con) {
        super(con);

        FRIEND_DAOS.put(UserDAO.class, new JDBCUserDAO(CON));
        FRIEND_DAOS.put(ProductDAO.class, new JDBCProductDAO(CON));
    }

    /**
     * Persists the new {@link Review reviews} passed as parameter to the
     * storage system.
     *
     * @param reviews the new {@code reviews} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Review reviews) throws DAOException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO reviews(id_product, id_creator, global_value, quality, service, value_for_money, title, description, date_creation) VALUES(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reviews.getProduct().getId());
            ps.setInt(2, reviews.getCreator().getId());
            ps.setInt(3, reviews.getGlobalValue());
            ps.setInt(4, reviews.getQuality());
            ps.setInt(5, reviews.getService());
            ps.setInt(6, reviews.getValueForMoney());
            ps.setString(7, reviews.getTitle());
            ps.setString(8, reviews.getDescription());
            ps.setTimestamp(9, new Timestamp(reviews.getDateCreation().getTime()));

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
                    throw new DAOException("Impossible to persist the new reviews");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    //TODO: log the exception
                }
                throw new DAOException("Impossible to persist the new reviews");
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
     * Returns the number of {@link Review reviews} stored on the persistence
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
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM reviews");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count reviews", ex);
        }

        return 0L;
    }

    /**
     * Returns the {@link Review review} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code review} to get.
     * @return the {@code review} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Review getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM reviews WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                Timestamp timestamp = rs.getTimestamp("date_creation");
                Date date = new Date(timestamp.getTime());
                Review review = new Review();
                review.setId(primaryKey);
                review.setDescription(rs.getString("description"));
                review.setDateCreation(date);
                review.setGlobalValue(rs.getInt("global_value"));
                review.setQuality(rs.getInt("quality"));
                review.setService(rs.getInt("service"));
                review.setValueForMoney(rs.getInt("value_for_money"));
                review.setTitle(rs.getString("title"));

                //Get user associate
                UserDAO userDao = getDAO(UserDAO.class);
                review.setCreator(userDao.getByPrimaryKey(rs.getInt("id_creator")));

                //Get product associate
                ProductDAO productDao = getDAO(ProductDAO.class);
                review.setProduct(productDao.getByPrimaryKey(rs.getInt("id_product")));

                return review;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the reviews for the passed primary key", ex);
        }
    }

    /**
     * Returns the list of the {@link Review review} with the product passed as
     * parameter.
     *
     * @param product the {@code product} of the {@code reviews} to get.
     * @param isLimited limited number of review
     * @return the list of the {@code reviews} with the product passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Review> getByProduct(Product product, boolean isLimited) throws DAOException {
        List<Review> reviews = new ArrayList<>();
        if (product == null) {
            throw new DAOException("product is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM reviews WHERE id_product = ?" + (isLimited ? " LIMIT " + limit : ""))) {
            stm.setInt(1, product.getId());
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {

                    Timestamp timestamp = rs.getTimestamp("date_creation");
                    Date date = new Date(timestamp.getTime());
                    Review review = new Review();
                    review.setId(rs.getInt("id"));
                    review.setDescription(rs.getString("description"));
                    review.setDateCreation(date);
                    review.setGlobalValue(rs.getInt("global_value"));
                    review.setQuality(rs.getInt("quality"));
                    review.setService(rs.getInt("service"));
                    review.setValueForMoney(rs.getInt("value_for_money"));
                    review.setTitle(rs.getString("title"));

                    //Get user associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    review.setCreator(userDao.getByPrimaryKey(rs.getInt("id_creator")));

                    //Get product associate
                    review.setProduct(product);

                    reviews.add(review);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the pictures from product", ex);
        }
        return reviews;
    }

    /**
     * Returns the list of all the valid {@link Review reviews} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code reviews}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Review> getAll() throws DAOException {

        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM reviews")) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {

                    Timestamp timestamp = rs.getTimestamp("date_creation");
                    Date date = new Date(timestamp.getTime());
                    Review review = new Review();
                    review.setId(rs.getInt("id"));
                    review.setDescription(rs.getString("description"));
                    review.setDateCreation(date);
                    review.setGlobalValue(rs.getInt("global_value"));
                    review.setQuality(rs.getInt("quality"));
                    review.setService(rs.getInt("service"));
                    review.setValueForMoney(rs.getInt("value_for_money"));
                    review.setTitle(rs.getString("title"));

                    //Get user associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    review.setCreator(userDao.getByPrimaryKey(rs.getInt("id_creator")));

                    //Get product associate
                    ProductDAO productDao = getDAO(ProductDAO.class);
                    review.setProduct(productDao.getByPrimaryKey(rs.getInt("id_product")));

                    reviews.add(review);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the pictures from shop", ex);
        }
        return reviews;
    }

    /**
     * Update the review passed as parameter and returns it.
     *
     * @param review the review used to update the persistence system.
     * @return the updated review.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Review update(Review review) throws DAOException {
        if (review == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed review is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("UPDATE reviews SET id_product = ?, id_creator = ?, global_value = ?, quality = ?, service = ?, value_for_money = ?, title = ?, description = ?, date_creation = ?  WHERE id = ?")) {
            std.setInt(1, review.getProduct().getId());
            std.setInt(2, review.getCreator().getId());
            std.setInt(3, review.getQuality());
            std.setInt(4, review.getService());
            std.setInt(5, review.getValueForMoney());
            std.setString(6, review.getTitle());
            std.setString(7, review.getDescription());
            std.setTimestamp(8, new Timestamp(review.getDateCreation().getTime()));

            if (std.executeUpdate() == 1) {
                return review;
            } else {
                throw new DAOException("Impossible to update the review");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the review", ex);
        }
    }

    /**
     * Returns the list of the {@link Review review} with the creator passed as
     * parameter.
     *
     * @param creator the {@code creator} of the {@code reviews} to get.
     * @return the list of the {@code reviews} with the product passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Review> getByCreator(User creator) throws DAOException {
        if (creator == null) {
            throw new DAOException("creator is null");
        }
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM reviews WHERE id_creator = ?")) {
            stm.setInt(1, creator.getId());
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {

                    Timestamp timestamp = rs.getTimestamp("date_creation");
                    Date date = new Date(timestamp.getTime());
                    Review review = new Review();
                    review.setId(rs.getInt("id"));
                    review.setDescription(rs.getString("description"));
                    review.setDateCreation(date);
                    review.setGlobalValue(rs.getInt("global_value"));
                    review.setQuality(rs.getInt("quality"));
                    review.setService(rs.getInt("service"));
                    review.setValueForMoney(rs.getInt("value_for_money"));
                    review.setTitle(rs.getString("title"));

                    //Get user associate
                    review.setCreator(creator);

                    //Get product associate
                    ProductDAO productDao = getDAO(ProductDAO.class);
                    review.setProduct(productDao.getByPrimaryKey(rs.getInt("id_product")));

                    reviews.add(review);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the pictures from creator", ex);
        }
        return reviews;
    }

    /**
     * Revemore the review passed as parameter and returns it.
     *
     * @param review the review used to remove the persistence system.
     * @return a boolean value, true if removed.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public boolean remove(Review review) throws DAOException {
        if (review == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed review is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("DELETE FROM reviews WHERE id =?")) {
            std.setInt(1, review.getId());

            if (std.executeUpdate() == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the review", ex);
        }
    }

}
