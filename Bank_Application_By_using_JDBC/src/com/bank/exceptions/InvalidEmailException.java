package com.bank.exceptions;

public class InvalidEmailException extends RuntimeException{
	@Override
	public String toString() {
		return getClass()+" invalid email id ";
	}
}
