/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.User;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import it.unitn.buyhub.utils.Utility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code User}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCUserDAO extends JDBCDAO<User, Integer> implements UserDAO {

    public JDBCUserDAO(Connection con) {
        super(con);
    }

    /**
     * Persists the new {@link Users users} passed as parameter to the storage
     * system.
     *
     * @param users the new {@code users} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    public Long insert(User user) throws DAOException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO users(username, password, first_name, last_name, email, capability,avatar) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getEmail());
            ps.setInt(6, user.getCapability());
            ps.setString(7, user.getAvatar());

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
                    throw new DAOException("Impossible to persist the new user");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    //TODO: log the exception
                }
                throw new DAOException("Impossible to persist the new user");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                //TODO: log the exception
            }
            throw new DAOException("Impossible to persist the new user", ex);
        }
    }

    /**
     * Returns the number of {@link User users} stored on the persistence system
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
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM users");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to users coordinates", ex);
        }

        return 0L;
    }

    /**
     * Returns the {@link User user} with the primary key equals to the one
     * passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code user} to get.
     * @return the {@code user} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public User getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                //rs.getInt("id"),rs.getInt("latitude"),rs.getInt("longitude"),rs.getString("address")
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setLastName(rs.getString("last_name"));
                user.setFirstName(rs.getString("first_name"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setCapability(rs.getInt("capability"));
                user.setUsername(rs.getString("username"));
                String avatar = rs.getString("avatar");
                if (avatar == null) {
                    avatar = "images/noimage.png";
                }
                user.setAvatar(avatar);

                return user;
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the user for the passed primary key", ex);
        }
    }

    /**
     * Returns the list of the {@link User user} with the password and username
     * passed as parameter.
     *
     * @param username the {@code username} of the {@code users} to get.
     * @param password the {@code password} of the {@code users} to get.
     * @return the {@code user} with the username and password passed parameter,
     * returns null if not exist or wrong password.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    public User getByUsernameAndPassord(String username, String password) throws DAOException {
        if ((username == null) || (password == null)) {
            throw new DAOException("Username and password are mandatory fields", new NullPointerException("username or password are null"));
        }

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            stm.setString(1, username);
            stm.setString(2, password);
            try (ResultSet rs = stm.executeQuery()) {

                int count = 0;
                while (rs.next()) {
                    count++;
                    if (count > 1) {
                        throw new DAOException("Unique constraint violated! There are more than one user with the same username! WHY???");
                    }
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setCapability(rs.getInt("capability"));
                    String avatar = rs.getString("avatar");
                    if (avatar == null) {
                        avatar = "images/noimage.png";
                    }
                    user.setAvatar(avatar);

                    return user;
                }

                return null;
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the user", ex);
        }
    }

    /**
     * Returns the list of all the valid admin {@link User users} stored by the
     * storage system.
     *
     * @return the list of all the admin valid {@code users}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<User> getAdmins() throws DAOException {
        List<User> users = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM users WHERE capability=? ORDER BY last_name")) {
            stm.setInt(1, Utility.CAPABILITY.ADMIN.ordinal());
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setCapability(rs.getInt("capability"));
                    String avatar = rs.getString("avatar");
                    if (avatar == null) {
                        avatar = "images/noimage.png";
                    }
                    user.setAvatar(avatar);

                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of admins", ex);
        }

        return users;
    }

    /**
     * Returns the list of all the valid {@link User users} stored by the
     * storage system.
     *
     * @return the list of all the valid {@code users}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<User> getAll() throws DAOException {
        List<User> users = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM users ORDER BY last_name")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setCapability(rs.getInt("capability"));
                    String avatar = rs.getString("avatar");
                    if (avatar == null) {
                        avatar = "images/noimage.png";
                    }
                    user.setAvatar(avatar);

                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of users", ex);
        }

        return users;
    }

    /**
     * Update the user passed as parameter and returns it.
     *
     * @param user the user used to update the persistence system.
     * @return the updated user.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public User update(User user) throws DAOException {
        if (user == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed user is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("UPDATE users SET username = ?, password = ?, first_name = ?, last_name = ?, email = ?, capability = ?, avatar = ? WHERE id = ?")) {
            std.setString(1, user.getUsername());
            std.setString(2, user.getPassword());
            std.setString(3, user.getFirstName());
            std.setString(4, user.getLastName());
            std.setString(5, user.getEmail());
            std.setInt(6, user.getCapability());
            std.setInt(8, user.getId());

            std.setString(7, user.getAvatar());
            if (std.executeUpdate() == 1) {
                return user;
            } else {
                throw new DAOException("Impossible to update the user");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the user: " + ex.toString());
        }
    }

    @Override
    public User getByEmail(String mail) throws DAOException {
        if (mail == null) {
            throw new DAOException("mail is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            stm.setString(1, mail);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                //rs.getInt("id"),rs.getInt("latitude"),rs.getInt("longitude"),rs.getString("address")
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setLastName(rs.getString("last_name"));
                user.setFirstName(rs.getString("first_name"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setCapability(rs.getInt("capability"));
                user.setUsername(rs.getString("username"));
                String avatar = rs.getString("avatar");
                if (avatar == null) {
                    avatar = "images/noimage.png";
                }
                user.setAvatar(avatar);

                return user;
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the user for the passed email", ex);
        }

    }

}
