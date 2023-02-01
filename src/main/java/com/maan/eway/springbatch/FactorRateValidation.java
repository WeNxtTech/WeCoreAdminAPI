package com.maan.eway.springbatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.FactorParamsInsert;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.master.service.impl.FactorRateMasterServiceImpl;
import com.maan.eway.res.DropDownRes;

@Component
public class FactorRateValidation {
	
	@Autowired
	private UtilityServiceImpl serviceImpl;
	
	public static DozerBeanMapper mapper =new DozerBeanMapper();
	
    private static Gson print  = new Gson();
  
    @Autowired
    private FactorRateMasterServiceImpl obj;
	
   
	@Async("fileUploadExeuter")
	 public CompletableFuture<String>  callValidationApi(List<FactorRateRawInsert> list, String groupId, String discreateColumns, String auth, Map<String, List<DropDownRes>> dropDownList)  {
		    ObjectMapper objMapper = new ObjectMapper();
			try {
				FactorRateSaveReq factorRateSaveReq=mapper.map(list.get(0), FactorRateSaveReq.class);
				List<FactorParmsRequestMapping> parmasMapping = new ArrayList<FactorParmsRequestMapping>();
				try {
				 parmasMapping =list.stream()
						.map(from -> {
							try {
								return objMapper.readValue(print.toJson(from), FactorParmsRequestMapping.class);
							} catch (JsonProcessingException e) {
								e.printStackTrace();
							}
							return null;
						})
				.collect(Collectors.toList());
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				List<FactorParamsInsert> params=parmasMapping.stream()
					.map(d ->mapper.map(d, FactorParamsInsert.class))
						.collect(Collectors.toList());
				
				factorRateSaveReq.setFactorParams(params);
				
				List<Error> errors =obj.factorRatingsValidation(factorRateSaveReq, dropDownList);
				
				serviceImpl.updateFactorRawRecords(factorRateSaveReq,groupId,errors,list.get(0).getTranId(),discreateColumns);
				
				return CompletableFuture.completedFuture("Success");
			}catch (Exception e) {
				 e.printStackTrace();
				
			}
			return CompletableFuture.completedFuture("Failed");

			
		}
	
	
	
	

}
