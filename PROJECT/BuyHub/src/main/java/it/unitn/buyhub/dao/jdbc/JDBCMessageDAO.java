/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Message;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Message}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCMessageDAO extends JDBCDAO<Message, Integer> implements MessageDAO {

    public JDBCMessageDAO(Connection con) {
        super(con);
        FRIEND_DAOS.put(UserDAO.class, new JDBCUserDAO(CON));
        FRIEND_DAOS.put(ReviewDAO.class, new JDBCReviewDAO(CON));
    }

    /**
     * Returns the number of {@link Message messages} stored on the persistence
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
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM messages");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count coordinates", ex);
        }

        return 0L;
    }

    /**
     * Returns the {@link Message message} with the primary key equals to the
     * one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code message} to get.
     * @return the {@code message} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Message getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM messages WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                Timestamp timestamp = rs.getTimestamp("date");
                Date date = new Date(timestamp.getTime());
                Message message = new Message();
                message.setId(rs.getInt("id"));
                message.setDescription(rs.getString("description"));
                message.setDate(date);

                //Get owner associate
                UserDAO userDao = getDAO(UserDAO.class);
                message.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                //Get validator
                UserDAO userDao2 = getDAO(UserDAO.class);
                message.setValidation(userDao2.getByPrimaryKey(rs.getInt("id_validation")));

                //Get review
                ReviewDAO reviewDao = getDAO(ReviewDAO.class);
                message.setReview(reviewDao.getByPrimaryKey(rs.getInt("id_review")));

                return message;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
    }

    /**
     * Returns the list of the {@link Message message} with the owner the one
     * passed as parameter.
     *
     * @param owner the {@code owner} of the {@code messages} to get.
     * @return the {@code message}the list of the {@link Message message} with
     * the owner the one passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Message> getByOwner(User owner) throws DAOException {
        List<Message> messages = new ArrayList<>();
        if (owner == null) {
            throw new DAOException("owner is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM messages WHERE id_owner = ?")) {
            stm.setInt(1, owner.getId());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("date");
                    Date date = new Date(timestamp.getTime());
                    Message message = new Message();
                    message.setId(rs.getInt("id"));
                    message.setDescription(rs.getString("description"));
                    message.setDate(date);

                    //Get owner associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    message.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                    //Get validator
                    UserDAO userDao2 = getDAO(UserDAO.class);
                    message.setValidation(userDao2.getByPrimaryKey(rs.getInt("id_validation")));

                    //Get review
                    ReviewDAO reviewDao = getDAO(ReviewDAO.class);
                    message.setReview(reviewDao.getByPrimaryKey(rs.getInt("id_review")));

                    messages.add(message);
                }
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
        return messages;
    }

    /**
     * Returns the list of the {@link Message message} with the validator the
     * one passed as parameter.
     *
     * @param validator the {@code validator} of the {@code messages} to get.
     * @return the {@code message}the list of the {@link Message message} with
     * the validator the one passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Message> getByValidation(User validator) throws DAOException {
        List<Message> messages = new ArrayList<>();
        if (validator == null) {
            throw new DAOException("validator is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM messages WHERE id_validation = ?")) {
            stm.setInt(1, validator.getId());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("date");
                    Date date = new Date(timestamp.getTime());
                    Message message = new Message();
                    message.setId(rs.getInt("id"));
                    message.setDescription(rs.getString("description"));
                    message.setDate(date);

                    //Get owner associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    message.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                    //Get validator
                    message.setValidation(validator);

                    //Get review
                    ReviewDAO reviewDao = getDAO(ReviewDAO.class);
                    message.setReview(reviewDao.getByPrimaryKey(rs.getInt("id_review")));

                    messages.add(message);
                }
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
        return messages;
    }

    /**
     * Returns the list of the {@link Message message} with the review passed as
     * parameter.
     *
     * @param review the {@code review} of the {@code messages} to get.
     * @return the {@code message}the list of the {@link Message message} with
     * the validator the one passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Message> getByReview(Review review) throws DAOException;

    /**
     * Returns the list of all the valid {@link Message messages} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code messages}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Message> getAll() throws DAOException;

    /**
     * Update the message passed as parameter and returns it.
     *
     * @param message the message used to update the persistence system.
     * @return the updated message.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Message update(Message message) throws DAOException;
}
