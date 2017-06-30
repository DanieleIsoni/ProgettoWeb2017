/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.entities.Message;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Notification}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public interface NotificationDAO extends DAO<Notification, Integer> {

    /**
     * Returns the number of {@link Notification notifications} stored on the
     * persistence system of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Persists the new {@link Notification message} passed as parameter to the
     * storage system.
     *
     * @param notification the new {@code notification} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Notification notification) throws DAOException;

    /**
     * Returns the {@link Notification notification} with the primary key equals
     * to the one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code notification} to get.
     * @return the {@code notification} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Notification getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns all the {@link Notification notification} owned by the user
     * passed as parameter, read and unread.
     *
     * @param user the {@code user} of the {@code notifications} to get.
     * @return the list of the {@code notifications} owned by the user passed as
     * parameter, read and unread.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Notification> getAllByUser(User user) throws DAOException;

    /**
     * Returns the read {@link Notification notification} owned by the user
     * passed as parameter.
     *
     * @param user the {@code user} of the {@code notifications} to get.
     * @return the list of the read {@code notifications} owned by the user
     * passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Notification> getReadByUser(User user) throws DAOException;

    /**
     * Returns the unread {@link Notification notification} owned by the user
     * passed as parameter.
     *
     * @param user the {@code user} of the {@code notifications} to get.
     * @return the list of the unread {@code notifications} owned by the user
     * passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Notification> getUnreadByUser(User user) throws DAOException;

    @Override
    public List<Notification> getAll() throws DAOException;

    /**
     * Update the notification passed as parameter and returns it.
     *
     * @param notification the notification used to update the persistence
     * system.
     * @return the updated notification.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Notification update(Notification notification) throws DAOException;
}
