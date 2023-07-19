package com.maan.eway.springbatch;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.eway.res.DropDownRes;

@Component
public class AsyncProcessThread {
	
	
	@Autowired
	FactorRateValidation factorRateValidation;
 
	Logger log = LogManager.getLogger(AsyncProcessThread.class); 
	
	public void asyncProcess(List<List<FactorRateRawInsert>> data,String discreateCol, String auth, Map<String, List<DropDownRes>> dropDownList){
		try {
			log.info("Calling AsyncProcessThread block : "+data.get(0).get(0).getTranId());
			
			List<CompletableFuture<String>> completableFutures =data.stream().map( p-> {
				CompletableFuture<String> response =factorRateValidation.callValidationApi(p, discreateCol,auth,dropDownList);
				return response;
			}).collect(Collectors.toList());
			
			@SuppressWarnings("unchecked")
			CompletableFuture<List<String>>[] cfArray = new CompletableFuture[completableFutures.size()];
			cfArray = completableFutures.toArray(cfArray);
			CompletableFuture.allOf(cfArray).join();
			
			log.info("Completed AsyncProcessThread block : "+data.get(0).get(0).getTranId());

			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

}
