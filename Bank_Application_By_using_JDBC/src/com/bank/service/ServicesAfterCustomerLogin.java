package com.bank.service;

import java.util.ArrayList;
import java.util.Scanner;

import com.bank.DAO.CustomerDAO;
import com.bank.DAO.TransactionDAO;
import com.bank.validation.CustomerValidation;

public class ServicesAfterCustomerLogin {
	private Long accountNumber_or_mobileNumber;
	
	public ServicesAfterCustomerLogin(Long accountNumber_or_mobileNumber) {
		this.accountNumber_or_mobileNumber = accountNumber_or_mobileNumber;
	}
	
	
	public void start(Scanner sc) {
		CustomerDAO customerDAO = new CustomerDAO();
		int choice =0;
		while(true) {
			System.out.println("\n---- Choose a Option ----");
			System.out.println("1) Check Account Balance");
			System.out.println("2) Withdraw money");
			System.out.println("3) Deposit money");
			System.out.println("4) Check all transactions");
			System.out.println("5) Change Pin");
			System.out.println("6) Show Full Account Details");
			System.out.println("7) Back to main Menu");
			choice = sc.nextInt();
			
//			for Option 1
			if(choice == 1 ) {
				double balance = customerDAO.getAccountBalanceByAccountNumberAndPin(accountNumber_or_mobileNumber);
				if(balance == -1)
					System.out.println("\nNo Cudstomer Found with this Account number");
				else
					System.out.println("Your Current balance is : "+balance);
			}
			
//			for Option 2
			else if(choice == 2 ) {
				System.out.println("Enter your pin: ");
				int pin = sc.nextInt();
				System.out.println("Enter money to be withdrawn : ");
				double amount = sc.nextDouble();
				customerDAO.withdwarMoney(accountNumber_or_mobileNumber, amount, pin);
			}
			
//			for Option 3
			else if(choice == 3 ) {
				System.out.println("Enter your pin: ");
				int pin = sc.nextInt();
				System.out.println("Enter money to be deposited : ");
				double amount = sc.nextDouble();
				customerDAO.depositMoney(accountNumber_or_mobileNumber, amount, pin);
			}
			
//			For Option 4
			else if(choice == 4) {
				long accountNumber = customerDAO.getAccountNumberByMobileNumberOrAccountNumber(accountNumber_or_mobileNumber);
				if(accountNumber == 0L)
					System.err.println("\nOperation failed for getting transaction ! try again");
				else {
					ArrayList<String> allTransactions = new TransactionDAO().getAllTransactions(accountNumber);
					if(allTransactions == null)
						System.out.println("\nNo Transactions yet");
					else {
						System.out.println("\n============= Transactions ==============\n");
						for(String s : allTransactions) {
							System.out.println(s);
						}
						System.out.println("\n==========================================\n");
					}
				}
			}
			
//			for Option 5
			else if(choice == 5 ) {
				System.out.println("Enter new Pin : ");
				int pin = sc.nextInt();
				// validating pin
				if(CustomerValidation.validatePin(pin)) {
					String captcha = new CustomerServices().generateCaptcha();
					System.out.println("Enter captcha : "+captcha);
					String inputtedCaptcha = sc.next();
					if(captcha.equals(inputtedCaptcha)) {
						customerDAO.changePin(pin, accountNumber_or_mobileNumber);
					}else
						System.err.println("\nInvalid Captcha ! Operation failed");
				}
				else
					System.err.println("\nInvalid New Pin ! Operation failed");
			}
			
//			for Option 6
			else if(choice == 6 ) {
				customerDAO.getCustomerByAccountNumberOrMobileNumber(accountNumber_or_mobileNumber);
			}
			
//			for Option 7
			else if(choice == 7 ) {
				break;
			}
			
//			Default option
			else{
				System.err.println("\nInvalid choice ! Please choose a correct option");
			}
			
			
		}
	}
}
