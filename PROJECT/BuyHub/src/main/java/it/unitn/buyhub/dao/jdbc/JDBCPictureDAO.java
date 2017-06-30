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
import java.sql.Timestamp;
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
    public List<Picture> getByOwner(User owner) throws DAOException;

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
    public List<Picture> getAll() throws DAOException;

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
    public Picture update(Picture picture) throws DAOException;

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
    public List<Picture> getByProduct(Product product) throws DAOException;

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
    public List<Picture> getByReview(Review review) throws DAOException;

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
    public List<Picture> getByShop(Shop shop) throws DAOException;
}
