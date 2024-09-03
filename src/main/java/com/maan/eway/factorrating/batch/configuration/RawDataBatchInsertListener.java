package com.maan.eway.factorrating.batch.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;

@Component("rawDataBatchInsertListener")
public class RawDataBatchInsertListener implements JobExecutionListener  {
	
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	private FactorRatingBatchServiceImpl service;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		String tranId =jobExecution.getJobParameters().getString("factor_id");
		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
				
				service.updateBatchTransaction (tranId, "RawData Validation Updated" ,"","Completed","S");
			   
			}else {
				
				service.updateBatchTransaction (tranId, "RawData Validation Failed" ,"","Completed","E");

				//service.updateBatchTransaction (tranId, "Raw Data insert failed" ,"","Failed","E");

			}

		}catch(Exception e) {
			service.updateRawDataRecords(tranId, e.getMessage() ,"Error","Error","E");
			e.printStackTrace();}
	}
	
	public void beforeJob(JobExecution jobExecution) {
		String tranId =jobExecution.getJobParameters().getString("factor_id");
		log.info("RAW TABLE INSERT HAS BEEN STARTED FOR THIS FACTOR_ID "+tranId+"");
	}

	

}
