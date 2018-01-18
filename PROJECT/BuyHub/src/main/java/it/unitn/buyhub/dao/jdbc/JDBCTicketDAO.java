/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Message;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.entities.Review;
import it.unitn.buyhub.dao.entities.Ticket;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Message}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCTicketDAO extends JDBCDAO<Ticket, Integer> implements TicketDAO {

    public JDBCTicketDAO(Connection con) {
        super(con);
        FRIEND_DAOS.put(UserDAO.class, new JDBCUserDAO(CON));
        FRIEND_DAOS.put(OrderDAO.class, new JDBCOrderDAO(CON));
        FRIEND_DAOS.put(ReviewDAO.class, new JDBCReviewDAO(CON));
    }

    /**
     * Returns the number of {@link Message messages} stored on the persistence
     * system of the application.
     *
     * @return the number of records present into the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     */
    @Override
    public Long getCount() throws DAOException {
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM tickets");) {
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
     * Returns the {@link Message message} with the primary key equals to the
     * one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code message} to get.
     * @return the {@code message} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Ticket getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM tickets WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {
                
                Ticket tick = new Ticket();
                rs.next();
                tick.setId(rs.getInt("id"));

                //Get orderDAO associate
                OrderDAO orderDAO = getDAO(OrderDAO.class);
                tick.setOrder(orderDAO.getByPrimaryKey(rs.getInt("order_id")));

               
                return tick;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
    }

    

   

    /**
     * Returns the list of all the valid {@link Message messages} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code messages}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Ticket> getAll() throws DAOException {
        List<Ticket> messages = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM tickets")) {
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                Ticket tick = new Ticket();
                rs.next();
                tick.setId(rs.getInt("id"));

                //Get orderDAO associate
                OrderDAO orderDAO = getDAO(OrderDAO.class);
                tick.setOrder(orderDAO.getByPrimaryKey(rs.getInt("order_id")));

               

                messages.add(tick);
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
        return messages;
    }

    /**
     * Update the message passed as parameter and returns it.
     *
     * @param message the message used to update the persistence system.
     * @return the updated message.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Ticket update(Ticket ticket) throws DAOException {
        if (ticket == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed message is null"));
        }

        //TODO
        return null;
    }

    /**
     * Persists the new {@link Message message} passed as parameter to the
     * storage system.
     *
     * @param message the new {@code coordinate} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Ticket tick) throws DAOException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO tickets(order_id) VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tick.getOrder().getId());

            if (ps.executeUpdate() == 1) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    try {
                        CON.rollback();
                    } catch (SQLException ex) {
                        //TODO: log the exception
                    }
                    throw new DAOException("Impossible to persist the new message");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    //TODO: log the exception
                }
                throw new DAOException("Impossible to persist the new message");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                //TODO: log the exception
            }
            throw new DAOException("Impossible to persist the new message", ex);
        }
    }


    @Override
    public Ticket getByOrder(Order order) throws DAOException {
         if (order == null) {
            throw new DAOException("order is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM tickets WHERE order_id = ?")) {
            stm.setInt(1, order.getId());
            try (ResultSet rs = stm.executeQuery()) {
                
                Ticket tick = new Ticket();
                rs.next();
                tick.setId(rs.getInt("id"));

                //Get orderDAO associate
                OrderDAO orderDAO = getDAO(OrderDAO.class);
                tick.setOrder(orderDAO.getByPrimaryKey(rs.getInt("order_id")));

               
                return tick;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
    }

}
