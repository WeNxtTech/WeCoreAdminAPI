package com.maan.eway.thread;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.maan.eway.bean.RatingFieldMaster;
import com.maan.eway.common.res.CommonDropdown;
import com.maan.eway.master.req.MasterApiCallReq;
import com.maan.eway.res.DropDownRes;

public class ThreadDropDownCall implements Callable<Object> {
	
	private MasterApiCallReq request;
	
	public ThreadDropDownCall(MasterApiCallReq request) {
		super();
		this.request = request;
	}

	@Override
	public synchronized Map<String,List<DropDownRes>> call() throws Exception {
		Map<String,List<DropDownRes>>  response = new HashMap<String,List<DropDownRes>>();
		List<DropDownRes> dropDownRes = new ArrayList<DropDownRes>();
		ResponseEntity<CommonDropdown> postForEntity =null;
		try {
			{
				RestTemplate   temp=new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(5)).build();
						
						
				HttpHeaders header=new HttpHeaders();
				header.setContentType(MediaType.APPLICATION_JSON);
				//header.setCharset("UTF-8");
				header.setBearerAuth(request.getTokenl());
				//"$2a$10$uTzL1oMsAD5AIT3RiHfAoeDwoEnfY57x0VMPqzGzgFfhWJUEFbv9G"
				HttpEntity<?> requestent = 
						new HttpEntity<>(this.request.getApiRequest(), header);

				System.out.println( new Date()+" Start "+ request.getApiLink());
				postForEntity = temp.exchange(this.request.getApiLink(),HttpMethod.POST, requestent, new ParameterizedTypeReference<CommonDropdown>() {} );
				System.out.println( new Date()+" End "+ request.getApiLink());

			}

			if(postForEntity.getStatusCode().is2xxSuccessful()) {
				DropDownRes[] commonResponse = postForEntity.getBody().getCommonResponse();
				dropDownRes = Arrays.asList(commonResponse);
				response.put(request.getParam() , dropDownRes);
				
			}			
		}catch (Exception e) {
			e.printStackTrace();					
		}finally {
			
		}
		return response;
	}

}
