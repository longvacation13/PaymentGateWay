package com.ebj.paymentgate.t2020April.dao;

import java.util.List;

import com.ebj.paymentgate.t2020April.dto.PaymentDto;
import com.ebj.paymentgate.t2020April.dto.PaymentListDto;

public interface PaymentDao {
			
	/** 최근 승인 건의 성공건수 조회 */
	public int selectSuccCount(PaymentDto pmtType) throws Exception;
	
	/** 최근 취소건의 성공건수 조회 */
	public int selectCancelSuccCount() throws Exception;
	
	/** PaymentId 발번 */
	public int getPaymentId() throws Exception; 
	
	/** 성공 및 실패 처리 */
	public int paymentProcessComplete(PaymentDto pdto) throws Exception;
	
	/** 결제 취소처리 */
	public int PaymentCancel(PaymentDto pdto) throws Exception;
	
	/** 결제 이력 체크 */
	public PaymentDto PaymentInfoCheck (PaymentDto pdto) throws Exception;
	
	/** 환불 이력 체크 */
	public PaymentDto CancelInfo(PaymentDto pdto) throws Exception;
	
	/** 환불 이력 체크 */
	public List<PaymentDto> selectRecentPaymentList(PaymentListDto pldto) throws Exception;
	
		
}
