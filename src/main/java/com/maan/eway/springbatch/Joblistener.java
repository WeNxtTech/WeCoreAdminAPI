package com.maan.eway.springbatch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Qualifier(value="FactorListener")
public class Joblistener extends JobExecutionListenerSupport implements ChunkListener {

	@Autowired
	private UtilityServiceImpl service;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		String tranId ="";
		Boolean status=false;
		String data = jobExecution.getJobParameters().getString("ewayBatchData");
		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
				SpringBatchMapperResponse factorData =new SpringBatchMapperResponse();
			    ObjectMapper mapper = new ObjectMapper();
			    try {
			    	factorData = mapper.readValue(data, SpringBatchMapperResponse.class);
			    	tranId =factorData.getTranId();
			    	service.updateMasterValidation(factorData);
			    	service.updateFactorRawRecordByTranId(tranId,factorData.getDiscreteColumn(),factorData.getAuthorization());
				} catch (Exception e) {
					service.updateBatchTransaction (tranId, e.getMessage() ,"","Failed","E");
					e.printStackTrace();
				} 
			   
			    service.updateBatchTransaction (tranId, "spring batch process completed " ,"","completed","S");
			   
			}else {
				
				service.updateBatchTransaction (tranId, "spring batch process failed" ,"","Failed","E");

			}

		}catch(Exception e) {
			service.updateBatchTransaction(tranId, e.getMessage() ,"Error","Error","E");
			e.printStackTrace();}
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		
	}

	@Override
	public void afterChunk(ChunkContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterChunkError(ChunkContext context) {
		// TODO Auto-generated method stub
		
	}

	
	
}
