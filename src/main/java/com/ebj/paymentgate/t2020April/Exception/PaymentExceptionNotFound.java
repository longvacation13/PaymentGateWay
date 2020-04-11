package com.ebj.paymentgate.t2020April.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class PaymentExceptionNotFound extends BaseException {
	private static final long serialVersionUID = 1L; 
	
	public PaymentExceptionNotFound(String msg) {
		super(msg); // 조상인 BaseException의 생성자를 호출
		System.out.println("PaymentExceptionNotFound===>");
	}	

}
 