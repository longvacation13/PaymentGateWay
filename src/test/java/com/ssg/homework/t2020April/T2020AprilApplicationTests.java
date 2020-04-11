package com.ssg.homework.t2020April;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ebj.paymentgate.t2020April.Service.CommonCodeService;

//import net.minidev.json.JSONObject;
//import net.minidev.json.parser.JSONParser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class T2020AprilApplicationTests {

	@Autowired
	MockMvc mockMvc;
		
	@Test
	public void mavenTestPhase() throws Exception {			
				
		//회원 ID 3개 취득
		String[] mbrIdArr = {"0000000010","0000000098", "0000000101"};
		
		//ArrayList 선언 ( pmtCode 정보 담기 위함 ) 
		List arrayList = new ArrayList();
		arrayList.addAll(CommonCodeService.getPmtCode()); // 결제 코드를 arraylist에 담음 										
		String testPmtCode = ""; //임의 지정할 결제코드  
		
		 
		 for(int i = 0; i < mbrIdArr.length ; i++)  //회원 3명
		 {			 			 		 								
				List<String> pmtIdListByMember = new ArrayList<String>();			 //회원별 pmtId 담을 변수		
			 
			 
			 for(int j=0; j < 7 ; j++)  			//승인 7회 
			 {				 					
				 	testPmtCode  = (String)arrayList.get((int)(Math.random() * 17)); // 결제코드 17개중 임의로 하나 저장 				 	
					//결제승인 요청 7개
				 	MvcResult mvcRes = this.mockMvc.perform(
				 			post("/api/pg/approve")
				 			.contentType(MediaType.APPLICATION_JSON).content("{\"mbrId\":\""+mbrIdArr[i]+"\", \"pmtCode\" : \""+testPmtCode+"\", \"pmtType\" : \"\", \"pmtAmt\" : 1547000  }"))				 			
				 			.andReturn();
				 			
				 			String content = mvcRes.getResponse().getContentAsString();
				 			JSONParser p = new JSONParser();
				 			JSONObject obj = (JSONObject)p.parse(content);
				 			
				 			//pmtId 정보 	
				 			pmtIdListByMember.add((String)obj.get("pmtId"));
			 }					

							   
				
			 //승인 건 중 취소 2회 부분환불 1회
			 for(int j=0; j < 3 ; j++)  //취소 2회 
			 {	
				 	
				 	if(j==2) //부분환불
				 	{				 		
				 		mockMvc.perform(post("/api/pg/partialCancel")
								   .param("mbrId", mbrIdArr[i]).param("pmtId", pmtIdListByMember.get(j)).param("pmtAmt", "15000"));
				 	}
				 		
				 	else //취소 
				 	{				 	
				 		mockMvc.perform(post("/api/pg/cancel")
								   .param("mbrId", mbrIdArr[i]).param("pmtId", pmtIdListByMember.get(j)).param("pmtAmt", "1547000"));
				 	}
									 
			 }
			 
			 MvcResult mvcResPaymentList = mockMvc.perform(get("/api/pg/getRecentPaymentList?mbrId="+mbrIdArr[i]+"&&succYn=&&size=10"))
					 .andReturn();

			 String contentPaymentList = mvcResPaymentList.getResponse().getContentAsString();
			 			 
			 String[] a = contentPaymentList.split("},"); 
			 
			 int SuccCnt = 0; 
			 int CancelCnt = 0; 			 
			 for(int l = 0; l < a.length ; l++)
			 {
				 String[] b = a[l].split(",");				 
				 String[] c = b[6].split(":"); 
				 c[1] = c[1].replaceAll("\"", "");
				 if(c[1].equals("10")) //승인
				 {
					SuccCnt++;
					System.out.println("승인내역:"+a[l]);
				 }
				 else
				 {
					 CancelCnt++;
					 System.out.println("취소내역:"+a[l]);
				 }
				 
			 }
			 System.out.println("회원ID ["+mbrIdArr[i]+"] 의 승인건수 : "+SuccCnt+" 취소건수 :"+CancelCnt); 		 			 						 			 	 
		 }

		

		
					
		
	}


}
