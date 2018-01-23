/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.entities.Message;
import it.unitn.buyhub.dao.entities.Ticket;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Message}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public interface MessageDAO extends DAO<Message, Integer> {

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
    public Long getCount() throws DAOException;

    /**
     * Persists the new {@link Message message} passed as parameter to the
     * storage system.
     *
     * @param message the new {@code coordinate} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Message message) throws DAOException;

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
    public Message getByPrimaryKey(Integer primaryKey) throws DAOException;

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
    public List<Message> getByOwner(User owner) throws DAOException;

    public List<Message> getByTicket(Ticket ticket) throws DAOException;

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
