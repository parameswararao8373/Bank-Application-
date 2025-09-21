package com.bank.exceptions;

public class InvalidGenderException extends RuntimeException{
	@Override
	public String toString() {
		return getClass()+" invalid gender ";
	}
}
