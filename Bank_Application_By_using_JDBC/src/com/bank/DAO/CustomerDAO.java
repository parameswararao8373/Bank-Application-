package com.bank.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.bank.DTO.CustomerDetails;
import com.bank.service.CustomerServices;

public class CustomerDAO {
	private MakeConnection connection = new ReturnConnection();
	 private Connection con = connection.makeConnection();
	
//	Method for getting all activate customers 
	public ArrayList<CustomerDetails> getAllCustomers() {
		final String get_all_customer_query = 
				"select * from customer_details";
		 ArrayList<CustomerDetails> listOfCustomerDetails = new ArrayList<CustomerDetails>();
		try {
			con = connection.makeConnection();
			PreparedStatement ps = con.prepareStatement(get_all_customer_query);
			ResultSet rs = ps.executeQuery();
			if(rs.isBeforeFirst()) {
				while(rs.next()) {
					CustomerDetails customerDetails = new CustomerDetails();
					customerDetails.setName(rs.getString("Customer_Name"));
					customerDetails.setAccount_Number(rs.getLong("Customer_Account_Number"));
					customerDetails.setEmail_Id(rs.getString("Customer_EmailId"));
					customerDetails.setMobile_Number(rs.getLong("Customer_Mobile_Number"));
					customerDetails.setAadhar_Number(rs.getLong("Customer_AadharCardNumber"));
					customerDetails.setAddress(rs.getString("Customer_Address"));
					customerDetails.setGender(rs.getString("Customer_Gender"));
					listOfCustomerDetails.add(customerDetails);
				}
				return listOfCustomerDetails;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
//	Method for customer Login
	public CustomerDetails customerLogin(long accountNumber_or_mobileNumber , int pin) {
		final String validate_customer_query = 
				" select * from customer_details where (Customer_Mobile_Number =? or Customer_Account_Number = ?) and Customer_PIN = ? ";
		try {
//			Long accountNumber = Long.parseLong(email_or_account);
			PreparedStatement ps = con.prepareStatement(validate_customer_query);
			ps.setLong(1, accountNumber_or_mobileNumber);
			ps.setLong(2, accountNumber_or_mobileNumber);
			ps.setInt(3, pin);
			ResultSet rs = ps.executeQuery();
			if(rs.isBeforeFirst()) {
				CustomerDetails customerDetails = new CustomerDetails();
				rs.next();
				customerDetails.setName(rs.getString("Customer_Name"));
				customerDetails.setGender(rs.getString("Customer_Gender"));
				return customerDetails;
			} else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
//	Method to get Account Balance By (accountNumber or mobileNumber ) and Pin
	public double getAccountBalanceByAccountNumberAndPin(Long accountNumber_or_mobileNumber) {
		final String get_account_balance_query = 
				"Select Customer_Initial_Amount from customer_details where ( Customer_Account_Number = ? or Customer_Mobile_Number = ? ) ";
		try {
			PreparedStatement ps = con.prepareStatement(get_account_balance_query);
			ps.setLong(1, accountNumber_or_mobileNumber);
			ps.setLong(2, accountNumber_or_mobileNumber);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getLong("Customer_Initial_Amount");
			else
				return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
//	Method for withdrawing money by (account number or mobileNumber) and pin
	Scanner sc = new Scanner(System.in);
	public void withdwarMoney(Long accountNumber_or_mobileNumber, double amount, int pin) {
		final String get_initial_amount_query = 
				"Select * from customer_details where ( Customer_Account_Number = ? or Customer_Mobile_Number = ? ) and Customer_PIN = ? ";
		final String withdrawMoney_query = 
				"update customer_details set Customer_Initial_Amount = ? where ( Customer_Account_Number = ? or Customer_Mobile_Number = ? )  ";
		// 1) validating amount
		if(amount<=0) {
			System.err.println("\nInvalid Entered amount ! Operation failed...");
		} else {
			try {
				PreparedStatement ps = con.prepareStatement(get_initial_amount_query);
				ps.setLong(1, accountNumber_or_mobileNumber);
				ps.setLong(2, accountNumber_or_mobileNumber);
				ps.setInt(3, pin);
				ResultSet rs = ps.executeQuery();
				// 2) validating pin is correct or not
				if(rs.next()) {
					String captcha = new CustomerServices().generateCaptcha();
					System.out.println("Enter captcha : "+captcha);
					String inputtedCaptcha = sc.next();
					// 3) validating captcha
					if(captcha.equals(inputtedCaptcha)) {
						double balance = rs.getDouble("Customer_Initial_Amount");
						// 4) validating initial amount
						if(amount>balance)
							System.out.println("\nInsufficient Amount ! Operation failed");
						else {
							balance = balance-amount;
							PreparedStatement ps2 = con.prepareStatement(withdrawMoney_query);
							ps2.setDouble(1, balance);
							ps2.setLong(2, accountNumber_or_mobileNumber);
							ps2.setLong(3, accountNumber_or_mobileNumber);
							int row = ps2.executeUpdate();
							if(row!=0) {
								// getting id and accountNumber
								int id = rs.getInt("Customer_Id");
								long accountNumber = rs.getLong("Customer_Account_Number");
								// saving transaction
								new TransactionDAO().saveTransaction("withdrawn",accountNumber, amount, id, balance);
								// Printing message
								System.out.println("\nSuccessfully withdrawn "+amount+" /Rs-");
								System.out.println("Your current balance is : "+balance+" /Rs-");
							}
							else
								System.out.println("\nOperation failed for withdrawing money..");
						}
					} else
						System.err.println("\nMismatched Captcha ! Operation failed...");
				}else
					System.err.println("\nInvalid Pin ! Operation failed...");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
//	Method for depositing money by (account number or mobileNumber) and pin
	public void depositMoney(Long accountNumber_or_mobileNumber, double amount ,int pin) {
		final String get_initial_amount_query = 
				"Select * from customer_details where ( Customer_Account_Number = ? or Customer_Mobile_Number = ? ) and Customer_PIN = ?  ";
		final String depositMoney_query = 
				"update customer_details set Customer_Initial_Amount = ? where ( Customer_Account_Number = ? or Customer_Mobile_Number = ? )  ";
		// 1) validating amount
		if(amount<=0)
			System.err.println("\nInvalid Amount ! Operation failed");
		else {
			try {
				PreparedStatement ps = con.prepareStatement(get_initial_amount_query);
				ps.setLong(1, accountNumber_or_mobileNumber);
				ps.setLong(2, accountNumber_or_mobileNumber);
				ps.setInt(3, pin);
				ResultSet rs = ps.executeQuery();
				// 2) validating is pin is correct
				if(rs.next()) {
					String captcha = new CustomerServices().generateCaptcha();
					System.out.println("Enter captcha : "+captcha);
					String inputtedCaptcha = sc.next();
					// 3) validating captcha is correct
					if(captcha.equals(inputtedCaptcha)) {
						double balance = rs.getDouble("Customer_Initial_Amount");
						balance = balance+amount;
						PreparedStatement ps2 = con.prepareStatement(depositMoney_query);
						ps2.setDouble(1, balance);
						ps2.setLong(2, accountNumber_or_mobileNumber);
						ps2.setLong(3, accountNumber_or_mobileNumber);
						int row = ps2.executeUpdate();
						if(row!=0) {
							// getting id and accountNumber
							int id = rs.getInt("Customer_Id");
							long accountNumber = rs.getLong("Customer_Account_Number");
							// saving transaction
							new TransactionDAO().saveTransaction("deposited",accountNumber, amount, id, balance);
							// Printing message
							System.out.println("\nSuccessfully deposited "+amount+" /Rs-");
							System.out.println("Your current balance is : "+balance+" /Rs-");
						}
						else
							System.out.println("\nOperation failed for depositing money..");
					} else
						System.err.println("\nCaptcha mismatched ! Operation failed...");
				}else
					System.err.println("\nInvalid Pin ! Operation failed...");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
//	Method to get Account Number by mobile number or Account Number
	// this needed because customer can login with mobileNumber also and with accountNumber also
	public long getAccountNumberByMobileNumberOrAccountNumber(Long mobileNumber_or_accountNumber) {
		final String get_accountNumber_query = 
				"select Customer_Account_Number from customer_details where Customer_Account_Number = ? or Customer_Mobile_Number = ? ";
		try {
			PreparedStatement ps = con.prepareStatement(get_accountNumber_query);
			ps.setLong(1, mobileNumber_or_accountNumber);
			ps.setLong(2 , mobileNumber_or_accountNumber);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getLong("Customer_Account_Number");
			else
				return 0L;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0L;
		}
	}
	
//	Method for changing Pin
	public void changePin(int pin, long accountNumber_or_mobileNumber) {
		final String change_pin_query = 
				"update customer_details set Customer_PIN = ? where Customer_Account_Number = ? or Customer_Mobile_Number = ? ";
		try {
			PreparedStatement ps = con.prepareStatement(change_pin_query);
			ps.setInt(1, pin);
			ps.setLong(2, accountNumber_or_mobileNumber);
			ps.setLong(3, accountNumber_or_mobileNumber);
			int row = ps.executeUpdate();
			if(row!=0)
				System.out.println("\nSuccessfully pin changed");
			else
				System.err.println("\nOperation failed for pin change ! try again");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	Get Customer By Account Number or Mobile Number
	public void getCustomerByAccountNumberOrMobileNumber(Long accountNumber_or_mobileNumber) {
		 final String select = "select * from customer_details where Customer_Account_Number = ? or Customer_Mobile_Number = ? ";
		 try {
			PreparedStatement ps = con.prepareStatement(select);
			ps.setLong(1, accountNumber_or_mobileNumber);
			ps.setLong(2, accountNumber_or_mobileNumber);
			ResultSet rs = ps.executeQuery();
			if(rs.isBeforeFirst()) {
				while(rs.next()) {
					System.out.println("\n---------------- Customer Details ---------------\n");
					System.out.println("Customer name : "+rs.getString("Customer_Name"));
					System.out.println("Customer Account number : "+rs.getLong("Customer_Account_Number"));
					System.out.println("Total balance : "+rs.getDouble("Customer_Initial_Amount"));
					System.out.println("Customer Email id : "+rs.getString("Customer_EmailId"));
					System.out.println("Customer mobile number : "+rs.getLong("Customer_Mobile_Number"));
					System.out.println("Customer Address : "+rs.getString("Customer_Address"));
					System.out.println("Customer Gender : "+rs.getString("Customer_Gender"));
					System.out.println("-----------------------------------------------------\n");
				}
			} else {
				System.out.println("\\nNo Customer exists with this Account number ! Operation Unsuccessfull...");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
	}
	
// 	Delete Customer By Account Number
	public void deleteCustomerByAccountNumber(Long accNumber) {
		 final String delete = "delete from customer_details where Customer_Account_Number = ? ";
		 try {
			PreparedStatement ps = con.prepareStatement(delete);
			ps.setLong(1, accNumber);
			int row = ps.executeUpdate();
			if(row!=0)
				System.out.println("\nSuccessfully deleted customer...");
			else
				System.out.println("\nNo Customer exists with this Account number ! Operation Unsuccessfull...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	Update Customer address By using Account Number
	public void updateCustomerAddress(Long accNumber, String address) {
		final String update = "update customer_details set Customer_Address = '?' where Customer_Account_Number = ?";
		 try {
			PreparedStatement ps = con.prepareStatement(update);
			ps.setString(1, address);
			ps.setLong(2, accNumber);
			int row = ps.executeUpdate();
			if(row!=0)
				System.out.println("\nSuccessfully updatd customer address...");
			else
				System.out.println("\nNo Customer exists with this Account number ! Operation Unsuccessfull...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}































