/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao;

import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Picture}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public interface PictureDAO extends DAO<Picture, Integer> {

    /**
     * Returns the number of {@link Picture pictures} stored on the persistence
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
     * Persists the new {@link Picture pictures} passed as parameter to the
     * storage system.
     *
     * @param pictures the new {@code pictures} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Picture pictures) throws DAOException;

    /**
     * Returns the {@link Picture picture} with the primary key equals to the
     * one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code picture} to get.
     * @return the {@code picture} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Picture getByPrimaryKey(Integer primaryKey) throws DAOException;

    /**
     * Returns the list of the {@link Picture picture} with the owner passed as
     * parameter.
     *
     * @param owner the {@code owner} of the {@code pictures} to get.
     * @return the list of the {@code pictures} with the owner passed as l
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Picture> getByOwner(User owner) throws DAOException;

    /**
     * Returns the list of all the valid {@link Picture pictures} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code pictures}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Picture> getAll() throws DAOException;

    /**
     * Update the picture passed as parameter and returns it.
     *
     * @param picture the picture used to update the persistence system.
     * @return the updated picture.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Picture update(Picture picture) throws DAOException;

    public long insertProductPicture(Product pr, Picture p) throws DAOException;

    /**
     * Returns the list of the {@link Picture picture} with the product passed
     * as parameter.
     *
     * @param product the {@code product} of the {@code pictures} to get.
     * @return the list of the {@code pictures} with the product passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Picture> getByProduct(Product product) throws DAOException;

    /**
     * Returns the list of the {@link Picture picture} with the review passed as
     * parameter.
     *
     * @param review the {@code product} of the {@code pictures} to get.
     * @return the list of the {@code pictures} with the review passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Picture> getByReview(Review review) throws DAOException;

    /**
     * Returns the list of the {@link Picture picture} with the shop passed as
     * parameter.
     *
     * @param shop the {@code product} of the {@code pictures} to get.
     * @return the list of the {@code pictures} with the shop passed as
     * parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Picture> getByShop(Shop shop) throws DAOException;
    
    public boolean removeProductPicture(Product product, Picture picture) throws DAOException;
}
