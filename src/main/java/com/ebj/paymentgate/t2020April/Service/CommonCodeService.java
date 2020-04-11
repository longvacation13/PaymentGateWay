package com.ebj.paymentgate.t2020April.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebj.paymentgate.t2020April.dao.CommonCodeDao;

/**
 * 공통 코드 캐싱 서비스 
 * 자주 사용하는 마스터성 코드정보를 캐시에 올려서 WAS 구동시 1회만 가져옴
 * @author junyoung
 *
 */

@Service
public class CommonCodeService {
  
	@Autowired
	public CommonCodeDao codeDao; 
	
	//static으로 선언하여 공용으록 가져감 
	private static List<HashMap<String, String>> CommPaymentCode = new ArrayList<HashMap<String, String>>();
	
	private static HashSet<String> UsingPmtCodeList = new HashSet<String>();
	
	
	/**
	 * 공통코드 메모리 등록
	 * 
	 */	
	@PostConstruct
	public void resetCodeList() throws Exception {
		
		if(CommPaymentCode.isEmpty()) {
			synchronized (CommPaymentCode) {
				if(CommPaymentCode.isEmpty()) {
					
					List<HashMap<String, String>> hashMapList; 
					
					//코드그룹 
					hashMapList = (ArrayList<HashMap<String,String>>)codeDao.selectCodeListGroup();					
					CommPaymentCode.clear();
					CommPaymentCode.addAll(hashMapList); 
				
				}
			}
		}
	}
	
	/**
	 * Code 정보 클리어 
	 * 
	 * @throws Exception
	 */
	public static void clear() throws Exception {
		CommPaymentCode.clear();
	}
	
	
	/**
	 * 공통코드 정보 
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<HashMap<String, String>> getCode() throws Exception {
		return CommPaymentCode; 
	}
	
	
	/**
	 * 특정 컬럼에 대한 값 저장 
	 * 
	 * @return
	 * @throws Exception
	 */
	public static HashSet<String> getPmtCode() throws Exception {
		
		// CommPaymentCode의 PmtCode 값만 Hash에 담음 		
		Iterator<HashMap<String, String>> iterator = CommPaymentCode.iterator();
		String key = ""; 
				
		while(iterator.hasNext())
		{			 
			HashMap<String, String> hashOne = (HashMap<String, String>)iterator.next(); // 1row ( {PARTCNCLYN=Y, PMTCODE=P0001, PMTNAME=신용카드-서버01, PMTTYPE=PT01} ) 			
			Iterator<String> iters = hashOne.keySet().iterator();  // 1 row 안에 key값 집합 (column 명) 
			
			while(iters.hasNext())
			{
				key = iters.next(); 				
				if(key.equals("PMT_CODE"))
				{					
					UsingPmtCodeList.add(hashOne.get(key));
				}
			}			
		}
		
		
		return UsingPmtCodeList;
		
	}
	
}
