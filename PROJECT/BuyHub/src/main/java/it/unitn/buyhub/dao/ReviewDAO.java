/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Review}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public interface ReviewDAO extends DAO<Review, Integer> {

    /**
     * Returns the number of {@link Review reviews} stored on the persistence
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
     * Returns the {@link Review review} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code review} to get.
     * @return the {@code review} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Review getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of the {@link Review review} with the product passed as
     * parameter.
     *
     * @param product the {@code product} of the {@code reviews} to get.
     * @return the list of the {@code reviews} with the product passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Review> getByProduct(Product product) throws DAOException;

    /**
     * Returns the list of all the valid {@link Review reviews} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code reviews}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Review> getAll() throws DAOException;

    /**
     * Update the review passed as parameter and returns it.
     *
     * @param review the review used to update the persistence system.
     * @return the updated review.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Review update(Review review) throws DAOException;

    /**
     * Returns the list of the {@link Review review} with the creator passed as
     * parameter.
     *
     * @param creator the {@code creator} of the {@code reviews} to get.
     * @return the list of the {@code reviews} with the product passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Review> getByCreator(User creator) throws DAOException;


}
