package com.bank.exceptions;

public class InvalidPinException extends RuntimeException{
	@Override
	public String toString() {
		return getClass()+ " : invalid pin ";
	}
}
