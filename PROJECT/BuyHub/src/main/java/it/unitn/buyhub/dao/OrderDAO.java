/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Order}.
 *
 * @author Massimo Girondi
 * @since 2017.04.25
 */
public interface OrderDAO extends DAO<Order, Integer> {

    /**
     * Returns the number of {@link Order orders} stored on the persistence system
     * of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     */
    @Override
    public Long getCount() throws DAOException;

    /**
     * Persists the new {@link Orders orders} passed as parameter to the storage
     * system.
     *
     * @param orders the new {@code orders} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Massimo Girondi
     * 
     */
    public Long insert(Order order) throws DAOException;

    /**
     * Returns the {@link Order order} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code order} to get.
     * @return the {@code order} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     * 
     */
    @Override
    public Order getByPrimaryKey(Integer primaryKey) throws DAOException;

        /**
     * Returns the {@link Order order} with the user equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@user  user} to get.
     * @return the {@code order} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     * 
     */
    
    public List<Order> getByUser(Integer primaryKey) throws DAOException;



    /**
     * Returns the list of all the valid {@link Order orders} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code orders}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     * 
     */
    @Override
    public List<Order> getAll() throws DAOException;


    /**
     * Update the order passed as parameter and returns it.
     *
     * @param order the order used to update the persistence system.
     * @return the updated order.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Massimo Girondi
     * 
     */
    @Override
    public Order update(Order order) throws DAOException;
    
    
    
        /**
     * Returns the {@link Order order} with the shop equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@shop shop} to get.
     * @return the {@code order} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     * 
     */
    public List<Order> getByShop(Integer primaryKey) throws DAOException;
            
}
