package com.maan.eway.vehicleupload;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;


@Qualifier("veh_apicall_listener")
public class ApiCallJobListener  implements JobExecutionListener{

	@Autowired
	private FactorRatingBatchServiceImpl service;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		String tranId = jobExecution.getJobParameters().getString("request_ref_no");
		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {	
				
			    service.updateMainDataDetails (tranId, "MainData insert has completed" ,"","completed","S"); 
			    
			}
		}catch(Exception e) {
			service.updateBatchTransaction(tranId, "MainData insert has completed" ,"Error","Error","E");
			e.printStackTrace();}
	}


}
