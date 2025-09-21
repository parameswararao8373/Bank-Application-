package com.bank.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.bank.DAO.AdminDAO;
import com.bank.DTO.CustomerDetails;

public class AdminService {
	
	private AdminDAO adminDAO = new AdminDAO();
	private static CustomerServices customerServices = new CustomerServices();
	
//	Method for validating admin details
	public void adminLogin(Scanner sc) {
		System.out.println("Enter email id : ");
		String email_id = sc.next();
		System.out.println("Enter password : ");
		String password = sc.next();
		int choice =0;
		if(adminDAO.authenticateAdminDetails(email_id, password)) {
			String captcha = customerServices.generateCaptcha();
			System.out.println("Enter Captcha : "+captcha);
			String inputtedCaptcha = sc.next();
			if(captcha.equals(inputtedCaptcha)) {
				
				System.out.println("\nLogin Successfull as admin...\n");
				
				while(true) {
					System.out.println("\n---- Choose a Option ----");
					System.out.println("1) To get All Customes");
					System.out.println("2) All Requests for new Account");
					System.out.println("3) To get all In-process accounts (Validated Accounts) ");
					System.out.println("4) To Get all Rejected Acounts");
					System.out.println("5) Validate requested accounts and set status (Rejected or In-Process) ");
					System.out.println("6) Generate Account and Pin number for In-Process accounts");
					System.out.println("7) Back to main MENU");
					choice = sc.nextInt();
					
//					For option 1
					if(choice == 1) {
						CustomerServices.showAllCustomers();
					}
					
//					For option 2
					else if(choice == 2) {
						ArrayList<CustomerDetails> customers = adminDAO.getAccounts("Pending");
						if(customers == null)
							System.out.println("\nNo new account requests...");
						else {
							Iterator<CustomerDetails>  itr = customers.iterator();
							while(itr.hasNext()) {
								CustomerDetails customerDetails = itr.next();
								System.out.println("Customer id : "+customerDetails.getCustomer_id());
								System.out.println("Customer name : "+customerDetails.getName());
								System.out.println("Customer email : "+customerDetails.getEmail_Id());
								System.out.println("Customer mobile number : "+customerDetails.getMobile_Number());
								System.out.println("Customer aadhar number : "+customerDetails.getAadhar_Number());
								System.out.println("-------------------------------------------------\n");
								
							}
						}
					}
					
//					For option 3
					else if(choice==3) {
						ArrayList<CustomerDetails> listOfCustomers = adminDAO.getAccounts("In-Process");
						if(listOfCustomers == null)
							System.out.println("\nNo Accounts with In-Process Status");
						else {
							for(CustomerDetails customerDetails : listOfCustomers) {
								System.out.println("Customer id : "+customerDetails.getCustomer_id());
								System.out.println("Customer name : "+customerDetails.getName());
								System.out.println("Customer mobile number : "+customerDetails.getMobile_Number());
								System.out.println("\n------------------------------------------------------");
							}
						}
					}
					
//					For Option 4
					else if(choice==4) {
						ArrayList<CustomerDetails> listOfCustomers = adminDAO.getAccounts("In-Process");
						if(listOfCustomers == null)
							System.out.println("\nNo Accounts with Rejected Status");
						else {
							for(CustomerDetails customerDetails : listOfCustomers) {
								System.out.println("Customer id : "+customerDetails.getCustomer_id());
								System.out.println("Customer name : "+customerDetails.getName());
								System.out.println("Customer mobile number : "+customerDetails.getMobile_Number());
								System.out.println("\n------------------------------------------------------");
							}
						}
					}
					
//					For Option 5
					else if(choice == 5) {
						adminDAO.validateNewRequestedAccountsAndSetStatus();
					}
					
//					For Option 6
					else if(choice == 6) {
						adminDAO.generateAccountAndPinAndSetStatus();
					}
					
					else if(choice == 7) {
						break;
					}
					
//					Default Option
					else {
						System.out.println("\nPlease choose a valid choice");
					}
					
				}
			} else {
				System.err.println("\nInvalid captcha ! Opearion failed...");
			}
			
			
		} else {
			System.err.println("Mismatched Email or Password");
		}
	}
}
