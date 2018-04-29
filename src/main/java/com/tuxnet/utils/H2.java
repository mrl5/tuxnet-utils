/**
 * Abstract class for interaction with H2 database
 *
 * References:
 * https://www.tutorialspoint.com/h2_database/h2_database_jdbc_connection.htm
 *
 * @author mrl5
 */

package com.tuxnet.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class H2 {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private final String dbUrl;
    private final String user;
    private final String pass;

    private Connection conn = null;
    private Statement stmt = null;

    public H2(String h2DBPath, String userName, String password) {
        dbUrl = h2DBPath;
        user = userName;
        pass = password;
    }

    public void connect(boolean verbose) throws Exception {
        /* STEP 1: Register JDBC driver */
        Class.forName(JDBC_DRIVER);

        /* STEP 2: Open a connection */
        if (verbose)
            System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(dbUrl, user, pass);
    }

    public void disconnect(boolean verbose) {
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException se2) {
        } // nothing we can do
        try {
            if (conn != null) conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        if (verbose)
            System.out.println("Disconnected from database.");
    }

    public List<List<String>> select(String sqlStatement, List<String> columns, boolean verbose) throws Exception {
        List<List<String>> result = new ArrayList<>();
        List<String> buffer = new ArrayList<>();

        /* STEP 3: Execute a query */
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sqlStatement);
        if (verbose)
            System.out.println(sqlStatement);

        /* STEP 4: Extract data from result set */
        while (rs.next()) {
            /* Retrieve by column name */
            for (String column : columns) {
                buffer.add(rs.getString(column));
            }
            result.add(new ArrayList<>(buffer));
            buffer.clear();
        }
        /* STEP 5: Clean-up environment */
        rs.close();
        return result;
    }

    //todo insert method
}
