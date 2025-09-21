package com.bank.exceptions;

public class DuplicateMobileNumberException extends RuntimeException{
	@Override
	public String toString() {
		return getClass()+" : invalid email id ";
	}
}
