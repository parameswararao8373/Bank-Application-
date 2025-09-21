package com.bank.main;

import java.util.Scanner;

import com.bank.service.AdminService;
import com.bank.service.CustomerServices;

public class BankMainClass {
	
	public static void main(String[] args) {	
		AdminService adminService = new AdminService();
		CustomerServices customerServices = new CustomerServices();
		Scanner sc = new Scanner(System.in);
			int choice =0;
		while(true) {
			System.out.println("\n**********Welcome To Bank**********");
			System.out.println("---- Choose a option ---- ");
			System.out.println("1) New Customer Regitration");
			System.out.println("2) Check your account status");
			System.out.println("3) Customer Login");
			System.out.println("4) Admin Login");
			System.out.println("5) Exit");
			choice = sc.nextInt();
			
//			For Option 1
			if(choice ==1) {
				customerServices.registerCustomer();
			}
			
//			For Option 2
			else if(choice == 2){
				System.out.println("Enter your mobile number : ");
				Long number = sc.nextLong();
				customerServices.getAccountStatusByMobileNumber(number);
			}
			
//			For Option 3
			else if (choice == 3) {
				System.out.println("Enter Mobile Number or Account Number : ");
				Long accountNumber_or_mobileNumber = sc.nextLong();
				System.out.println("Enter Pin : ");
				int pin = sc.nextInt();
				customerServices.customerLogin(accountNumber_or_mobileNumber, pin);
			}
			
//			For Option 4
			else if (choice == 4) {
				adminService.adminLogin(sc);
			}
			
//			For Option 5
			else if(choice==5) {
				System.out.println("-------------- Thanks --------------");
				break;
			}
			
//			Default Option
			else {
				System.err.println("/nPlease choose a correct Option");
				
			}
		}
	}
}
