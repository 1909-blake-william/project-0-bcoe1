package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.util.AuthUtil;
import com.revature.util.ConnectionUtil;

public class UserDaoSQL implements UserDao {
	
	AuthUtil authUtil = AuthUtil.instance;

	@Override
	public int save(User u) {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "INSERT INTO bank_users (user_id, username, password, user_role) "
					+ " VALUES (bank_users_id_seq.nextval ,?,?,?)";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, "User");

			return ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<User> findAll() {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM bank_users WHERE user_role != 'Admin'";

			PreparedStatement ps = c.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			List<User> users = new ArrayList<>();

			while (rs.next()) {

				int id = rs.getInt("user_id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String role = rs.getString("user_role");
				List<Account> account = AccountDaoSQL.getAccounts(id);

				User user = new User(id, username, password, role, account);
				//System.out.println(user);

				users.add(user);
			}

			return users;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
			return null;
		}
	}

	@Override
	public User findById(int id) {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM bank_users WHERE user_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			// List<User> users = new ArrayList<>();

			if (rs.next()) {

				String username = rs.getString("username");
				String password = rs.getString("password");
				String role = rs.getString("user_role");
				List<Account> account = AccountDaoSQL.getAccounts(id);

				User user = new User(id, username, password, role, account);
				return user;

			} else {
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		
		try (Connection c = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM bank_users " + "WHERE username = ? AND password = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int id = rs.getInt("user_id");

				String role = rs.getString("user_role");
				List<Account> account = AccountDaoSQL.getAccounts(id);

				User user = new User(id, username, password, role, account);
				return user;
			} else {
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public User findByUsername(String username) {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM bank_users WHERE username = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();
			// List<User> users = new ArrayList<>();

			if (rs.next()) {

				int id = rs.getInt("user_id");

				String password = rs.getString("password");
				String role = rs.getString("user_role");
				List<Account> account = AccountDaoSQL.getAccounts(id);

				User user = new User(id, username, password, role, account);
				return user;

			} else {
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}

	}

	@Override
	public int deleteAccount(int id) {
		try (Connection c = ConnectionUtil.getConnection()) {

			//String sql = "DELETE FROM accounts " + " WHERE account_id = ?";
			String sqlFindBalance = "SELECT balance FROM accounts WHERE account_id = ?";
			PreparedStatement psBalance = c.prepareStatement(sqlFindBalance);
			psBalance.setInt(1, id);
			ResultSet rs = psBalance.executeQuery();
			rs.next();
			double balance = rs.getDouble("balance");
			
			
			
			String sql = "UPDATE accounts SET status = 'Inactive', balance = 0 WHERE account_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, id);
		
			ps.executeUpdate();
			authUtil.getCurrentUser().setAccount(AccountDaoSQL.getAccounts(authUtil.getCurrentUser().getId()));
			
			System.out.println("$" + balance + " withdrawn.");
			
			String sql2 = "INSERT INTO account_history (account_history_id, account_id, balance_change) "
					+ " VALUES (account_history_id_seq.nextval ,?,?)";
			PreparedStatement ps2 = c.prepareStatement(sql2);
			
			
			ps2.setInt(1, id);
			ps2.setDouble(2, balance*-1);
			
			
			
			ps2.executeUpdate();
			
			authUtil.getCurrentUser().setAccount(AccountDaoSQL.getAccounts(authUtil.getCurrentUser().getId()));
			
			return 1;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;

		}
		

	}

	@Override
	public int createAccount(double balance, String type, int owner) {
		try (Connection c = ConnectionUtil.getConnection()) {

			String sql = "INSERT INTO accounts (account_id, balance, account_type, account_owner) "
					+ " VALUES (bank_account_id_seq.nextval ,?,?,?)";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setDouble(1, balance);
			ps.setString(2, type);
			ps.setInt(3, owner);
			
			
			ps.executeUpdate();
			//update list off accounts before dealing with history
			authUtil.getCurrentUser().setAccount(AccountDaoSQL.getAccounts(authUtil.getCurrentUser().getId()));

			//add a row to account history table
			
			String sql2 = "INSERT INTO account_history (account_history_id, account_id, balance_change) "
					+ " VALUES (account_history_id_seq.nextval ,?,?)";
			PreparedStatement ps2 = c.prepareStatement(sql2);
			
		
			
			
			
			List<Account> listAccounts = authUtil.getCurrentUser().getAccount();
			int newAccountId = 0;
			for (int i = 0; i < authUtil.getCurrentUser().getAccount().size(); i++) {
				int x = listAccounts.get(i).getAccountId();
				if(x > newAccountId) {
					newAccountId = x;
				}
			}
			
			
			ps2.setInt(1, newAccountId);
			ps2.setDouble(2, balance);
			
			
			
			ps2.executeUpdate();
			
			authUtil.getCurrentUser().setAccount(AccountDaoSQL.getAccounts(authUtil.getCurrentUser().getId()));
			
			return 1;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return 0;

		}

	}
}
