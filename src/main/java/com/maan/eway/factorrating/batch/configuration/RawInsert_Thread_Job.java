package com.maan.eway.factorrating.batch.configuration;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import com.google.gson.Gson;
import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;
import com.maan.eway.fileupload.FileUploadInputRequest;

public class RawInsert_Thread_Job  implements Runnable{
	
	Logger log = LogManager.getLogger(RawInsert_Thread_Job.class);
	
	private FileUploadInputRequest fileUploadInputRequest;

	private JobLauncher jobLauncher;
	
	private Job rawdataInsertBatchJob;
	
	private Long totalRows;
	
	private String factor_id;
	
	public RawInsert_Thread_Job(FileUploadInputRequest res, JobLauncher jobLauncher, Job rawdataInsertBatchJob, Long totalRows, String factor_id) {
		this.fileUploadInputRequest =res;
		this.jobLauncher =jobLauncher;
		this.rawdataInsertBatchJob =rawdataInsertBatchJob;
		this.totalRows=totalRows;
		this.factor_id=factor_id;
	}
	
	Gson print = new Gson();

	@Override
	public void run() {
		try {
			log.info("RawInsert Thread has started at "+new Date()+" for "+fileUploadInputRequest.getTranId()+"");
			
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong("time", System.currentTimeMillis())
			     	.addString("ewayBatchData", print.toJson(fileUploadInputRequest))
			     	.addLong("totalRecords", totalRows)
			     	.addLong("gridSize", 20L)
			     	.addString("factor_id", factor_id)
			        .toJobParameters();
			jobLauncher.run(rawdataInsertBatchJob, jobParameters);
			
			log.info("RawInsert Thread has completed at "+new Date()+" for "+fileUploadInputRequest.getTranId()+"");
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
	}

}
