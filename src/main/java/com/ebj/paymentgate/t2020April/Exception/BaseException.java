package com.ebj.paymentgate.t2020April.Exception;

public class BaseException extends RuntimeException {

	public BaseException(String message) {		
		super(message);
		System.out.println("Base Exception===>");
	}
}
 