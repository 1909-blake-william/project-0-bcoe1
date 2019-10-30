package com.revature.prompt;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.revature.daos.UserDao;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.util.AuthUtil;

public class LoginPrompt implements Prompt{
	
	private Scanner scan = new Scanner(System.in);
	private UserDao userDao = UserDao.currentImplementation;
	private AuthUtil authUtil = AuthUtil.instance;

	@Override
	public Prompt run() {
		System.out.println("Enter 1 to login");
		System.out.println("Enter 2 to register");
		String choice = scan.nextLine();
		switch (choice) {
		case "1": {
			System.out.println("Enter username:");
			String username = scan.nextLine();
			System.out.println("Enter password");
			String password = scan.nextLine();

			User u = authUtil.login(username, password);
			
			
			if (u == null) {
				System.out.println("Invalid Credentials");
				break;
			} else if(u.getRole().equals("Admin")) {
				return new AdminPrompt();	
			}
			else {
				return new MainMenuPrompt();
			}
		}
		case "2": {
			System.out.println("Enter new username:");
			String username = scan.nextLine();
			User u = userDao.findByUsername(username);
			if (u != null) {
				System.out.println("invalid username");
				break;
			}
			System.out.println("Enter new Password:");
			String password = scan.nextLine();
			List<Account> accounts = new ArrayList<>();
			//	authUtil.getCurrentUser().setAccount(accounts);
			User newUser = new User(0, username, password, "User", accounts);
			userDao.save(newUser);
			break;
		}
		default:
			System.out.println("invalid option");
			break;
		}
		return this;
	}

}
