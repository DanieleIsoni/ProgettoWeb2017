/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.buyhub.examplemysql;

import com.mysql.cj.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author matteo
 */
public class TestClass {

    static Connection crunchifyConn = null;
    static PreparedStatement crunchifyPrepareStat = null;

    public static void main(String[] argv) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
            e.printStackTrace();
            return;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            crunchifyConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/buyhub?serverTimezone=UTC", "root", "");
            if (crunchifyConn != null) {
                System.out.println("Connection Successful! Enjoy. Now it's time to push data");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
            return;
        }

        

        try {
            DatabaseMetaData md = crunchifyConn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                System.out.printf("table name: %s%n", rs.getString("TABLE_NAME"));
            }
            
            System.out.println("\n\nCONNESSIONE AL DB RIUSCITA\n\n");
            
        } catch (SQLException e) {
            System.out.println("MySQL Query Failed!");
            e.printStackTrace();
        }
    }
}
