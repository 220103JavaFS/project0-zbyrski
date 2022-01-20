package com.revature.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    public static Connection getConnection() throws SQLException {
        //For many frameworks using JDBC, it is necessary to "register" the driver package you are using
        //This is to make the framework aware of it
        try {
            Class.forName("org.postgresql.Driver");
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String url = "jdbc:postgresql://javafs220103.cvqp92fi7v6o.us-east-1.rds.amazonaws.com:5432/bankapp";
        //It is possible and preferable to hide this information in environment variables
        //These are accessed by calling System.getenv("variable name");
        String username = "postgres";
        String password = "BSpassword#01";
        //String username = System.getenv("SQLUsername");
        //String password = System.getenv("SQLPassword");

        return DriverManager.getConnection(url, username, password);
    }

    /*
    public static void main(String[] args) {
        try {
            getConnection();
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
    */

}
