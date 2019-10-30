package com.revature.prompt;

import java.util.List;
import java.util.Scanner;

import com.revature.daos.AccountDao;
import com.revature.daos.UserDao;
import com.revature.util.AuthUtil;

public class PickAccountPrompt implements Prompt {

	private Scanner scan = new Scanner(System.in);
	private UserDao userDao = UserDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;
	private AccountDao accountDao = AccountDao.currentImplementation;

	@Override
	public Prompt run() {

		int numAccounts = authUtil.getCurrentUser().getAccount().size();
		for (int i = 0; i < numAccounts; i++) {

			System.out.println("Account " + (i + 1) + ": " + authUtil.getCurrentUser().getAccount().get(i));
		}
		System.out.println("Select account to access.");
		int accountNum = scan.nextInt();
		accountNum -= 1;
		scan.nextLine();
		System.out.println(" ");

		System.out.println("Press 1 to deposit funds.");
		System.out.println("Press 2 to withdraw funds.");
		System.out.println("Press 3 to view transaction history.");
		System.out.println("Press 4 to return to previous menu.");

		String prompt = scan.nextLine();

		switch (prompt) {
		case "1": {
			System.out.println("How much would you like to deposit?");
			double amount = scan.nextDouble();
			scan.nextLine();
			if (amount < 0) {
				System.out.println("Numbers must be greater than or equal to 0");
				return this;
			}
			amount = Math.floor(amount * 100) / 100;
			int Id = authUtil.getCurrentUser().getAccount().get(accountNum).getAccountId();
			accountDao.deposit(amount, Id);
			System.out.println(" ");
			return this;
		}

		case "2": {
			System.out.println("How much would you like to withdraw?");
			double amount = scan.nextDouble();
			scan.nextLine();
			if (amount < 0) {
				System.out.println("Numbers must be greater than or equal to 0");
				return this;
			}
			amount = Math.floor(amount * 100) / 100;
			int Id = authUtil.getCurrentUser().getAccount().get(accountNum).getAccountId();
			accountDao.withdraw(amount, Id);

			System.out.println(" ");
			return this;
		}

		case "3": {
			// get the account id from the account chosen from lsit of accounts
			int Id = authUtil.getCurrentUser().getAccount().get(accountNum).getAccountId();

			List<Double> transactionHistory = accountDao.viewHistory(Id);

			int i = 0;
			for (Double transaction : transactionHistory) {

				if (transaction < 0) {
					
					if (i % 5 == 4) {
						System.out.println(", -$" + transaction*-1);
					} else if (i % 5 == 0) {
						System.out.print("-$" + transaction*-1);
					} else {
						System.out.print(", -$" + transaction*-1);

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
			System.out.println(" ");
			if (i % 5 == 0) {
				System.out.println(" ");
			}
			return this;
		}

		case "4": {
			System.out.println(" ");
			return new MainMenuPrompt();
		}
		default:
			System.out.println("Invalid response");
			break;
		}
		return this;
	}

}
