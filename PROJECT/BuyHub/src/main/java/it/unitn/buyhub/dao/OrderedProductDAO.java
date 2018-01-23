/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.entities.OrderedProduct;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.utils.Pair;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Product}.
 *
 * @author Massimo Girondi
 * @since 2017.04.25
 */
public interface OrderedProductDAO extends DAO<OrderedProduct, Pair<Integer, Integer>> {

    /**
     * Returns the number of {@link Product products} stored on the persistence
     * system of the application.
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
     * Persists the new {@link Product products} passed as parameter to the
     * storage system.
     *
     * @param products the new {@code products} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(OrderedProduct products) throws DAOException;

    /**
     * Returns the {@link Product product} with the primary key equals to the
     * one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code product} to get.
     * @return the {@code product} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     * @since 1.0.170425
     */
    @Override
    public OrderedProduct getByPrimaryKey(Pair<Integer, Integer> primaryKey) throws DAOException;

    public List<OrderedProduct> getByOrder(int orderId) throws DAOException;

    /**
     * Returns the list of all the valid {@link Product products} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code products}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     * @since 1.0.170425
     */
    @Override
    public List<OrderedProduct> getAll() throws DAOException;

    /**
     * Update the product passed as parameter and returns it.
     *
     * @param product the product used to update the persistence system.
     * @return the updated product.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Massimo Girondi
     * @since 1.0.170425
     */
    @Override
    public OrderedProduct update(OrderedProduct product) throws DAOException;

}
