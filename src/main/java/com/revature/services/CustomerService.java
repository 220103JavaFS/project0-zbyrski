package com.revature.services;

import com.revature.dao.CustomerDAO;
import com.revature.models.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger("CustomerService Logger");
    private CustomerDAO customerDAO = new CustomerDAO();

    public String addNewCustomer(String firstName, String lastName, String address, String city, String state) {
        String customerId = createCustomerId();
        customerDAO.addNewCustomer(customerId, firstName, lastName, address, city, state);
        return customerId;
    }

    public Customer getCustomer(String pathId, String customerId, String employeeId) {
        if (pathId.equals(customerId) || !employeeId.equals("")) {
            logger.info("The current user has permission to read this data");
            return customerDAO.getCustomer(pathId);
        }else{
            logger.debug("The current user does not have permission to read this data.");
            return null;
        }
    }

    public List<Customer> getAllCustomers(String employeeId) {
        return customerDAO.getAllCustomers();
    }

    String createCustomerId(){
        String id = "";
        Set<String> customerIds = new CustomerDAO().getCustomerIds();
        Random r = new Random();

        do {
            while (id.length() < 10) {
                if (id.length() == 0){
                    id += (r.nextInt(9) + 1);
                }else {
                    id += r.nextInt(10);
                }
            }
        }while (customerIds.contains(id));

        return id;
    }

    public boolean updateCustomer(String pathId, String customerId, String employeeId, Map<String, List<String>> elements) {
        if (customerDAO.exists(pathId) && (pathId.equals(customerId) || (new EmployeeService().hasProperPermissions(employeeId)))){
            logger.info("The current user has permission to update the information");
            return customerDAO.updateCustomer(pathId, elements);
        }else{
            logger.debug("The current user does not have permission to perform this update.");
            return false;
        }
    }
}
