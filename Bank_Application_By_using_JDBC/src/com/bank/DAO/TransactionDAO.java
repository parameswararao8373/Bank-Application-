package com.bank.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransactionDAO {
	
//	Making connection
	private MakeConnection connection = new ReturnConnection();
	private Connection con = connection.makeConnection();
	
//	Method for saving transactions like - Withdraw or Deposit
	public void saveTransaction(String transactionMode , long accountNumber, double amount, int id , double newBalance) {
		final String save_transaction_query = 
				"insert into transaction_table ( Transactions, Customer_Account_Number, Customer_Id) values (?,?,?) ";
		final String transaction_info = amount+" /Rs- "+transactionMode+" on "+LocalDate.now()+" at "+LocalTime.now().withNano(0)+" (New balance : "+newBalance+" /Rs-)";
		try {
			PreparedStatement ps = con.prepareStatement(save_transaction_query);
			ps.setString(1, transaction_info);
			ps.setLong(2, accountNumber);
			ps.setInt(3, id);
			int row = ps.executeUpdate();
			if(row==0) {
				System.out.println("Failed to save transaction ("+transactionMode+") for id : "+id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	Method to get All Transactions 
	public ArrayList<String> getAllTransactions(Long accountNumber){
		ArrayList<String> allTransactions = new ArrayList<String>();
		final String get_allTransactions_query =
				"select Transactions from transaction_table where Customer_Account_Number = ? ";
		try {
			PreparedStatement ps = con.prepareStatement(get_allTransactions_query);
			ps.setLong(1, accountNumber);
			ResultSet rs = ps.executeQuery();
			if(rs.isBeforeFirst()) {
				while(rs.next()) {
					allTransactions.add(rs.getString("Transactions"));
				}
				return allTransactions;
			} else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}







