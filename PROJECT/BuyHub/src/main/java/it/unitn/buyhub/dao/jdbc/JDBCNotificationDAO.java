/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Message;
import it.unitn.buyhub.dao.entities.Notification;
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
 * system that interact with {@code Notification}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCNotificationDAO extends JDBCDAO<Notification, Integer> implements NotificationDAO {

    public JDBCNotificationDAO(Connection con) {
        super(con);
        FRIEND_DAOS.put(UserDAO.class, new JDBCUserDAO(CON));
    }

    /**
     * Persists the new {@link Notification notification} passed as parameter to
     * the storage system.
     *
     * @param notification the new {@code notification} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(Notification notification) throws DAOException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO notifications(description, date_creation, status, id_user) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, notification.getDescription());
            ps.setTimestamp(2, new Timestamp(notification.getDateCreation().getTime()));
            ps.setBoolean(3, notification.isStatus());
            ps.setInt(4, notification.getUser().getId());

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
                    throw new DAOException("Impossible to persist the new notification");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    //TODO: log the exception
                }
                throw new DAOException("Impossible to persist the new notification");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                //TODO: log the exception
            }
            throw new DAOException("Impossible to persist the new notification", ex);
        }
    }

    /**
     * Returns the number of {@link Notification notifications} stored on the
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
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM notifications");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count notifications", ex);
        }

        return 0L;
    }

    /**
     * Returns the {@link Notification notification} with the primary key equals
     * to the one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code notification} to get.
     * @return the {@code notification} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Notification getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM notifications WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                Timestamp timestamp = rs.getTimestamp("date_creation");
                Date date = new Date(timestamp.getTime());
                Notification notification = new Notification();
                notification.setId(primaryKey);
                notification.setDescription(rs.getString("description"));
                notification.setDateCreation(date);
                notification.setStatus(rs.getBoolean("status"));

                //Get user associate
                UserDAO userDao = getDAO(UserDAO.class);
                notification.setUser(userDao.getByPrimaryKey(rs.getInt("id_user")));

                return notification;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
    }

    /**
     * Returns all the {@link Notification notification} owned by the user
     * passed as parameter, read and unread.
     *
     * @param user the {@code user} of the {@code notifications} to get.
     * @return the list of the {@code notifications} owned by the user passed as
     * parameter, read and unread.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Notification> getAllByUser(User user) throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        if (user == null) {
            throw new DAOException("primaryKey is user");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM notifications WHERE id_user = ?")) {
            stm.setInt(1, user.getId());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("date_creation");
                    Date date = new Date(timestamp.getTime());
                    Notification notification = new Notification();
                    notification.setId(rs.getInt("id"));
                    notification.setDescription(rs.getString("description"));
                    notification.setDateCreation(date);
                    notification.setStatus(rs.getBoolean("status"));

                    //Get user associate
                    notification.setUser(user);

                    notifications.add(notification);
                }

            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the coordinates for the passed primary key", ex);
        }
        return notifications;
    }

    /**
     * Returns the read {@link Notification notification} owned by the user
     * passed as parameter.
     *
     * @param user the {@code user} of the {@code notifications} to get.
     * @return the list of the read {@code notifications} owned by the user
     * passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Notification> getReadByUser(User user) throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        if (user == null) {
            throw new DAOException("primaryKey is user");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM notifications WHERE id_user = ? AND status = 1")) {
            stm.setInt(1, user.getId());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("date_creation");
                    Date date = new Date(timestamp.getTime());
                    Notification notification = new Notification();
                    notification.setId(rs.getInt("id"));
                    notification.setDescription(rs.getString("description"));
                    notification.setDateCreation(date);
                    notification.setStatus(rs.getBoolean("status"));

                    //Get user associate
                    notification.setUser(user);

                    notifications.add(notification);
                }

            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the notifications", ex);
        }
        return notifications;
    }

    /**
     * Returns the unread {@link Notification notification} owned by the user
     * passed as parameter.
     *
     * @param user the {@code user} of the {@code notifications} to get.
     * @return the list of the unread {@code notifications} owned by the user
     * passed as parameter.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public List<Notification> getUnreadByUser(User user) throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        if (user == null) {
            throw new DAOException("primaryKey is user");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM notifications WHERE id_user = ? AND status = 0")) {
            stm.setInt(1, user.getId());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("date_creation");
                    Date date = new Date(timestamp.getTime());
                    Notification notification = new Notification();
                    notification.setId(rs.getInt("id"));
                    notification.setDescription(rs.getString("description"));
                    notification.setDateCreation(date);
                    notification.setStatus(rs.getBoolean("status"));

                    //Get user associate
                    notification.setUser(user);

                    notifications.add(notification);
                }

            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the notifications", ex);
        }
        return notifications;
    }

    @Override
    public List<Notification> getAll() throws DAOException {
        List<Notification> notifications = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM notifications")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("date_creation");
                    Date date = new Date(timestamp.getTime());
                    Notification notification = new Notification();
                    notification.setId(rs.getInt("id"));
                    notification.setDescription(rs.getString("description"));
                    notification.setDateCreation(date);
                    notification.setStatus(rs.getBoolean("status"));

                    //Get user associate
                    UserDAO userDao = getDAO(UserDAO.class);
                    notification.setUser(userDao.getByPrimaryKey(rs.getInt("id_user")));

                    notifications.add(notification);

                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the notifications", ex);
        }
        return notifications;
    }

    /**
     * Update the notification passed as parameter and returns it.
     *
     * @param notification the notification used to update the persistence
     * system.
     * @return the updated notification.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Notification update(Notification notification) throws DAOException {
        if (notification == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed notification is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("UPDATE notifications SET description = ?, date_creation = ?, status = ?, id_user = ? WHERE id = ?")) {
            std.setString(1, notification.getDescription());
            std.setTimestamp(2, new Timestamp(notification.getDateCreation().getTime()));
            std.setBoolean(3, notification.isStatus());
            std.setInt(4, notification.getUser().getId());
            std.setInt(5, notification.getId());
            if (std.executeUpdate() == 1) {
                return notification;
            } else {
                throw new DAOException("Impossible to update the notification");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the notification", ex);
        }
    }
}
