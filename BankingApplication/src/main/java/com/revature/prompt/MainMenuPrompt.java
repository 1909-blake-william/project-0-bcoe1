package com.revature.prompt;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.revature.daos.AccountDao;
import com.revature.daos.AccountDaoSQL;
import com.revature.daos.UserDao;
import com.revature.models.Account;
import com.revature.util.AuthUtil;

public class MainMenuPrompt implements Prompt {

	private Scanner scan = new Scanner(System.in);
	private UserDao userDao = UserDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	private AccountDao accountDao = AccountDao.currentImplementation;

	@Override
	public Prompt run() {
		System.out.println("Press 1 to add an account");
		System.out.println("Press 2 to close an account");
		System.out.println("Press 3 to view accounts");
		System.out.println("Press 4 to logout");

		String choice = scan.nextLine();

		switch (choice) { // add accounts
		case "1": {
			System.out.println("Add accounts");
			System.out.println("What type of account is this?");
			String type = scan.nextLine();
			System.out.println("Initial deposit amount?");
			double balance = scan.nextDouble();
			scan.nextLine();
			if (balance < 0) {
				System.out.println("Initial deposit must be at least 0.");
				System.out.println(" ");

				return this;
			}
			balance = Math.floor(balance * 100)/100;
			userDao.createAccount(balance, type, authUtil.getCurrentUser().getId());
			
			


			System.out.println("Account created.");
			System.out.println(" ");
			return this;
		}

		case "2": {

			int numAccounts = authUtil.getCurrentUser().getAccount().size();
			for (int i = 0; i < numAccounts; i++) {

				System.out.println("Account " + (i + 1) + ": " + authUtil.getCurrentUser().getAccount().get(i));
			}
			System.out.println("Select account to close.");
			int accountNum = scan.nextInt();
			if (accountNum > numAccounts || accountNum < 1) {
				System.out.println("Not a valid account.");
				System.out.println(" ");
				return this;
			}
			accountNum -= 1;
			scan.nextLine();

			System.out.println("Are you sure that you want to delete account " + (accountNum + 1) + "?");
			String check = scan.nextLine().toLowerCase();

			if ("yes".equals(check) || "y".equals(check)) {
				int id = authUtil.getCurrentUser().getAccount().get(accountNum).getAccountId();
				userDao.deleteAccount(id);
				//authUtil.getCurrentUser().setAccount(AccountDaoSQL.getAccounts(authUtil.getCurrentUser().getId()));
			//fix this last line so it doesnt need to call SQL dao
				System.out.println("Account closed.");
				System.out.println(" ");

			} else {
				System.out.println("Account not closed.");
				System.out.println(" ");
			}

			return this;
		}

		case "3": {

			return new PickAccountPrompt();
		}

		case "4": {
			System.out.println("Logout");
			authUtil.logout();
			return new LoginPrompt();
		}

		default:
			System.out.println("Invalid selection.");
			return this;
		}

	}

}
