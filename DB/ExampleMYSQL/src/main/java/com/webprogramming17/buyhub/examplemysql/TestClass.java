/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webprogramming17.buyhub.examplemysql;

import com.mysql.cj.jdbc.PreparedStatement;
import java.sql.Connection;
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
            crunchifyConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/buyhub", "root", "root");
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
        
        System.out.println("\n\nCONNESSIONE AL DB RIUSCITA\n\n");

        /*
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = crunchifyConn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM test_table");

            // or alternatively, if you don't know ahead of time that
            // the query will be a SELECT...
           System.out.println("\n\nCONNESSIONE AL DB RIUSCITA\n\n");

            // Now do something with the ResultSet ....
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        */
    }
}
