package com.maan.eway.vehicleupload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;

import okhttp3.MediaType;

@Component("veh_apicall_processor")
public class APIItemProcessor implements ItemProcessor<EserviceMotorDetailsRaw, EserviceMotorDetailsRaw>  {

	Logger log = LogManager.getLogger(APIItemProcessor.class);
	

	@Value("${tira.api}")
	private  String tiraApi;
	
	@Value("${save.vehicle.api}")
	private  String vehicleApi;
	
	@Value("${premium.calc.api}")
	private  String calcApi;
	
	@Value("${employee.validation.api}")
	private  String employeeValidationApi;
	
	@Value("${employee.delete.api}")
	private  String employeeDeleteApi;
	
	@Value("${employee.merge.api}")
	private  String employeeMergeApi;
	
	@Value("${passenger.save.api}")
	private  String travelSaveApi;
	
	@Value("${save.vehicleInfo.api}")
	private String saveVehicleApi;
	
	@Autowired
	private VehicleAsynchronousProcess service;
	    
    private static Gson print =new Gson();
    
    
   private static  MediaType mediaType =MediaType.parse("application/json");
	
	
    String auth;
	
	public APIItemProcessor(String Authorization) {
		this.auth =Authorization;
	}
	

	@Override
	public EserviceMotorDetailsRaw process(EserviceMotorDetailsRaw p) throws Exception {
		List<Map<String,Object>> errorList =new ArrayList<>();
		try {
			HashMap<String, Object> vehicleRequest =new LinkedHashMap<String,Object>();
			HashMap<String, Object> calcRequest =new LinkedHashMap<String,Object>();
			HashMap<String, Object> saveVehicleInfo =new LinkedHashMap<String,Object>();
			
			//save vehicle info 
			if("100019".equals(p.getCompanyId().toString())) {
				saveVehicleInfo.put("Insuranceid", p.getCompanyId());
				saveVehicleInfo.put("BranchCode", p.getBranchCode());
				saveVehicleInfo.put("AxelDistance", "");
				saveVehicleInfo.put("Chassisnumber", p.getChassisNumber());
				saveVehicleInfo.put("Color", p.getColorId());
				saveVehicleInfo.put("CreatedBy", StringUtils.isBlank(p.getCreatedBy())?p.getLoginId():p.getCreatedBy());
				saveVehicleInfo.put("EngineNumber", p.getEngineNumber());
				saveVehicleInfo.put("FuelType", p.getFuelType());
				saveVehicleInfo.put("Grossweight", p.getGrossWeight());
				saveVehicleInfo.put("ManufactureYear", p.getManufactureYear());
				saveVehicleInfo.put("MotorCategory", p.getMotorCategoryId());
				saveVehicleInfo.put("Motorusage", p.getMotorUsageDesc());
				saveVehicleInfo.put("NumberOfAxels", "");
				saveVehicleInfo.put("OwnerCategory",StringUtils.isBlank(p.getOwnerCategory())?"1":p.getOwnerCategory());
				saveVehicleInfo.put("Registrationnumber", StringUtils.isBlank(p.getSearchByData())?p.getChassisNumber():p.getSearchByData());
				saveVehicleInfo.put("ResEngineCapacity", p.getResEngineCapacity());
				saveVehicleInfo.put("ResOwnerName", StringUtils.isBlank(p.getCustomerName())?"":p.getCustomerName());
				saveVehicleInfo.put("ResStatusCode", "Y");
				saveVehicleInfo.put("ResStatusDesc", "None");
				saveVehicleInfo.put("SeatingCapacity", p.getSeatingCapacity());
				saveVehicleInfo.put("Tareweight", p.getTareWeight());
				saveVehicleInfo.put("Vehcilemodel", StringUtils.isBlank(p.getVehicleModel())?"":p.getVehicleModel());
				saveVehicleInfo.put("VehicleType", StringUtils.isBlank(p.getBodyTypeDesc())?"":p.getBodyTypeDesc());
				saveVehicleInfo.put("Vehiclemake", StringUtils.isBlank(p.getVehicleMake())?"":p.getVehicleMake());
				saveVehicleInfo.put("RegistrationDate", null);
				
				String saveVehicleReq =print.toJson(saveVehicleInfo);
				log.info("callCreateQuote || saveVehicleInfoReq " + saveVehicleReq);
			
				Map<String,Object> vehResponse =service.callApi(saveVehicleReq,auth,mediaType,saveVehicleApi);
				log.info("callCreateQuote || saveVehicleInfoRes " + print.toJson(vehResponse));
				
				errorList=vehResponse.get("ErrorMessage")==null?null:(List<Map<String,Object>>)vehResponse.get("ErrorMessage");

			}
			
			if("100019".equals(p.getCompanyId().toString()) && errorList.isEmpty()) {
				// vehicle request
				vehicleRequest.put("BrokerBranchCode", StringUtils.isBlank(p.getBrokerBranchcode())?"":p.getBrokerBranchcode());
				vehicleRequest.put("SectionId", StringUtils.isBlank(p.getInsuranceTypeId())?null:Arrays.asList(p.getInsuranceTypeId()));	
				vehicleRequest.put("AcExecutiveId", StringUtils.isBlank(p.getAcExecutiveid())?"":p.getAcExecutiveid());
				vehicleRequest.put("BrokerCode", StringUtils.isBlank(p.getBrokerCode())?"":p.getBrokerCode());
				vehicleRequest.put("LoginId", StringUtils.isBlank(p.getLoginId())?"":p.getLoginId());
				vehicleRequest.put("SubUserType", StringUtils.isBlank(p.getSubUsertype())?"":p.getSubUsertype());
				vehicleRequest.put("ApplicationId", StringUtils.isBlank(p.getApplicationId())?"":p.getApplicationId());
				vehicleRequest.put("Motorusage", StringUtils.isBlank(p.getMotorUsageDesc())?"":p.getMotorUsageDesc());
				vehicleRequest.put("MotorusageId", StringUtils.isBlank(p.getMotorUsageId())?"":p.getMotorUsageId());
				vehicleRequest.put("CustomerReferenceNo",StringUtils.isBlank(p.getCustomerReferenceno())?"":p.getCustomerReferenceno());
				vehicleRequest.put("RequestReferenceNo", p.getRequestReferenceNo());
				vehicleRequest.put("Idnumber", StringUtils.isBlank(p.getIdNumber())?"":p.getIdNumber());
				vehicleRequest.put("VehicleId", p.getSno());
				vehicleRequest.put("AcccessoriesSumInsured", p.getAccessoriesSuminsured());
				vehicleRequest.put("AxelDistance", StringUtils.isBlank(p.getAxelDistance())?"":p.getAxelDistance());
				vehicleRequest.put("Chassisnumber", StringUtils.isBlank(p.getReqChassisNo())?p.getChassisNumber():p.getReqChassisNo());
				vehicleRequest.put("CreatedBy", StringUtils.isBlank(p.getCreatedBy())?p.getLoginId():p.getCreatedBy());
				vehicleRequest.put("Insurancetype", StringUtils.isBlank(p.getInsuranceTypeId())?"":p.getInsuranceTypeId());
				vehicleRequest.put("InsuranceId", p.getCompanyId());
				vehicleRequest.put("InsuranceClass", StringUtils.isBlank(p.getInsuranceClassId())?"3":p.getInsuranceClassId());
				vehicleRequest.put("BranchCode", StringUtils.isBlank(p.getBranchCode())?"":p.getBranchCode());
				vehicleRequest.put("AgencyCode", StringUtils.isBlank(p.getAgencyCode())?"":p.getAgencyCode());
				vehicleRequest.put("ProductId", p.getProductId());
				vehicleRequest.put("SumInsured", StringUtils.isBlank(p.getVehicleSuminsured())?"":p.getVehicleSuminsured());
				vehicleRequest.put("Vehcilemodel",StringUtils.isBlank(p.getVehicleModel())?"":p.getVehicleModel());
				vehicleRequest.put("VehicleType", StringUtils.isBlank(p.getBodyTypeId())?"":p.getBodyTypeId());
				vehicleRequest.put("VehicleTypeId", StringUtils.isBlank(p.getBodyTypeId())?"":p.getBodyTypeId());
				vehicleRequest.put("Vehiclemake",StringUtils.isBlank(p.getVehicleMake())?"":p.getVehicleMake());
				vehicleRequest.put("WindScreenSumInsured", StringUtils.isBlank(p.getWindshieldSuminsured())?"":p.getWindshieldSuminsured());
				vehicleRequest.put("PolicyStartDate", StringUtils.isBlank(p.getPolicyStartDate())?"":p.getPolicyStartDate());
				vehicleRequest.put("PolicyEndDate", StringUtils.isBlank(p.getPolicyEndDate())?"":p.getPolicyEndDate());
				vehicleRequest.put("Currency", StringUtils.isBlank(p.getCurrency())?"":p.getCurrency());
				vehicleRequest.put("ExchangeRate", StringUtils.isBlank(p.getExchangeRate())?"":p.getExchangeRate());
				vehicleRequest.put("SavedFrom", StringUtils.isBlank(p.getSavedFrom())?"WEB":p.getSavedFrom());					
				vehicleRequest.put("UserType", StringUtils.isBlank(p.getUserType())?"":p.getUserType()); 
				vehicleRequest.put("SourceType",StringUtils.isBlank(p.getSourceType())?"":p.getSourceType()); 
				vehicleRequest.put("CustomerCode",StringUtils.isBlank(p.getCustomerCode())?"":p.getCustomerCode());
				String claimYn =StringUtils.isBlank(p.getClaimYn())?"N":"yes".equalsIgnoreCase(p.getClaimYn())?"Y":"N";
				vehicleRequest.put("ClaimYn", claimYn);
				vehicleRequest.put("CustomerName",StringUtils.isBlank(p.getCustomerName())?"":p.getCustomerName());
				vehicleRequest.put("BdmCode", StringUtils.isBlank(p.getBdmCode())?"":p.getBdmCode());
				vehicleRequest.put("Registrationnumber", StringUtils.isBlank(p.getSearchByData())?p.getChassisNumber():p.getSearchByData());
				vehicleRequest.put("SourceTypeId", StringUtils.isBlank(p.getSourceTypeId())?"":p.getSourceTypeId());
		
				vehicleRequest.put("EngineNumber", StringUtils.isBlank(p.getEngineNumber())?"":p.getEngineNumber());
				vehicleRequest.put("ManufactureYear", StringUtils.isBlank(p.getManufactureYear())?"":p.getManufactureYear());
				String EndorsementYn =StringUtils.isBlank(p.getEndorsementYn())?"N":p.getEndorsementYn();
				String collateralYn =StringUtils.isBlank(p.getCollateralYn())?"NO":p.getCollateralYn();
				String promcodeYn =StringUtils.isBlank(p.getHavePromocode())?"N":p.getHavePromocode();
				
				if("Y".equalsIgnoreCase(promcodeYn)) {
					vehicleRequest.put("HavePromoCode",promcodeYn);
					vehicleRequest.put("PromoCode", StringUtils.isBlank(p.getPromocode())?"":p.getPromocode());
				}else {
					vehicleRequest.put("HavePromoCode",promcodeYn);
					vehicleRequest.put("PromoCode", null);
				}
				
				
				vehicleRequest.put("CarAlarmYn", StringUtils.isBlank(p.getCarAlaramyn())?"N":p.getCarAlaramyn().equalsIgnoreCase("yes")?"Y":"N");
	
				if("Yes".equalsIgnoreCase(collateralYn)) {
					vehicleRequest.put("CollateralYn", "Y");
					vehicleRequest.put("BorrowerType", StringUtils.isBlank(p.getBorrowerType())?"":"Bank".equalsIgnoreCase(p.getBorrowerType())?"1":"2");
					
					if("Bank".equalsIgnoreCase(p.getBorrowerType())){
						vehicleRequest.put("CollateralName",p.getBankId());
					}else {
						vehicleRequest.put("CollateralName", p.getCollateral());
					}
				
					vehicleRequest.put("FirstLossPayee", StringUtils.isBlank(p.getFirstLossPayee())?"":p.getFirstLossPayee());
					
				}else {
					vehicleRequest.put("CollateralYn", "N");
	
				}
				
				if("Y".equals(claimYn)) {
					vehicleRequest.put("accident" ,"Y");
				}
				vehicleRequest.put("NcdYn", claimYn);
				vehicleRequest.put("Gpstrackinginstalled", "yes".equalsIgnoreCase(p.getGpsTrackingEnabled())?"Y":"N");
	
				if("Y".equalsIgnoreCase(EndorsementYn)) {
					vehicleRequest.put("EndorsementDate", StringUtils.isBlank(p.getEndorsementDate())?"":p.getEndorsementDate());
					vehicleRequest.put("EndorsementEffectiveDate", StringUtils.isBlank(p.getEndorsementEffectiveDate())?"":p.getEndorsementEffectiveDate());			
					vehicleRequest.put("EndorsementRemarks", StringUtils.isBlank(p.getEndorsementRemarks())?"":p.getEndorsementRemarks());
					vehicleRequest.put("EndorsementType", StringUtils.isBlank(p.getEndorsementType())?"":p.getEndorsementType());
					vehicleRequest.put("EndorsementTypeDesc", StringUtils.isBlank(p.getEndorsementTypeDesc())?"":p.getEndorsementTypeDesc());
					vehicleRequest.put("EndtCategoryDesc",StringUtils.isBlank(p.getEndtCategoryDesc())?"":p.getEndtCategoryDesc());
					vehicleRequest.put("EndtCount",StringUtils.isBlank(p.getEndtCount())?"":p.getEndtCount());
					vehicleRequest.put("EndtPrevPolicyNo", StringUtils.isBlank(p.getEndtPrevPolicyno())?"":p.getEndtPrevPolicyno());
					vehicleRequest.put("EndtStatus", StringUtils.isBlank(p.getEndtStatus())?"":p.getEndtStatus());
					vehicleRequest.put("IsFinanceEndt", StringUtils.isBlank(p.getIsFinanceEndt())?"":p.getIsFinanceEndt());
					vehicleRequest.put("OrginalPolicyNo", StringUtils.isBlank(p.getOrginalPolicyno())?"":p.getOrginalPolicyno());
					vehicleRequest.put("EndorsementYn", EndorsementYn);
				}else {
					vehicleRequest.put("EndorsementYn", EndorsementYn);
	
				}
	
				// default values
				vehicleRequest.put("TppdIncreaeLimit", StringUtils.isBlank(p.getExtendedSuminsured())?"":p.getExtendedSuminsured()); 
				vehicleRequest.put("FleetOwnerYn", "N");
				vehicleRequest.put("Status","Y");
				
				String vehRequest =print.toJson(vehicleRequest);
				log.info("callCreateQuote || saveMotorReq " + vehRequest);
			
				Map<String,Object> vehResponse =service.callApi(vehRequest,auth,mediaType,vehicleApi);
				log.info("callCreateQuote || saveMotorRes " + print.toJson(vehResponse));
				
				String responseStatus =vehResponse.get("Message")==null?"":vehResponse.get("Message").toString();
				errorList=vehResponse.get("ErrorMessage")==null?null:(List<Map<String,Object>>)vehResponse.get("ErrorMessage");

				if(errorList.isEmpty()) {
					if("Success".equalsIgnoreCase(responseStatus)) {
							
							List<Map<String,Object>> resultList =(List<Map<String,Object>>) vehResponse.get("Result");
							
							Map<String,Object> data =resultList.get(0);
							// calc request
							calcRequest.put("InsuranceId", p.getCompanyId());
							calcRequest.put("BranchCode", p.getBranchCode());
							calcRequest.put("AgencyCode", p.getAgencyCode());
							calcRequest.put("SectionId", p.getSectionId());
							calcRequest.put("ProductId", p.getProductId());
							calcRequest.put("MSRefNo", data.get("MSRefNo")==null?"":data.get("MSRefNo").toString());
							calcRequest.put("VehicleId", data.get("VehicleId")==null?"":data.get("VehicleId").toString());
							calcRequest.put("CdRefNo", data.get("CdRefNo")==null?"":data.get("CdRefNo").toString());
							calcRequest.put("VdRefNo", data.get("VdRefNo")==null?"":data.get("VdRefNo").toString());
							calcRequest.put("CreatedBy",StringUtils.isBlank(p.getCreatedBy())?"":p.getCreatedBy());	
							calcRequest.put("RequestReferenceNo", p.getRequestReferenceNo());
							calcRequest.put("EffectiveDate", p.getPolicyStartDate());
							calcRequest.put("PolicyEndDate", p.getPolicyEndDate());
							calcRequest.put("CoverModification", null);
							calcRequest.put("LocationId", "1");
							
							String calcReq =print.toJson(calcRequest);
							log.info("callCreateQuote || calcRequest " + calcReq);
							
							Map<String,Object> calcResponse=service.callApi(calcReq,auth,mediaType,calcApi);
							log.info("callCreateQuote || calcResponse " + print.toJson(calcResponse));	
					}
				}
			}
			
			if(!errorList.isEmpty()) {
				p.setErrorDesc(print.toJson(errorList));
				p.setStatus("E");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
}
