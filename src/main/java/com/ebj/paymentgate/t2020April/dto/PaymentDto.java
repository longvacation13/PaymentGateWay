package com.ebj.paymentgate.t2020April.dto;

import java.sql.Timestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "PAYMENT ENTITY DTO")
public class PaymentDto {

	@ApiModelProperty(example = "0000000001", notes = "결제ID")
	private String pmtId;

	@ApiModelProperty(example = "0000000345", notes = "회원ID ( MEMBER.MBR_ID 컬럼 참고 )")
	private String mbrId; 

	@ApiModelProperty(example = "P0001", notes = "결제코드 ( PAYMENT_MST.PMT_CODE 컬럼 참고 )")
	private String pmtCode;

	@ApiModelProperty(example = "PT02", notes = "결제타입 ( PAYMENT_MST.PMT_TYPE 컬럼 참고 )")
	private String pmtType;

	@ApiModelProperty(example = "Y", notes = "성공여부 ( Y/N ) ")
	private String succYn;

	@ApiModelProperty(example = "처리완료되었습니다", notes = "성공메세지")
	private String succMsg;

	@ApiModelProperty(example = "10", notes = "승인타입 ( 10-승인 / 20-취소 )")
	private String aprvType;

	@ApiModelProperty(example = "1580335864850", notes = "승인일시")
	private Timestamp aprvTime;

	@ApiModelProperty(example = "1574785", notes = "결제금액")
	private Long pmtAmt;
    	
	public String getPmtId() {
		return pmtId;
	}

	public void setPmtId(String pmtId) {
		this.pmtId = pmtId;
	}

	public String getMbrId() {
		return mbrId;
	}

	public void setMbrId(String mbrId) {
		this.mbrId = mbrId;
	}

	public String getPmtCode() {
		return pmtCode;
	}

	public void setPmtCode(String pmtCode) {
		this.pmtCode = pmtCode;
	}

	public String getPmtType() {
		return pmtType;
	}

	public void setPmtType(String pmtType) {
		this.pmtType = pmtType;
	}

	public String getSuccYn() {
		return succYn;
	}

	public void setSuccYn(String succYn) {
		this.succYn = succYn;
	}

	public String getSuccMsg() {
		return succMsg;
	}

	public void setSuccMsg(String succMsg) {
		this.succMsg = succMsg;
	}

	public String getAprvType() {
		return aprvType;
	}

	public void setAprvType(String aprvType) {
		this.aprvType = aprvType;
	}

	public Timestamp getAprvTime() {
		return aprvTime;
	}

	public void setAprvTime(Timestamp aprvTime) {
		this.aprvTime = aprvTime;
	}

	public Long getPmtAmt() {		
		return pmtAmt;
	}

	public void setPmtAmt(Long pmtAmt) {		
		this.pmtAmt = pmtAmt;
	}

}
