package com.maan.eway.nonmotor.upload;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(value = "nonmot_validation_step_listener")
public class NonMotStepExecutionListener implements StepExecutionListener{
	
	
	private static ObjectMapper objectMapper = new ObjectMapper();

    private List<HashMap<String,Object>> occupation_list;
    
    private List<HashMap<String,Object>> building_list;
    
    private List<HashMap<String,Object>> country_list;
    
    private List<HashMap<String,Object>> relation_list;
    
    private List<HashMap<String,Object>> contentType_list;
	
    private List<HashMap<String,Object>> gender_list;
    
    private List<HashMap<String,Object>> month_list;


    public void beforeStep(StepExecution stepExecution) {
    	
    	String productId = stepExecution.getJobExecution().getExecutionContext().getString("productId");

    	try {
	    	if("14".equals(productId) || "15".equals(productId) || "32".equals(productId) || "19".equals(productId)) {
	    		String occupation = stepExecution.getJobExecution().getExecutionContext().getString("occupation");
	        	String building = stepExecution.getJobExecution().getExecutionContext().getString("building");
	        	String monthOfYear = stepExecution.getJobExecution().getExecutionContext().getString("monthOfYear");
	
	        	List<HashMap<String, Object>> occupation_list = objectMapper.readValue(
						occupation, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				List<HashMap<String, Object>> building_list = objectMapper.readValue(
						building, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				List<HashMap<String, Object>> month_list = objectMapper.readValue(
						monthOfYear, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
	        	
				
				this.occupation_list=occupation_list.stream().distinct().collect(Collectors.toList());
				this.building_list=building_list.stream().distinct().collect(Collectors.toList());
				this.month_list=month_list.stream().distinct().collect(Collectors.toList());
				
	
			}else if("4".equals(productId)) {
				
		    	String gender = stepExecution.getJobExecution().getExecutionContext().getString("gender");
		    	String relation = stepExecution.getJobExecution().getExecutionContext().getString("relation");
		    	String country = stepExecution.getJobExecution().getExecutionContext().getString("country");
		    	
		    	
		    	List<HashMap<String, Object>> gender_list = objectMapper.readValue(
						gender, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
		    	
		    	List<HashMap<String, Object>> country_list = objectMapper.readValue(
						country, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				List<HashMap<String, Object>> relation_list = objectMapper.readValue(
						relation, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				this.gender_list=gender_list.stream().distinct().collect(Collectors.toList());
				this.country_list=country_list.stream().distinct().collect(Collectors.toList());
				this.relation_list=relation_list.stream().distinct().collect(Collectors.toList());

				
				
			}else if("59".equals(productId) ||"26".equals(productId) || "24".equals(productId)) {
				String contentType = stepExecution.getJobExecution().getExecutionContext().getString("contentType");
		    	String relation = stepExecution.getJobExecution().getExecutionContext().getString("relation");
		    
		    	List<HashMap<String, Object>> relation_list = objectMapper.readValue(
						relation, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
		    	
		    	List<HashMap<String, Object>> contentType_list = objectMapper.readValue(
						contentType, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
		    	
				this.relation_list=relation_list.stream().distinct().collect(Collectors.toList());
				this.contentType_list=contentType_list.stream().distinct().collect(Collectors.toList());

			}else {
				String occupation = stepExecution.getJobExecution().getExecutionContext().getString("occupation");
	        	String building = stepExecution.getJobExecution().getExecutionContext().getString("building");
		    	String relation = stepExecution.getJobExecution().getExecutionContext().getString("relation");
	
		    	
		    	List<HashMap<String, Object>> occupation_list = objectMapper.readValue(
						occupation, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
		    	
		    	List<HashMap<String, Object>> relation_list = objectMapper.readValue(
						relation, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
		    	
		    	List<HashMap<String, Object>> building_list = objectMapper.readValue(
						building, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
		    	
		    	this.occupation_list=occupation_list.stream().distinct().collect(Collectors.toList());
				this.relation_list=relation_list.stream().distinct().collect(Collectors.toList());
				this.building_list=building_list.stream().distinct().collect(Collectors.toList());

			}
    	
		} catch (Exception e) {
			e.printStackTrace();
		} 
    
    				
    }


	public List<HashMap<String, Object>> getOccupation_list() {
		return occupation_list;
	}


	public void setOccupation_list(List<HashMap<String, Object>> occupation_list) {
		this.occupation_list = occupation_list;
	}


	public List<HashMap<String, Object>> getBuilding_list() {
		return building_list;
	}


	public void setBuilding_list(List<HashMap<String, Object>> building_list) {
		this.building_list = building_list;
	}


	public List<HashMap<String, Object>> getCountry_list() {
		return country_list;
	}


	public void setCountry_list(List<HashMap<String, Object>> country_list) {
		this.country_list = country_list;
	}


	public List<HashMap<String, Object>> getRelation_list() {
		return relation_list;
	}


	public void setRelation_list(List<HashMap<String, Object>> relation_list) {
		this.relation_list = relation_list;
	}


	public List<HashMap<String, Object>> getContentType_list() {
		return contentType_list;
	}


	public void setContentType_list(List<HashMap<String, Object>> contentType_list) {
		this.contentType_list = contentType_list;
	}


	public List<HashMap<String, Object>> getGender_list() {
		return gender_list;
	}


	public void setGender_list(List<HashMap<String, Object>> gender_list) {
		this.gender_list = gender_list;
	}


	public List<HashMap<String, Object>> getMonth_list() {
		return month_list;
	}


	public void setMonth_list(List<HashMap<String, Object>> month_list) {
		this.month_list = month_list;
	}
    
    
    
}

