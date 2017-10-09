/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code User}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public interface UserDAO extends DAO<User, Integer> {

    /**
     * Returns the number of {@link User users} stored on the persistence system
     * of the application.
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
     * Persists the new {@link Users users} passed as parameter to the storage
     * system.
     *
     * @param users the new {@code users} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(User user) throws DAOException;

    /**
     * Returns the {@link User user} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code user} to get.
     * @return the {@code user} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public User getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of the {@link User user} with the password and username
     * passed as parameter.
     *
     * @param username the {@code username} of the {@code users} to get.
     * @param password the {@code password} of the {@code users} to get.
     * @return the {@code user} with the username and password passed parameter,
     * returns null if not exist or wrong password.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public User getByUsernameAndPassord(String username, String password) throws DAOException;

    /**
     * Returns the list of all the valid {@link User users} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code users}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<User> getAll() throws DAOException;

        /**
     * Returns the list of all the valid {@link User users} stored by the
     * storage system that have admin privileges.
     *
     * @return the list of all the valid admin {@code users}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     * @since 1.0.170425
     */
    public List<User> getAdmins() throws DAOException;
    /**
     * Update the user passed as parameter and returns it.
     *
     * @param user the user used to update the persistence system.
     * @return the updated user.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public User update(User user) throws DAOException;
}
