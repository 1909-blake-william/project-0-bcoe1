package com.revature.Driver;

import com.revature.prompt.LoginPrompt;
import com.revature.prompt.Prompt;

public class BankDriver {
	public static void main(String[] args) {
		
		
		
		
		
		Prompt p = new LoginPrompt();
		while(true) {
			p = p.run();
			
		}
	}

}



// make an admin account in sql, and then add ways for it to view more info




//admin - see all users, see all accounts, see all history,
//or accounts of a user, or history of a user, or history of an account

