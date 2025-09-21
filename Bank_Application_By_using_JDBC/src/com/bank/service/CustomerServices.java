package com.bank.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import com.bank.DAO.AdminDAO;
import com.bank.DAO.CustomerDAO;
import com.bank.DTO.CustomerDetails;
import com.bank.exceptions.DuplicateEmail_Id_Exception;
import com.bank.exceptions.DuplicateMobileNumberException;
import com.bank.exceptions.InvalidAadharNumber;
import com.bank.exceptions.InvalidAmountException;
import com.bank.exceptions.InvalidCustomerMobileNumber;
import com.bank.exceptions.InvalidCustomerNameException;
import com.bank.exceptions.InvalidEmailException;
import com.bank.exceptions.InvalidGenderException;

public class CustomerServices {
	
	// Loose coupling
	private CustomerDetails customerDetails;
	private static CustomerDAO customerDAO = new CustomerDAO();
	private static AdminDAO adminDAO = new AdminDAO();
	private Scanner sc = new Scanner(System.in);
	
//	Method for registering a Customer to admin side => AdminDAO
	public void registerCustomer() {
		System.out.println("Enter customer name : ");
		String name = sc.next();
		System.out.println("Enter customer email id : ");
		String email = sc.next();
		System.out.println("Enter customer mobile number : ");
		long mobileNumber = sc.nextLong();
		System.out.println("Enter customer aadhar number : ");
		long aadharNumber = sc.nextLong();
		System.out.println("Enter customer address : ");
		String address = sc.next();
		System.out.println("Enter customer gender : ");
		String gender = sc.next();
		System.out.println("Enter customer Initial amount : ");
		double amount = sc.nextDouble();
		
		while(true) {
			try {
				customerDetails = new CustomerDetails(name, email, mobileNumber, aadharNumber, address, gender, amount);
				// using admin DAO
				adminDAO.registerCustomer(customerDetails);
				break;
			} catch (InvalidCustomerNameException e) {
				System.err.print("Entered name [ "+name+" ] is Invalid  ! Please Re-enter : ");
				name = sc.next();
			} catch (InvalidEmailException e) {
				System.err.print("Entered email [ "+email+" ] is Invalid  ! Please Re-enter : ");
				email = sc.next();
			} catch (DuplicateEmail_Id_Exception e) {
				System.err.print("Entered email [ "+email+" ] is Duplicate  ! Please Re-enter : ");
				email = sc.next();
			} catch (InvalidCustomerMobileNumber e) {
				System.err.print("Entered mobile number [ "+mobileNumber+" ] is Invalid  ! Please Re-enter : ");
				mobileNumber = sc.nextLong();
			} catch (DuplicateMobileNumberException e) {
				System.err.print("Entered mobile number [ "+mobileNumber+" ] is Duplicate  ! Please Re-enter : ");
				mobileNumber = sc.nextLong();
			} catch (InvalidAadharNumber e) {
				System.err.print("Entered Aadhar number [ "+aadharNumber+" ] is Invalid  ! Please Re-enter : ");
				aadharNumber = sc.nextLong();
			} catch (InvalidGenderException e) {
				System.err.print("Entered gender [ "+gender+" ] is Invalid  ! Please Re-enter : ");
				gender = sc.next();
			} catch (InvalidAmountException e) {
				System.err.print("Entered initial amount [ "+amount+" ] is Invalid  ! Please Re-enter : ");
				amount = sc.nextDouble();
			}
		}
	}
	
//	Method for getting a registered account status by admin side => AdminDAO
	public void getAccountStatusByMobileNumber(Long mobileNumber) {
		String status = adminDAO.getAccountStatusByMobileNumber(mobileNumber);
		if(status.equals(""))
			System.out.println("\nOperation failed for checking account status ! try again...");
		else if (status.equalsIgnoreCase("No mobile number matched with registered account"))
			System.out.println(status);
		else
			System.out.println("Your account status is : "+status);
	}
	
//	Method to show All Customers => CustomerDAO
	public static void showAllCustomers() {
		ArrayList<CustomerDetails> listOfCustomerDetails = customerDAO.getAllCustomers();
		if(listOfCustomerDetails == null)
			System.out.println("No customers are created yet...");
		else {
			Iterator<CustomerDetails> itr = listOfCustomerDetails.iterator();
			while(itr.hasNext()) {
				CustomerDetails customerDetails = itr.next();
				customerDetails.showCustomerDetails();
			}
		}
	}
	
//	Method for generating Captcha
	public String generateCaptcha() {
		Random random = new Random();
		int num =  random.nextInt(10);
		int num2 = Math.round(random.nextInt(25));
		int num3 = random.nextInt(25);
		char ch1 = (char) (65+num2);
		char ch2 = (char) (97+num3);
		return (ch1+""+num)+(ch2+""+num2);
	}
	
//	Method for customer Login
	public void customerLogin(long accountNumber_or_mobileNumber , int pin) {
		CustomerDetails customerDetails = customerDAO.customerLogin(accountNumber_or_mobileNumber, pin);
		if(customerDetails!=null) {
			String captcha = generateCaptcha();
			System.out.println("Enter captcha : "+captcha);
			String enteredCaptcha = sc.next();
			if(captcha.equals(enteredCaptcha)) {
				String gender = customerDetails.getGender();
				if(gender.equalsIgnoreCase("Male")) {

					System.out.println("\n Hello \n Mr. "+customerDetails.getName());
				
					// Creating object of ServicesAfterCustomerLogin
					ServicesAfterCustomerLogin afterLoginServices = new ServicesAfterCustomerLogin(accountNumber_or_mobileNumber);
					afterLoginServices.start(sc);
				}
				else if(gender.equalsIgnoreCase("Female"))
					System.out.println("\n Hello \n Miss. "+customerDetails.getName());	
			}
			else
				System.err.println("\nInvalid Captcha ! operation failedd...");
		}else
			System.err.println("\nInvalid Credentials ! Operation failded...");
	}
	
	
}


