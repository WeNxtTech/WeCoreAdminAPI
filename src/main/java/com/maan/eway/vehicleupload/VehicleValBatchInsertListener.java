package com.maan.eway.vehicleupload;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.repository.EserviceMotorDetailsRawRepository;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;
import com.maan.eway.springbatch.TransactionControlDetails;

@Component("veh_val_Listener")
public class VehicleValBatchInsertListener implements JobExecutionListener {
	
	@Autowired
	private FactorRatingBatchServiceImpl service;
	
	Gson print = new Gson();
	
	@Autowired
	private EserviceMotorDetailsRawRepository eserviceMotorDetailsRaw;
	
	@Autowired
	private TransactionControlDetailsRepository transRepo;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		String tranId = jobExecution.getJobParameters().getString("request_ref_no");
		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {					
			   
				findDuplicateValues(tranId);
				service.updateMainDataDetails (tranId, "Validation has completed" ,"","completed","S"); 
			   
			    TransactionControlDetails tcd= transRepo.findByRequestReferenceNo(tranId);
			    Integer valid_records =eserviceMotorDetailsRaw.countByRequestReferenceNoAndStatusNot(tranId, "E");
			    Integer error_records =tcd.getTotalRecords() - valid_records;
			    tcd.setValidRecords(valid_records);
			    tcd.setErrorRecords(error_records);
			    transRepo.save(tcd);
			}
		}catch(Exception e) {
			service.updateBatchTransaction(tranId, "Validation has Failed" ,"Error","Error","E");
			e.printStackTrace();
		}
	}
	
	private void findDuplicateValues(String tranId) {
		try {
			List<EserviceMotorDetailsRaw> motor_list = eserviceMotorDetailsRaw.findByRequestReferenceNo(tranId);
			Map<String,List<EserviceMotorDetailsRaw>> group_list =motor_list.stream().collect(Collectors.groupingBy(p -> p.getSearchByData().toUpperCase()));
			List<EserviceMotorDetailsRaw> duplicate_data = group_list.entrySet().stream()
					.filter(p -> p.getValue().size()>1).map(p -> p.getValue())
					.flatMap( f -> f.stream())
					.collect(Collectors.toList());
			
			if(duplicate_data.size()>0) {
				duplicate_data.parallelStream().forEach(p ->{
					String error_desc ="~Duplicate Registration Number has Found";
					p.setErrorDesc(StringUtils.isBlank(p.getErrorDesc())?error_desc:p.getErrorDesc()+error_desc);
					eserviceMotorDetailsRaw.save(p);
				
				});
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void beforeJob(JobExecution jobExecution) {
		String companyId = jobExecution.getJobParameters().getString("company_id");
		String productId = jobExecution.getJobParameters().getString("productId");

		List<Map<String,String>> motorCategory =eserviceMotorDetailsRaw.getMotorCategory(companyId);
		List<Map<String,String>> bodyTypes =eserviceMotorDetailsRaw.getBodyTypes(companyId);
		List<Map<String,String>> banktypes =eserviceMotorDetailsRaw.getBanktypes(companyId);
		List<Map<String,String>> color_types =eserviceMotorDetailsRaw.getColorTypes(companyId);

		if(!"100040".equals(companyId)) {
			List<Map<String,String>> section =eserviceMotorDetailsRaw.getSections(companyId,productId);
			List<Map<String,String>> vehicleUsage =eserviceMotorDetailsRaw.getVehicleUsage(companyId);
			List<Map<String,String>> policyTypes =eserviceMotorDetailsRaw.getPolicyTypes(companyId,productId);
	
			
	        jobExecution.getExecutionContext().put("colorTypes", print.toJson(color_types));
	        jobExecution.getExecutionContext().put("motorCategory", print.toJson(motorCategory));
			jobExecution.getExecutionContext().put("section",  print.toJson(section));
			jobExecution.getExecutionContext().put("bodyTypes", print.toJson(bodyTypes));
			jobExecution.getExecutionContext().put("banktypes",  print.toJson(banktypes));
			jobExecution.getExecutionContext().put("vehicleUsage",  print.toJson(vehicleUsage));
			jobExecution.getExecutionContext().put("policyTypes", print.toJson(policyTypes));
			
		}else if("100040".equals(companyId)) {
			
			List<Map<String,String>> insurance_class =eserviceMotorDetailsRaw.getSanlamInsuranceTypeClass(companyId);
			List<Map<String,String>> deductibles =eserviceMotorDetailsRaw.getSanlamDeductibles(companyId);
			List<Map<String,String>> aggregated_value =eserviceMotorDetailsRaw.getSanlamAggregatorValue(companyId);
			List<Map<String,String>> municipal_traffic =eserviceMotorDetailsRaw.getSanlamMuniTraff(companyId);
			List<Map<String,String>> insurance_type =eserviceMotorDetailsRaw.getVehicleUsage(companyId);
			List<Map<String,String>> vehicleValue =eserviceMotorDetailsRaw.getSanlamVehValue(companyId);
			List<Map<String,String>> make =eserviceMotorDetailsRaw.getMakeList(companyId);
			List<Map<String,String>> model =eserviceMotorDetailsRaw.getModelList(companyId);
			
			
	        jobExecution.getExecutionContext().put("insurance_class", print.toJson(insurance_class));
	        jobExecution.getExecutionContext().put("deductibles", print.toJson(deductibles));
			jobExecution.getExecutionContext().put("aggregated_value",  print.toJson(aggregated_value));
			jobExecution.getExecutionContext().put("municipal_traffic", print.toJson(municipal_traffic));
			jobExecution.getExecutionContext().put("insurance_type",  print.toJson(insurance_type));
			jobExecution.getExecutionContext().put("vehicleValue",  print.toJson(vehicleValue));
			jobExecution.getExecutionContext().put("make",  print.toJson(make));
			jobExecution.getExecutionContext().put("model",  print.toJson(model));
	
		}
		
		    jobExecution.getExecutionContext().put("colorTypes", print.toJson(color_types));
	        jobExecution.getExecutionContext().put("motorCategory", print.toJson(motorCategory));
			jobExecution.getExecutionContext().put("bodyTypes", print.toJson(bodyTypes));
			jobExecution.getExecutionContext().put("banktypes",  print.toJson(banktypes));
		    jobExecution.getExecutionContext().put("companyId", companyId);

	}

}