package com.maan.eway.vehicleupload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;


@Component("veh_val_ItemProcessor")
public class VehValidationProcessor implements ItemProcessor<EserviceMotorDetailsRaw,EserviceMotorDetailsRaw>{
	 
	 @Autowired
	 private CustomStepExecutionListener stepExecutionListener;
	 


	@Override
	public EserviceMotorDetailsRaw process(EserviceMotorDetailsRaw item) throws Exception {
		String error_desc="";
		try {
				
			// bank id update
			if("Bank".equalsIgnoreCase(item.getBorrowerType())) {
				
				HashMap<String, Object> bank_map =stepExecutionListener.getBanktypes_list()
						.stream().filter( p ->item.getCollateralBankName().toUpperCase()
						.equalsIgnoreCase(p.get("BANK_FULL_NAME").toString().toUpperCase()))
						.collect(Collectors.toList()).get(0);
				
				String bank_id =bank_map.size()>0?bank_map.get("BANK_CODE").toString():"";
				item.setBankId(bank_id);
				
			}
			
			// section id update block
			
			HashMap<String, Object> section_map =stepExecutionListener.getSection_list()
					.stream().filter( p ->item.getInsuranceTypeDesc().toUpperCase()
					.equalsIgnoreCase(p.get("SECTION_NAME").toString().toUpperCase()))
					.collect(Collectors.toList()).get(0);
			
			String section_id =section_map.size()>0?section_map.get("SECTION_ID").toString():"";
			item.setSectionId(Integer.valueOf(section_id));
			item.setInsuranceTypeId(section_id);
			// Body id update block
			
			
			HashMap<String, Object> body_map =stepExecutionListener.getBodyType_list()
					.stream().filter( p ->item.getBodyTypeDesc().toUpperCase()
					.equalsIgnoreCase(p.get("BODY_NAME_EN").toString().toUpperCase()))
					.collect(Collectors.toList()).get(0);
			
			String body_type_id =body_map.size()>0?body_map.get("BODY_ID").toString():"";
			item.setBodyTypeId(body_type_id);
			
			// policy type id update block
			
		
			HashMap<String, Object> policytype_map =stepExecutionListener.getPolicyTypes_list()
					.stream().filter( p ->item.getInsuranceClassDesc().toUpperCase()
					.equalsIgnoreCase(p.get("POLICY_TYPE_NAME").toString().toUpperCase()))
					.collect(Collectors.toList()).get(0);
			
			String policytypeId =policytype_map.size()>0?policytype_map.get("POLICY_TYPE_ID").toString():"";
			item.setInsuranceClassId(policytypeId);
			
		// vehicle usage type id update block
			
		
			HashMap<String, Object> vehicle_usage_map =stepExecutionListener.getVehicleUsage_list()
					.stream().filter( p ->item.getMotorUsageDesc().toUpperCase()
					.equalsIgnoreCase(p.get("VEHICLE_USAGE_DESC").toString().toUpperCase()))
					.collect(Collectors.toList()).get(0);
			
			String vehicle_usage_id =vehicle_usage_map.size()>0?vehicle_usage_map.get("VEHICLE_USAGE_ID").toString():"";
			item.setMotorUsageId(vehicle_usage_id);
			
			// motorcategory type id update block
			
			HashMap<String, Object> motor_category_map =stepExecutionListener.getMotor_list()
					.stream().filter( p ->item.getMotorCategory().toUpperCase()
					.equalsIgnoreCase(p.get("ITEM_VALUE").toString().toUpperCase()))
					.collect(Collectors.toList()).get(0);
			
			String motor_category_id =motor_category_map.size()>0?motor_category_map.get("ITEM_CODE").toString():"";
			item.setMotorCategoryId(motor_category_id);
			
		// color type id update block
			
			HashMap<String, Object> color_type_map =stepExecutionListener.getColorTypes()
					.stream().filter( p ->item.getColor().toUpperCase()
					.equalsIgnoreCase(p.get("COLOR_DESC").toString().toUpperCase()))
					.collect(Collectors.toList()).get(0);
			
			String color_id =color_type_map.size()>0?color_type_map.get("COLOR_ID").toString():"";
			item.setColorId(color_id);
			
			
			// error desc update block
			
			error_desc+=item.getSectionId()==null || item.getSectionId()==0?"SectionId does not update~":"";
			error_desc+=StringUtils.isBlank(item.getInsuranceTypeId())?"InsuranceTypeId does not update~":"";
			error_desc+=StringUtils.isBlank(item.getInsuranceClassId())?"InsuranceClassId does not update~":"";
			error_desc+=StringUtils.isBlank(item.getMotorCategoryId())?"MotorCategoryId does not update~":"";
			error_desc+=StringUtils.isBlank(item.getBodyTypeId())?"BodyTypeId does not update~":"";
			error_desc+=StringUtils.isBlank(item.getMotorUsageId())?"MotorUsageId does not update~":"";
			error_desc+=StringUtils.isBlank(item.getColorId())?"ColorId does not update~":"";

			
			if("Bank".equalsIgnoreCase(item.getBorrowerType())) 
				error_desc+=StringUtils.isBlank(item.getBankId())?"BankId does not update":"";
			
			String status =StringUtils.isNotBlank(error_desc)?"E":"Y";
			item.setStatus(status);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

}
