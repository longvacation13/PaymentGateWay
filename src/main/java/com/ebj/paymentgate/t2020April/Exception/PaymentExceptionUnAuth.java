package com.ebj.paymentgate.t2020April.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class PaymentExceptionUnAuth extends BaseException {
	private static final long serialVersionUID = 1L; 
	
	public PaymentExceptionUnAuth(String msg) {
		super(msg);
	}	

}
 