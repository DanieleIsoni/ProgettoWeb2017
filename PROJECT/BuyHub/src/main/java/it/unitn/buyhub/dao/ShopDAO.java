/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Shop}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public interface ShopDAO extends DAO<Shop, Integer> {

    /**
     * Returns the number of {@link Shop shops} stored on the persistence system
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
     * Persists the new {@link Shop shops} passed as parameter to the
     * storage system.
     *
     * @param shops the new {@code shops} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Shop shops) throws DAOException;

    /**
     * Returns the {@link Shop shop} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code shop} to get.
     * @return the {@code shop} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Shop getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of the {@link Shop shop} with the owner passed as
     * parameter.
     *
     * @param owner the {@code owner} of the {@code shops} to get.
     * @return the list of the {@code shops} with the owner passed as l
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public Shop getByOwner(User owner) throws DAOException;

    /**
     * Returns the list of the {@link Shop shop} with the name that contains the
     * one passed as parameter.
     *
     * @param name the {@code name} of the {@code shops} to get.
     * @return the list of the {@code shops} with the name that contains the one
     * passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Shop> getByName(String name) throws DAOException;

    /**
     * Returns the list of all the valid {@link Shop shops} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code shops}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Shop> getAll() throws DAOException;

    /**
     * Update the shop passed as parameter and returns it.
     *
     * @param shop the shop used to update the persistence system.
     * @return the updated shop.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Shop update(Shop shop) throws DAOException;

    /**
     * Returns the {@link Shop shop} with the distance from me less or equals
     * than the one passed as parameter.
     *
     * @param myLatitude the current latitude
     * @param myLongitude the current longitude
     * @param range search range in km
     * @return the {@code shop} with the distance from less or equals distance
     * than the one passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Shop> getByRange(double myLatitude, double myLongitude, int range) throws DAOException;

}
