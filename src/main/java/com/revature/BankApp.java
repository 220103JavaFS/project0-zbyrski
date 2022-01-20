package com.revature;

import com.revature.controllers.*;
import io.javalin.Javalin;

public class BankApp {

    private static Javalin app;

    public static void main(String[] args){
        app = Javalin.create();
        configure(new LoginController(), new CustomerController(), new EmployeeController(), new AccountController());
        app.start();
    }

    private static void configure(Controller... controllers) {
        for (Controller c : controllers) {
            c.addRoutes(app);
        }
    }
}
