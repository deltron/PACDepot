package com.pacdepot.database;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Helper methods for relational database access using JDBC.
 */
public class DBUtil {
    // buffer size when reading long strings
    private static final int BUF_SIZE = 250;

    // a map of table names to maximum ID numbers
    private static Map tableToMaxIDMap = new HashMap();

    /**
     * Close a statement and connection.
     */
    public static void close(Statement stmt, Connection con) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception ignored1) {
            }
        }
        if (con != null) {
            try {
                // return the connection to the pool
                putPooledConnection(con);
            } catch (Exception ignored2) {
            }
        }
    }

    /**
     * @return a new Connection to the database.
     */
    public static Connection getConnection(String dbURL)
            throws SQLException {
        // get a connection from the pool
        return getPooledConnection(dbURL);
    }

    /**
     * Close any connections that are still open. The Servlet will
     * call this method from its destroy() method.
     */
    public static void closeAllConnections() {
        // empty the connection pool and close all connections
        emptyPool();
    }


    /**
     * Store a long text field in the database. For example, a message's
     * text will be quite long and cannot be stored using JDBC's
     * setString() method.
     */
    public static void setLongString(PreparedStatement stmt,
            int columnIndex, String data) throws SQLException {
        if (data.length() > 0) {
            stmt.setAsciiStream(columnIndex,
                    new ByteArrayInputStream(data.getBytes()),
                    data.length());
        } else {
            // this 'else' condition was introduced as a bug fix.  It was
            // discovered that the 'setAsciiStream' code shown above
            // caused MS Access to throw a "function sequence error"
            // when the string was zero length.  This code now works.
            stmt.setString(columnIndex, "");
        }
    }


    /**
     * @return a long text field from the database.
     */
    public static String getLongString(ResultSet rs, int columnIndex)
            throws SQLException {
        try {
            InputStream in = rs.getAsciiStream(columnIndex);
            if (in == null) {
                return "";
            }

            byte[] arr = new byte[BUF_SIZE];
            StringBuffer buf = new StringBuffer();
            int numRead = in.read(arr);
            while (numRead != -1) {
                buf.append(new String(arr, 0, numRead));
                numRead = in.read(arr);
            }
            return buf.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new SQLException(ioe.getMessage());
        }
    }

    /**
     * Compute a new unique ID. It is assumed that the specified table
     * has a column named 'id' of type 'long'. It is assumed that
     * that all parts of the program will use this method to compute
     * new IDs.
     * @return the next available unique ID for a table.
     */
    public static synchronized long getNextID(String tableName,
            Connection con) throws SQLException {
        Statement stmt = null;

        try {
            // if a max has already been retreived from this table,
            // compute the next id without hitting the database
            if (tableToMaxIDMap.containsKey(tableName)) {
                Long curMax = (Long) tableToMaxIDMap.get(tableName);
                Long newMax = new Long(curMax.longValue() + 1L);
                tableToMaxIDMap.put(tableName, newMax);
                return newMax.longValue();
            }

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT MAX(id) FROM " + tableName);
            long max = 0;
            if (rs.next()) {
                max = rs.getLong(1);
            }
            max++;
            tableToMaxIDMap.put(tableName, new Long(max));
            return max;
        } finally {
            // just close the statement
            close(stmt, null);
        }
    }

    //////////////////////////////////////////////////////////////////
    // a rudimentary connection pool
    private static Stack availableConnections = new Stack();

    private static synchronized Connection getPooledConnection(String dbURL)
            throws SQLException {
        if (!availableConnections.isEmpty()) {
          //  System.out.println("Reusing a connection");
            return (Connection) availableConnections.pop();
        } else {
          //  System.out.println("Creating a connection");
            return DriverManager.getConnection(dbURL);
        }
    }

    private static synchronized void putPooledConnection(Connection con) {
        availableConnections.push(con);
    }

    private static synchronized void emptyPool() {
        while (!availableConnections.isEmpty()) {
            Connection con = (Connection) availableConnections.pop();
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }

    //////////////////////////////////////////////////////////////////
}
