package com.revature.services;

import com.revature.dao.EmployeeDAO;
import com.revature.models.BankEmployee;
import com.revature.models.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger("EmployeeService Logger");
    EmployeeDAO employeeDAO = new EmployeeDAO();

    public Customer getEmployee(String id, String employeeId) {
        if (!employeeId.equals("")) {
            logger.info("The current user has permission to update/access the information");
            return employeeDAO.getEmployee(id);
        }else{
            logger.debug("The current user does not have permission to read this data.");
            return null;
        }
    }

    public List<BankEmployee> getAllEmployees(String employeeId) {
        if (!employeeId.equals("")) {
            logger.info("The current user has permission to update/access the information");
            return employeeDAO.getAllEmployees();
        }else{
            logger.debug("The current user does not have permission to read this data.");
            return null;
        }
    }

    public String addNewEmployee(String customerId, boolean isAdmin) {
        String employeeId = createEmployeeId();
        employeeDAO.addEmployee(employeeId, customerId, isAdmin);

        return employeeId;
    }

    private String createEmployeeId(){
        String id;
        Set<String> employeeIds = new EmployeeDAO().getEmployeeIds();
        Random r = new Random();

        do {
            id = "";

            while (id.length() < 12) {
                //eliminates leading and trailing zeros
                if (id.length() == 0){
                    id += (r.nextInt(9) + 1);
                }else {
                    id += r.nextInt(10);
                }
            }
        }while (employeeIds.contains(id));

        return id;
    }

    public boolean hasProperPermissions(String employeeId) {
        if (employeeId.equals("")){
            return false;
        }else{
            return employeeDAO.getEmployeeLevel(employeeId);
        }
    }
}
