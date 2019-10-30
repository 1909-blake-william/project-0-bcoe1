package com.revature.daos;

import java.util.List;

import com.revature.models.Account;
import com.revature.util.AuthUtil;

public class AccountDaoSerialization implements AccountDao{ //add throws exception up here
	
	//private UserDao userDao = UserDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	private List<Account> accounts = authUtil.getCurrentUser().getAccount();
	private UserDao userDao = UserDao.currentImplementation;
	

	@Override
	public double withdraw(double amount, int accountNum) {
		
		Account account = accounts.get(accountNum);
		if(amount < 0) {
			System.out.println("Only a positive amount can be withdrawn.");
			return account.getBalance();
		}
		if(account.getBalance() < amount) {
			System.out.println("Insufficient funds.");
			return account.getBalance();
			
		} else {
			account.setBalance(account.getBalance()-amount);
			account.getHistory().add(-1*amount);
			userDao.save(authUtil.getCurrentUser());
			return account.getBalance();
		}
		
	}

	@Override
	public double deposit(double amount, int accountNum) {
		
		Account account = accounts.get(accountNum);
		if(amount < 0) {
			System.out.println("Only a positive amount can be deposited.");
			return account.getBalance();
		}
		
			account.setBalance(account.getBalance()+amount);
			account.getHistory().add(amount);
			userDao.save(authUtil.getCurrentUser());
			return account.getBalance();
		
	}

	@Override
	public List<Double> viewHistory(int accountNum) {
		Account account = accounts.get(accountNum);
		System.out.println(account.getHistory().toString());
		return null;
	}

	@Override
	public List<Double> viewAllHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> viewAllAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

}
