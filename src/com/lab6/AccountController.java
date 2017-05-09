package com.lab6;

import com.lab6.domain.Account;
import com.lab6.service.AccountService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
}
