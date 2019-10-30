package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.util.AuthUtil;
import com.revature.util.ConnectionUtil;

public class AccountDaoSQL implements AccountDao {

	
	private AuthUtil authUtil = AuthUtil.instance;
	
	public static List<Account> getAccounts(int userId) {
		
		
		

		try (Connection c = ConnectionUtil.getConnection()) {
			List<Account> accounts = new ArrayList<>();

			//String sql = "SELECT * FROM accounts " + "WHERE account_owner = ?";
			String sql = "SELECT * FROM accounts " + "WHERE account_owner = ? AND status = 'Active'";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String type = rs.getString("account_type");
				double balance = rs.getDouble("balance");
				int accountId = rs.getInt("account_id");
				List<Double> history = getHistory(accountId);
				

				 Account account = new Account(type, balance, history, accountId);
				 accounts.add(account);
			}
			return accounts;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static List<Double> getHistory(int accountId) {
		

		try (Connection c = ConnectionUtil.getConnection()) {
			
			List<Double> history = new ArrayList<>();

			String sql = "SELECT balance_change FROM account_history " + "WHERE account_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, accountId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				history.add(rs.getDouble("balance_change"));
			}
			return history;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}

	}

	@Override
	public double withdraw(double amount, int Id) {
		
		try (Connection c = ConnectionUtil.getConnection()) {
			
		
			String sql = "SELECT * FROM accounts " + "WHERE account_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, Id);

			ResultSet rs = ps.executeQuery();
			rs.next();
			double newBalance = rs.getDouble("balance") - amount;
			
			if(newBalance < 0) {
				System.out.println("Not enough funds available.");
				return 0;
			}
			
			System.out.println("New balance is $" + Math.floor(newBalance*100)/100);
			String sql2 = "UPDATE accounts SET balance = ? WHERE account_id = ?";

			PreparedStatement ps2 = c.prepareStatement(sql2);
			ps2.setDouble(1, newBalance);
			ps2.setInt(2, Id);
			
			
			ps2.executeUpdate();
			authUtil.getCurrentUser().setAccount(AccountDaoSQL.getAccounts(authUtil.getCurrentUser().getId()));
			
			String sql3 = "INSERT INTO account_history (account_history_id, account_id, balance_change) "
					+ " VALUES (account_history_id_seq.nextval ,?,?)";
			PreparedStatement ps3= c.prepareStatement(sql3);
			
			
			ps3.setInt(1, Id);
			ps3.setDouble(2, amount*-1);
			
			
			
			ps3.executeUpdate();
			
			authUtil.getCurrentUser().setAccount(AccountDaoSQL.getAccounts(authUtil.getCurrentUser().getId()));
			

			return 1;

			

		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public double deposit(double amount, int Id) {
		
		
		try (Connection c = ConnectionUtil.getConnection()) {
			

			String sql = "SELECT * FROM accounts " + "WHERE account_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, Id);

			ResultSet rs = ps.executeQuery();
			rs.next();
			double newBalance = rs.getDouble("balance") + amount;
			System.out.println("New balance is $" + Math.floor(newBalance*100)/100);
			
			String sql2 = "UPDATE accounts SET balance = ? WHERE account_id = ?";

			PreparedStatement ps2 = c.prepareStatement(sql2);
			ps2.setDouble(1, newBalance);
			ps2.setInt(2, Id);
			ps2.executeUpdate();
			authUtil.getCurrentUser().setAccount(AccountDaoSQL.getAccounts(authUtil.getCurrentUser().getId()));
			
			
			
			String sql3 = "INSERT INTO account_history (account_history_id, account_id, balance_change) "
					+ " VALUES (account_history_id_seq.nextval ,?,?)";
			PreparedStatement ps3= c.prepareStatement(sql3);
			
			
			ps3.setInt(1, Id);
			ps3.setDouble(2, amount);
			
			
			
			ps3.executeUpdate();
			
			authUtil.getCurrentUser().setAccount(AccountDaoSQL.getAccounts(authUtil.getCurrentUser().getId()));
			

			return 1;

			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}

	@Override
	public List<Double> viewHistory(int Id) {
		try (Connection c = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM account_history " + "WHERE account_id = ?";

			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, Id);
			

			ResultSet rs = ps.executeQuery();
			List<Double> doubles = new ArrayList<>();
			while (rs.next()) {

				int histId = rs.getInt("account_history_id");

				double change = rs.getDouble("balance_change");
				

				doubles.add(change);
				
			} 
			return doubles;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public List<Double> viewAllHistory() {
		
		try (Connection c = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM account_history ";

			PreparedStatement ps = c.prepareStatement(sql);
			
			

			ResultSet rs = ps.executeQuery();
			List<Double> doubles = new ArrayList<>();
			while (rs.next()) {

				int histId = rs.getInt("account_history_id");

				double change = rs.getDouble("balance_change");
				

				doubles.add(change);
				
			} 
			return doubles;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Account> viewAllAccounts() {
		try (Connection c = ConnectionUtil.getConnection()) {
			
			String sql = "SELECT * FROM accounts";

			PreparedStatement ps = c.prepareStatement(sql);
			
			

			ResultSet rs = ps.executeQuery();
			List<Account> accounts = new ArrayList<>();
			while (rs.next()) {

				int Id = rs.getInt("account_id");

				double balance = rs.getDouble("balance");
				
				String accountType = rs.getString("account_type");
				
				List<Double> history = getHistory(Id);
				
				Account account = new Account(accountType, balance, history, Id);

				accounts.add(account);
				
			} 
			return accounts;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return null;
		}
	}

}
