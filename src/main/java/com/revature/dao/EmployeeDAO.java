package com.revature.dao;

import com.revature.models.BankEmployee;
import com.revature.models.Customer;
import com.revature.models.EmployeeType;
import com.revature.utils.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.revature.models.EmployeeType.ADMIN;
import static com.revature.models.EmployeeType.TELLER;

public class EmployeeDAO {

    private static final Logger logger = LoggerFactory.getLogger("EmployeeDAO Logger");

    public boolean addEmployee(String employeeId, String customerId, boolean isAdmin) {
        String sqlStatement = "INSERT INTO employees (employee_id, customer_id, admin_level) ";
        sqlStatement += "VALUES (" + employeeId + ", " + customerId + ", " + isAdmin + ");";

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

    public Set<String> getEmployeeIds(){
        String sqlStatement = "SELECT employee_id FROM employees;";
        Set<String> employeeIds = new HashSet<>();

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            while (rs.next()){
                employeeIds.add(rs.getString("employee_id"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return employeeIds;
    }

    public boolean getEmployeeLevel(String employeeId) {
        String sqlStatement = "SELECT admin_level FROM employees WHERE employees.employee_Id = \'" + employeeId + "\';";
        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            if (rs.next()) {
                return rs.getBoolean("admin_level");
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }
        return false;
    }

    public Customer getEmployee(String employeeId) {
        String sqlStatement = "SELECT * FROM employees WHERE employees.employee_Id = \'" + employeeId + "\';";
        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            if (rs.next()){
                String customerId = rs.getString("customer_id");
                Customer c = new CustomerDAO().getCustomer(customerId);
                boolean isAdmin = rs.getBoolean("admin_level");
                EmployeeType type;
                if (isAdmin){
                    type = ADMIN;
                }else{
                    type = TELLER;
                }

                BankEmployee e = new BankEmployee(c, employeeId, type);
                return e;
            }else{
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }
        return null;
    }

    public List<BankEmployee> getAllEmployees() {
        String sqlStatement = "SELECT * FROM employees;";
        try (Connection conn = ConnectionUtil.getConnection()){
            List<BankEmployee> employees = new ArrayList<>();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            CustomerDAO customerDAO = new CustomerDAO();
            while (rs.next()){
                String employeeId = rs.getString("employee_id");
                String customerId = rs.getString("customer_id");
                Customer c = customerDAO.getCustomer(customerId);
                boolean isAdmin = rs.getBoolean("admin_level");
                EmployeeType type;
                if (isAdmin){
                    type = ADMIN;
                }else{
                    type = TELLER;
                }

                BankEmployee e = new BankEmployee(c, employeeId, type);
                employees.add(e);
            }

            if (employees.isEmpty()){
                return null;
            }else{
                return employees;
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }
        return null;
    }
}
