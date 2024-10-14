package com.maan.eway.factorrating.batch.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.res.DropDownRes;

public class FactorStepExecutionListener implements StepExecutionListener{
	private static ObjectMapper objectMapper = new ObjectMapper();

	private  Map<String,List<DropDownRes>> dropDownList = new HashMap<String, List<DropDownRes>>();
	
	 public void beforeStep(StepExecution stepExecution) {
		 
		String dropdown_data = (String) stepExecution.getJobExecution().getExecutionContext().get("dropdown_data");
		 
		 try {
			 
			 
			 Map<String, List<Map<String, Object>>> dropDownMap =objectMapper.readValue(dropdown_data, Map.class);
				
			 for (Map.Entry<String, List<Map<String, Object>>> entry : dropDownMap.entrySet()) {
		            String category = entry.getKey();
		            List<DropDownRes> dropDownResList = entry.getValue().stream()
		                .map(map -> new DropDownRes(
		                     map.get("code")==null?"":map.get("code").toString(),
		                     map.get("codeDesc")==null?"":map.get("codeDesc").toString(),		                    	 "",
		                     map.get("status")==null?"":map.get("status").toString(),		                    		 ""
		                   	    	          
		                ))
		                .collect(Collectors.toList());
		            dropDownList.put(category, dropDownResList);
			 }
			

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
			
		 
	 }


	public Map<String, List<DropDownRes>> getDropDownList() {
		return dropDownList;
	}


	public void setDropDownList(Map<String, List<DropDownRes>> dropDownList) {
		this.dropDownList = dropDownList;
	}
	 
	 
}
