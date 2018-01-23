/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Shop;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import it.unitn.buyhub.utils.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Shop}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCShopDAO extends JDBCDAO<Shop, Integer> implements ShopDAO {

    public JDBCShopDAO(Connection con) {
        super(con);
        FRIEND_DAOS.put(UserDAO.class, new JDBCUserDAO(CON));
        // FRIEND_DAOS.put(CoordinateDAO.class, new JDBCCoordinateDAO(CON));
    }

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
    public Long getCount() throws DAOException {
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM shops");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count shops", ex);
        }

        return 0L;
    }

    /**
     * Persists the new {@link Shop shops} passed as parameter to the storage
     * system.
     *
     * @param shops the new {@code shops} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Shop shops) throws DAOException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO shops(name, description, website, id_owner,shipment, validity,shipment_costs) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, shops.getName());
            ps.setString(2, shops.getDescription());
            ps.setString(3, shops.getWebsite());
            ps.setInt(4, shops.getOwner().getId());
            ps.setString(5, shops.getShipment());
            ps.setInt(6, shops.getValidity());
            ps.setDouble(7, shops.getShipment_cost());
            if (ps.executeUpdate() == 1) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    try {
                        CON.rollback();
                    } catch (SQLException ex) {
                        Log.warn(ex);
                    }
                    throw new DAOException("Impossible to persist the new shops");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    Log.warn(ex);
                }
                throw new DAOException("Impossible to persist the new shops");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                Log.warn(ex1);
            }
            throw new DAOException("Impossible to persist the new notification", ex);
        }
    }

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
    public Shop getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM shops WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();

                Shop shop = new Shop();
                shop.setId(primaryKey);
                shop.setDescription(rs.getString("description"));
                shop.setWebsite(rs.getString("website"));
                shop.setName(rs.getString("name"));
                shop.setShipment(rs.getString("shipment"));
                shop.setValidity(rs.getInt("validity"));
                shop.setShipment_cost(rs.getDouble("shipment_costs"));
                //Get owner associate
                UserDAO userDao = getDAO(UserDAO.class);
                shop.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                return shop;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the shops for the passed primary key", ex);
        }
    }

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
    public Shop getByOwner(User owner) throws DAOException {
        Shop shop;
        if (owner == null) {
            throw new DAOException("owner is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM shops WHERE id_owner = ?")) {
            stm.setInt(1, owner.getId());
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();

                shop = new Shop();
                shop.setId(rs.getInt("id"));
                shop.setDescription(rs.getString("description"));
                shop.setWebsite(rs.getString("website"));
                shop.setName(rs.getString("name"));
                shop.setShipment(rs.getString("shipment"));
                shop.setValidity(rs.getInt("validity"));
                shop.setShipment_cost(rs.getDouble("shipment_costs"));
                shop.setOwner(owner);

                return shop;
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the shops for the passed primary key", ex);
        }
    }

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
    public List<Shop> getByName(String name) throws DAOException {
        List<Shop> shops = new ArrayList<>();
        if (name == null) {
            throw new DAOException("name is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM shops WHERE name LIKE '%?%'")) {
            stm.setString(1, name);
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {

                    Shop shop = new Shop();
                    shop.setId(rs.getInt("id"));
                    shop.setDescription(rs.getString("description"));
                    shop.setWebsite(rs.getString("website"));
                    shop.setName(rs.getString("name"));

                    shop.setShipment_cost(rs.getDouble("shipment_costs"));
                    shop.setShipment(rs.getString("shipment"));
                    shop.setValidity(rs.getInt("validity"));

                    //Get owner associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    shop.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                    shops.add(shop);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the shops for the passed primary key", ex);
        }
        return shops;
    }

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
    public List<Shop> getAll() throws DAOException {
        List<Shop> shops = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM shops")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Shop shop = new Shop();
                    shop.setId(rs.getInt("id"));
                    shop.setDescription(rs.getString("description"));
                    shop.setName(rs.getString("name"));
                    shop.setWebsite(rs.getString("website"));
                    shop.setShipment(rs.getString("shipment"));
                    shop.setValidity(rs.getInt("validity"));

                    shop.setShipment_cost(rs.getDouble("shipment_costs"));

                    //Get owner associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    shop.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

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
    public Shop update(Shop shop) throws DAOException {
        if (shop == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed shop is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("UPDATE shops SET name = ?, description = ?, website = ?, id_owner = ?, shipment = ?, validity = ?, shipment_costs=? WHERE id = ?")) {
            std.setString(1, shop.getName());
            std.setString(2, shop.getDescription());
            std.setString(3, shop.getWebsite());
            std.setInt(4, shop.getOwner().getId());
            std.setString(5, shop.getShipment());
            std.setInt(6, shop.getValidity());
            std.setDouble(7, shop.getShipment_cost());
            std.setInt(8, shop.getId());

            if (std.executeUpdate() == 1) {
                return shop;
            } else {
                throw new DAOException("Impossible to update the shop");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the shop", ex);
        }
    }

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
    @Override
    public List<Shop> getByRange(double myLatitude, double myLongitude, int range) throws DAOException {
        List<Shop> shops = new ArrayList<>();
        if (range < 1) {
            throw new DAOException("range is to low");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT shops.* from shops, coordinates WHERE shops.id=coordinates.id_shop AND coordinates.id IN (SELECT id, ( 6371 * acos( cos( radians( ? ) ) * cos( radians( `latitude` ) ) * cos( radians( `longitude` ) - radians( ? ) ) + sin(radians(?)) * sin(radians(`latitude`)) ) ) `distance` FROM coordinates HAVING `distance` < ? ORDER BY `distance`)")) {
            stm.setDouble(1, myLatitude);
            stm.setDouble(2, myLongitude);
            stm.setDouble(3, myLatitude);
            stm.setInt(4, range);
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Shop shop = new Shop();
                    shop.setId(rs.getInt("id"));
                    shop.setDescription(rs.getString("description"));
                    shop.setName(rs.getString("name"));
                    shop.setWebsite(rs.getString("website"));
                    shop.setShipment(rs.getString("shipment"));
                    shop.setValidity(rs.getInt("validity"));

                    shop.setShipment_cost(rs.getDouble("shipment_costs"));

                    //Get owner associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    shop.setOwner(userDao.getByPrimaryKey(rs.getInt("id_owner")));

                    shops.add(shop);

                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed range", ex);
        }
        return shops;
    }

}
