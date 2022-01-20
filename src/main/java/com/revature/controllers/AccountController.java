package com.revature.controllers;

import com.revature.models.BankAccount;
import com.revature.services.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;
import java.util.List;

public class AccountController extends Controller{

    private static final Logger logger = LoggerFactory.getLogger("AccountController Logger");
    private AccountService accountService = new AccountService();

    private Handler getAllAccounts = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String employeeId = ctx.cookieStore("employee_id");
            List<BankAccount> list = accountService.getAllAccounts(employeeId);
            if (list != null) {
                ctx.json(list);
                ctx.status(200);
                logger.info("The list of bank accounts was retrieved.");
            }else{
                ctx.status(400);
                logger.error("The list of bank accounts was not retrieved.");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler getUserAccounts = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String employeeId = ctx.cookieStore("employee_id");
            String customerId = ctx.formParam("customer_id");
            if (customerId == null){
                customerId = ctx.cookieStore("employee_id");
            }
            List<BankAccount> list = accountService.getUserAccounts(customerId, ctx.cookieStore("customer_id"), employeeId);
            if (list != null) {
                ctx.json(list);
                ctx.status(200);
                logger.info("The list of bank accounts for the designated customer was retrieved.");
            }else{
                ctx.status(400);
                logger.error("The list of bank accounts for the designated customer was not retrieved.");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler getOneAccount = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String employeeId = ctx.cookieStore("employee_id");
            String customerId = ctx.formParam("customer_id");
            String accountNumber = ctx.formParam("account_number");
            BankAccount account = accountService.getAccount(accountNumber, customerId, ctx.cookieStore("customer_id"), employeeId);
            if (account != null) {
                ctx.json(account);
                ctx.status(200);
                logger.info("The bank account for the designated customer was retrieved.");
            }else{
                ctx.status(400);
                logger.error("The bank account for the designated customer was not retrieved.");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler addAccount = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String employeeId = ctx.cookieStore("employee_id");
            String customerId = ctx.formParam("customer_id");
            if (accountService.createNewAccount(customerId, employeeId)) {
                ctx.status(200);
                logger.info("A new bank account was created for the designated customer");
            }else{
                System.out.println(400);
                logger.error("A new bank account was not created for the designated customer");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler deposit = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String employeeId = ctx.cookieStore("employee_id");
            String customerId = ctx.formParam("customer_id");
            String accountNumber = ctx.formParam("account_number");
            double amount = Double.parseDouble(ctx.formParam("amount"));
            if (accountService.deposit(accountNumber, customerId, ctx.cookieStore("customer_id"), employeeId, amount)) {
                ctx.status(200);
                logger.info("The deposit of funds was successful.");
            }else{
                ctx.status(400);
                logger.error("The deposit of funds failed.");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler withdraw = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String employeeId = ctx.cookieStore("employee_id");
            String customerId = ctx.formParam("customer_id");
            String accountNumber = ctx.formParam("account_number");
            double amount = Double.parseDouble(ctx.formParam("amount"));
            if (accountService.withdraw(accountNumber, customerId, ctx.cookieStore("customer_id"), employeeId, amount)) {
                ctx.status(200);
                logger.info("The withdrawal of funds was successful.");
            }else{
                ctx.status(400);
                logger.error("The withdrawal of funds failed.");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    private Handler transfer = (ctx) -> {
        HttpSession session = ctx.req.getSession(false);
        if (session != null) {
            String employeeId = ctx.cookieStore("employee_id");
            String customerId = ctx.formParam("customer_id");
            String account1 = ctx.formParam("account1");
            String account2 = ctx.formParam("account2");
            double amount = Double.parseDouble(ctx.formParam("amount"));
            if (accountService.transfer(account1, account2, customerId, ctx.cookieStore("customer_id"), employeeId, amount)) {
                ctx.status(200);
                logger.info("The transfer of funds was successful.");
            }else{
                ctx.status(400);
                logger.error("The transfer of funds failed.");
            }
        }else{
            logger.debug("There isn't a session in progress.");
            ctx.status(400);
        }
    };

    @Override
    public void addRoutes(Javalin app) {
        app.get("/accounts/get_one/{bank_id}", getOneAccount);
        app.get("/accounts/get_user_accounts", getUserAccounts);
        app.get("/accounts/get_all", getAllAccounts);
        app.post("/accounts/create", addAccount);
        app.post("/accounts/deposit", deposit);
        app.post("/accounts/withdraw", withdraw);
        app.post("/accounts/transfer", transfer);
    }
}
