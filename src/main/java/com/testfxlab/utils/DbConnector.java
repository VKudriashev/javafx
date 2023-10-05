package com.testfxlab.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnector {

    private static String url, user, password;

    static {

        try {
            Properties properties = new Properties();
            properties.load(DbConnector.class.getResourceAsStream("/properties/db.properties"));
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
        } catch (IOException e) {
            System.out.println("Failed to read db.properties " + e);
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Failed to get connection " + e);
            throw e;
        }
    }
}
