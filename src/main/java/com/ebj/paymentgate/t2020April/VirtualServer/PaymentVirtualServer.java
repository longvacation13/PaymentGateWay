package com.ebj.paymentgate.t2020April.VirtualServer;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebj.paymentgate.t2020April.dao.PaymentDao;
import com.ebj.paymentgate.t2020April.dao.PaymentVirtualServerDao;
import com.ebj.paymentgate.t2020April.dto.PaymentDto;

/**
 * 결제서버 가상 클래스
 * 역할 : 9:1의  비율로 성공, 실패처리함 
 * @author junyoung
 */

@Service
public class PaymentVirtualServer {
	
	@Autowired
	PaymentDao dao;
	
	
	public Boolean PaymentAuthTransaction(PaymentDto pdto) throws Exception
	{
		//해당되는 결제코드, 결제방법의 최근 성공 건수 조회
		int SuccCnt = dao.selectSuccCount(pdto); 		
		if(SuccCnt == 9)
		{					
			return false; 			
		}
		else
		{		
			//성공처리		
			return true;
		}		
	}
}
