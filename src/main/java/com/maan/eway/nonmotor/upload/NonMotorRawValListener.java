package com.maan.eway.nonmotor.upload;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;

public class NonMotorRawValListener implements JobExecutionListener{

	@Autowired
	private FactorRatingBatchServiceImpl service;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		String tranId = jobExecution.getJobParameters().getString("request_ref_no");
		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {	
				
			    service.updateMainDataDetails (tranId, "Validation has been updated" ,"","completed","S"); 
			    
			}
		}catch(Exception e) {
			service.updateBatchTransaction(tranId, "Validation has been failed" ,"Error","Error","E");
			e.printStackTrace();}
	}


}