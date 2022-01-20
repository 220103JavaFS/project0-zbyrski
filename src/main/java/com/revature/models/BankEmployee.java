package com.revature.models;

import java.util.Properties;

public class BankEmployee extends Customer{

    private String employeeId;
    private EmployeeType type;

    public BankEmployee(){ }

    public BankEmployee(Customer c, String employeeId, EmployeeType type){
        super(c.getCustomerId(), c.getFirstName(), c.getLastName(), c.getAddress(), c.getCity(), c.getState());
        this.employeeId = employeeId;
        this.type = type;
    }

    public EmployeeType getType(){
        return this.type;
    }

    public String getEmployeeId(){
        return this.employeeId;
    }
}
