package com.ebj.paymentgate.t2020April.dao;

import java.util.Optional;

import org.springframework.lang.Nullable;


public interface MemberDao {

	/** 회원여부 체크 */
	@Nullable	
	public String MemberCheck(String mbrId);
	
}
