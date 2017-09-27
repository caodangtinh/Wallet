package com.bitcoin.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    public static Connection getConnection() {
        Properties props = new Properties();
        Connection connection = null;
        try {
            InputStream fis = ClassLoader.getSystemResourceAsStream("db.properties");
            props.load(fis);
            // load the Driver Class
            Class.forName(props.getProperty("jdbc.driverClassName"));
            // create the connection now
            connection = DriverManager.getConnection(props.getProperty("jdbc.url"), props.getProperty("jdbc.username"), props.getProperty("jdbc.password"));
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
