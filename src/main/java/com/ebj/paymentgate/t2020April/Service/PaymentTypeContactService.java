package com.ebj.paymentgate.t2020April.Service;

import org.springframework.stereotype.Service;

/** 
 * 통신서버 contact 서비스 
 * 
 */

@Service
public class PaymentTypeContactService {

	/**
	 * 어떤 pmtType 사용할지 결정 
	 * PmtCode에 pmtType 여러개 있을 경우 
	 * @param pmtCode
	 * @return
	 */	
	
	public String SelectPaymentType(String pmtCode, String pmtType)
	{
		String ret = "";   
		
		if(pmtCode.equals("P0001")) //신용카드
		{
			if(pmtType.isEmpty() || (pmtType == null))
			{
				String[] PgType = {"PT01","PT02","PT03"};
				int random = (int)(Math.random() * 3); //배열 0부터 2까지중 임의로 하나 
				ret = PgType[random];
			}
		}
		else if(pmtCode.equals("P0003")) //휴대폰
		{
			if(pmtType.isEmpty() || (pmtType == null))
			{
				String[] PgType = {"PT11","PT12"};
				int random = (int)(Math.random() * 2); //배열 0부터 1까지중 임의로 하나 
				ret = PgType[random]; 
			}			
		}
		//위에 해당하지 않는 결제수단의 경우 빈값 보냄
	
			
		return ret; 
	}
}
