package com.bank.DTO;

import com.bank.exceptions.InvalidAadharNumber;
import com.bank.exceptions.InvalidAmountException;
import com.bank.exceptions.InvalidCustomerMobileNumber;
import com.bank.exceptions.InvalidCustomerNameException;
import com.bank.exceptions.InvalidEmailException;
import com.bank.exceptions.InvalidGenderException;
import com.bank.exceptions.InvalidPinException;
import com.bank.validation.CustomerValidation;

public class CustomerDetails {
	private int customer_id;
	private String name;
	private String email_Id;
	private long mobile_Number;
	private long aadhar_Number;
	private String address;
	private String gender;
	private double amount;
	private long account_Number;
	private int pin;
	
	public CustomerDetails() {}
	
	public CustomerDetails(String name, String email_Id, long mobile_Number, long aadhar_Number, String address,
			String gender,  double amount) {
		super();
		
		if(!CustomerValidation.validateName(name))
			throw new InvalidCustomerNameException();
		
		if(!CustomerValidation.validateEmailId(email_Id)) 
			throw new InvalidEmailException();
		
		if(!CustomerValidation.validateCustomerMobileNumber(mobile_Number)) 
			throw new InvalidCustomerMobileNumber();
		
		if(!CustomerValidation.validateAadharNumber(aadhar_Number))
			throw new InvalidAadharNumber();
		
		if(!CustomerValidation.validateGender(gender))
			throw new InvalidGenderException();
		
		if(!CustomerValidation.validateAmount(amount))
			throw new InvalidAmountException();
		
		
		this.name = name;
		this.email_Id = email_Id;
		this.mobile_Number = mobile_Number;
		this.aadhar_Number = aadhar_Number;
		this.address = address;
		this.gender = gender;
		this.amount = amount;
	}
	
	
	
	
	public CustomerDetails(String name, String email_Id, long mobile_Number, long aadhar_Number, String address,
			String gender, double amount, long account_Number, int pin) {
		
		this( name,  email_Id,  mobile_Number, aadhar_Number,  address,
				 gender,   amount);
		
		if(!CustomerValidation.validatePin(pin))
			throw new InvalidPinException();
		
		this.account_Number = account_Number;
		this.pin = pin;
	}




	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(!CustomerValidation.validateName(name))
			throw new InvalidCustomerNameException();
		this.name = name;
	}
	
	public String getEmail_Id() {
		
		return email_Id;
	}
	public void setEmail_Id(String email_Id) {
		if(!CustomerValidation.validateEmailId(email_Id))
			throw new InvalidEmailException();
		this.email_Id = email_Id;
	}
	
	public long getMobile_Number() {
		return mobile_Number;
	}
	public void setMobile_Number(long mobile_Number) {
		if(!CustomerValidation.validateCustomerMobileNumber(mobile_Number))
			throw new InvalidCustomerMobileNumber();
		this.mobile_Number = mobile_Number;
	}
	
	public long getAadhar_Number() {
		return aadhar_Number;
	}
	public void setAadhar_Number(long aadhar_Number) {
		if(!CustomerValidation.validateAadharNumber(aadhar_Number))
			throw new InvalidAadharNumber();
		this.aadhar_Number = aadhar_Number;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		if(!CustomerValidation.validateGender(gender))
			throw new InvalidGenderException();
		this.gender = gender;
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		if(!CustomerValidation.validateAmount(amount))
			throw new InvalidAmountException();
		this.amount = amount;
	}
	
	public void setPin(int pin) {
		if(!CustomerValidation.validatePin(pin))
			throw new InvalidPinException();
		this.pin = pin;
	}
	
	
	public long getAccount_Number() {
		return account_Number;
	}

	public void setAccount_Number(long account_Number) {
		this.account_Number = account_Number;
	}
	
	
	

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public int getPin() {
		return pin;
	}

	@Override
	public String toString() {
		return "CustomerDetails [name=" + name + ", email_Id=" + email_Id + ", mobile_Number=" + mobile_Number
				+ ", aadhar_Number=" + aadhar_Number + ", address=" + address + ", gender=" + gender + ", amount="
				+ amount + "]";
	}
		public void showCustomerDetails() {
		System.out.println("Customer name : "+getName());
		System.out.println("Customer Account number : "+getAccount_Number());
		System.out.println("Customer email id : "+getEmail_Id());
		System.out.println("Customer mobile number : "+getMobile_Number());
		System.out.println("Customer aadhar number : "+getAadhar_Number());
		System.out.println("Customer address : "+getAddress());
		System.out.println("Customer gender : "+getGender());
		System.out.println("-----------------------------------------------\n");
	}
		
}
