package com.revature.util;


import com.revature.daos.UserDao;
import com.revature.daos.UserDaoSQL;
import com.revature.models.User;

public class AuthUtil {

	public static final AuthUtil instance = new AuthUtil();

	private UserDaoSQL dbDao = new UserDaoSQL();
//	private UserDao dbDao = UserDao.currentImplementation;
	private User currentUser = null;

	private AuthUtil() {
		super();
	}

	public User login(String username, String password) {

		User u = dbDao.findByUsernameAndPassword(username, password);
		currentUser = u;
		return u;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void logout() {
		currentUser = null;
	}

}
