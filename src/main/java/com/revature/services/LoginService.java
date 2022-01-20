package com.revature.services;

import com.revature.dao.LoginDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger("LoginService Logger");
    private LoginDAO loginDAO = new LoginDAO();

    public boolean updatePassword(String customerId, String customerId2, String employeeId, Map<String, List<String>> elements) {
        if (customerId.equals(customerId2) || new EmployeeService().hasProperPermissions(employeeId)) {
            logger.info("The current user has permission to update/access the information");
            String oldEncodedPassword = encodePassword(elements.get("old_password").get(0));
            String newEncodedPassword = encodePassword(elements.get("new_password").get(0));
            return loginDAO.updatePassword(elements.get("username").get(0), oldEncodedPassword, newEncodedPassword);
        }else{
            logger.debug("The current user does not have permission to perform this update.");
            return false;
        }
    }

    private String encodePassword(String password){
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedPassword = encoder.encodeToString(password.getBytes());

        return encodedPassword;
    }

    private boolean hasUniqueUsername(String username) {
        Set<String> registeredUsernames = loginDAO.getUsernames();
        if (registeredUsernames == null){
            return true;
        }
        if (registeredUsernames.contains(username)) {
            return false;
        }else{
            return true;
        }
    }

    public String[] setUser(Map<String, List<String>> elements) {
        String username = elements.get("username").get((0));
        String password = elements.get("password").get((0));
        String encodedPassword = encodePassword(password);
        String[] ids = loginDAO.getUserIds(username, encodedPassword);
        return ids;
    }

    public boolean addEmployeeLogin(Map<String, List<String>> elements) {
        String username = elements.get("username").get(0);
        if (hasUniqueUsername(username)) {
            String firstName = elements.get("first_name").get(0);
            String lastName = elements.get("last_name").get(0);
            String address = elements.get("address").get(0);
            String city = elements.get("city").get(0);
            String state = elements.get("state").get(0);
            String customerId = new CustomerService().addNewCustomer(firstName, lastName, address, city, state);

            if (customerId != null){
                boolean isAdmin = Boolean.parseBoolean(elements.get("isAdmin").get(0));
                String employeeId = new EmployeeService().addNewEmployee(customerId, isAdmin);
                if (employeeId != null){
                    String password = elements.get("password").get(0);
                    String encodedPassword = encodePassword(password);
                    return loginDAO.addEmployeeLogin(customerId, employeeId, username, encodedPassword);
                }
            }
        }

        return false;
    }

    public boolean addCustomerLogin(Map<String, List<String>> elements) {
        String username = elements.get("username").get(0);
        if (hasUniqueUsername(username)) {
            String firstName = elements.get("first_name").get(0);
            String lastName = elements.get("last_name").get(0);
            String address = elements.get("address").get(0);
            String city = elements.get("city").get(0);
            String state = elements.get("state").get(0);
            String customerId = new CustomerService().addNewCustomer(firstName, lastName, address, city, state);

            if (customerId != null){
                String password = elements.get("password").get(0);
                String encodedPassword = encodePassword(password);
                return loginDAO.addCustomerLogin(customerId, username, encodedPassword);
            }
        }

        return false;
    }
}
