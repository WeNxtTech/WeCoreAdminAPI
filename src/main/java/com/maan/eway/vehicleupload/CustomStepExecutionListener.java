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

    private List<HashMap<String,Object>> insurance_class;
    
    private List<HashMap<String,Object>> deductibles;
    
    private List<HashMap<String,Object>> aggregated_value;

    private List<HashMap<String,Object>> municipal_traffic;

    private List<HashMap<String,Object>> insurance_type;
    
    private List<HashMap<String,Object>> vehicleValue;
    
    private List<HashMap<String,Object>> make;
    
    private List<HashMap<String,Object>> model;
    
    
    
    
    public List<HashMap<String, Object>> getMake() {
		return make;
	}

	public void setMake(List<HashMap<String, Object>> make) {
		this.make = make;
	}

	public List<HashMap<String, Object>> getModel() {
		return model;
	}

	public void setModel(List<HashMap<String, Object>> model) {
		this.model = model;
	}

	@Override
    public void beforeStep(StepExecution stepExecution) {
       
    	String companyId = (String) stepExecution.getJobExecution().getExecutionContext().get("companyId");
    	String motorcategor_string = (String) stepExecution.getJobExecution().getExecutionContext().get("motorCategory");
    	String banktypes_string = (String) stepExecution.getJobExecution().getExecutionContext().get("banktypes");
    	String bodyType_string = (String) stepExecution.getJobExecution().getExecutionContext().get("bodyTypes");
    	String colorTypes_string = (String) stepExecution.getJobExecution().getExecutionContext().get("colorTypes");

    	if(!"100040".equals(companyId)) {
    	
	    	// Retrieve data from JobExecution context and store in StepExecution context
	    	String section_string = (String) stepExecution.getJobExecution().getExecutionContext().get("section");
	    	String vehicleUsage_string = (String) stepExecution.getJobExecution().getExecutionContext().get("vehicleUsage");
	    	String policyTypes_string = (String) stepExecution.getJobExecution().getExecutionContext().get("policyTypes");
	
	    	try {
	    		
				List<HashMap<String, Object>> section_list = objectMapper.readValue(
						section_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				
				List<HashMap<String, Object>> vehicleUsage_list = objectMapper.readValue(
						vehicleUsage_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				List<HashMap<String, Object>> policyTypes_list = objectMapper.readValue(
						policyTypes_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				
				this.section_list=section_list.stream().distinct().collect(Collectors.toList());
				this.policyTypes_list=policyTypes_list.stream().distinct().collect(Collectors.toList());
				this.vehicleUsage_list=vehicleUsage_list.stream().distinct().collect(Collectors.toList());
	    	

				} catch (Exception e) {
					e.printStackTrace();
				} 
	    	
    	}else if("100040".equals(companyId)) {
    		
    		// Retrieve data from JobExecution context and store in StepExecution context
	    	String insurance_class_string = (String) stepExecution.getJobExecution().getExecutionContext().get("insurance_class");
	    	String deductibles_string = (String) stepExecution.getJobExecution().getExecutionContext().get("deductibles");
	    	String aggregated_value_string = (String) stepExecution.getJobExecution().getExecutionContext().get("aggregated_value");
	    	String municipal_traffic_string = (String) stepExecution.getJobExecution().getExecutionContext().get("municipal_traffic");
	    	String insurance_type_string = (String) stepExecution.getJobExecution().getExecutionContext().get("insurance_type");
	    	String vehicleValue_string = (String) stepExecution.getJobExecution().getExecutionContext().get("vehicleValue");
	    	String make_string = (String) stepExecution.getJobExecution().getExecutionContext().get("make");
	    	String model_string = (String) stepExecution.getJobExecution().getExecutionContext().get("model");

	    	try {
				List<HashMap<String, Object>> insurance_class = objectMapper.readValue(
						insurance_class_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				
				List<HashMap<String, Object>> deductibles = objectMapper.readValue(
						deductibles_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				List<HashMap<String, Object>> aggregated_value = objectMapper.readValue(
						aggregated_value_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				List<HashMap<String, Object>> municipal_traffic = objectMapper.readValue(
						municipal_traffic_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				
				List<HashMap<String, Object>> insurance_type = objectMapper.readValue(
						insurance_type_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				List<HashMap<String, Object>> vehicleValue = objectMapper.readValue(
						vehicleValue_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				List<HashMap<String, Object>> make_list = objectMapper.readValue(
						make_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				List<HashMap<String, Object>> model_list = objectMapper.readValue(
						model_string, new TypeReference<List<HashMap<String, Object>>>() {}
				    );
				
				
				this.insurance_class=insurance_class.stream().distinct().collect(Collectors.toList());
				this.deductibles=deductibles.stream().distinct().collect(Collectors.toList());
				this.aggregated_value=aggregated_value.stream().distinct().collect(Collectors.toList());
				this.municipal_traffic=municipal_traffic.stream().distinct().collect(Collectors.toList());
				this.insurance_type=insurance_type.stream().distinct().collect(Collectors.toList());
				this.vehicleValue=vehicleValue.stream().distinct().collect(Collectors.toList());
				
				this.make=make_list.stream().distinct().collect(Collectors.toList());
				this.model=model_list.stream().distinct().collect(Collectors.toList());
			
			} catch (Exception e) {
				e.printStackTrace();
			} 
    		
    	}
    	
    	try {
	    	List<HashMap<String, Object>> motorCategory_list = objectMapper.readValue(
					motorcategor_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
	    	
	    	List<HashMap<String, Object>> banktypes_list = objectMapper.readValue(
					banktypes_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
			
			List<HashMap<String, Object>> bodyTypes_list = objectMapper.readValue(
					bodyType_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
	    	
	
			List<HashMap<String, Object>> colorTypes_list = objectMapper.readValue(
					colorTypes_string, new TypeReference<List<HashMap<String, Object>>>() {}
			    );
			
			this.banktypes_list=banktypes_list.stream().distinct().collect(Collectors.toList());
			this.colorTypes=colorTypes_list.stream().distinct().collect(Collectors.toList());
			this.motor_list=motorCategory_list.stream().distinct().collect(Collectors.toList());
			this.bodyType_list=bodyTypes_list.stream().distinct().collect(Collectors.toList());
			
			
    	}catch (Exception e) {
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

	public List<HashMap<String, Object>> getInsurance_class() {
		return insurance_class;
	}

	public void setInsurance_class(List<HashMap<String, Object>> insurance_class) {
		this.insurance_class = insurance_class;
	}

	public List<HashMap<String, Object>> getDeductibles() {
		return deductibles;
	}

	public void setDeductibles(List<HashMap<String, Object>> deductibles) {
		this.deductibles = deductibles;
	}

	public List<HashMap<String, Object>> getAggregated_value() {
		return aggregated_value;
	}

	public void setAggregated_value(List<HashMap<String, Object>> aggregated_value) {
		this.aggregated_value = aggregated_value;
	}

	public List<HashMap<String, Object>> getMunicipal_traffic() {
		return municipal_traffic;
	}

	public void setMunicipal_traffic(List<HashMap<String, Object>> municipal_traffic) {
		this.municipal_traffic = municipal_traffic;
	}

	public List<HashMap<String, Object>> getInsurance_type() {
		return insurance_type;
	}

	public void setInsurance_type(List<HashMap<String, Object>> insurance_type) {
		this.insurance_type = insurance_type;
	}

	public List<HashMap<String, Object>> getVehicleValue() {
		return vehicleValue;
	}

	public void setVehicleValue(List<HashMap<String, Object>> vehicleValue) {
		this.vehicleValue = vehicleValue;
	}

	
	
  
    
   
}