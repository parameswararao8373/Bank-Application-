package com.bank.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import com.bank.DTO.CustomerDetails;

public class AdminDAO {
	
//	Making Connection	
	private MakeConnection connection = new ReturnConnection();
	private Connection con = connection.makeConnection();
	
//	Method for registering a customer
	public void registerCustomer(CustomerDetails customerDetails) {
		final String register_customer_query =
				"insert into manage_customers ( "
				+ "Customer_Name, Customer_EmailId, Customer_Mobile_Number, Customer_AadharCardNumber,"
				+ " Customer_Initial_Amount, Customer_Address, Customer_Gender) values (?,?,?,?,?,?,?)";
		try {
			con = connection.makeConnection();
			PreparedStatement ps = con.prepareStatement(register_customer_query);
			ps.setString(1, customerDetails.getName());
			ps.setString(2, customerDetails.getEmail_Id());
			ps.setLong(3, customerDetails.getMobile_Number());
			ps.setLong(4, customerDetails.getAadhar_Number());
			ps.setDouble(5, customerDetails.getAmount());
			ps.setString(6, customerDetails.getAddress());
			ps.setString(7, customerDetails.getGender());

			int row = ps.executeUpdate();
			if (row != 0)
				System.out.println("\nRequest sent successfully...");
			else {
				System.out.println("\nOperation failed for registering a customer ! try again");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	Method for getting a Account status 
	public String getAccountStatusByMobileNumber(Long mobileNumber) {
		final String check_account_status_query = 
				"select Status from manage_customers where Customer_Mobile_Number = ? ";
		try {
			PreparedStatement ps = con.prepareStatement(check_account_status_query);
			ps.setLong(1, mobileNumber);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("Status");
			else
				return "No mobile number matched with registered account";
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}
	
//	Method for validating an admin details
	public boolean authenticateAdminDetails(String emailId, String password) {
		final String validate_admin_query = 
				"select * from admin_details where EmailId = ? and Password = ?";
		try {
			PreparedStatement ps = con.prepareStatement(validate_admin_query);
			ps.setString(1, emailId);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	now a combined method which will check duplicate aadhar or mobile or email id
	public boolean isDuplicateAadharNumberOrMobileNumberOrEmail_Id(String email_id, long mobile_number, long aadhar_number) {
		final String query = "select * from customer_details where Customer_EmailId =? or Customer_Mobile_Number =? or Customer_AadharCardNumber = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email_id);
			ps.setLong(2, mobile_number);
			ps.setLong(3, aadhar_number);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	method to set status as Rejected or Accepted or In-Process
	public void setStatus(String status, int id) {
		final String update_status = "update manage_customers set Status = ? where  Customer_Id = ? ";
		try {
			PreparedStatement ps = con.prepareStatement(update_status);
			ps.setString(1, status);
			ps.setInt(2, id);
			int row = ps.executeUpdate();
			if(row!=0) {
				System.out.println("\nsetted status as "+status+" for id : "+id);
			}else
				System.err.println("\nOperation failed for setting status as "+status+" ! try again");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
//	Method to get all Accounts with status = Pending or Rejected or In-Process
	public ArrayList<CustomerDetails> getAccounts(String status) {
		// storing all pending customers
		ArrayList<CustomerDetails> listOfCustomerDetails = new ArrayList<CustomerDetails>();
		final String get_accounts_query = 
				"select * from manage_customers where status = ? ";
		try {
			PreparedStatement ps = con.prepareStatement(get_accounts_query);
			ps.setString(1, status);
			ResultSet rs =  ps.executeQuery();
			if(rs.isBeforeFirst()) {
				while(rs.next()) {
					CustomerDetails customerDetails = new CustomerDetails();
					customerDetails.setCustomer_id(rs.getInt("Customer_Id"));
					customerDetails.setName(rs.getString("Customer_Name"));
					customerDetails.setEmail_Id(rs.getString("Customer_EmailId"));
					customerDetails.setMobile_Number(rs.getLong("Customer_Mobile_Number"));
					customerDetails.setAadhar_Number(rs.getLong("Customer_AadharCardNumber"));
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
	
//	method for setting account number and pin
	private void setAccountNumberAndPin(Long account_number, int pin, int id) {
		final String setAccountNumberAndPin = "update manage_customers set Customer_Account_Number = ? , Customer_PIN = ?  where Customer_Id = ?";
		try {
			PreparedStatement ps = con.prepareStatement(setAccountNumberAndPin);
			ps.setLong(1, account_number);
			ps.setInt(2, pin);
			ps.setInt(3, id);
			int row = ps.executeUpdate();
			if(row!=0)
				System.out.println("Account number and pin setted for id : "+id);
			else
				System.err.println("\nOperation failed for setting account number and pin ! try-again");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	Method for Generate And set Account Number and pin for In-Process Accounts
	private void generateAndSetAccountNumberAndPin(int id) {
		AdminDAO adminDAO = new AdminDAO();
		Random random = new Random();
		Long account_number;
		int pin = 1000 + random.nextInt(9000);
		// checking that generated account number is already present in the table 
		String check_accountNumber_already_exists = "select 1 from customer_details where Customer_Account_Number = ? ";
		
		// Generating account number and pin
		while(true) {
			try {
				account_number = 10000000L + random.nextInt(90000000);
				PreparedStatement ps = con.prepareStatement(check_accountNumber_already_exists);
				ps.setLong(1, account_number);
				ResultSet rs = ps.executeQuery();
				if(!rs.isBeforeFirst())
					break;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Setting account number and pin
		adminDAO.setAccountNumberAndPin(account_number,pin, id);
	}
	
	
	
//	Method for Validating new Requested Account
	// also for setting status as (Rejected or In-Process)
	public void validateNewRequestedAccountsAndSetStatus() {
		ArrayList<CustomerDetails> customers = new AdminDAO().getAccounts("Pending");
		AdminDAO adminDAO = new AdminDAO();
		int id;
		String email_id;
		Long mobile_number;
		Long aadhar_number;
		
		if(customers != null) {
			for(CustomerDetails customer : customers) {
				id = customer.getCustomer_id();
				email_id = customer.getEmail_Id();
				mobile_number = customer.getMobile_Number();
				aadhar_number = customer.getAadhar_Number();
				if(adminDAO.isDuplicateAadharNumberOrMobileNumberOrEmail_Id(email_id, mobile_number, aadhar_number)) {
					adminDAO.setStatus("Rejected",id);
				}else {
					adminDAO.setStatus("In-Process",id);
				}
			}
		} else {
			System.out.println("\nNo Account to validate...");
		}
		
	}
	
//	Method for adding customer to customer table
	public void addCustomer(int id) {
		
		final String get_customer_with_in_process_status = "select * from manage_customers where Customer_Id = ? ";
		final String add_customer_query = 
				" insert into customer_details ( "
				+ "Customer_Name, Customer_EmailId, Customer_Mobile_Number, Customer_AadharCardNumber, "
				+ "Customer_Initial_Amount, Customer_Address, Customer_Gender, Customer_Account_Number, Customer_PIN) values (?,?,?,?,?,?,?,?,?) ";
		try {
			PreparedStatement ps0 = con.prepareStatement(get_customer_with_in_process_status);
			ps0.setInt(1, id);
			ResultSet rs = ps0.executeQuery();
			PreparedStatement ps = con.prepareStatement(add_customer_query);
			if(rs.isBeforeFirst()) {
				while(rs.next()) {
					ps.setString(1, rs.getString("Customer_Name"));
					ps.setString(2, rs.getString("Customer_EmailId"));
					ps.setLong(3, rs.getLong("Customer_Mobile_Number"));
					ps.setLong(4, rs.getLong("Customer_AadharCardNumber"));
					ps.setDouble(5, rs.getDouble("Customer_Initial_Amount"));
					ps.setString(6, rs.getString("Customer_Address"));
					ps.setString(7, rs.getString("Customer_Gender"));
					ps.setLong(8, rs.getLong("Customer_Account_Number"));
					ps.setInt(9, rs.getInt("Customer_PIN"));
					int row = ps.executeUpdate();
					if(row != 0)
						System.out.println("Customer added with id : "+rs.getInt("Customer_Id"));
					else
						System.err.println("Operation failed for adding customer ! try again");
				}
			}else
				System.err.println("\n...Opearation failed for adding an customer ! try again");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	Method for generating pin and account 
	//	also set status as Accepted
		// also add customers to customer table
	public void generateAccountAndPinAndSetStatus() {
		AdminDAO adminDAO = new AdminDAO();
		ArrayList<CustomerDetails> customerDetails = adminDAO.getAccounts("In-Process");
		if(customerDetails == null)
			System.out.println("\n No Customers with In-Process status");
		else {
			for(CustomerDetails customer : customerDetails) {
				int id = customer.getCustomer_id();
				// Generating account and pin number 
				adminDAO.generateAndSetAccountNumberAndPin(id);
				
				// Setting status as Accepted
				adminDAO.setStatus("Accepted",id);
				
				// Adding validated customer to customer table
				adminDAO.addCustomer(id);
			}
		}
	}
}



