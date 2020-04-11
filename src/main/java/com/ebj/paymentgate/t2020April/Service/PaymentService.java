package com.ebj.paymentgate.t2020April.Service;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebj.paymentgate.t2020April.Exception.PaymentExceptionNotFound;
import com.ebj.paymentgate.t2020April.Exception.PaymentExceptionUnAuth;
import com.ebj.paymentgate.t2020April.Service.CommonCodeService;
import com.ebj.paymentgate.t2020April.Service.Interface.IPaymentService;
import com.ebj.paymentgate.t2020April.VirtualServer.PaymentVirtualServer;
import com.ebj.paymentgate.t2020April.dao.PaymentDao;
import com.ebj.paymentgate.t2020April.dto.PaymentDto;
import com.ebj.paymentgate.t2020April.dto.PaymentListDto;

/**
 * 결제 서비스 
 * 결제코드 관련 승인/취소/결제 데이터 조회  
 * @author junyoung 
 */

@Service
public class PaymentService implements IPaymentService { 

	/** Dao */
	@Autowired
	private PaymentDao dao;
		
	/** 회원정보 서비스  */
	@Autowired
	private MemberService _Member;	
	
	/** 가상 승인 서버 (9:1)비율 성공, 실패 return  */
	@Autowired
	private PaymentVirtualServer _VServer; 
	
	/** pmtType 지정 기능 */
	@Autowired
	private PaymentTypeContactService _PaymentTypeService;
	
	/** 비즈니스 로그 저장 용도 */
	final Logger L = LoggerFactory.getLogger(this.getClass());			

	/**
	 * 취소시 필수값 체크 
	 * 
	 * @param pdto
	 * @throws Exception
	 */
	private void EssentialParamCheckCancel(PaymentDto pdto) throws Exception
	{
		
		if((pdto.getMbrId().isEmpty() || pdto.getMbrId() == null) || (pdto.getPmtId().isEmpty() || pdto.getPmtId() == null) || (pdto.getPmtAmt() == null))
		{			
			L.warn("[필수값 없음]  mbrId:"+pdto.getMbrId()+"/pmtId:"+pdto.getPmtId()+"/pmtAmt:"+pdto.getPmtAmt());
			throw new PaymentExceptionUnAuth("필수값 없음");			 
		}
	}
	
