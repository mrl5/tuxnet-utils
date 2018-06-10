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
 * @version 3.0
 */

public class H2 {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String JDBC_HEADER = "jdbc:h2:";
    private static final String SUFFIX = ".mv.db";
    private String h2DBPath;
    private String username;
    private String password;
    private String dbUri;
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
        this.h2DBPath = h2DBPath;
        try {
            dbUriFormatter();
        } catch (NullPointerException e) {
            System.err.println("WARNING: \"h2DBPath\" argument has illegal \"null\" value.");
        }
        this.username = username;
        this.password = password;
    }

    /**
     * Formats H2 database URI
     */
    private void dbUriFormatter() {
        dbUri = h2DBPath;
        if (!h2DBPath.startsWith(JDBC_HEADER))
            dbUri = JDBC_HEADER + h2DBPath;
        if (dbUri.endsWith(SUFFIX))
            dbUri = dbUri.replace(SUFFIX, "");
    }

    /**
     * By default, if the database specified in the URL does not yet exist, a new (empty) database is created automatically.
     *
     * @return true: if database exists <br>false: if database doesn't exist
     */
    private boolean checkIfExists() {
        try {
            File db = new File(h2DBPath);
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
     * @return true: if connection succeed <br>false: if connection failed
     */
    public boolean connect(boolean checkIfExists, boolean verbose) {
        boolean status;
        if (!checkIfExists)
            status = connect(verbose);
        else {
            if (checkIfExists())
                status = connect(verbose);
            else {
                System.err.println("Database doesn't exist. Aborting.");
                status = false;
            }
        }
        return status;
    }

    /**
     * Connects to the database.
     *
     * @param verbose if true - gives output to stdout
     * @return true: if connection succeed <br>false: if connection failed
     */
    private boolean connect(boolean verbose) {
        try {
            /* STEP 1: Register JDBC driver */
            Class.forName(JDBC_DRIVER);

            /* STEP 2: Open a connection */
            if (verbose) System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(dbUri, username, password);
            return true;
        } catch (NullPointerException e) {
            System.err.println("NullPointerException");
            return false;
        } catch (SQLException e) {
            System.err.println("SQLException:");
            System.err.println(e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Disconnects from H2 database
     *
     * @param verbose if true - gives output to stdout
     */
    public void disconnect(boolean verbose) {
        try {
            stmt.close();
        } catch (NullPointerException e) {
            System.err.println("NullPointerException");
        } catch (SQLException se2) {
            System.err.println("SQLException");
        }
        try {
            conn.close();
            if (verbose) System.out.println("Disconnected from database.");
        } catch (NullPointerException e) {
            System.err.println("NullPointerException");
        } catch (SQLException e) {
            System.err.println("SQLException:");
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run <i>SELECT</i> SQL statement
     *
     * @param sqlStatement SQL statement which will be sent to the database
     * @param columns      retrieved columns from SQL query response
     * @param verbose      if true - gives sqlStatement to stdout
     * @return the SQL query response
     */

    public List<List<String>> select(String sqlStatement, List<String> columns, boolean verbose) {
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
            System.err.println("NullPointerException");
        } catch (SQLException e) {
            System.err.println("SQLException:");
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Runs SQL statement with SQLException handling.
     *
     * @param sqlStatement SQL statement which will be sent to the database
     * @param verbose      if true - gives sqlStatement to stdout
     */
    public void runSQLStatement(String sqlStatement, boolean verbose) {
        try {
            /* STEP 3: Execute statement */
            stmt = conn.createStatement();
            if (verbose) System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);
        } catch (NullPointerException e) {
            System.err.println("NullPointerException");
        } catch (SQLException e) {
            System.err.println("SQLException:");
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs SQL statement w/o SQLException handling.
     *
     * @param sqlStatement SQL statement which will be sent to the database
     * @param verbose      if true - gives sqlStatement to stdout
     * @throws Exception SQLException and Exception (SQLException leftovers)
     */

    public void runSQLStatement2(String sqlStatement, boolean verbose) throws Exception {
        try {
            /* STEP 3: Execute statement */
            stmt = conn.createStatement();
            if (verbose) System.out.println(sqlStatement);
            stmt.executeUpdate(sqlStatement);
        } catch (NullPointerException e) {
            System.err.println("NullPointerException");
        }
    }

    /**
     * @return H2 database <a href="https://en.wikipedia.org/wiki/Uniform_Resource_Identifier">URI</a>
     */
    public String getDbUri() {
        return dbUri;
    }

    public String getH2DBPath() {
        return h2DBPath;
    }

    public void setH2DBPath(String h2DBPath) {
        this.h2DBPath = h2DBPath;
        dbUriFormatter();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /* probably not good idea, but is needed at this time */
    //todo: hash?
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
