package com.maan.eway.nonmotor.upload;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.batch.req.EwayBatchReq;
import com.maan.eway.batch.res.EwayUploadRes;
import com.maan.eway.springbatch.TransactionControlDetails;

@Component
public class NonMotJobExecutionListener implements JobExecutionListener{

	@Autowired
	private TransactionControlDetailsRepository tcdRepository;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		try {
			EwayUploadRes uploadResponse = new EwayUploadRes();
			EwayBatchReq request= new EwayBatchReq();
			
			String ewayBatchReq = jobExecution.getJobParameters().getString("EwayBatchReq");
			
			ObjectMapper mapper = new ObjectMapper();
			request = mapper.readValue(ewayBatchReq, EwayBatchReq.class);
			uploadResponse=request.getEwayUploadRes();
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
				//service.validateRawTableRecords(uploadResponse)	;	
				//service.fileUploadProgress(uploadResponse,"Y","Completed","Raw table Insert Batch completed","50");
				TransactionControlDetails tcd =tcdRepository.findByRequestReferenceNo(uploadResponse.getRequestReferenceNo());
				tcd.setStatus("S");
				tcd.setProgressDescription("COMPLETED.");
				tcdRepository.save(tcd);
			}else {
				TransactionControlDetails tcd =tcdRepository.findByRequestReferenceNo(uploadResponse.getRequestReferenceNo());
				tcd.setStatus("E");
				tcd.setProgressDescription("FAILED.");
				tcdRepository.save(tcd);
			}
		}catch(Exception e) {e.printStackTrace();}
	}
	
}
