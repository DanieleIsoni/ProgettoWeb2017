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
 * system that interact with {@code Picture}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCPictureDAO extends JDBCDAO<Picture, Integer> implements PictureDAO {

    public JDBCPictureDAO(Connection con) {
        super(con);
        FRIEND_DAOS.put(UserDAO.class, new JDBCUserDAO(CON));
        FRIEND_DAOS.put(ShopDAO.class, new JDBCShopDAO(CON));
    }

    /**
     * Returns the number of {@link Picture pictures} stored on the persistence
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
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM pictures");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count pictures", ex);
        }

        return 0L;
    }

    /**
     * Returns the {@link Picture picture} with the primary key equals to the
     * one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code picture} to get.
     * @return the {@code picture} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Picture getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM pictures WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();

                Picture picture = new Picture();
                picture.setId(primaryKey);
                picture.setDescription(rs.getString("description"));
                picture.setPath(rs.getString("path"));
                picture.setName(rs.getString("name"));

                //Get user associate
                UserDAO userDao = getDAO(UserDAO.class);
                picture.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                return picture;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the pictures for the passed primary key", ex);
        }
    }

    /**
     * Returns the list of the {@link Picture picture} with the owner passed as
     * parameter.
     *
     * @param owner the {@code owner} of the {@code pictures} to get.
     * @return the list of the {@code pictures} with the owner passed as l
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Picture> getByOwner(User owner) throws DAOException {
        List<Picture> pictures = new ArrayList<>();
        if (owner == null) {
            throw new DAOException("owner is user");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM pictures WHERE id_owner = ?")) {
            stm.setInt(1, owner.getId());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {

                    Picture picture = new Picture();
                    picture.setId(rs.getInt("id"));
                    picture.setDescription(rs.getString("description"));
                    picture.setName(rs.getString("name"));
                    picture.setPath(rs.getString("path"));
                    picture.setOwner(owner);

                    pictures.add(picture);
                }

            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the pictures for the passed owner", ex);
        }
        return pictures;
    }

    /**
     * Returns the list of all the valid {@link Picture pictures} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code pictures}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Picture> getAll() throws DAOException {
        List<Picture> pictures = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM pictures")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {

                    Picture picture = new Picture();
                    picture.setId(rs.getInt("id"));
                    picture.setDescription(rs.getString("description"));
                    picture.setName(rs.getString("name"));
                    picture.setPath(rs.getString("path"));
                    //Get user associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    picture.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                    pictures.add(picture);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the pictures for the passed owner", ex);
        }
        return pictures;
    }

    /**
     * Update the picture passed as parameter and returns it.
     *
     * @param picture the picture used to update the persistence system.
     * @return the updated picture.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Picture update(Picture picture) throws DAOException {
        if (picture == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed picture is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("UPDATE pictures SET name = ?, description = ?, description = ?, id_owner = ? WHERE id = ?")) {
            std.setString(1, picture.getName());
            std.setString(2, picture.getDescription());
            std.setString(3, picture.getPath());
            std.setInt(4, picture.getOwner().getId());
            if (std.executeUpdate() == 1) {
                return picture;
            } else {
                throw new DAOException("Impossible to update the notification");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the notification", ex);
        }
    }

    /**
     * Returns the list of the {@link Picture picture} with the product passed
     * as parameter.
     *
     * @param product the {@code product} of the {@code pictures} to get.
     * @return the list of the {@code pictures} with the product passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Picture> getByProduct(Product product) throws DAOException {
        List<Picture> pictures = new ArrayList<>();
        if (product == null) {
            throw new DAOException("product is null");
        }
        try (PreparedStatement stm = CON.prepareStatement(
                "SELECT pi.* FROM pictures pi "
                + "JOIN pictures_products pp "
                + "ON pi.id=pp.id_picture "
                + "JOIN products pr "
                + "ON pp.id_product=pr.id "
                + "WHERE pr.id = ?")) {
            stm.setInt(1, product.getId());
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {

                    Picture picture = new Picture();
                    picture.setId(rs.getInt("id"));
                    picture.setDescription(rs.getString("description"));
                    picture.setName(rs.getString("name"));
                    picture.setPath(rs.getString("path"));
                    //Get user associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    picture.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));
                     
                    pictures.add(picture);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            System.out.println(ex.toString());
            throw new DAOException("Impossible to get the pictures from product", ex);
            
        }
            
        return pictures;
    }

    /**
     * Returns the list of the {@link Picture picture} with the review passed as
     * parameter.
     *
     * @param review the {@code product} of the {@code pictures} to get.
     * @return the list of the {@code pictures} with the review passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Picture> getByReview(Review review) throws DAOException {
        List<Picture> pictures = new ArrayList<>();
        if (review == null) {
            throw new DAOException("review is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT pi.* FROM pictures pi, pictures_reviews pp, products pr WHERE pi.id = pp.id_picture AND pp.id_review = ?")) {
            stm.setInt(1, review.getId());
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {

                    Picture picture = new Picture();
                    picture.setId(rs.getInt("id"));
                    picture.setDescription(rs.getString("description"));
                    picture.setName(rs.getString("name"));
                    picture.setPath(rs.getString("path"));
                    //Get user associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    picture.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                    pictures.add(picture);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the pictures from product", ex);
        }
        return pictures;
    }

    /**
     * Returns the list of the {@link Picture picture} with the shop passed as
     * parameter.
     *
     * @param shop the {@code product} of the {@code pictures} to get.
     * @return the list of the {@code pictures} with the shop passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Picture> getByShop(Shop shop) throws DAOException {
        List<Picture> pictures = new ArrayList<>();
        if (shop == null) {
            throw new DAOException("shop is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT pi.* FROM pictures pi, pictures_shops pp WHERE pi.id = pp.id_picture AND pp.id_shop = ?")) {
      
            stm.setInt(1, shop.getId());
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {

                    Picture picture = new Picture();
                    picture.setId(rs.getInt("id"));
                    picture.setDescription(rs.getString("description"));
                    picture.setName(rs.getString("name"));
                    picture.setPath(rs.getString("path"));
                    //Get user associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    picture.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                    pictures.add(picture);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the pictures from shop", ex);
        }
        return pictures;
    }

    /**
     * Persists the new {@link Picture pictures} passed as parameter to the
     * storage system.
     *
     * @param pictures the new {@code pictures} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Picture pictures) throws DAOException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO pictures(name, description, description, id_owner) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pictures.getName());
            ps.setString(2, pictures.getDescription());
            ps.setString(3, pictures.getPath());
            ps.setInt(4, pictures.getOwner().getId());

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
                    throw new DAOException("Impossible to persist the new pictures");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    //TODO: log the exception
                }
                throw new DAOException("Impossible to persist the new pictures");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                //TODO: log the exception
            }
            throw new DAOException("Impossible to persist the new pictures", ex);
        }
    }
}
