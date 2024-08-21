package com.maan.eway.vehicleupload;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomStepExecutionListener implements StepExecutionListener {
	
	private static ObjectMapper objectMapper = new ObjectMapper();

    private List<HashMap<String,Object>> motor_list;
    
    private List<HashMap<String,Object>> section_list;
    
    private List<HashMap<String,Object>> bodyType_list;
    
    private List<HashMap<String,Object>> banktypes_list;
    
    private List<HashMap<String,Object>> vehicleUsage_list;
    
    private List<HashMap<String,Object>> policyTypes_list;

    private List<HashMap<String,Object>> colorTypes;

   
    
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Retrieve data from JobExecution context and store in StepExecution context
    	String motorcategor_string = (String) stepExecution.getJobExecution().getExecutionContext().get("motorCategory");
    	String section_string = (String) stepExecution.getJobExecution().getExecutionContext().get("section");
    	String banktypes_string = (String) stepExecution.getJobExecution().getExecutionContext().get("banktypes");
    	String bodyType_string = (String) stepExecution.getJobExecution().getExecutionContext().get("bodyTypes");
    	String vehicleUsage_string = (String) stepExecution.getJobExecution().getExecutionContext().get("vehicleUsage");
    	String policyTypes_string = (String) stepExecution.getJobExecution().getExecutionContext().get("policyTypes");
    	String colorTypes_string = (String) stepExecution.getJobExecution().getExecutionContext().get("colorTypes");

    	try {
			List<HashMap<String, Object>> section_list = objectMapper.readValue(
					section_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
			
			List<HashMap<String, Object>> motorCategory_list = objectMapper.readValue(
					motorcategor_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
			
			List<HashMap<String, Object>> banktypes_list = objectMapper.readValue(
					banktypes_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
			
			List<HashMap<String, Object>> bodyTypes_list = objectMapper.readValue(
					bodyType_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
			
			List<HashMap<String, Object>> vehicleUsage_list = objectMapper.readValue(
					vehicleUsage_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
			
			List<HashMap<String, Object>> policyTypes_list = objectMapper.readValue(
					policyTypes_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
			
			List<HashMap<String, Object>> colorTypes_list = objectMapper.readValue(
					colorTypes_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
			
			this.section_list=section_list.stream().distinct().collect(Collectors.toList());
			this.motor_list=motorCategory_list.stream().distinct().collect(Collectors.toList());
			this.banktypes_list=banktypes_list.stream().distinct().collect(Collectors.toList());
			this.bodyType_list=bodyTypes_list.stream().distinct().collect(Collectors.toList());
			this.policyTypes_list=policyTypes_list.stream().distinct().collect(Collectors.toList());
			this.vehicleUsage_list=vehicleUsage_list.stream().distinct().collect(Collectors.toList());
			this.colorTypes=colorTypes_list.stream().distinct().collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
		} 

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

	public List<HashMap<String, Object>> getMotor_list() {
		return motor_list;
	}

	public void setMotor_list(List<HashMap<String, Object>> motor_list) {
		this.motor_list = motor_list;
	}

	public List<HashMap<String, Object>> getSection_list() {
		return section_list;
	}

	public void setSection_list(List<HashMap<String, Object>> section_list) {
		this.section_list = section_list;
	}

	public List<HashMap<String, Object>> getBodyType_list() {
		return bodyType_list;
	}

	public void setBodyType_list(List<HashMap<String, Object>> bodyType_list) {
		this.bodyType_list = bodyType_list;
	}

	public List<HashMap<String, Object>> getBanktypes_list() {
		return banktypes_list;
	}

	public void setBanktypes_list(List<HashMap<String, Object>> banktypes_list) {
		this.banktypes_list = banktypes_list;
	}

	public List<HashMap<String, Object>> getVehicleUsage_list() {
		return vehicleUsage_list;
	}

	public void setVehicleUsage_list(List<HashMap<String, Object>> vehicleUsage_list) {
		this.vehicleUsage_list = vehicleUsage_list;
	}

	public List<HashMap<String, Object>> getPolicyTypes_list() {
		return policyTypes_list;
	}

	public void setPolicyTypes_list(List<HashMap<String, Object>> policyTypes_list) {
		this.policyTypes_list = policyTypes_list;
	}


	public List<HashMap<String, Object>> getColorTypes() {
		return colorTypes;
	}

	public void setColorTypes(List<HashMap<String, Object>> colorTypes) {
		this.colorTypes = colorTypes;
	}

	
  
    
   
}