package com.maan.eway.nonmotor.upload;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;
import java.util.Optional;
public class NonMotorRawDataValProcess implements ItemProcessor<EwayEmplyeeDetailRaw, EwayEmplyeeDetailRaw> {
	
	@Autowired
	private NonMotStepExecutionListener query;

	@Override
	public EwayEmplyeeDetailRaw process(EwayEmplyeeDetailRaw item) throws Exception {
		
		String error_desc ="";
		String productId =item.getProductId().toString();
    	if("14".equals(productId) || "15".equals(productId) || "32".equals(productId) || "19".equals(productId)){
    		
    		Optional<String> occupation_code = query.getOccupation_list().stream().filter(p -> p.get("ITEM_VALUE").toString().replace(" ", "")
    				.equalsIgnoreCase(item.getOccupationDesc().replace(" ", "")))
    		.map(p -> p.get("ITEM_CODE").toString()).findFirst();
    		
    		Optional<String> month_code = query.getMonth_list().stream().filter(p -> p.get("ITEM_VALUE").toString().replace(" ", "")
    				.equalsIgnoreCase(item.getMonthOfJoiningDesc().replace(" ", "")))
    		.map(p -> p.get("ITEM_CODE").toString()).findFirst();
    		
    		
    		Optional<String> location_code = query.getBuilding_list().stream().filter(p -> p.get("ITEM_VALUE").toString().replace(" ", "")
    				.equalsIgnoreCase(item.getLocationDesc().replace(" ", "")))
    		.map(p -> p.get("ITEM_CODE").toString()).findFirst();
    		
    		
    		if(occupation_code.isPresent()) 
    			item.setOccupationId((String)occupation_code.get());
    		else
    			error_desc ="Not a valid occupation text~";
    		
    		if(month_code.isPresent()) 
    			item.setMonthOfJoining((String)month_code.get());
    		else
    			error_desc ="Not a valid month text~";
    		
    		if(location_code.isPresent()) 
    			item.setLocationId((String)location_code.get());
    		else
    			error_desc ="Not a valid location text~";
    		
    		
    		if(StringUtils.isNotBlank(error_desc)) {
    			item.setErrorDesc(error_desc);
    			item.setStatus("E");
    		}
    		
    		
    	}else if("59".equals(productId) ||"26".equals(productId) || "24".equals(productId)) {
    		
    		
    		
    	}else if("4".equals(productId)){
    		
    	}

		return item;
	}

	

}
