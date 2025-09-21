package com.bank.exceptions;

public class InvalidAadharNumber extends RuntimeException{
	@Override
	public String toString() {
		return getClass()+" invalid aadhar number ";
	}
}
