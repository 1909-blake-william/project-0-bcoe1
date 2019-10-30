package com.revature.prompt;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.revature.daos.AccountDao;
import com.revature.daos.AccountDaoSQL;
import com.revature.daos.UserDao;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.util.AuthUtil;

import oracle.net.aso.j;

public class AdminPrompt implements Prompt {

	private Scanner scan = new Scanner(System.in);
	private UserDao userDao = UserDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	private AccountDao accountDao = AccountDao.currentImplementation;
	private AccountDaoSQL accountDaoSQL = new AccountDaoSQL();

	public int printTransactions(int i, List<Double> transactionHistory) {

		for (Double transaction : transactionHistory) {

			if (transaction < 0) {

				if (i == 0) {
					System.out.print("$" + transaction* -1);
				} else if (i % 5 == 4) {
					System.out.println(", -$" + transaction * -1);
				} else if (i % 5 == 0) {
					System.out.print("-$" + transaction * -1);
				} else {
					System.out.print(", -$" + transaction * -1);

				}
				i++;

			} else {

				if (i == 0) {
					System.out.print("$" + transaction);
				} else if (i % 5 == 4) {
					System.out.println(", $" + transaction);
				} else if (i % 5 == 0) {
					System.out.print("$" + transaction);
				} else {
					System.out.print(", $" + transaction);

				}
				i++;
			}

		}
		
		return i;

	}

	@Override
	public Prompt run() {
		System.out.println("Press 1 to view all users.");
		System.out.println("Press 2 to view all accounts.");
		System.out.println("Press 3 to view all accounts of a specific user.");
		System.out.println("Press 4 to view all transaction history.");
		System.out.println("Press 5 to view all transaction history of a specific user.");
		System.out.println("Press 6 to view all transaction history of a specific account.");
		System.out.println("Press 7 to logout.");

		String selection = scan.nextLine();
		switch (selection) {
		case "1": {
			List<User> allUsers = userDao.findAll();
			for (User u : allUsers) {
				System.out.println(u);
			}

			return this;
		}
		case "2": {
			List<Account> accounts = accountDao.viewAllAccounts();

			for (Account a : accounts) {
				System.out.println(a);
			}

			System.out.println(" ");

			return this;
		}
		case "3": {

			List<User> allUsers = userDao.findAll();

			for (User u : allUsers) {
				System.out.println(u);

			}
			System.out.println("Pick a user by id.");
			int user = scan.nextInt();
			scan.nextLine();
			List<Account> accounts = accountDaoSQL.getAccounts(user);
			for (Account a : accounts) {
				System.out.println(a);
			}
			System.out.println();

			return this;
		}
		case "4": {

			List<Double> transactionHistory = accountDao.viewAllHistory();

			int i = 0;
			printTransactions(i, transactionHistory);
			System.out.println(" ");

			return this;
		}
		case "5": {

			List<User> allUsers = userDao.findAll();

			for (User u : allUsers) {
				System.out.println(u);

			}
			System.out.println("Pick a user by id.");
			int user = scan.nextInt();
			scan.nextLine();
			List<Account> accounts = accountDaoSQL.getAccounts(user);
			int i = 0;
			for (Account a : accounts) {

				List<Double> transactionHistory = accountDao.viewHistory(a.getAccountId());

				i = printTransactions(i, transactionHistory);
			}
			System.out.println(" ");
			

			return this;
		}
		case "6": {
			List<User> allUsers = userDao.findAll();

			for (User u : allUsers) {
				System.out.println(u);

			}
			System.out.println("Pick a user by id.");
			int user = scan.nextInt();
			scan.nextLine();
			List<Account> accounts = accountDaoSQL.getAccounts(user);
			int j = 1;
			for (Account a : accounts) {
				System.out.println(j + ". " + a);
				j++;
			}

			System.out.println("Pick an account.");
			int accountId = scan.nextInt();
			scan.nextLine();

			accountId = accounts.get(accountId - 1).getAccountId();

			List<Double> transactionHistory = accountDao.viewHistory(accountId);
			int i = 0;

			printTransactions(i, transactionHistory);
			
			System.out.println(" ");

			return this;
		}
		case "7": {

			System.out.println("Logout");
			authUtil.logout();
			return new LoginPrompt();
		}

		default: {
			return this;
		}
		}

	}

}
