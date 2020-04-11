package com.ebj.paymentgate.t2020April.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebj.paymentgate.t2020April.Exception.PaymentExceptionNotFound;
import com.ebj.paymentgate.t2020April.Service.Interface.IMemberService;
import com.ebj.paymentgate.t2020April.dao.MemberDao;


/**
 * 회원 서비스 
 * 회원 정보 유효성 등 회원 관련 정보 체크 
 * 
 */

@Service
public class MemberService implements IMemberService {
	
	@Autowired
	private MemberDao dao;
	
	/** 비즈니스 로그 저장 용도 */
	final Logger L = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 현재 있는 회원인지 여부 체크  
	 * @return Y : 맞음, N : 아님 
	 */
	public String MemberCheck(String mbrId) throws Exception
	{
		String ret = dao.MemberCheck(mbrId);		 
		if(ret == null)  //회원정보 없음
		{
			ret = "N"; 
			L.warn("[회원정보없음]  mbrId:"+mbrId);
			throw new PaymentExceptionNotFound("회원정보 없음");			
		}
		else
		{
			return ret;
		}
				
	}
}
