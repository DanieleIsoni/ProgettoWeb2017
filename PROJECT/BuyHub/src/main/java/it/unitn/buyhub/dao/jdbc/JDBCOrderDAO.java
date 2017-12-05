/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.dao.jdbc;

import it.unitn.buyhub.dao.OrderDAO;
import it.unitn.buyhub.dao.OrderedProductDAO;
import it.unitn.buyhub.dao.PictureDAO;
import it.unitn.buyhub.dao.ProductDAO;
import it.unitn.buyhub.dao.ShopDAO;
import it.unitn.buyhub.dao.entities.Coordinate;
import it.unitn.buyhub.dao.entities.Order;
import it.unitn.buyhub.dao.entities.OrderedProduct;
import it.unitn.buyhub.dao.entities.Picture;
import it.unitn.buyhub.dao.entities.Product;
import it.unitn.buyhub.dao.persistence.DAO;
import it.unitn.buyhub.dao.persistence.exceptions.DAOException;
import it.unitn.buyhub.dao.persistence.exceptions.DAOFactoryException;
import it.unitn.buyhub.dao.persistence.jdbc.JDBCDAO;
import it.unitn.buyhub.utils.Pair;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author massimo
 */
public class JDBCOrderDAO extends JDBCDAO<Order,  Integer> implements OrderDAO{

    
    public JDBCOrderDAO(Connection con) {
        super(con);
        FRIEND_DAOS.put(OrderedProduct.class, new JDBCOrderedProductDAO(CON));
    }

    @Override
    public Long getCount() throws DAOException {
         try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM orders");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count coordinates", ex);
        }

        return 0L; }

    @Override
    public Order getByPrimaryKey(Integer primaryKey) throws DAOException {
            if (primaryKey == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM orders WHERE order_id = ?")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                Order o=new Order();
               
                o.setId(rs.getInt("order_id"));
                o.setPaid(rs.getBoolean("paid"));
                o.setUser_id(rs.getInt("user_id"));
                o.setShipment(rs.getString("shipment"));
                o.setShipment_cost(rs.getDouble("shipment_cost"));
                     
                OrderedProductDAO orderedproductDao=getDAO(OrderedProductDAO.class);
                List<OrderedProduct> products = orderedproductDao.getByOrder(o.getId());
                for (OrderedProduct product : products) {
                    o.add(product);
                }
                
                return o;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the product for the passed primary key", ex);
        }
       
    }

    @Override
    public List<Order> getAll() throws DAOException {
         List<Order> orders = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT *  FROM orders o ORDER BY id ")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    
                    rs.next();
                    Order o=new Order();
                    o.setId(rs.getInt("order_id"));
                    o.setPaid(rs.getBoolean("paid"));
                    o.setUser_id(rs.getInt("user_id"));
                    o.setShipment(rs.getString("shipment"));
                    o.setShipment_cost(rs.getDouble("shipment_cost"));
                    

                    OrderedProductDAO orderedproductDao=getDAO(OrderedProductDAO.class);
                    List<OrderedProduct> products = orderedproductDao.getByOrder(o.getId());
                    for (OrderedProduct product : products) {
                        o.add(product);
                    }

                        orders.add(o);
                    }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the products for the passed primary key", ex);
        }
        return orders;  
    }

    @Override
    public Order update(Order o) throws DAOException {
    if (o == null) {
               throw new DAOException("parameter not valid", new IllegalArgumentException("The passed order is null"));
           }

           try (PreparedStatement std = CON.prepareStatement("UPDATE orders SET shipment=? shipment_costs=?, paid=? WHERE order_id = ?")) {
               
               std.setString(1,o.getShipment());
               std.setDouble(2,o.getShipment_cost());
               std.setBoolean(3,o.isPaid());
               std.setInt(4,o.getId());

               
               if (std.executeUpdate() == 1) {
                   return o;
               } else {
                   throw new DAOException("Impossible to update the order ");
               }
           } catch (SQLException ex) {
               throw new DAOException("Impossible to update the order ", ex);
           }    
    }

    @Override
    public Long insert(Order order) throws DAOException {
         try (PreparedStatement ps = CON.prepareStatement("INSERT INTO orders_products(user_id,shipment,shipment_costs,paid) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getUser_id());
                ps.setString(2, order.getShipment());
                ps.setDouble(3, order.getShipment_cost());
                ps.setBoolean(4, order.isPaid());
                
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
                    throw new DAOException("Impossible to persist the new product");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    //TODO: log the exception
                }
                throw new DAOException("Impossible to persist the new product");
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

    @Override
    public List<Order> getByUser(Integer primaryKey) throws DAOException {
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT *  FROM orders o WHERE user_id=? ORDER BY id ")) {
            stm.setInt(1, primaryKey);
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    
                    rs.next();
                    Order o=new Order();
                    o.setId(rs.getInt("order_id"));
                    o.setPaid(rs.getBoolean("paid"));
                    o.setUser_id(rs.getInt("user_id"));
                    o.setShipment(rs.getString("shipment"));
                    o.setShipment_cost(rs.getDouble("shipment_cost"));
                    

                    OrderedProductDAO orderedproductDao=getDAO(OrderedProductDAO.class);
                    List<OrderedProduct> products = orderedproductDao.getByOrder(o.getId());
                    for (OrderedProduct product : products) {
                        o.add(product);
                    }

                        orders.add(o);
                    }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the products for the passed primary key", ex);
        }
        return orders;  
    }
}
