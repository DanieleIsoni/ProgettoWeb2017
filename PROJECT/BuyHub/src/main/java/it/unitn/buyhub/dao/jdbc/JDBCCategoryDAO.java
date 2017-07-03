/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.*;
import it.unitn.buyhub.dao.entities.Category;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * All concrete DAO must implement this interface to handle the persistence
 * system that interact with {@code Category}.
 *
 * @author Matteo Battilana
 * @since 2017.04.25
 */
public class JDBCCategoryDAO extends JDBCDAO<Category, Integer> implements CategoryDAO {

    public JDBCCategoryDAO(Connection con) {
        super(con);
    }

    /**
     * Returns the number of {@link Category categories} stored on the
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
        try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM categories");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count categories", ex);
        }

        return 0L;
    }

    /**
     * Returns the {@link Category category} with the primary key equals to the
     * one passed as parameter.
     *
     * @param primaryKey the {@code id} of the {@code category} to get.
     * @return the {@code category} with the id equals to the one passed as
     * parameter or {@code null} if no entities with that id is not present into
     * the storage system.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Category getByPrimaryKey(Integer primaryKey) throws DAOException {
        if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM categories WHERE id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                //rs.getInt("id"),rs.getInt("latitude"),rs.getInt("longitude"),rs.getString("address")
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setDescription(rs.getString("description"));

                return category;
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the categories for the passed primary key", ex);
        }
    }

    /**
     * Returns the list of all the valid {@link Category categories} stored by
     * the storage system.
     *
     * @return the list of all the valid {@code categories}.
     * @throws DAOException if an error occurred during the information
     * retrieving.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public List<Category> getAll() throws DAOException {
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM categories")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("id"));
                    category.setName(rs.getString("name"));
                    category.setDescription(rs.getString("description"));
                    categories.add(category);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of categories", ex);
        }

        return categories;
    }

    /**
     * Update the category passed as parameter and returns it.
     *
     * @param category the category used to update the persistence system.
     * @return the updated category.
     * @throws DAOException if an error occurred during the action.
     *
     * @author Matteo Battilana
     * @since 1.0.170425
     */
    @Override
    public Category update(Category category) throws DAOException {
        if (category == null) {
            throw new DAOException("parameter not valid", new IllegalArgumentException("The passed category is null"));
        }

        try (PreparedStatement std = CON.prepareStatement("UPDATE categories SET name = ?, description = ? WHERE id = ?")) {
            std.setString(1, category.getName());
            std.setString(2, category.getDescription());
            std.setInt(3, category.getId());
            if (std.executeUpdate() == 1) {
                return category;
            } else {
                throw new DAOException("Impossible to update the category");
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the category", ex);
        }
    }

    /**
     * Persists the new {@link Category category} passed as parameter to the
     * storage system.
     *
     * @param category the new {@code category} to persist.
     * @return the id of the new persisted record.
     * @throws DAOException if an error occurred during the persist action.
     *
     * @author Stefano Chirico
     * @since 1.0.170425
     */
    @Override
    public Long insert(Category category) throws DAOException {
        try (PreparedStatement ps = CON.prepareStatement("INSERT INTO categories(name, description) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

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
                    throw new DAOException("Impossible to persist the new category");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    //TODO: log the exception
                }
                throw new DAOException("Impossible to persist the new category");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                //TODO: log the exception
            }
            throw new DAOException("Impossible to persist the new category", ex);
        }
    }
}
