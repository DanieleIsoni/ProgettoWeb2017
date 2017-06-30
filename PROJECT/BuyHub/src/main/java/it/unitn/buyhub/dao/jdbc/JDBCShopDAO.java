/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Notification;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Shop}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCShopDAO extends JDBCDAO<Shop, Integer> implements ShopDAO{

    /**
     * Returns the number of {@link Shop shops} stored on the persistence
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
     * Returns the {@link Shop shop} with the primary key equals to the
     * one passed as parameter.
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
    public List<Shop> getByOwner(User owner) throws DAOException;
    
       /**
     * Returns the list of the {@link Shop shop} with the name that contains the
     * one passed as parameter.
     *
     * @param name the {@code name} of the {@code shops} to get.
     * @return the list of the {@code shops} with the name that contains the
     * one passed as parameter.
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
    public List<Shop> getAll() throws DAOException{
        List<Shop> shops = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM shops")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Shop shop = new Shop();
                    shop.setId(rs.getInt("id"));
                    shop.setDescription(rs.getString("description"));
                    shop.setName(rs.getString("name"));
                    shop.setWebsite(rs.getString("website"));
                    shop.setGlobalValue(rs.getInt("global_value"));

                    //Get owner associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    shop.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));
                    
                    //Get creator associate
                    UserDAO userDao2 = getDAO(UserDAO.class);
                    shop.setCreator(userDao2.getByPrimaryKey(rs.getInt("id_creator")));

                    shops.add(shop);

                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the shops", ex);
        }
        return shops;
    }

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
    public Shop update(Shop shop) throws DAOException{
        if (shop == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed shop is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("UPDATE shops SET name = ?, description = ?, website = ?, id_owner = ?, id_creator = ?, global_value = ? WHERE id = ?")) {
            std.setString(1, shop.getName());
            std.setString(2, shop.getDescription());
            std.setString(3, shop.getWebsite());
            std.setInt(4, shop.getOwner().getId());
            std.setInt(5, shop.getCreator().getId());
            std.setInt(6, shop.getGlobalValue());
            std.setInt(7, shop.getId());
            if (std.executeUpdate() == 1) {
                return shop;
            } else {
                throw new DAOException("Impossible to update the shop");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the shop", ex);
        }
    }
}
