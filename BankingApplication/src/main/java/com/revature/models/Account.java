package com.revature.models;

import java.util.List;

public class Account{
	
	private String type;
	private double balance;
	private List<Double> history;
	private int accountId;
	
	
	
	



	@Override
	public String toString() {
		return "Account [type=" + type + ", balance=$" + balance + "]";
		//return "Account [type=" + type + ", balance=$" + balance + ", history=" + history + "]";
	}







	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}







	public Account(String type, double balance, List<Double> history, int accountId) {
		super();
		this.type = type;
		this.balance = balance;
		this.history = history;
		this.accountId = accountId;
	}







	public String getType() {
		return type;
	}







	public void setType(String type) {
		this.type = type;
	}







	public double getBalance() {
		return balance;
	}







	public void setBalance(double balance) {
		this.balance = balance;
	}







	public List<Double> getHistory() {
		return history;
	}







	public void setHistory(List<Double> history) {
		this.history = history;
	}







	public int getAccountId() {
		return accountId;
	}







	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}







	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountId;
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((history == null) ? 0 : history.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}







	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountId != other.accountId)
			return false;
		if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
			return false;
		if (history == null) {
			if (other.history != null)
				return false;
		} else if (!history.equals(other.history))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}







	
	

}
