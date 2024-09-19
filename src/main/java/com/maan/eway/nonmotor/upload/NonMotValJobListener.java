package com.maan.eway.nonmotor.upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;
import com.maan.eway.batch.repository.EserviceMotorDetailsRawRepository;
import com.maan.eway.batch.repository.EwayEmplyeeDetailRawRepository;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;
import com.maan.eway.springbatch.TransactionControlDetails;

@Component
public class NonMotValJobListener implements JobExecutionListener {
	@Autowired
	private FactorRatingBatchServiceImpl service;
	
	Gson print = new Gson();
	
	@Autowired
	private EwayEmplyeeDetailRawRepository eserviceEmpDetailsRaw;
	
	@Autowired
	private TransactionControlDetailsRepository transRepo;
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		String request_ref_no = jobExecution.getJobParameters().getString("request_ref_no");
		String productId = jobExecution.getJobParameters().getString("productId");

		try {
			if (jobExecution.getStatus() == BatchStatus.COMPLETED) {					
			   
				if("14".equals(productId) || "15".equals(productId) || "32".equals(productId) || "19".equals(productId))
					findDuplicateNationalityId(request_ref_no);
				else if("4".equals(productId))
					findDuplicatePassportNo(request_ref_no);
				else if("59".equals(productId) ||"26".equals(productId) || "24".equals(productId))
					findDuplicateSerialNo(request_ref_no);
				
				
				
				
				service.updateMainDataDetails (request_ref_no, "Validation has completed" ,"","completed","S"); 
			   
			    TransactionControlDetails tcd= transRepo.findByRequestReferenceNo(request_ref_no);
			    Integer valid_records =eserviceEmpDetailsRaw.countByRequestReferenceNoAndStatusNot(request_ref_no, "E");
			    Integer error_records =tcd.getTotalRecords() - valid_records;
			    tcd.setValidRecords(valid_records);
			    tcd.setErrorRecords(error_records);
			    transRepo.save(tcd);
			}
		}catch(Exception e) {
			service.updateBatchTransaction(request_ref_no, "Validation has Failed" ,"Error","Error","E");
			e.printStackTrace();
		}
	}
	
	private void findDuplicateNationalityId(String tranId) {
		try {
			List<EwayEmplyeeDetailRaw> motor_list = eserviceEmpDetailsRaw.findByRequestReferenceNo(tranId);
			Map<String,List<EwayEmplyeeDetailRaw>> group_list =motor_list.stream().collect(Collectors.groupingBy(p -> p.getNationalityId()));
			List<EwayEmplyeeDetailRaw> duplicate_data = group_list.entrySet().stream()
					.filter(p -> p.getValue().size()>1).map(p -> p.getValue())
					.flatMap( f -> f.stream())
					.collect(Collectors.toList());
			
			if(duplicate_data.size()>0) {
				duplicate_data.parallelStream().forEach(p ->{
					String error_desc ="~Duplicate NationalityIds have found";
					p.setErrorDesc(StringUtils.isBlank(p.getErrorDesc())?error_desc:p.getErrorDesc()+error_desc);
					eserviceEmpDetailsRaw.save(p);
				
				});
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void findDuplicatePassportNo(String tranId) {
		try {
			List<EwayEmplyeeDetailRaw> motor_list = eserviceEmpDetailsRaw.findByRequestReferenceNo(tranId);
			Map<String,List<EwayEmplyeeDetailRaw>> group_list =motor_list.stream().collect(Collectors.groupingBy(p -> p.getPassportNo()));
			List<EwayEmplyeeDetailRaw> duplicate_data = group_list.entrySet().stream()
					.filter(p -> p.getValue().size()>1).map(p -> p.getValue())
					.flatMap( f -> f.stream())
					.collect(Collectors.toList());
			
			if(duplicate_data.size()>0) {
				duplicate_data.parallelStream().forEach(p ->{
					String error_desc ="~Duplicate Passport Numbers have found";
					p.setErrorDesc(StringUtils.isBlank(p.getErrorDesc())?error_desc:p.getErrorDesc()+error_desc);
					eserviceEmpDetailsRaw.save(p);
				
				});
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void findDuplicateSerialNo(String tranId) {
		try {
			List<EwayEmplyeeDetailRaw> motor_list = eserviceEmpDetailsRaw.findByRequestReferenceNo(tranId);
			Map<String,List<EwayEmplyeeDetailRaw>> group_list =motor_list.stream().collect(Collectors.groupingBy(p -> p.getSerialNumber()));
			List<EwayEmplyeeDetailRaw> duplicate_data = group_list.entrySet().stream()
					.filter(p -> p.getValue().size()>1).map(p -> p.getValue())
					.flatMap( f -> f.stream())
					.collect(Collectors.toList());
			
			if(duplicate_data.size()>0) {
				duplicate_data.parallelStream().forEach(p ->{
					String error_desc ="~Duplicate serialnumbers have found";
					p.setErrorDesc(StringUtils.isBlank(p.getErrorDesc())?error_desc:p.getErrorDesc()+error_desc);
					eserviceEmpDetailsRaw.save(p);
				
				});
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void beforeJob(JobExecution jobExecution) {
		String companyId = jobExecution.getJobParameters().getString("company_id");
		String productId = jobExecution.getJobParameters().getString("productId");
		String quote_no = jobExecution.getJobParameters().getString("quote_no");
		String section_id = jobExecution.getJobParameters().getString("section_id");
		String request_ref_no = jobExecution.getJobParameters().getString("request_ref_no");

		List<Map<String,String>> occupation =eserviceEmpDetailsRaw.getOccupation(companyId,productId);
		List<Map<String,String>> building =eserviceEmpDetailsRaw.getBuildingDet(request_ref_no);
		List<Map<String,String>> country =eserviceEmpDetailsRaw.getCountry(companyId);		
		List<Map<String,String>> relation =eserviceEmpDetailsRaw.getRelationType(companyId);
		List<Map<String,String>> monthOfYear =eserviceEmpDetailsRaw.getMonthOfYear();

		String content_desc ="3".equals(section_id)?"All Risk":"Content";
		List<Map<String,String>> contentType =eserviceEmpDetailsRaw.getContentType(companyId,content_desc);

		List<Map<String,String>> gender =new ArrayList<>();
		Map<String,String> map = new HashMap<String, String>();
		map.put("MALE", "M");
		map.put("FEMALE", "F");
		gender.add(map);
				
		
		if("14".equals(productId) || "15".equals(productId) || "32".equals(productId) || "19".equals(productId)) {
			
			jobExecution.getExecutionContext().put("occupation", print.toJson(occupation));
		    jobExecution.getExecutionContext().put("building", print.toJson(building));
			jobExecution.getExecutionContext().put("monthOfYear", print.toJson(monthOfYear));

		}else if("4".equals(productId)) {
			jobExecution.getExecutionContext().put("gender", print.toJson(gender));
			jobExecution.getExecutionContext().put("relation",  print.toJson(relation));
			jobExecution.getExecutionContext().put("country", print.toJson(country));

		
		}else if("59".equals(productId) ||"26".equals(productId) || "24".equals(productId)) {
			jobExecution.getExecutionContext().put("contentType",  print.toJson(contentType));
			jobExecution.getExecutionContext().put("relation",  print.toJson(relation));

		}else {
			jobExecution.getExecutionContext().put("occupation", print.toJson(occupation));
		    jobExecution.getExecutionContext().put("building", print.toJson(building));
			jobExecution.getExecutionContext().put("relation",  print.toJson(relation));

		}

		jobExecution.getExecutionContext().put("productId", productId);

	}

}