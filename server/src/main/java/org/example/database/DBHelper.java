package org.example.database;

import org.example.interfaces.Console;

import java.sql.*;

public class DBHelper {
    private String URL;
    private String USER;
    private String PASSWORD;
    private Connection connection;
    protected Console console;

    public DBHelper(Console console, String host, String databaseName, String userName, String password) {
        this.URL = "jdbc:postgresql://" + host + "/" + databaseName;
        this.USER = userName;
        this.PASSWORD = password;
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {

            console.printError("DBHelper.DBHelper");
            console.printError(e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            console.printError(e);
        }
    }
    public String getDatabaseName() throws SQLException {
        return connection.getCatalog();
    }

    public String getSchemaName() throws SQLException {
        return connection.getSchema();
    }

    public String getURL() {
        return this.URL;
    }
}