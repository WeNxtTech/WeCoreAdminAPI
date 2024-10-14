package com.maan.eway.factorrating.batch.configuration;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import com.google.gson.Gson;
import com.maan.eway.res.DropDownRes;

public class Grouping_Thread_Job implements Runnable {

	Logger log = LogManager.getLogger(MainInsert_Thread_Job.class);	
	private String tran_id;
	private String discreate_columns;
	private Job groupingJob;
	private JobLauncher jobLauncher;
	private String isDiscreate;
	private Long total_records;
	private String token;
	private String rageColumns;
	private String minimumRateYn;
	
	public Grouping_Thread_Job(String tran_id, String discreate_columns, String isDiscreate, Job groupingJob, JobLauncher jobLauncher,
			Long total_records,String token,String rageColumns,String minimumRateYn) {
		this.tran_id=tran_id;
		this.discreate_columns=discreate_columns;
		this.groupingJob=groupingJob;
		this.jobLauncher=jobLauncher;
		this.isDiscreate=isDiscreate;
		this.total_records=total_records;
		this.token=token;
		this.rageColumns=rageColumns;
		this.minimumRateYn = minimumRateYn;
	}

	@Override
	public void run() {
		log.info("Grouping_Thread_Job start at "+new Date()+" : FactorId = "+tran_id+"");
		
		try {
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong("time", System.currentTimeMillis())
					.addString("factor_id", tran_id)
					.addString("discreate_columns", discreate_columns)
					.addString("isDiscreate", isDiscreate)
					.addString("rage_columns", rageColumns)
					.addString("token", token)
					.addLong("total_records", total_records)
					.addString("", minimumRateYn)
					.toJobParameters();
			jobLauncher.run(groupingJob, jobParameters);
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		
		log.info("Grouping_Thread_Job end at "+new Date()+" : FactorId = "+tran_id+"");

		
	}

}
