package com.revature.controllers;

import com.revature.models.BankEmployee;
import com.revature.models.Customer;
import com.revature.services.EmployeeService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;

public class EmployeeController extends Controller{

    private static final Logger logger = LoggerFactory.getLogger("EmployeeController Logger");
    private EmployeeService employeeService = new EmployeeService();

    private Handler getAllEmployees = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String employeeId = ctx.cookieStore("employee_id");
            List<BankEmployee> list = employeeService.getAllEmployees(employeeId);

            if (list != null) {
                ctx.json(list);
                ctx.status(200);
                logger.info("A full list of the employees was retrieved successfully.");
            }else{
                ctx.status(400);
                logger.error("The employee list could not be retrieved");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler getOneEmployee = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String pathId = ctx.pathParam("id");
            String employeeId = ctx.cookieStore("employee_id");
            Customer c = employeeService.getEmployee(pathId, employeeId);

            if (c != null){
                ctx.json(c);
                ctx.status(200);
                logger.info("The designated employee was retrieved.");
            }else{
                ctx.status(400);
                logger.debug("The designated user could not be retrieved");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/employee/get_employee/{id}", getOneEmployee);
        app.get("/employee/get_all_employees", getAllEmployees);
    }
}
