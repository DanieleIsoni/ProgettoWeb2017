/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                coordinate.setLatitude(rs.getInt("latitude"));
                coordinate.setLongitude(rs.getInt("longitude"));
                coordinate.setAddress(rs.getString("address"));

                return coordinate;
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
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
                coordinate.setLatitude(rs.getInt("latitude"));
                coordinate.setLongitude(rs.getInt("longitude"));
                coordinate.setAddress(rs.getString("address"));

                return coordinate;
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
    public Coordinate getByRange(double myLatitude, double myLongitude, int range) throws DAOException {
        if (range < 1) {
            throw new DAOException("range is to low");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT *, ( 6371 * acos( cos( radians( ? ) ) * cos( radians( `latitude` ) ) * cos( radians( `longitude` ) - radians( ? ) ) + sin(radians(?)) * sin(radians(`latitude`)) ) ) `distance` FROM coordinates HAVING `distance` < ? ORDER BY `distance`")) {
            stm.setDouble(1, myLatitude);
            stm.setDouble(2, myLongitude);
            stm.setDouble(3, myLatitude);
            stm.setInt(4, range);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                //rs.getInt("id"),rs.getInt("latitude"),rs.getInt("longitude"),rs.getString("address")
                Coordinate coordinate = new Coordinate();
                coordinate.setId(rs.getInt("id"));
                coordinate.setLatitude(rs.getInt("latitude"));
                coordinate.setLongitude(rs.getInt("longitude"));
                coordinate.setAddress(rs.getString("address"));

                return coordinate;
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed range", ex);
        }
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
                    coordinate.setLatitude(rs.getInt("latitude"));
                    coordinate.setLongitude(rs.getInt("longitude"));
                    coordinate.setAddress(rs.getString("address"));
                    coordinates.add(coordinate);
                }
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

        try (PreparedStatement std = CON.prepareStatement("UPDATE coordinates SET latitude = ?, longitude = ?, address = ? WHERE id = ?")) {
            std.setDouble(1, coordinate.getLatitude());
            std.setDouble(2, coordinate.getLongitude());
            std.setString(3, coordinate.getAddress());
            std.setInt(4, coordinate.getId());
            if (std.executeUpdate() == 1) {
                return coordinate;
            } else {
                throw new DAOException("Impossible to update the coordinate");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the coordinate", ex);
        }
    }
}