package com.bank.exceptions;

public class DuplicateEmail_Id_Exception extends RuntimeException{
	@Override
	public String toString() {
		return getClass()+" : duplicate email id ";
	}
}
