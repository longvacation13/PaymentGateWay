package com.ebj.paymentgate.t2020April.dao;

import com.ebj.paymentgate.t2020April.dto.PaymentDto;

/**
 * 가상 결제 서버 처리 
 * 
 */

public interface PaymentVirtualServerDao {
	/** 최근 승인 건의 성공건수 조회 */
	public int selectSuccCount(PaymentDto pmtType) throws Exception;
}
