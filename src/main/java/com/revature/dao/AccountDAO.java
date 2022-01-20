package com.revature.dao;

import com.revature.models.BankAccount;
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

public class AccountDAO {

    private static final Logger logger = LoggerFactory.getLogger("AccountDAO Logger");

    public static Set<String> getAccountNumbers() {
        String sqlStatement = "SELECT account_number FROM bank_accounts;";
        Set<String> accountIds = new HashSet<>();

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            while (rs.next()){
                accountIds.add(rs.getString("account_number"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        if (accountIds.isEmpty()){
            return null;
        }else {
            return accountIds;
        }
    }

    public BankAccount getAccount(String accountNumber){
        String sqlStatement = "SELECT * FROM user_accounts JOIN bank_accounts ON " +
                "(user_accounts.account_number = bank_accounts.account_number) " +
                "WHERE bank_accounts.account_number = \'" + accountNumber + "\';";

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            if (rs.next()) {
                String ownerNumber = rs.getString("customer_id");
                double balance = rs.getDouble("account_balance");
                BankAccount a = new BankAccount(ownerNumber, accountNumber, balance);
            }else{
                return null;
            }

        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return null;
    }

    public List<BankAccount> getUserAccounts(String customerId){
        String sqlStatement = "SELECT * FROM user_accounts JOIN bank_accounts ON " +
                "(user_accounts.account_number = bank_accounts.account_number) " +
                "WHERE user_accounts.customer_id = \'" + customerId + "\';";

        try (Connection conn = ConnectionUtil.getConnection()){
            List<BankAccount> accountList = new ArrayList<>();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            while (rs.next()) {
                String ownerNumber = rs.getString("customer_id");
                String accountNumber = rs.getString("account_number");
                double balance = rs.getDouble("account_balance");
                BankAccount a = new BankAccount(ownerNumber, accountNumber, balance);
                accountList.add(a);
            }

            if (accountList.isEmpty()){
                return null;
            }else {
                return accountList;
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return null;
    }

    public List<BankAccount> getAllAccounts(){
        String sqlStatement = "SELECT * FROM user_accounts JOIN bank_accounts ON " +
                "(user_accounts.account_number = bank_accounts.account_number);";

        try (Connection conn = ConnectionUtil.getConnection()){
            List<BankAccount> accountList = new ArrayList<>();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            while (rs.next()) {
                String ownerNumber = rs.getString("customer_id");
                String accountNumber = rs.getString("account_number");
                double balance = rs.getDouble("account_balance");
                BankAccount a = new BankAccount(ownerNumber, accountNumber, balance);
                accountList.add(a);
            }

            if (accountList.isEmpty()){
                return null;
            }else {
                return accountList;
            }
        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return null;
    }

    public boolean withdraw(String account, double amount){
        String sqlStatement = "SELECT account_balance FROM bank_accounts WHERE account_number = \'" + account + "\';";

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            if (rs.next()) {
                double balance = rs.getDouble("account_balance");
                if (balance >= amount){
                    balance -= amount;
                    String sqlStatement2 = "UPDATE bank_accounts SET account_balance = " + balance + " WHERE account_number = '" + account + "';";
                    statement.execute(sqlStatement2);
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }

        }catch (SQLException e){
            e.printStackTrace();
            logger.error("The connection to the database failed.");
        }

        return false;
    }

    public boolean deposit(String account, double amount){
        String sqlStatement = "SELECT * FROM bank_accounts WHERE account_number = \'" + account + "\';";

        try (Connection conn = ConnectionUtil.getConnection()){
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sqlStatement);
            logger.info("The connection was established and the query was run against the database");
            if (rs.next()) {
                double balance = rs.getDouble("account_balance");
                balance += amount;
                String sqlStatement2 = "UPDATE bank_accounts SET account_balance = " + balance + " WHERE account_number = '" + account + "';";
                statement.execute(sqlStatement2);
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

    public boolean transfer(String account1, String account2, double amount){
        String sqlStatement = "SELECT \"transfer\"('" + account1 + "', '" + account2 + "', " + amount + ");";

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

    public boolean createNewAccount(String accountNumber) {
        String sqlStatement = "INSERT INTO bank_accounts (account_number, account_balance) "
                + "VALUES (\'" + accountNumber + "\', 0.00);";

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

    public boolean connectUserToAccount(String customerId, String accountNumber){
        String sqlStatement = "INSERT INTO user_accounts (customer_id, account_number) " +
                "VALUES (\'" + customerId + "\', \'" + accountNumber + "\');";

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
}
