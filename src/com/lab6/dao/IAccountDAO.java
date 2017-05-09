package com.lab6.dao;

import com.lab6.domain.Account;

import java.util.Collection;


public interface IAccountDAO {
	public void saveAccount(Account account);
	public void updateAccount(Account account);
	public Account loadAccount(long accountnumber);
	public Collection<Account> getAccounts();
}
