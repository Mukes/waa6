package com.lab6.service;


import com.lab6.dao.AccountDAO;
import com.lab6.dao.IAccountDAO;
import com.lab6.domain.Account;
import com.lab6.domain.Customer;
import com.lab6.jms.IJMSSender;
import com.lab6.jms.JMSSender;
import com.lab6.logging.ILogger;
import com.lab6.logging.Logger;

import java.util.Collection;

public class AccountService implements IAccountService {
	private IAccountDAO accountDAO;
	private ICurrencyConverter currencyConverter;
	private IJMSSender jmsSender;
	private ILogger logger;

	/*private static final AccountService ACCOUNT_SERVICE = new AccountService();

	public static AccountService getAccountService(){
		return ACCOUNT_SERVICE;
	}*/
	public AccountService(){
		accountDAO=new AccountDAO();
		currencyConverter= new CurrencyConverter();
		jmsSender =  new JMSSender();
		logger = new Logger();
		initAccounts();
	}

	private void initAccounts(){
		// create 2 accounts;
		createAccount(1263862, "Frank Brown");
		createAccount(4253892, "John Doe");
		//use account 1;
		deposit(1263862, 240);
		deposit(1263862, 529);
		withdrawEuros(1263862, 230);
		//use account 2;
		deposit(4253892, 12450);
		depositEuros(4253892, 200);
		transferFunds(4253892, 1263862, 100, "payment of invoice 10232");
		// show balances
		System.out.println("initialised");
	}

	public Account createAccount(long accountNumber, String customerName) {
		Account account = new Account(accountNumber);
		Customer customer = new Customer(customerName);
		account.setCustomer(customer);
		accountDAO.saveAccount(account);
		logger.log("createAccount with parameters accountNumber= "+accountNumber+" , customerName= "+customerName);
		return account;
	}

	public void deposit(long accountNumber, double amount) {
		Account account = accountDAO.loadAccount(accountNumber);
		account.deposit(amount);
		accountDAO.updateAccount(account);
		logger.log("deposit with parameters accountNumber= "+accountNumber+" , amount= "+amount);
		if (amount > 10000){
			jmsSender.sendJMSMessage("Deposit of $ "+amount+" to account with accountNumber= "+accountNumber);
		}
	}

	public Account getAccount(long accountNumber) {
		Account account = accountDAO.loadAccount(accountNumber);
		return account;
	}

	public Collection<Account> getAllAccounts() {
		return accountDAO.getAccounts();
	}

	public void withdraw(long accountNumber, double amount) {
		Account account = accountDAO.loadAccount(accountNumber);
		account.withdraw(amount);
		accountDAO.updateAccount(account);
		logger.log("withdraw with parameters accountNumber= "+accountNumber+" , amount= "+amount);
	}

	public void depositEuros(long accountNumber, double amount) {
		Account account = accountDAO.loadAccount(accountNumber);
		double amountDollars = currencyConverter.euroToDollars(amount);
		account.deposit(amountDollars);
		accountDAO.updateAccount(account);
		logger.log("depositEuros with parameters accountNumber= "+accountNumber+" , amount= "+amount);
		if (amountDollars > 10000){
			jmsSender.sendJMSMessage("Deposit of $ "+amount+" to account with accountNumber= "+accountNumber);
		}
	}

	public void withdrawEuros(long accountNumber, double amount) {
		Account account = accountDAO.loadAccount(accountNumber);
		double amountDollars = currencyConverter.euroToDollars(amount);
		account.withdraw(amountDollars);
		accountDAO.updateAccount(account);
		logger.log("withdrawEuros with parameters accountNumber= "+accountNumber+" , amount= "+amount);
	}

	public void transferFunds(long fromAccountNumber, long toAccountNumber, double amount, String description) {
		Account fromAccount = accountDAO.loadAccount(fromAccountNumber);
		Account toAccount = accountDAO.loadAccount(toAccountNumber);
		fromAccount.transferFunds(toAccount, amount, description);
		accountDAO.updateAccount(fromAccount);
		accountDAO.updateAccount(toAccount);
		logger.log("transferFunds with parameters fromAccountNumber= "+fromAccountNumber+" , toAccountNumber= "+toAccountNumber+" , amount= "+amount+" , description= "+description);
		if (amount > 10000){
			jmsSender.sendJMSMessage("TransferFunds of $ "+amount+" from account with accountNumber= "+fromAccount+" to account with accountNumber= "+toAccount);
		}
	}
}
