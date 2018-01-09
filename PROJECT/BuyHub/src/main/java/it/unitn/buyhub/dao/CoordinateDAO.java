/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Coordinate}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public interface CoordinateDAO extends DAO<Coordinate, Integer> {

    /**
     * Returns the number of {@link Coordinate coordinates} stored on the
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
     * Persists the new {@link Coordinate coordinate} passed as parameter to the
     * storage system.
     *
     * @param coordinate the new {@code coordinate} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Coordinate coordinate) throws DAOException;

    /**
     * Returns the {@link Coordinate coordinate} with the primary key equals to
     * the one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code coordinate} to get.
     * @return the {@code coordinate} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Coordinate getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the {@link Coordinate coordinate} with the address that contains
     * the one passed as parameter.
     *
     * @param address the {@code address} of the {@code coordinate} to get.
     * @return the {@code coordinate} with the address that contains the one
     * passed as parameter or {@code null} if no entities with that id is not
     * present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public Coordinate getByAddress(String address) throws DAOException;

    /**
     * Returns the {@link Coordinate coordinate} with the distance from me less
     * or equals than the one passed as parameter.
     *
     * @param myLatitude the current latitude
     * @param myLongitude the current longitude
     * @param range search range
     * @return the {@code coordinate} with the distance from me less or equals
     * than the one passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Coordinate> getByRange(double myLatitude, double myLongitude, int range) throws DAOException;

    /**
     * Returns the list of all the valid {@link Coordinate coordinates} stored
     * by the storage system.
     *
     * @return the list of all the valid {@code coordinates}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Coordinate> getAll() throws DAOException;

    /**
     * Update the coordinate passed as parameter and returns it.
     *
     * @param coordinate the coordinate used to update the persistence system.
     * @return the updated coordinate.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Coordinate update(Coordinate coordinate) throws DAOException;

    /**
     * Returns the {@link Coordinate coordinate} with the shop key equals to the
     * one passed as parameter.
     *
     * @param s the {@code id} of the {@code shop}'s coordinates to get.
     * @return the list of {@code coordinate} with the shop id equals to the one
     * passed as parameter or {@code null} if no entities with that shop's id is
     * not present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     * @since 1.0.170714
     */
    public List<Coordinate> getByShop(Shop s) throws DAOException;
    
    
    public void remove(Coordinate coordinate) throws DAOException;
}
