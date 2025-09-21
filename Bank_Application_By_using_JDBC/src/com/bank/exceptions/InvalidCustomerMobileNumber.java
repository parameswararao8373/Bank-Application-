package com.bank.exceptions;

public class InvalidCustomerMobileNumber extends RuntimeException{
	@Override
	public String toString() {
		return getClass()+" invalid mobile number ";
	}
}