	/**
	 * 승인시 필수값 체크 
	 * 
	 * @param pdto
	 * @throws Exception
	 */
	private void EssentialParamCheckApprove(PaymentDto pdto) throws Exception
	{		
		if((pdto.getMbrId().isEmpty() || pdto.getMbrId() == null) || (pdto.getPmtCode() == null) || (pdto.getPmtAmt() == null))
		{			
			L.warn("[필수값 없음]  mbrId:"+pdto.getMbrId()+"/pmtCode:"+pdto.getPmtCode()+"/pmtAmt:"+pdto.getPmtAmt());
			throw new PaymentExceptionUnAuth("필수값 없음");			
		}
	}		
	
	
	/**
	 * 결제코드 승인(Approve) 처리 
	 * PaymentVirtualServer를 이용해서 승인처리 진행함 
	 * @return
	 */
	public void PaymentProcess(PaymentDto pdto) throws Exception {																	
		
		//1. 필수값 체크 
		EssentialParamCheckApprove(pdto);
		
		//2. 회원 여부 체크 
		String mbrChk = _Member.MemberCheck(pdto.getMbrId());		
		if(mbrChk == "N") //없는 회원
		{
			//진행하지 않고 종료 			
			throw new PaymentExceptionNotFound("회원 정보 없음");
		}	
		
				
		//3. Hashset으로 허용된 결제코드인지 찾기 ( true이면 신규추가되는 코드이기 때문에 에러 )
		boolean usingPaymentCode = CommonCodeService.getPmtCode().add(pdto.getPmtCode()); 		
		if(usingPaymentCode) //true일 경우 caching에 올라간 코드에 없는 새로운 코드 = 존재하지 않는 결제코드 
		{
			//허용되지 않은 결제코드 
			throw new PaymentExceptionNotFound("매칭되는 결제코드 없음.");
		}		
		
				
		//4. pmtType 체크  > 없을 경우 PmtType 임의 지정
		String pmtType = ""; // 결제타입 
		if(pdto.getPmtType().isEmpty() || pdto.getPmtType() == null)
		{
			pmtType = _PaymentTypeService.SelectPaymentType(pdto.getPmtCode(), pdto.getPmtType());
			pdto.setPmtType(pmtType);
		}
					
		//5. 가상서버 통신 (성공/실패 결과 값만 넘겨줌)  
		boolean tranRet = _VServer.PaymentAuthTransaction(pdto);												
		if(tranRet)
		{
			//성공 반환받음 			
			pdto.setSuccYn("Y");         //성공여부
			pdto.setSuccMsg("Success");  //성공메시지
			pdto.setAprvType("10"); 		 //승인(approve) 
			pdto.setAprvTime(new Timestamp(System.currentTimeMillis()));
			dao.paymentProcessComplete(pdto); 				
			
			//로그 정보 저장
			L.info("[Success Info] AprvType:"+pdto.getAprvType()+"/ PmtId:"+pdto.getPmtId());
			
		}
		else
		{
			//실패 받환받음 
			pdto.setSuccYn("N");
			pdto.setSuccMsg("Fail");
			pdto.setAprvType("10"); 		 //승인(approve) 
			pdto.setAprvTime(new Timestamp(System.currentTimeMillis()));
			dao.paymentProcessComplete(pdto);
			
			//로그 정보 저장
			L.info("[Fail Info] AprvType:"+pdto.getAprvType()+"/ PmtId:"+pdto.getPmtId());
			
		}									
	}
	
	
	/**
	 * 취소처리 (Cancel, Partial) 
	 * cancelType : C - 전체취소 / P - 부분취소
	 * return -2 : 없는 회원 / -1 : 승인이력 없음
	 */
	public void PaymentCancel(String cancelType, PaymentDto pdto) throws Exception
	{		
		String pmtType = ""; 
		
		boolean tranRet = false;
		//1. 회원 여부 체크 
		String mbrChk = _Member.MemberCheck(pdto.getMbrId());		
		if(mbrChk == "N") //없는 회원
		{
			//진행하지 않고 종료 			 
			throw new PaymentExceptionNotFound("회원 정보 없음");
		}	
		
		//2. 필수값 체크 
		EssentialParamCheckCancel(pdto);
		
		//3. 승인 이력 체크
		PaymentDto PaymentInfo = dao.PaymentInfoCheck(pdto);
		if(PaymentInfo == null)
		{
			//승인이력 없음 
			throw new PaymentExceptionNotFound("승인 결제ID 내역이 없음.");
		}
		else
		{
			//pmtType 임의 지정
			pmtType = _PaymentTypeService.SelectPaymentType(PaymentInfo.getPmtCode(), "");
			pdto.setPmtType(pmtType);
			pdto.setPmtCode(PaymentInfo.getPmtCode());
		}
											
		//4. 금액 검증 ( 해당되는 건의 sum으로 결과 나와야함 )   
		PaymentDto CancelData = dao.CancelInfo(pdto);					
		if(CancelData == null) //결제이력이 없음 
		{
			//에러 처리
			L.warn("[취소처리 에러 - 승인 결제ID 내역이 없음] mbrId:"+pdto.getMbrId()+"/pmtId:"+pdto.getPmtId());
			throw new PaymentExceptionNotFound("승인 결제ID 내역이 없음");	
		}
		else //결제이력이 있음 
		{			
			
			// 요청받은 금액이 환불가능 금액보다 클경우 에러 
			if((pdto.getPmtAmt() > CancelData.getPmtAmt()))
			{
				//금액 오류 에러 			
				L.warn("[금액오류 - 결제금액 오류.] mbrId:"+pdto.getMbrId()+"/pmtId:"+pdto.getPmtId());
				throw new PaymentExceptionUnAuth("결제금액 오류.");	
			}
			//2. 금액이 동일하고, C(전액) or P(부분) > 환불처리
			else if((cancelType.equals("C") || cancelType.equals("P")) && (pdto.getPmtAmt().equals(CancelData.getPmtAmt())))
			{
				//전액 환불				
				tranRet = _VServer.PaymentAuthTransaction(pdto);				
				
				if(tranRet) //환불처리 성공
				{
					pdto.setSuccYn("Y");         //성공여부
					pdto.setSuccMsg(PaymentInfo.getPmtId());  //origin pmtId
					pdto.setAprvType("20"); 	 //취소(cancel) 
					pdto.setAprvTime(new Timestamp(System.currentTimeMillis()));						
					dao.paymentProcessComplete(pdto); 	
					//로그 정보 저장
					L.info("[Success Info] AprvType:"+pdto.getAprvType()+"/ PmtId:"+pdto.getPmtId());
				}
				else 		//환불처리 실패 
				{
					pdto.setSuccYn("N");         //성공여부
					pdto.setSuccMsg(PaymentInfo.getPmtId());  //origin pmtId
					pdto.setAprvType("20"); 	 //취소(cancel) 
					pdto.setAprvTime(new Timestamp(System.currentTimeMillis()));
					dao.paymentProcessComplete(pdto);					
					//로그 정보 저장
					L.info("[Fail Info] AprvType:"+pdto.getAprvType()+"/ PmtId:"+pdto.getPmtId());
				}
					
			}
			else if(cancelType.equals("P") && (pdto.getPmtAmt() <= CancelData.getPmtAmt()))
			{
				//금액이 다름, P 부분환불
				tranRet = _VServer.PaymentAuthTransaction(pdto);
				if(tranRet) //환불처리 성공
				{
					pdto.setSuccYn("Y");         //성공여부
					pdto.setSuccMsg(PaymentInfo.getPmtId());  //origin pmtId
					pdto.setAprvType("20"); 	 //취소(cancel) 
					pdto.setAprvTime(new Timestamp(System.currentTimeMillis()));
					dao.paymentProcessComplete(pdto);	
					//로그 정보 저장
					L.info("[Success Info] AprvType:"+pdto.getAprvType()+"/ PmtId:"+pdto.getPmtId());
				}
				else 		//환불처리 실패 
				{
					pdto.setSuccYn("N");         //성공여부
					pdto.setSuccMsg(PaymentInfo.getPmtId());  //origin pmtId
					pdto.setAprvType("20"); 	 //취소(cancel) 
					pdto.setAprvTime(new Timestamp(System.currentTimeMillis()));
					dao.paymentProcessComplete(pdto);	
					//로그 정보 저장
					L.info("[Fail Info] AprvType:"+pdto.getAprvType()+"/ PmtId:"+pdto.getPmtId());
				}				
			}
			else
			{
				//알수 없는 에러 
				throw new Exception("알수없는 에러");
			}						
		}
											
	}
	
	
	/**
	 * 결제내역 조회 (GetRecentPayment)
	 * 
	 * @param mbrId
	 * @param succYn
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<PaymentDto> GetRecentPaymentList(String mbrId, String succYn, Integer size) throws Exception
	{
		
		//1. 필수값 체크 
		if((mbrId == null) || mbrId.isEmpty())
		{
			throw new PaymentExceptionNotFound("등록된 회원ID 없음");
		}
		
		//2. 회원 여부 체크 
		String mbrChk = _Member.MemberCheck(mbrId);	
		if(mbrChk == "N") //없는 회원
		{
			//진행하지 않고 종료 			
			throw new PaymentExceptionNotFound("회원 정보 없음");
		}	
		
		// 결제내역 조회 param
		PaymentListDto pldto = new PaymentListDto();			
		pldto.setMbrId(mbrId);
		pldto.setSuccYn(succYn);
		pldto.setSize(size);
		
		// 3. 결제내역 조회 
		List<PaymentDto> list =  dao.selectRecentPaymentList(pldto);
		
		return list;
	}
	
	

}
