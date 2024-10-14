package com.maan.eway.factorrating.batch.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.master.service.impl.FactorRateMasterServiceImpl;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.FactorRateRawMasterRepository;

@Component("groupingJobListener")
public class GroupingJobListener implements JobExecutionListener {
	
	Logger log = LogManager.getLogger(getClass());

	
	@Autowired
	private FactorRatingBatchServiceImpl service;
	
	@Autowired
	private FactorRateMasterServiceImpl rateMasterServiceImpl;
	
	@Autowired
	private FactorRateRawMasterRepository rawMasterRepository;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		String tranId = jobExecution.getJobParameters().getString("factor_id");
		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			
				service.updateBatchTransaction (tranId, "RawData Validation Updated" ,"","Completed","S");
			 
			}else {
				
				service.updateBatchTransaction (tranId, "spring batch process failed" ,"","Failed","E");

			}

		}catch(Exception e) {
			service.updateBatchTransaction(tranId, e.getMessage() ,"Error","Error","E");
			e.printStackTrace();}
	}
	
	public void beforeJob(JobExecution jobExecution) {
		Map<String,List<DropDownRes>> dropDownList = new HashMap<String,List<DropDownRes>>();
		String tranId =jobExecution.getJobParameters().getString("factor_id");
		String token =jobExecution.getJobParameters().getString("token");

		FactorRateRawInsert frri =rawMasterRepository.findByTranIdAndSno(tranId,1);
		FactorRateSaveReq factorRateSaveReq = new FactorRateSaveReq();
		factorRateSaveReq.setAgencyCode(frri.getAgencyCode());
		factorRateSaveReq.setBranchCode(frri.getBranchCode());
		factorRateSaveReq.setCompanyId(frri.getCompanyId());
		factorRateSaveReq.setCoverId(frri.getCoverId().toString());
		factorRateSaveReq.setSectionId(frri.getSectionId().toString());
		factorRateSaveReq.setProductId(frri.getProductId().toString());
		factorRateSaveReq.setFactorTypeId(frri.getFactorTypeId().toString());
		
		dropDownList= rateMasterServiceImpl.masterDiscreteApiCall(factorRateSaveReq,token.replaceAll("Bearer ", "").split(",")[0]);						
		
		jobExecution.getExecutionContext().put("dropdown_data", new Gson().toJson(dropDownList));
		log.info("VALIDATION HAS BEEN STARTED FOR THIS FACTOR_ID "+tranId+"");
	}

}

	