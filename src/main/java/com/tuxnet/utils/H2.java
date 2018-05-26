package com.tuxnet.utils;

import java.io.File;
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
 * <br>1) <a href="https://www.tutorialspoint.com/h2_database/h2_database_jdbc_connection.htm">
 * https://www.tutorialspoint.com/h2_database/h2_database_jdbc_connection.htm</a>
 * <br>2) <a href="http://www.h2database.com/html/tutorial.html#creating_new_databases">
 * http://www.h2database.com/html/tutorial.html#creating_new_databases</a>
 *
 * @author mrl5
 * @version 2.0
 */

public class H2 {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private String dbUri;
    private final String user;
    private final String pass;

    private Connection conn = null;
    private Statement stmt = null;

    /**
     * H2 constructor.
     *
     * @param h2DBPath path to H2 database
     * @param username valid username to given H2 database
     * @param password username's password
     */
    public H2(String h2DBPath, String username, String password) {
        try {
            String jdbcHeader = "jdbc:h2:";
            if (!h2DBPath.startsWith(jdbcHeader))
                dbUri = jdbcHeader + h2DBPath;
            else
                dbUri = h2DBPath;
        } catch (NullPointerException e) {
            System.err.println("WARNING: \"h2DBPath\" argument has illegal \"null\" value.");
        }
        user = username;
        pass = password;
    }

    /**
     * By default, if the database specified in the URL does not yet exist, a new (empty) database is created automatically.
     *
     * @param verbose if true - gives output to stdout
     * @return true: if database exists <br>false: if database doesn't exist
     */
    private boolean checkIfExists(boolean verbose) {
        try {
            String dbUri = this.dbUri.substring(8) + ".mv.db";
            File db = new File(dbUri);
            if (!db.exists())
                System.out.printf("Database doesn't exist: %s\n", dbUri);
            return db.exists();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Connects to the database. By default, if the database specified in the URL does not yet exist, a new (empty) database is created automatically.
     *
     * @param checkIfExists if true: before connecting to database method which checks existence of database will be called
     * @param verbose       if true - gives output to stdout
     * @throws Exception ClassNotFoundException: when driver declared in JDBC_DRIVER doesn't exist
     *                   <br>SQLException: when connection to H2 database failed
     */
    public void connect(boolean checkIfExists, boolean verbose) throws Exception {
        if (!checkIfExists)
            connect(verbose);
        else {
            if (checkIfExists(verbose))
                connect(verbose);
            else
                System.err.println("Database doesn't exist. Aborting.");
        }
    }

    /**
     * Connects to the database.
     *
     * @param verbose if true - gives output to stdout
     * @throws Exception ClassNotFoundException: when driver declared in JDBC_DRIVER doesn't exist
     *                   <br>SQLException: when connection to H2 database failed
     */
    private void connect(boolean verbose) throws Exception {
        /* STEP 1: Register JDBC driver */
        Class.forName(JDBC_DRIVER);

        /* STEP 2: Open a connection */
        if (verbose) System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(dbUri, user, pass);
    }

    /**
     * Disconnects from H2 database
     *
     * @param verbose if true - gives output to stdout
     */
    public void disconnect(boolean verbose) {
        try {
            try {
                stmt.close();
            } catch (NullPointerException e) {
            }
        } catch (SQLException se2) {
        } // nothing we can do
        try {
            try {
                conn.close();
                if (verbose) System.out.println("Disconnected from database.");
            } catch (NullPointerException e) {
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }

    /**
     * Run <i>SELECT</i> SQL statement
     *
     * @param sqlStatement SQL statement which will be sent to the database
     * @param columns      retrieved columns from SQL query response
     * @param verbose      if true - gives sqlStatement to stdout
     * @return the SQL query response
     * @throws Exception SQLException if an SQL error occurs
     */

    public List<List<String>> select(String sqlStatement, List<String> columns, boolean verbose) throws Exception {
        List<List<String>> result = new ArrayList<>();
        List<String> buffer = new ArrayList<>();

        try {
            /* STEP 3: Execute a query */
            stmt = conn.createStatement();
            if (verbose) System.out.println(sqlStatement);
            ResultSet rs = stmt.executeQuery(sqlStatement);

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
        } catch (NullPointerException e) {
        } //nothing to do

        return result;
    }

    /**
     * Runs SQL statement.
     *
     * @param sqlStatement SQL statement which will be sent to the database
     * @param verbose      if true - gives sqlStatement to stdout
     * @throws Exception SQLException if an SQL error occurs
     */
    public void runSQLStatement(String sqlStatement, boolean verbose) throws Exception {
        try {
            /* STEP 3: Execute statement */
            stmt = conn.createStatement();
            if (verbose) System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);
        } catch (NullPointerException e) {
        } //nothing to do
    }

    /**
     * @return H2 database <a href="https://en.wikipedia.org/wiki/Uniform_Resource_Identifier">URI</a>
     */
    public String getDbUri() {
        return dbUri;
    }
}
