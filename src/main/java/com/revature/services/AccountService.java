package com.revature.services;

import com.revature.dao.AccountDAO;
import com.revature.models.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger("AccountService Logger");
    private AccountDAO accountDAO = new AccountDAO();

    public boolean createNewAccount(String customerId, String employeeId) {
        if (new EmployeeService().hasProperPermissions(employeeId)) {
            logger.info("The current user has permission to update/access the information");
            String accountNumber = createAccountNumber();
            if (accountDAO.createNewAccount(accountNumber)){
                return accountDAO.connectUserToAccount(customerId, accountNumber);
            }else{
                return false;
            }
        }else{
            logger.debug("The current user does not have permission to create a new bank account");
            return false;
        }
    }

    public BankAccount getAccount(String pathAccount, String customerId, String currentUser, String employeeId) {
        if (customerId.equals(currentUser) || !employeeId.equals("")) {
            logger.info("The current user has permission to read the information");
            return accountDAO.getAccount(pathAccount);
        }else{
            return null;
        }
    }

    public List<BankAccount> getAllAccounts(String employeeId) {
        if (!employeeId.equals("")) {
            logger.info("The current user has permission to update/access the information");
            return accountDAO.getAllAccounts();
        }else{
            logger.debug("The current user does not have permission to read this data.");
            return null;
        }
    }

    public List<BankAccount> getUserAccounts(String customerId, String currentUser, String employeeId) {
        if (customerId.equals(currentUser) || !employeeId.equals("")){
            logger.info("The current user has permission to update/access the information");
            return accountDAO.getUserAccounts(customerId);
        }else {
            logger.debug("The current user does not have permission to read this data.");
            return null;
        }
    }

    String createAccountNumber(){
        String id = "";
        Set<String> accountNumbers = accountDAO.getAccountNumbers();
        Random r = new Random();

        do {
            while (id.length() < 17) {
                if (id.length() == 0){
                    id += (r.nextInt(9) + 1);
                }else {
                    id += r.nextInt(10);
                }
            }
        }while (accountNumbers.contains(id));

        return id;
    }

    public boolean withdraw(String account, String customerId, String currentUser, String employeeId, double amount) {
        if (customerId.equals(currentUser) || new EmployeeService().hasProperPermissions(employeeId)){
            logger.info("The current user has permission to perform this function");
            return accountDAO.withdraw(account, amount);
        }else{
            logger.debug("The current user does not have permission to perform this function.");
            return false;
        }
    }

    public boolean deposit(String account, String customerId, String currentUser, String employeeId, double amount) {
        if (customerId.equals(currentUser) || new EmployeeService().hasProperPermissions(employeeId)){
            logger.info("The current user has permission to perform this function");
            return accountDAO.deposit(account, amount);
        }else{
            logger.debug("The current user does not have permission to perform this function.");
            return false;
        }
    }

    public boolean transfer(String account1, String account2, String customerId, String currentUser, String employeeId, double amount) {
        if (customerId.equals(currentUser) || new EmployeeService().hasProperPermissions(employeeId)){
            logger.info("The current user has permission to perform this function");
            return accountDAO.transfer(account1, account2, amount);
        }else{
            logger.debug("The current user does not have permission to perform this function.");
            return false;
        }
    }
}
