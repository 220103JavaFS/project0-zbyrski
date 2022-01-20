package com.revature.dao;

import com.revature.utils.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class LoginDAO {

    private static final Logger logger = LoggerFactory.getLogger("LoginDAO Logger");

    public boolean addCustomerLogin(String customerId, String username, String password) {
        String sqlStatement = "INSERT INTO login (username, user_password, customer_id) ";
            sqlStatement += "VALUES (\'" + username + "\', \'" + password + "\', \'" + customerId + "\');";

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            statement.execute(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return false;
    }

    public boolean addEmployeeLogin(String customerId, String employeeId, String username, String password) {
        String sqlStatement = "INSERT INTO login (username, user_password, customer_id, employee_id) ";
            sqlStatement += "VALUES (\'" + username + "\', \'" + password + "\', \'" + customerId + "\', \'"
                    + employeeId + "\');";

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            statement.execute(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return false;
    }

    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String sqlStatement = "UPDATE login SET user_password=\'" + newPassword + "\' WHERE username=\'" + username + "\'" +
                    " AND user_password=\'" + oldPassword + "\';";
            Statement statement = conn.createStatement();
            statement.execute(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return false;
    }

    public Set<String> getUsernames() {
        try (Connection conn = ConnectionUtil.getConnection()){
            Set<String> usernames = new HashSet<>();
            String sqlStatement = "SELECT username FROM login;";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            while (rs.next()){
                usernames.add(rs.getString("username"));
            }

            return usernames;
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return null;
    }

    //Used during login process
    public String[] getUserIds(String username, String encodedPassword) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String[] ids = new String[2];
            String sqlStatement = "SELECT customer_id, employee_id FROM login WHERE username = \'" + username + "\' " +
                    " AND user_password = \'" + encodedPassword + "\';";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            if (rs.next()){
                ids[0] = rs.getString("customer_id");
                ids[1] = rs.getString("employee_id");
                return ids;
            }else{
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return null;
    }
}
