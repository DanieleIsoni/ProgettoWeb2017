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
import it.unitn.buyhub.utils.Log;
import it.unitn.buyhub.utils.Pair;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author massimo
 */
public class JDBCOrderedProductDAO extends JDBCDAO<OrderedProduct,  Pair<Integer,Integer>> implements OrderedProductDAO{

    
    public JDBCOrderedProductDAO(Connection con) {
        super(con);
      //  FRIEND_DAOS.put(Product.class, new JDBCProductDAO(CON));
    }

    @Override
    public Long getCount() throws DAOException {
         try (PreparedStatement stmt = CON.prepareStatement("SELECT COUNT(*) FROM orders_products");) {
            ResultSet counter = stmt.executeQuery();
            if (counter.next()) {
                return counter.getLong(1);
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count coordinates", ex);
        }

        return 0L; }

    @Override
    public OrderedProduct getByPrimaryKey( Pair<Integer,Integer> primaryKey) throws DAOException {
            if (primaryKey.Left == null || primaryKey.Right == null) {
            throw new DAOException("primaryKey is null");
        }
        try (PreparedStatement stm = CON.prepareStatement("SELECT * FROM orders_products WHERE order_id = ? AND product_id=?")) {
            stm.setInt(1, primaryKey.Left);
            stm.setInt(2, primaryKey.Right);
            try (ResultSet rs = stm.executeQuery()) {

                rs.next();
                ProductDAO productDao=getDAO(ProductDAO.class);
                Product p=productDao.getByPrimaryKey(rs.getInt("product_id"));
                OrderDAO orderDAO=getDAO(OrderDAO.class);
                Order o=orderDAO.getByPrimaryKey(rs.getInt("order_id"));
                OrderedProduct orderedproduct = new OrderedProduct(p,rs.getInt("quantity"),o);
                    
                orderedproduct.setPrice(rs.getDouble("price"));
                
                return orderedproduct;
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the product for the passed primary key", ex);
        }
       
    }

    @Override
    public List<OrderedProduct> getAll() throws DAOException {
         List<OrderedProduct> products = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT *  FROM orders_products op ORDER BY id ")) {
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    
                    ProductDAO productDao=getDAO(ProductDAO.class);
                    Product p=productDao.getByPrimaryKey(rs.getInt("product_id"));
                    OrderDAO orderDAO=getDAO(OrderDAO.class);
                    Order o=orderDAO.getByPrimaryKey(rs.getInt("order_id"));
                    OrderedProduct product = new OrderedProduct(p,rs.getInt("quantity"),o);
                    product.setPrice(rs.getDouble("price"));
                    products.add(product);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the products for the passed primary key", ex);
        }
        return products;  
    }

    @Override
    public OrderedProduct update(OrderedProduct product) throws DAOException {
    if (product == null) {
               throw new DAOException("parameter not valid", new IllegalArgumentException("The passed product is null"));
           }

           try (PreparedStatement std = CON.prepareStatement("UPDATE orders_product SET quantity=?, price = ?  WHERE order_id = ? AND product_id=?")) {
               
               std.setInt(1,product.getQuantity());
               std.setDouble(2, product.getPrice());
               std.setInt(3, product.getOrder().getId());
               std.setInt(4, product.getId());
               

               if (std.executeUpdate() == 1) {
                   return product;
               } else {
                   throw new DAOException("Impossible to update the product ");
               }
           } catch (SQLException ex) {
               throw new DAOException("Impossible to update the product ", ex);
           }    
    }

    @Override
    public Long insert(OrderedProduct products) throws DAOException {
         try (PreparedStatement ps = CON.prepareStatement("INSERT INTO orders_products(order_id,product_id,price,quantity) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, products.getOrder().getId());
            ps.setInt(2, products.getId());            
            ps.setDouble(3, products.getPrice());
            ps.setInt(4, products.getQuantity());
            // System.out.println(ps.toString());
            
            if (ps.executeUpdate() == 1) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    try {
                        CON.rollback();
                    } catch (SQLException ex) {
                        //TODO: log the exception
                        Log.error("Error placing order: "+ex);

                    }
                    throw new DAOException("Impossible to persist the new product - (1)");
                }
            } else {
                try {
                    CON.rollback();
                } catch (SQLException ex) {
                    //TODO: log the exception
                    Log.error("Error placing order: "+ex);
                    
                    

                }
                throw new DAOException("Impossible to persist the new product (2)");
            }
        } catch (SQLException ex) {
            try {
                CON.rollback();
            } catch (SQLException ex1) {
                
                Log.error("Error placing order: "+ex);

                //TODO: log the exception
            }
            
            throw new DAOException("Impossible to persist the new product (3)", ex);
        }
        
    }

    @Override
    public List<OrderedProduct> getByOrder(int orderId) throws DAOException {
           List<OrderedProduct> products = new ArrayList<>();
        try (PreparedStatement stm = CON.prepareStatement("SELECT *  FROM orders_products op WHERE order_id=? ORDER BY id ")) {
            stm.setInt(1,orderId);
            try (ResultSet rs = stm.executeQuery()) {

                while (rs.next()) {
                    
                    ProductDAO productDao=getDAO(ProductDAO.class);
                    Product p=productDao.getByPrimaryKey(rs.getInt("product_id"));
                    OrderDAO orderDAO=getDAO(OrderDAO.class);
                    Order o=orderDAO.getByPrimaryKey(rs.getInt("order_id"));
                    OrderedProduct product = new OrderedProduct(p,rs.getInt("quantity"),o);
                    
                    product.setPrice(rs.getDouble("price"));
                    products.add(product);
                }

            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the products for the passed primary key", ex);
        }
        return products;  
    
    }


}
