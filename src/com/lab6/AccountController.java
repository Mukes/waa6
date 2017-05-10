package com.lab6;

import com.lab6.domain.Account;
import com.lab6.service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 985552 on 5/9/2017.
 */
@Path("/account")
public class AccountController {
    private AccountService accountService;
    public AccountController(){
        accountService = new AccountService();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccounts(){
        List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
        if (accounts.size()>0){
            return Response.ok(accounts, MediaType.APPLICATION_JSON).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{accountNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccounts(@PathParam("accountNo") long accountNo){
        Account account = accountService.getAccount(accountNo);
        if (account==null){
            return Response.ok(account, MediaType.APPLICATION_JSON).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Account account) {
        account = accountService.createAccount(account.getAccountnumber(), account.getCustomer().getName());
        if (account!=null){
            return Response.status(201).entity(account).build();
        }
        return Response.status(400).entity("Error in creating account").build();
    }

    /**
     * Passing values for Deposit and withdraw has security issue
     * But Were used just to demonstrate use of query param in Jax RS
     * @param accountNo
     * @param deposit
     * @param withdraw
     * @return
     */
    @PUT
    @Path("/{accountNo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("accountNo") long accountNo, @QueryParam("deposit") double deposit, @QueryParam("withdraw") double withdraw ) {
        if (deposit>0){
            accountService.deposit(accountNo, deposit);
            return Response.status(200).entity(deposit + "$ sucessfully Deposited").build();
        }
        if (withdraw>0){
            accountService.deposit(accountNo, withdraw);
            return Response.status(200).entity(withdraw + "$ sucessfully Withdrawn").build();
        }
        return Response.status(400).entity("Invalid Input").build();
    }
}
