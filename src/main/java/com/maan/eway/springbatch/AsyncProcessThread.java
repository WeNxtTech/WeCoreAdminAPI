package com.maan.eway.springbatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
	
	public void asyncProcess(Map<String,List<FactorRateRawInsert>> loadList,String discreateCol, String auth, Map<String, List<DropDownRes>> dropDownList){
		List<CompletableFuture<String>> completableFutures =new ArrayList<CompletableFuture<String>>();
		try {
			log.info("Calling AsyncProcessThread block");
			
			for(Map.Entry<String,List<FactorRateRawInsert>> factor :loadList.entrySet()) {				 
				CompletableFuture<String> response=factorRateValidation.callValidationApi(factor.getValue(), factor.getKey(), discreateCol,auth,dropDownList);
				completableFutures.add(response);			 
			}					
			@SuppressWarnings("unchecked")
			CompletableFuture<List<String>>[] cfArray = new CompletableFuture[completableFutures.size()];
			cfArray = completableFutures.toArray(cfArray);
			CompletableFuture.allOf(cfArray).join();
			
			log.info("Completed AsyncProcessThread block");

			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

}
