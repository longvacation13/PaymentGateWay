package com.ebj.paymentgate.t2020April.Service.Interface;

import java.util.List;

import com.ebj.paymentgate.t2020April.dto.PaymentDto;

public interface IPaymentService {
	
	/** 결제코드 승인(Approve) 처리  */
	void PaymentProcess(PaymentDto pdto) throws Exception;
	
	/** 취소처리 (Cancel, Partial)  */
	void PaymentCancel(String cancelType, PaymentDto pdto) throws Exception;
	
	/** 결제내역 조회 (GetRecentPayment) */
	List<PaymentDto> GetRecentPaymentList(String mbrId, String succYn, Integer size) throws Exception;
	
}
