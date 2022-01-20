package com.revature.controllers;

import com.revature.models.Customer;
import com.revature.services.CustomerService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class CustomerController extends Controller{

    private static final Logger logger = LoggerFactory.getLogger("CustomerController Logger");
    private CustomerService customerService = new CustomerService();

    private Handler getAllCustomers = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String employeeId = ctx.cookieStore("employee_id");
            List<Customer> list = customerService.getAllCustomers(employeeId);
            if (list != null) {
                ctx.json(list);
                ctx.status(200);
                logger.info("A full list of customers was retrieved");
            }else{
                ctx.status(400);
                logger.error("The customer list could not be retrieved.");
            }
        }else{
            ctx.status(400);
            logger.debug("There isn't a session in progress.");
        }
    };

    private Handler getCustomerInfo = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String pathId = ctx.pathParam("id");
            String customerId = ctx.cookieStore("customer_id");
            String employeeId = ctx.cookieStore("employee_id");
            Customer c = customerService.getCustomer(pathId, customerId, employeeId);
            if (c != null) {
                ctx.json(c);
                ctx.status(200);
                logger.info("The designated customer was retrieved.");
            }else{
                ctx.status(400);
                logger.error("The designated customer could not be retrieved.");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler updateCustomer = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String pathId = ctx.pathParam("id");
            String customerId = ctx.cookieStore("customer_id");
            String employeeId = ctx.cookieStore("employee_id");
            Map<String, List<String>> elements = ctx.formParamMap();
            if (customerService.updateCustomer(pathId, customerId, employeeId, elements)) {
                ctx.status(200);
                logger.info("The info for the designated customer was updated.");
            }else{
                logger.error("The info for the designated customer was not updated.");
                ctx.status(400);
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/customers/get/{id}", getCustomerInfo);
        app.get("/customers/get_all", getAllCustomers);
        app.post("/customers/update/{id}", updateCustomer);
    }
}
