package com.bank.exceptions;

public class InvalidAmountException extends RuntimeException{
	@Override
	public String toString() {
		return getClass()+" invalid initial amount";
	}
}
