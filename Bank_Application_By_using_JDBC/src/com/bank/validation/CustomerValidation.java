package com.bank.validation;

public class CustomerValidation {
	
//	Database for mobile Number

	public static boolean validateName(String name) {
		name = name.trim();
		int spaces=0, digits=0, special=0;
		
		for(int i=0;i<name.length();i++) {
			char ch = name.charAt(i);
			if(Character.isAlphabetic(ch)) {}
			else if(Character.isDigit(ch)) digits++;
			else if(Character.isWhitespace(ch)) spaces++;
			else special++;
		}
		return (name.length()>=3 && digits==0 && spaces<=2 && special==0);
		
	}
	
	public static boolean validateCustomerMobileNumber(long number) {
		if(number>=6000000000L && number<=9999999999L)
			return true;
		else
			return false;
	}
	
	public static boolean validateAadharNumber(long aadharNum) {
		if(aadharNum>=100000000000L && aadharNum<=999999999999L)
			return true;
		else
			return false;
	}
	
	public static boolean validateAmount(double amount) {
		if(amount>0)
			return true;
		else
			return false;
	}
	
	public static boolean validateEmailId(String id) {
		id = id.trim();
		if(id.endsWith("@gmail.com") && id.length()>=15)
			return true;
		else
			return false;
	}
	
	public static boolean validateGender(String gender) {
		if(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"))
			return true;
		else
			return false;
	}
	
	public static boolean validatePin(int pin) {
		if(pin>0) {
			String s = pin+"";
			return s.length()==4;
		}else
			return false;
	}
	
	
	
}
