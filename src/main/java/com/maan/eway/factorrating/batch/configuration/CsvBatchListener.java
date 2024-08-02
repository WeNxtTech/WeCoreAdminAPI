package com.maan.eway.factorrating.batch.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;
import org.springframework.batch.core.JobExecutionListener;


@Component("CsvBatchListener")
public class CsvBatchListener implements JobExecutionListener  {
	
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	private FactorRatingBatchServiceImpl service;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		String tranId = jobExecution.getJobParameters().getString("factor_id");
		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {			
			    service.updateCsvRecordDetails (tranId, "CSV Convertion Completed.." ,"","completed","S"); 
			}else {		
				service.updateBatchTransaction (tranId, "CSV Convertion Failed.." ,"","Failed","E");
			}

		}catch(Exception e) {
			service.updateBatchTransaction(tranId, e.getMessage() ,"Error","Error","E");
			e.printStackTrace();}
	}
	
	public void beforeJob(JobExecution jobExecution) {
		String tranId = jobExecution.getJobParameters().getString("factor_id");
		log.info("CSV CONVERTION HAS BEEN STARTED FOR THIS FACTOR_ID FOR THIS FACTOR = "+tranId+" ");
	}

}