package com.ebj.paymentgate.t2020April.dto;

/** 
 * 결제내역 조회 parameter Dto
 * 
 * */
public class PaymentListDto {
	
	private String mbrId;
	private String succYn;
	private Integer size;
	
	public String getMbrId() {
		return mbrId;
	} 
	public void setMbrId(String mbrId) {
		this.mbrId = mbrId;
	}
	public String getSuccYn() {
		return succYn;
	}
	public void setSuccYn(String succYn) {
		this.succYn = succYn;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	} 
	
	
}
