package com.revature.daos;

import java.util.List;

import com.revature.models.Account;

public interface AccountDao {
	
	AccountDao currentImplementation = new AccountDaoSQL();
	
	//List<Account> getAccounts(int userId);
	
	double withdraw(double amount, int accountNum);
	
	double deposit(double amount, int accountNum);
	
	List<Double> viewHistory(int accountNum);
	
	List<Double> viewAllHistory();
	
	List<Account> viewAllAccounts();

}
