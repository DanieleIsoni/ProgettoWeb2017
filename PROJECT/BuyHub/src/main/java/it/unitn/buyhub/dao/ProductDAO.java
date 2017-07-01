/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Product}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public interface ProductDAO extends DAO<Product, Integer> {

    /**
     * Returns the number of {@link Product products} stored on the persistence
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
    public Long insert(Product products) throws DAOException;

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
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Product getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of the {@link Product product} with the shop passed as
     * parameter.
     *
     * @param shop the {@code shop} of the {@code products} to get.
     * @return the list of the {@link Product product} with the shop the one
     * passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Product> getByShop(Shop shop) throws DAOException;

    /**
     * Returns the list of all the valid {@link Product products} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code products}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Product> getAll() throws DAOException;

    /**
     * Returns the list of all the valid {@link Product products} stored by the
     * storage system with the price in the range.
     *
     * @param min the min price
     * @param max the max price
     * @return the list of all the valid {@code products}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Product> getAllWithPriceRange(double min, double max) throws DAOException;

    /**
     * Update the product passed as parameter and returns it.
     *
     * @param product the product used to update the persistence system.
     * @return the updated product.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Product update(Product product) throws DAOException;

    /**
     * Returns the list of the {@link Product product} with the name passed as
     * parameter.
     *
     * @param name the {@code name} of the {@code products} to get.
     * @return the list of the {@link Product product} with the name that
     * contains the one passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Product> getByName(String name) throws DAOException;

    /**
     * Returns the list of the {@link Product product} with the name that
     * contains the one passed as parametera and the price in the selected gap.
     *
     * @param name the {@code name} of the {@code products} to get.
     * @param min the {@code min} price of the {@code products} to get.
     * @param max the {@code max} price of the {@code products} to get.
     * @return the list of the {@link Product product} with the name that
     * contains the one passed as parametera and the price in the selected gap.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Product> getByNameAndPriceRange(String name, double min, double max) throws DAOException;
}
