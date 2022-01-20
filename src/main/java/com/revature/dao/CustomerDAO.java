package com.revature.dao;

import com.revature.models.Customer;
import com.revature.utils.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CustomerDAO {

    private static final Logger logger = LoggerFactory.getLogger("CustomerDAO Logger");

    public static Set<String> getCustomerIds() {
        String sqlStatement = "SELECT customer_id FROM customers;";
        Set<String> customerIds = new HashSet<>();

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            while (rs.next()){
                customerIds.add(rs.getString("customer_id"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return customerIds;
    }

    public List<Customer> getAllCustomers() {
        String sqlStatement = "SELECT * FROM customers;";

        try (Connection conn = ConnectionUtil.getConnection()){
            List<Customer> customerList = new ArrayList<>();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            while (rs.next()) {
                String customerId = rs.getString("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                Customer c = new Customer(customerId, firstName, lastName, address, city, state);
                customerList.add(c);
            }

            return customerList;
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return null;
    }

    public Customer getCustomer(String id) {
        String sqlStatement = "SELECT * FROM customers WHERE customer_id=\'" + id + "\';";

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            if (rs.next()) {
                String customerId = rs.getString("customer_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                Customer c = new Customer(customerId, firstName, lastName, address, city, state);
                return c;
            }else{
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return null;
    }

    public boolean addNewCustomer(String customerId, String firstName, String lastName, String address, String city, String state) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String sqlStatement = "INSERT INTO customers (customer_id, first_name, last_name, address, city, state) ";
            sqlStatement += "VALUES (\'" + customerId + "\', \'" + firstName + "\', \'" + lastName + "\', \'" +
                    address + "\', \'" + city + "\', \'" + state + "\');";
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

    public boolean updateCustomer(String customerId, Map<String, List<String>> elements) {
        try (Connection conn = ConnectionUtil.getConnection()){
            String firstName = elements.get("first_name").get(0);
            String lastName = elements.get("last_name").get(0);
            String address = elements.get("address").get(0);
            String city = elements.get("city").get(0);
            String state = elements.get("state").get(0);
            String sqlStatement = "UPDATE customers SET first_name=\'" + firstName + "\', last_name=\'" + lastName +
                    "\', address=\'" + address + "\', city=\'" + city + "\', state=\'" + state + "\'" +
                    "WHERE customer_id=\'" + customerId + "\';";
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

    public boolean exists(String pathId) {
        String sqlStatement = "SELECT customer_id FROM customers WHERE customer_id = \'" + pathId + "\';";

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            if (rs.next()) {
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }
        return false;
    }
}
