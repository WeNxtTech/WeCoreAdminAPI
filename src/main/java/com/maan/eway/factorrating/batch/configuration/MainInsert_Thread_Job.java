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
import com.maan.eway.bean.ListItemValue;

public class MainInsert_Thread_Job implements Runnable{
	
	Logger log = LogManager.getLogger(MainInsert_Thread_Job.class);

	private Integer amendId ;
	private Map<String, Object> coverDetails;
	private List<ListItemValue> itemValues;
	private Long total_records;
	private String tran_id;
	private Long gridSize;
	private JobLauncher jobLauncher;
	private Job mainDataInsertJob;
	
	private Gson print = new Gson();
	
	public MainInsert_Thread_Job(Integer amendId, Map<String, Object> coverDetails, List<ListItemValue> itemValues,
			Long total_records, String tran_id, Long gridSize, JobLauncher jobLauncher, Job mainDataInsertJob) {
		
		this.amendId=amendId;
		this.coverDetails=coverDetails;
		this.itemValues=itemValues;
		this.total_records=total_records;
		this.tran_id=tran_id;
		this.gridSize=gridSize;
		this.mainDataInsertJob=mainDataInsertJob;
		this.jobLauncher=jobLauncher;
	}

	@Override
	public void run() {
		log.info("MainInsert_Thread_Job start at "+new Date()+" : FactorId = "+tran_id+"");
		
		try {
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong("time", System.currentTimeMillis())
					.addLong("totalRecords", total_records)
					.addString("factor_id", tran_id)
					.addLong("gridSize", gridSize)
					.addString("listItemValue", print.toJson(itemValues))
					.addString("coverDetails", print.toJson(coverDetails))
					.addLong("amendId", (long) amendId)
					.toJobParameters();
			jobLauncher.run(mainDataInsertJob, jobParameters);
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		
		log.info("MainInsert_Thread_Job end at "+new Date()+" : FactorId = "+tran_id+"");

		
	}

}
