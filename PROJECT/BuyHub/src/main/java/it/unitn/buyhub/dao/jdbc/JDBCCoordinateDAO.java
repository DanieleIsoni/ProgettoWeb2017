/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Shop;
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
 * system that interact with {@code Coordinate}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCCoordinateDAO extends JDBCDAO<Coordinate, Integer> implements CoordinateDAO {

    public JDBCCoordinateDAO(Connection con) {
        super(con);
        FRIEND_DAOS.put(ShopDAO.class, new JDBCShopDAO(CON));
    }

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
    public Long getCount() throws DAOException {
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM coordinates");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count coordinates", ex);
        }

        return 0L;
    }

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
    public Coordinate getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM coordinates WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                //rs.getInt("id"),rs.getInt("latitude"),rs.getInt("longitude"),rs.getString("address")
                Coordinate coordinate = new Coordinate();
                coordinate.setId(rs.getInt("id"));
                coordinate.setLatitude(rs.getDouble("latitude"));
                coordinate.setLongitude(rs.getDouble("longitude"));
                coordinate.setAddress(rs.getString("address"));

                coordinate.setOpening_hours(rs.getString("opening_hours"));
                ShopDAO shopDao = getDAO(ShopDAO.class);
                Shop s = shopDao.getByPrimaryKey(rs.getInt("id_shop"));
                coordinate.setShop(s);
                return coordinate;
            } catch (DAOFactoryException ex) {
                throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
    }

    /**
     * Returns the {@link Coordinate coordinate} with the shop key equals to the
     * one passed as parameter.
     *
     * @param s the {@code id} of the {@code shop}'s coordinates to get.
     * @return the {@code coordinate} with the shop id equals to the one passed
     * as parameter or {@code null} if no entities with that shop's id is not
     * present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Massimo Girondi
     * @since 1.0.170714
     */
    @Override
    public List<Coordinate> getByShop(Shop s) throws DAOException {

        if (s == null) {
            throw new DAOException("primaryKey is null");
        }
        List<Coordinate> coordinates = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM coordinates c WHERE c.id_shop = ?")) {

            stm.setInt(1, s.getId());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    //rs.getInt("id"),rs.getInt("latitude"),rs.getInt("longitude"),rs.getString("address")
                    Coordinate coordinate = new Coordinate();
                    coordinate.setId(rs.getInt("id"));
                    coordinate.setLatitude(rs.getDouble("latitude"));
                    coordinate.setLongitude(rs.getDouble("longitude"));
                    coordinate.setAddress(rs.getString("address"));

                    coordinate.setOpening_hours(rs.getString("opening_hours"));
                    ShopDAO shopDao = getDAO(ShopDAO.class);
                    coordinate.setShop(s);

                    coordinates.add(coordinate);
                }
                return coordinates;
            } catch (DAOFactoryException ex) {
                throw new DAOException("Impossible to get the coordinates for the passed shop", ex);
            }
        } catch (SQLException ex) {

            throw new DAOException("Impossible to get the coordinates for the passed shop", ex);
        }
    }

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
    public Coordinate getByAddress(String address) throws DAOException {
        if (address == null) {
            throw new DAOException("address is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM coordinates WHERE address = '%?%'")) {
            stm.setString(1, address);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                //rs.getInt("id"),rs.getInt("latitude"),rs.getInt("longitude"),rs.getString("address")
                Coordinate coordinate = new Coordinate();
                coordinate.setId(rs.getInt("id"));
                coordinate.setLatitude(rs.getDouble("latitude"));
                coordinate.setLongitude(rs.getDouble("longitude"));
                coordinate.setAddress(rs.getString("address"));
                coordinate.setOpening_hours(rs.getString("opening_hours"));
                ShopDAO shopDao = getDAO(ShopDAO.class);
                coordinate.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                return coordinate;
            } catch (DAOFactoryException ex) {
                throw new DAOException("Impossible to get the coordinates for the passed address", ex);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed address", ex);
        }
    }

    /**
     * Returns the {@link Coordinate coordinate} with the distance from me less
     * or equals than the one passed as parameter.
     *
     * @param myLatitude the current latitude
     * @param myLongitude the current longitude
     * @param range search range in km
     * @return the {@code coordinate} with the distance from me less or equals
     * than the one passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Coordinate> getByRange(double myLatitude, double myLongitude, int range) throws DAOException {
        List<Coordinate> coordinates = new ArrayList<>();
        if (range < 1) {
            throw new DAOException("range is to low");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT *, ( 6371 * acos( cos( radians( ? ) ) * cos( radians( `latitude` ) ) * cos( radians( `longitude` ) - radians( ? ) ) + sin(radians(?)) * sin(radians(`latitude`)) ) ) `distance` FROM coordinates HAVING `distance` < ? ORDER BY `distance`")) {
            stm.setDouble(1, myLatitude);
            stm.setDouble(2, myLongitude);
            stm.setDouble(3, myLatitude);
            stm.setInt(4, range);
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    //rs.getInt("id"),rs.getInt("latitude"),rs.getInt("longitude"),rs.getString("address")
                    Coordinate coordinate = new Coordinate();
                    coordinate.setId(rs.getInt("id"));
                    coordinate.setLatitude(rs.getDouble("latitude"));
                    coordinate.setLongitude(rs.getDouble("longitude"));
                    coordinate.setAddress(rs.getString("address"));
                    coordinate.setOpening_hours(rs.getString("opening_hours"));
                    ShopDAO shopDao = getDAO(ShopDAO.class);
                    coordinate.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                    coordinates.add(coordinate);
                }

            } catch (DAOFactoryException ex) {
                throw new DAOException("Impossible to get the coordinates for the passed range", ex);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed range", ex);
        }
        return coordinates;
    }

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
    public List<Coordinate> getAll() throws DAOException {
        List<Coordinate> coordinates = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM coordinates")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Coordinate coordinate = new Coordinate();
                    coordinate.setId(rs.getInt("id"));
                    coordinate.setLatitude(rs.getDouble("latitude"));
                    coordinate.setLongitude(rs.getDouble("longitude"));
                    coordinate.setAddress(rs.getString("address"));
                    coordinate.setOpening_hours(rs.getString("opening_hours"));
                    ShopDAO shopDao = getDAO(ShopDAO.class);
                    coordinate.setShop(shopDao.getByPrimaryKey(rs.getInt("id_shop")));

                    coordinates.add(coordinate);
                }
            } catch (DAOFactoryException ex) {
                throw new DAOException("Impossible to get the list of coordinates", ex);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of coordinates", ex);
        }

        return coordinates;
    }

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
    public Coordinate update(Coordinate coordinate) throws DAOException {
        if (coordinate == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed coordinate is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("UPDATE coordinates SET latitude = ?, longitude = ?, address = ?, opening_hours = ?  WHERE id = ?")) {
            std.setDouble(1, coordinate.getLatitude());
            std.setDouble(2, coordinate.getLongitude());
            std.setString(3, coordinate.getAddress());

            std.setString(4, coordinate.getOpening_hours());
            std.setInt(5, coordinate.getId());

            if (std.executeUpdate() == 1) {
                return coordinate;
            } else {
                throw new DAOException("Impossible to update the coordinate");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the coordinate", ex);
        }
    }

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
    @Override
    public Long insert(Coordinate coordinate) throws DAOException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO coordinates(latitude, longitude, address,id_shop, opening_hours) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, coordinate.getLatitude());
            ps.setDouble(2, coordinate.getLongitude());
            ps.setString(3, coordinate.getAddress());
            ps.setInt(4, coordinate.getShop().getId());
            ps.setString(5, coordinate.getOpening_hours());
            Log.info(ps.toString());
            if (ps.executeUpdate() == 1) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    try {
                        CON.rollback();
                    } catch (SQLException ex) {
                        Log.error(ex);
                    }
                    throw new DAOException("Impossible to persist the new coordinate");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    Log.error(ex);
                }
                throw new DAOException("Impossible to persist the new coordinate");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                Log.error(ex1);
            }
            throw new DAOException("Impossible to persist the new coordinate", ex);
        }
    }

    /**
     * Remove the coordinate passed as parameter and returns it.
     *
     * @param coordinate the coordinate used to remove the persistence system.
     * @return a boolean value, true if removed.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Daniele Isoni
     * @since 1.0.170425
     */
    @Override
    public boolean remove(Coordinate coordinate) throws DAOException {
        try (PreparedStatement stm = CON.prepareStatement("DELETE FROM coordinates WHERE coordinates.id = ?")) {
            stm.setInt(1, coordinate.getId());
            if (stm.executeUpdate() == 1) {
                Log.info("Coordinate " + coordinate.getId() + " deleted");
                return true;
            } else {
                Log.error("Error in executing delete");
                return false;
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to delete the coordinate", ex);
        }
    }
    
}
