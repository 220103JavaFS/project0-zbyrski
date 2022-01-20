package com.revature.controllers;

import com.revature.services.LoginService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class LoginController extends Controller{

    private static final Logger logger = LoggerFactory.getLogger("LoginController Logger");
    private LoginService loginService = new LoginService();

    private Handler login = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session == null) {
            Map<String, List<String>> elements = ctx.formParamMap();
            String[] ids = loginService.setUser(elements);

            if (ids != null) {
                logger.info("Login is successful");
                ctx.req.getSession();
                ctx.cookieStore("customer_id", ids[0]);
                logger.info("\"customer_id\" has been stored in this session cookie");
                if (ids[1] != null) {
                    logger.info("\"employee_id\" has been stored in this session cookie");
                    ctx.cookieStore("employee_id", ids[1]);
                } else {
                    logger.info("\"employee_id\" has been stored as an empty string because this user is not an employee");
                    ctx.cookieStore("employee_id", "");
                }

                ctx.status(200);
            } else {
                logger.error("Login has failed");
                ctx.status(400);
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler updatePassword = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null){
            String customerId = ctx.cookieStore("customer_id");
            String customerId2 = ctx.pathParam("id");
            String employeeId = ctx.cookieStore("employee_id");
            Map<String, List<String>> elements = ctx.formParamMap();

            if (loginService.updatePassword(customerId, customerId2, employeeId, elements)){
                logger.info("Password update was successful");
                ctx.status(201);
            }else{
                logger.error("Password update has failed.");
                ctx.status(400);
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler createCustomerLogin = (ctx) -> {
        Map<String, List<String>> elements = ctx.formParamMap();

        if (loginService.addCustomerLogin(elements)){
            logger.info("A new user was successfully created");
            ctx.status(201);
        }else{
            logger.error("A new user could not be created");
            ctx.status(400);
        }
    };

    private Handler createEmployeeLogin = (ctx) -> {
        Map<String, List<String>> elements = ctx.formParamMap();

        if (loginService.addEmployeeLogin(elements)){
            logger.info("A new employee has been created");
            ctx.status(201);
        }else{
            logger.error("A new employee was not created");
            ctx.status(400);
        }
    };

    private Handler logout = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null){
            session.invalidate();
            logger.info("The user has logged out of this session");
            ctx.status(200);
        }else{
            logger.error("The logout function has failed");
            ctx.status(400);
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        //app.get("/login/get_login", getOneLogin);
        //app.get("/login/get_all_logins", getAllLogins);
        app.post("/login", login);
        app.post("/login/update_password/{id}", updatePassword);
        app.post("/login/create_customer", createCustomerLogin);
        app.post("/login/create_employee", createEmployeeLogin);
        app.post("/logout", logout);
    }
}
