package com.ebj.paymentgate.t2020April.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 예외처리 handler 
 * 
 * @author junyoung
 *
 */

@ControllerAdvice
@RestController
public class GlobalExceptionHandler { 

	@ResponseStatus(HttpStatus.UNAUTHORIZED)	
	@ExceptionHandler(value = PaymentExceptionUnAuth.class)
	public String handleBaseException(PaymentExceptionUnAuth e)
	{
		return e.getMessage();
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)	
	@ExceptionHandler(value = PaymentExceptionNotFound.class)
	public String handleBaseException(PaymentExceptionNotFound e)
	{
		System.out.println("GlobalExceptionHandler===>");
		return e.getMessage();
	}
		
	
	
	@ExceptionHandler(value = Exception.class)
	public String handleException(Exception e) 
	{
		return e.getMessage();
	}
}
