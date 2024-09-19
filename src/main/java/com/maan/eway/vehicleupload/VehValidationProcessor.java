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
		
		
		if(100040!=item.getCompanyId()) {
			item = updateNonSanlamCompanyMasterIds(item);
		}else if(100040==item.getCompanyId()) {			
			item = updateSanlamCompanyMasterIds(item);
		}
			
		return item;
	}


	private EserviceMotorDetailsRaw updateSanlamCompanyMasterIds(EserviceMotorDetailsRaw item) {
		String error_desc="";
		try {
			
			
			// make Id update
			List<HashMap<String, Object>> make_list =stepExecutionListener.getMake()
					.stream().filter( p ->StringUtils.isBlank(item.getVehicleMake().toUpperCase())?
					 null:item.getVehicleMake().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("MAKE_NAME_EN").toString().toUpperCase().replace(" ", "")))
					.collect(Collectors.toList());
			
			
			if(make_list.size()>0) {
				String make_id = make_list.get(0).size()>0?make_list.get(0).get("MAKE_ID").toString():"";
				item.setVehicleMakeId(make_id);			
			}
			
			// model id update block
			
			if(StringUtils.isNotBlank(item.getVehicleMakeId())) {
				
				java.util.function.Predicate<Map<String,Object>> make_filter =p ->item.getVehicleMakeId().equals(p.get("MAKE_ID")==null?"":p.get("MAKE_ID").toString());
				// make Id update
				List<HashMap<String, Object>> model_list =stepExecutionListener.getModel()
						.stream()
						.filter(make_filter)
						.filter( p ->StringUtils.isBlank(item.getVehicleModel().toUpperCase())?
								null:item.getVehicleModel().toUpperCase().replace(" ", "")
								.equalsIgnoreCase(p.get("MODEL_NAME_EN").toString().toUpperCase().replace(" ", "")))						
						.collect(Collectors.toList());
				
				
				if(model_list.size()>0) {
					String model_id = model_list.get(0).size()>0?model_list.get(0).get("MODEL_ID").toString():"";
					item.setVehicleModelId(model_id);			
				}
				
			}
			
			
			
			
			
			// insurance_class id update block
			
			List<HashMap<String, Object>> insurance_classList =stepExecutionListener.getInsurance_class()
					.stream().filter( p ->StringUtils.isBlank(item.getInsuranceClassDesc().toUpperCase())?
					 null:item.getInsuranceClassDesc().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("INDUSTRY_TYPE_DESC").toString().toUpperCase().replace(" ", "")))
					.collect(Collectors.toList());
			
			
			if(insurance_classList.size()>0) {
				String insurance_class_id =insurance_classList.get(0).size()>0?insurance_classList.get(0).get("INDUSTRY_TYPE_ID").toString():"";
				item.setInsuranceClassId(insurance_class_id);
				item.setSectionId(Integer.valueOf(insurance_class_id));
			}
			
			
			// deductible id update block
			
		
			List<HashMap<String, Object>> deductible_list =stepExecutionListener.getDeductibles()
					.stream().filter( p ->StringUtils.isBlank(item.getDeductiblesDesc().toUpperCase())?
					 null:item.getDeductiblesDesc().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("ITEM_VALUE").toString().toUpperCase().replace(" ", "")))
					.collect(Collectors.toList());
			
			if(deductible_list.size()>0) {
				String deductible_id =deductible_list.get(0).size()>0?deductible_list.get(0).get("ITEM_CODE").toString():"";
				item.setDeductiblesId(deductible_id);
			}
			
		// aggregatorValue id update block
			
		
			List<HashMap<String, Object>> aggregated_value_list =stepExecutionListener.getAggregated_value()
					.stream().filter( p ->StringUtils.isBlank(item.getAggregatedValueDesc().toUpperCase())?
					 null:item.getAggregatedValueDesc().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("ITEM_VALUE").toString().toUpperCase().replace(" ", "").replace(",", "")))
					.collect(Collectors.toList());
			
			if(aggregated_value_list.size()>0) {
				String aggregated_value_id =aggregated_value_list.get(0).size()>0?aggregated_value_list.get(0).get("ITEM_CODE").toString():"";
				item.setAggregatedValueId(aggregated_value_id);
			}
			
			// vehicle_value id update block
			
			
			List<HashMap<String, Object>> vehicle_value_list =stepExecutionListener.getVehicleValue()
					.stream().filter( p ->StringUtils.isBlank(item.getVehicleValueTypeDesc().toUpperCase())?
					 null:item.getVehicleValueTypeDesc().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("ITEM_VALUE").toString().toUpperCase().replace(" ", "")))
					.collect(Collectors.toList());
			
			if(vehicle_value_list.size()>0) {
				String vehicle_value_id =vehicle_value_list.get(0).size()>0?vehicle_value_list.get(0).get("ITEM_CODE").toString():"";
				item.setVehicleValueTypeId(vehicle_value_id);
			}
			
		// municipal_traffic id update block
			
		
			List<HashMap<String, Object>> municipal_traffic_list =stepExecutionListener.getMunicipal_traffic()
					.stream().filter( p ->StringUtils.isBlank(item.getMunicipalityOfTrafficDesc().toUpperCase())?
					 null:item.getMunicipalityOfTrafficDesc().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("ITEM_VALUE").toString().toUpperCase().replace(" ", "")))
					.collect(Collectors.toList());
			
			if(municipal_traffic_list.size()>0) {
				String municipal_traffic_id =municipal_traffic_list.get(0).size()>0?municipal_traffic_list.get(0).get("ITEM_CODE").toString():"";
				item.setMunicipalityOfTrafficId(municipal_traffic_id);
			}
			
			
			// insurance_type_id update block
	
			List<HashMap<String, Object>> vehicle_usage_list =stepExecutionListener.getInsurance_type()
					.stream().filter( p ->StringUtils.isBlank(item.getInsuranceTypeDesc().toUpperCase())?
					 null:item.getInsuranceTypeDesc().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("VEHICLE_USAGE_DESC").toString().toUpperCase().replace(" ", "")))
					.collect(Collectors.toList());
			
			
			
			if(vehicle_usage_list.size()>0) {
				String vehicle_usage_id =vehicle_usage_list.get(0).size()>0?vehicle_usage_list.get(0).get("VEHICLE_USAGE_ID").toString():"";
				item.setMotorUsageId(vehicle_usage_id);
				item.setInsuranceTypeId(vehicle_usage_id);
			}
			
			// bank id update
			if("Bank".equalsIgnoreCase(item.getBorrowerType())) {
				
				List<HashMap<String, Object>> bank_list =stepExecutionListener.getBanktypes_list()
						.stream().filter( p ->StringUtils.isBlank(item.getCollateralBankname().toUpperCase())?
						 null:item.getCollateralBankname().toUpperCase().replace(" ", "")
						.equalsIgnoreCase(p.get("BANK_FULL_NAME").toString().toUpperCase().replace(" ", "")))
						.collect(Collectors.toList());
				
				if(bank_list.size()>0) {
					String bank_id =bank_list.get(0).size()>0?bank_list.get(0).get("BANK_CODE").toString():"";
					item.setBankId(bank_id);
				}
				
			}
			
			
			// motorcategory type id update block
			
			/*List<HashMap<String, Object>> motor_category_list =stepExecutionListener.getMotor_list()
					.stream().filter( p ->StringUtils.isBlank(item.getMotorCategory().toUpperCase())?
					 null:item.getMotorCategory().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("ITEM_VALUE").toString().toUpperCase().replace(" ", "")))
					.collect(Collectors.toList());
			
			if(motor_category_list.size()>0) {
				String motor_category_id =motor_category_list.get(0).size()>0?motor_category_list.get(0).get("ITEM_CODE").toString():"";
				item.setMotorCategoryId(motor_category_id);
			}*/
			
		// color type id update block
			
			List<HashMap<String, Object>> color_type_list =stepExecutionListener.getColorTypes()
					.stream().filter( p ->StringUtils.isBlank(item.getColor().toUpperCase())?
					null:item.getColor().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("COLOR_DESC").toString().toUpperCase().replace(" ", "")))
					.collect(Collectors.toList());
			
			if(color_type_list.size()>0) {
				String color_id =color_type_list.get(0).size()>0?color_type_list.get(0).get("COLOR_ID").toString():"";
				item.setColorId(color_id);
			}
			
			
			// Body id update block
			
			
			List<HashMap<String, Object>> body_list =stepExecutionListener.getBodyType_list()
					.stream().filter( p ->StringUtils.isBlank(item.getBodyTypeDesc().toUpperCase())?
					 null:item.getBodyTypeDesc().toUpperCase().replace(" ", "")
					.equalsIgnoreCase(p.get("BODY_NAME_EN").toString().toUpperCase().replace(" ", "")))
					.collect(Collectors.toList());
			if(body_list.size()>0) {
				String body_type_id =body_list.get(0).size()>0?body_list.get(0).get("BODY_ID").toString():"";
				item.setBodyTypeId(body_type_id);
			}
			
			// error update block

			error_desc+=StringUtils.isBlank(item.getInsuranceClassId())?"Please type valid InsuranceClassDesc~":"";
			error_desc+=StringUtils.isBlank(item.getDeductiblesId())?"Please type valid Deductibles~":"";
			error_desc+=StringUtils.isBlank(item.getAggregatedValueId())?"Please type valid AggregatedValue~":"";
			error_desc+=StringUtils.isBlank(item.getVehicleValueTypeId())?"Please type valid VehicleValueType~":"";
			error_desc+=StringUtils.isBlank(item.getMunicipalityOfTrafficId())?"Please type valid MunicipalityOfTraffic~":"";
			error_desc+=StringUtils.isBlank(item.getBodyTypeId())?"Please type valid BodyTypeDesc~":"";
			error_desc+=StringUtils.isBlank(item.getColorId())?"Please type valid ColorDesc~":"";
			error_desc+=StringUtils.isBlank(item.getInsuranceTypeId())?"Please type valid InsuranceType~":"";
			error_desc+=StringUtils.isBlank(item.getVehicleMakeId())?"Please type valid make~":"";
			error_desc+=StringUtils.isBlank(item.getVehicleModelId())?"Please type valid make and model~":"";

			
			if("Bank".equalsIgnoreCase(item.getBorrowerType())) 
				error_desc+=StringUtils.isBlank(item.getBankId())?"Please type valid BankNameDesc":"";
			
			String status =StringUtils.isNotBlank(error_desc)?"E":"Y";
			item.setStatus(status);
			item.setErrorDesc(error_desc);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return item;
	}



	private EserviceMotorDetailsRaw updateNonSanlamCompanyMasterIds(EserviceMotorDetailsRaw item) {
			String error_desc="";
			
			try {
			
				// section id update block
				
				List<HashMap<String, Object>> section_list =stepExecutionListener.getSection_list()
						.stream().filter( p ->StringUtils.isBlank(item.getInsuranceTypeDesc().toUpperCase())?
						 null:item.getInsuranceTypeDesc().toUpperCase().replace(" ", "")
						.equalsIgnoreCase(p.get("SECTION_NAME").toString().toUpperCase().replace(" ", "")))
						.collect(Collectors.toList());
				
				
				if(section_list.size()>0) {
					String section_id =section_list.get(0).size()>0?section_list.get(0).get("SECTION_ID").toString():"";
					item.setSectionId(Integer.valueOf(section_id));
					item.setInsuranceTypeId(section_id);
				}
				
				// policy type id update block
				
			
				List<HashMap<String, Object>> policytype_list =stepExecutionListener.getPolicyTypes_list()
						.stream().filter( p ->StringUtils.isBlank(item.getInsuranceClassDesc().toUpperCase())?
						 null:item.getInsuranceClassDesc().toUpperCase().replace(" ", "")
						.equalsIgnoreCase(p.get("POLICY_TYPE_NAME").toString().toUpperCase().replace(" ", "")))
						.collect(Collectors.toList());
				
				if(policytype_list.size()>0) {
					String policytypeId =policytype_list.get(0).size()>0?policytype_list.get(0).get("POLICY_TYPE_ID").toString():"";
					item.setInsuranceClassId(policytypeId);
				}
				
			// vehicle usage type id update block
				
			
				List<HashMap<String, Object>> vehicle_usage_list =stepExecutionListener.getVehicleUsage_list()
						.stream().filter( p ->StringUtils.isBlank(item.getMotorUsageDesc().toUpperCase())?
						 null:item.getMotorUsageDesc().toUpperCase().replace(" ", "")
						.equalsIgnoreCase(p.get("VEHICLE_USAGE_DESC").toString().toUpperCase().replace(" ", "")))
						.collect(Collectors.toList());
				
				if(vehicle_usage_list.size()>0) {
					String vehicle_usage_id =vehicle_usage_list.get(0).size()>0?vehicle_usage_list.get(0).get("VEHICLE_USAGE_ID").toString():"";
					item.setMotorUsageId(vehicle_usage_id);
				}
				
				// bank id update
				if("Bank".equalsIgnoreCase(item.getBorrowerType())) {
					
					List<HashMap<String, Object>> bank_list =stepExecutionListener.getBanktypes_list()
							.stream().filter( p ->StringUtils.isBlank(item.getCollateralBankname().toUpperCase())?
							 null:item.getCollateralBankname().toUpperCase().replace(" ", "")
							.equalsIgnoreCase(p.get("BANK_FULL_NAME").toString().toUpperCase().replace(" ", "")))
							.collect(Collectors.toList());
					
					if(bank_list.size()>0) {
						String bank_id =bank_list.get(0).size()>0?bank_list.get(0).get("BANK_CODE").toString():"";
						item.setBankId(bank_id);
					}
					
				}
				
				
				// motorcategory type id update block
				
				List<HashMap<String, Object>> motor_category_list =stepExecutionListener.getMotor_list()
						.stream().filter( p ->StringUtils.isBlank(item.getMotorCategory().toUpperCase())?
						 null:item.getMotorCategory().toUpperCase().replace(" ", "")
						.equalsIgnoreCase(p.get("ITEM_VALUE").toString().toUpperCase().replace(" ", "")))
						.collect(Collectors.toList());
				
				if(motor_category_list.size()>0) {
					String motor_category_id =motor_category_list.get(0).size()>0?motor_category_list.get(0).get("ITEM_CODE").toString():"";
					item.setMotorCategoryId(motor_category_id);
				}
				
			// color type id update block
				
				List<HashMap<String, Object>> color_type_list =stepExecutionListener.getColorTypes()
						.stream().filter( p ->StringUtils.isBlank(item.getColor().toUpperCase())?
						null:item.getColor().toUpperCase().replace(" ", "")
						.equalsIgnoreCase(p.get("COLOR_DESC").toString().toUpperCase().replace(" ", "")))
						.collect(Collectors.toList());
				
				if(color_type_list.size()>0) {
					String color_id =color_type_list.get(0).size()>0?color_type_list.get(0).get("COLOR_ID").toString():"";
					item.setColorId(color_id);
				}
				
				
				// Body id update block
				
				
				List<HashMap<String, Object>> body_list =stepExecutionListener.getBodyType_list()
						.stream().filter( p ->StringUtils.isBlank(item.getBodyTypeDesc().toUpperCase())?
						 null:item.getBodyTypeDesc().toUpperCase().replace(" ", "")
						.equalsIgnoreCase(p.get("BODY_NAME_EN").toString().toUpperCase().replace(" ", "")))
						.collect(Collectors.toList());
				if(body_list.size()>0) {
					String body_type_id =body_list.get(0).size()>0?body_list.get(0).get("BODY_ID").toString():"";
					item.setBodyTypeId(body_type_id);
				}
				
				
				
				// error desc update block
				
				//error_desc+=item.getSectionId()==null || item.getSectionId()==0?"SectionId does not update~":"";
				error_desc+=StringUtils.isBlank(item.getInsuranceTypeId())?"Please type valid InsuranceTypeDesc~":"";
				error_desc+=StringUtils.isBlank(item.getInsuranceClassId())?"Please type valid InsuranceClassDesc~":"";
				error_desc+=StringUtils.isBlank(item.getMotorUsageId())?"Please type valid MotorUsageDesc~":"";						
				error_desc+=StringUtils.isBlank(item.getMotorCategoryId())?"Please type valid MotorCategory~":"";
				error_desc+=StringUtils.isBlank(item.getBodyTypeId())?"Please type valid BodyTypeDesc~":"";
				error_desc+=StringUtils.isBlank(item.getColorId())?"Please type valid ColorDesc~":"";
				
				if("Bank".equalsIgnoreCase(item.getBorrowerType())) 
					error_desc+=StringUtils.isBlank(item.getBankId())?"Please type valid BankNameDesc":"";
				
				String status =StringUtils.isNotBlank(error_desc)?"E":"Y";
				item.setStatus(status);
				item.setErrorDesc(error_desc);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		
		return item;
	}

}
