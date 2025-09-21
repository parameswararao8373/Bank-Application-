package com.bank.exceptions;

public class InvalidCustomerNameException extends RuntimeException{
	@Override
	public String toString() {
		return getClass()+" : invalid name ";
	}
}
