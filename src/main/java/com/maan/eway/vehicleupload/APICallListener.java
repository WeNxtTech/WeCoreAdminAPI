package com.maan.eway.vehicleupload;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;

@Component("veh_apicall_listener")
public class APICallListener implements JobExecutionListener {
	
	@Autowired
	private FactorRatingBatchServiceImpl service;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		String tranId = jobExecution.getJobParameters().getString("factor_id");
		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {	
				
			    service.updateMainDataDetails (tranId, "MainData insert has completed" ,"","completed","S"); 
			}else {		
				service.updateMainDataDetails (tranId, "MainData insert has failed" ,"","Failed","E");
			}

		}catch(Exception e) {
			service.updateBatchTransaction(tranId, e.getMessage() ,"Error","Error","E");
			e.printStackTrace();}
	}

}