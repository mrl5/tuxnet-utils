package com.tuxnet.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for interaction with H2 database.
 * <p>
 * References:
 * <br> 1) <a href="https://www.tutorialspoint.com/h2_database/h2_database_jdbc_connection.htm">
 * https://www.tutorialspoint.com/h2_database/h2_database_jdbc_connection.htm</a>
 *
 * @author mrl5
 */

public class H2 {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private final String dbUrl;
    private final String user;
    private final String pass;

    private Connection conn = null;
    private Statement stmt = null;

    /**
     * H2 constructor.
     *
     * @param h2DBPath path to H2 database
     * @param userName valid username to given H2 database
     * @param password username's password
     */

    public H2(String h2DBPath, String userName, String password) {
        dbUrl = h2DBPath;
        user = userName;
        pass = password;
    }

    /**
     * Connect to H2 database
     *
     * @param verbose if true - gives output to stdout
     * @throws Exception ClassNotFoundException: when driver declared in JDBC_DRIVER doesn't exist
     *                   <br>SQLException: when connection to H2 database failed
     */

    public void connect(boolean verbose) throws Exception {
        /* STEP 1: Register JDBC driver */
        Class.forName(JDBC_DRIVER);

        /* STEP 2: Open a connection */
        if (verbose)
            System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(dbUrl, user, pass);
    }

    /**
     * Disconnect from H2 database
     *
     * @param verbose if true - gives output to stdout
     */

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

    /**
     * Run <i>SELECT</i> SQL statement
     *
     * @param sqlStatement SQL statement which will be sent to the database
     * @param columns      retrieved columns from SQL query response
     * @param verbose      if true - gives sqlStatement to stdout
     * @return the SQL query response
     * @throws Exception SQLException
     */

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
