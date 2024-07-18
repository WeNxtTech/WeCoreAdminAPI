package com.maan.eway.vehicleupload;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.repository.EserviceMotorDetailsRawRepository;
import com.maan.eway.batch.repository.EwayEmplyeeDetailRawRepository;
import com.maan.eway.batch.repository.ProductEmployeesDetailsRepository;
import com.maan.eway.batch.req.ProductEmployeeSaveReq;
import com.maan.eway.batch.res.EwayUploadRes;
import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.error.Error;
import com.maan.eway.res.CommonRes;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Component
public class VehicleAsynchronousProcess {
	
	
	Logger log =LogManager.getLogger(VehicleAsynchronousProcess.class);
	
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
	private EserviceMotorDetailsRawRepository eserviceRepository;
	
	@Autowired
	private EwayEmplyeeDetailRawRepository ewayEmployeeRepo;
	
	@Autowired
	private ProductEmployeesDetailsRepository productRepo;
	
    private static ObjectMapper mapper =new ObjectMapper();
    
    private static Gson print =new Gson();
    
    private static  OkHttpClient httpClient =new OkHttpClient.Builder()
			.connectTimeout(60, TimeUnit.SECONDS)
			.writeTimeout(60,TimeUnit.SECONDS)
			.readTimeout(60,TimeUnit.SECONDS)
			.build();
    
   private static  MediaType mediaType =MediaType.parse("application/json");
	


   @Async("fileUploadExeuter")
	public CompletableFuture<String> validateTira(EserviceMotorDetailsRaw rawData, EwayUploadRes uploadResponse){
		String response ="Success";
		try {
			log.info("Thread Name :"+Thread.currentThread().getName());
			String searchByValue =StringUtils.isBlank(rawData.getTiraSearchByDesc())?"":rawData.getTiraSearchByDesc().replaceAll("\\s", "");
			String regNo ="RegisterNumber".equalsIgnoreCase(searchByValue)?rawData.getSearchByData():"";
			String chassisNo ="ChassisNumber".equalsIgnoreCase(searchByValue)?rawData.getSearchByData():"";
			String josnString ="{\"ReqRegNumber\":\"{REG_NO}\",\"ReqChassisNumber\":\"{CHASSIS_NO}\"}";
			String request =josnString.replace("{REG_NO}", regNo).replace("{CHASSIS_NO}", chassisNo);
			log.info("Tira request :" +request);
			
			okhttp3.MediaType content_type = okhttp3.MediaType.parse("application/json");
			@SuppressWarnings("deprecation")
			RequestBody body = RequestBody.create(content_type,request);			
			
			Request req =new Request.Builder()
					.addHeader("Authorization", "Bearer "+uploadResponse.getToken())
					.url(tiraApi)
					.post(body)
					.build();
			
			Response res =httpClient.newCall(req).execute();
			String resString =res.body().string();
			log.info("Tira response :" +resString);
			CommonRes map =mapper.readValue(resString, CommonRes.class);
			if(CollectionUtils.isEmpty(map.getErrorMessage())) {
				@SuppressWarnings("unchecked")
				Map<String,Object> d =(Map<String,Object>) map.getCommonResponse();
				rawData.setAxelDistance(d.get("AxelDistance")==null?"0":d.get("AxelDistance").toString());
				rawData.setColor(d.get("Color")==null?"":d.get("Color").toString());
				rawData.setCreatedBy(d.get("CreatedBy")==null?"":d.get("CreatedBy").toString());
				rawData.setEngineNumber(d.get("EngineNumber")==null?"":d.get("EngineNumber").toString());
				rawData.setFuelType(d.get("FuelType")==null?"":d.get("FuelType").toString());
				rawData.setGrossWeight(d.get("Grossweight")==null?"":d.get("Grossweight").toString());
				rawData.setManufactureYear(d.get("ManufactureYear")==null?"":d.get("ManufactureYear").toString());
				rawData.setMotorCategory(d.get("MotorCategory")==null?"":d.get("MotorCategory").toString());
				rawData.setNumberOfAxels(d.get("NumberOfAxels")==null?"0":d.get("NumberOfAxels").toString());
				rawData.setOwnerCategory(d.get("OwnerCategory")==null?"":d.get("OwnerCategory").toString());
				rawData.setPolicyYn(d.get("PolicyYn")==null?"":d.get("PolicyYn").toString());
				rawData.setReqChassisNo(d.get("ReqChassisNumber")==null?"":d.get("ReqChassisNumber").toString());
				rawData.setReqRegNumber(d.get("ReqRegNumber")==null?"":d.get("ReqRegNumber").toString());
				rawData.setReqCompanyCode(d.get("ReqCompanyCode")==null?"":d.get("ReqCompanyCode").toString());
				rawData.setResEngineCapacity(d.get("ResEngineCapacity")==null?"":d.get("ResEngineCapacity").toString());
				rawData.setResMsgSignature(d.get("ResMsgSignature")==null?"":d.get("ResMsgSignature").toString());
				rawData.setResOwnerName(d.get("ResOwnerName")==null?"":d.get("ResOwnerName").toString());
				rawData.setResStatusCode(d.get("ResStatusCode")==null?"":d.get("ResStatusCode").toString());
				rawData.setResStatusDesc(d.get("ResStatusDesc")==null?"":d.get("ResStatusDesc").toString());
				rawData.setSavedFrom(d.get("SavedFrom")==null?"":d.get("SavedFrom").toString());
				rawData.setSeatingCapacity(d.get("SeatingCapacity")==null?"0":d.get("SeatingCapacity").toString());
				rawData.setTareWeight(d.get("Tareweight")==null?"":d.get("Tareweight").toString());
				rawData.setVehicleMake(d.get("Vehiclemake")==null?"":d.get("Vehiclemake").toString());
				rawData.setVehicleModel(d.get("Vehcilemodel")==null?"":d.get("Vehcilemodel").toString());
				rawData.setVehicleType(d.get("VehicleType")==null?"":d.get("VehicleType").toString());
				rawData.setTiraStatus("Y");
				eserviceRepository.saveAndFlush(rawData);	
			}else {
				rawData.setTiraStatus("E");
				rawData.setTiraErrorDesc("Tira data not found");
				eserviceRepository.saveAndFlush(rawData);
			}
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			rawData.setTiraStatus("E");
			rawData.setTiraErrorDesc(e.getMessage());
			eserviceRepository.saveAndFlush(rawData);
		}
		
		return CompletableFuture.completedFuture(response);
	}
	
	@SuppressWarnings("unchecked")
	@Async("fileUploadExeuter")
	public  CompletableFuture<List<Map<String,Object>>> createQuote(EserviceMotorDetailsRaw p, String auth,Long vehicleId){
		String response ="Success";
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
				saveVehicleInfo.put("Color", p.getColor());
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
			
				Map<String,Object> vehResponse =callApi(saveVehicleReq,auth,mediaType,saveVehicleApi);
				log.info("callCreateQuote || saveVehicleInfoRes " + print.toJson(vehResponse));
				
				errorList=vehResponse.get("ErrorMessage")==null?null:(List<Map<String,Object>>)vehResponse.get("ErrorMessage");

			}
			
			if("100019".equals(p.getCompanyId().toString()) && errorList.isEmpty()) {
				// vehicle request
				vehicleRequest.put("BrokerBranchCode", StringUtils.isBlank(p.getBrokerBranchcode())?"":p.getBrokerBranchcode());
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
				vehicleRequest.put("VehicleId", vehicleId);
				vehicleRequest.put("AcccessoriesSumInsured", p.getAccessoriesSuminsured());
				vehicleRequest.put("AxelDistance", StringUtils.isBlank(p.getAxelDistance())?"":p.getAxelDistance());
				vehicleRequest.put("Chassisnumber", StringUtils.isBlank(p.getReqChassisNo())?p.getChassisNumber():p.getReqChassisNo());
				vehicleRequest.put("CreatedBy", StringUtils.isBlank(p.getCreatedBy())?p.getLoginId():p.getCreatedBy());
				vehicleRequest.put("Insurancetype", StringUtils.isBlank(p.getInsuranceTypeId())?"":Arrays.asList(p.getInsuranceTypeId()));
				vehicleRequest.put("SectionId", StringUtils.isBlank(p.getInsuranceTypeId())?"":p.getInsuranceTypeId());
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
			
				Map<String,Object> vehResponse =callApi(vehRequest,auth,mediaType,vehicleApi);
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
							
							String calcReq =print.toJson(calcRequest);
							log.info("callCreateQuote || calcRequest " + calcReq);
							
							Map<String,Object> calcResponse=callApi(calcReq,auth,mediaType,calcApi);
							log.info("callCreateQuote || calcResponse " + print.toJson(calcResponse));
							
							//List<Map<String,Object>> calcResStatus=calcResponse.get("CoverList")==null?null:(List<Map<String,Object>>)calcResponse.get("CoverList");
							
							/*if(!CollectionUtils.isEmpty(calcResStatus)) {
								synchronized(this) {
									TransactionControlDetails trans=transactionRepo.findByCompanyIdAndProductIdAndRequestReferenceNo(p.getCompanyId(), p.getProductId(), p.getRequestReferenceNo());
									Long validRecord =trans.getValidRecords().longValue();
									Long minusValidRecord=validRecord-1;
									trans.setValidRecords(minusValidRecord.intValue());
									trans.setMovedRecords(trans.getMovedRecords()==null || "0".equals(String.valueOf(trans.getMovedRecords()))?1:trans.getMovedRecords()+1);
									transactionRepo.saveAndFlush(trans);
								}
							}*/
							
							p.setApiStatus("Y");
							p.setCalcRequest(calcReq);
							p.setMotorRequest(vehRequest);
							eserviceRepository.saveAndFlush(p);
						}else if("Failed".equals(responseStatus)) {
							String motorErrorDesc=vehResponse.get("ErrorMessage")==null?"":vehResponse.get("ErrorMessage").toString();
							p.setVehicleId(Integer.valueOf(vehicleId.toString()));
							p.setApiStatus("E");
							p.setMotorRequest(vehRequest); 
							p.setMotorErrorDesc(motorErrorDesc);
							eserviceRepository.saveAndFlush(p);
						}
				}else {
					return CompletableFuture.completedFuture(errorList);
				}
			}else {
				return CompletableFuture.completedFuture(errorList);
			}
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response ="Failed";
		}
		return CompletableFuture.completedFuture(errorList);
	}
	
	public Map<String,Object> callApi(String req,String auth,MediaType mediaType,String api) {
		try {
			
			if(mediaType==null)
				mediaType=this.mediaType;
	      //  log.info("Api Request ==>" +req);

			RequestBody requestBody =RequestBody.create(req,mediaType);
			
			Response response =null;
			
			Request request =new Request.Builder()
					.addHeader("Authorization", "Bearer "+auth)
					.url(api)
					.post(requestBody)
					.build();
			
			response =httpClient.newCall(request).execute();
			String responseString =response.body().string();
	        //log.info("Api Response ==>" +responseString);

			Integer statusCode =response.code();
			@SuppressWarnings("unchecked")
			
			Map<String,Object> resMap =mapper.readValue(responseString, Map.class);
			resMap.put("StatusCode", statusCode);
			return resMap;
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
		return null;
	}

	public Map<String,Object> createEmployee(List<Map<String,Object>> employeeList, String auth) {
		Map<String,Object> apiResponse=null;
		try {
			Map<String,Object> request = new LinkedHashMap<String,Object>();
			String uploadType =employeeList.get(0).get("UPLOAD_TYPE")==null?"Add":employeeList.get(0).get("UPLOAD_TYPE").toString();
			List<Map<String,Object>> parallelStream =employeeList.parallelStream()
					.map(data ->{
						Map<String,Object> object =new LinkedHashMap<String,Object>();
						object.put("EmployeeName", data.get("EMPLOYEE_NAME")==null?"":data.get("EMPLOYEE_NAME"));
						object.put("Createdby", data.get("CREATED_BY")==null?"":data.get("CREATED_BY").toString());
						object.put("InsuranceId", data.get("COMPANY_ID")==null?"":data.get("COMPANY_ID").toString());
						object.put("OccupationDesc",data.get("OCCUPATION_DESC")==null?"":data.get("OCCUPATION_DESC"));
						object.put("OccupationId",data.get("OCCUPATION_ID")==null?"":data.get("OCCUPATION_ID"));
						object.put("RiskId", data.get("RISK_ID")==null?"":data.get("RISK_ID"));
						object.put("Salary",data.get("SALARY")==null?"":data.get("SALARY"));
						object.put("NationalityId", data.get("NATIONALITY_ID")==null?"":data.get("NATIONALITY_ID"));
						object.put("DateOfJoiningYear", data.get("DATE_OF_JOINING")==null?"":data.get("DATE_OF_JOINING"));
						object.put("DateOfJoiningMonth", data.get("DATE_OF_JOIN_MONTH")==null?"":data.get("DATE_OF_JOIN_MONTH"));
						object.put("DateOfBirth", data.get("DATE_OF_BIRTH")==null?"":data.get("DATE_OF_BIRTH"));
						object.put("EmployeeId","");// data.get("EMPLOYEE_ID")==null?"":data.get("EMPLOYEE_ID"));
						object.put("Address", data.get("ADDRESS")==null?"":data.get("ADDRESS"));
						object.put("LocationId", data.get("LOCATION_ID")==null?"":data.get("LOCATION_ID"));
						object.put("QuoteNo", data.get("QUOTE_NO")==null?"":data.get("QUOTE_NO"));
						object.put("RequestReferenceNo",employeeList.get(0).get("REQUEST_REFERENCE_NO")==null?"":employeeList.get(0).get("REQUEST_REFERENCE_NO").toString());
						object.put("SectionId", data.get("SECTION_ID")==null?"":data.get("SECTION_ID"));
						return object;
					}).collect(Collectors.toList());
			
			
			request.put("Createdby", employeeList.get(0).get("CREATED_BY")==null?"":employeeList.get(0).get("CREATED_BY").toString());
			request.put("EmpcountSIvalidYN", "Y");
			request.put("ExcelUploadYN", "N");
			request.put("InsuranceId", employeeList.get(0).get("COMPANY_ID")==null?"":employeeList.get(0).get("COMPANY_ID").toString());
			request.put("ProductId", employeeList.get(0).get("PRODUCT_ID")==null?"":employeeList.get(0).get("PRODUCT_ID").toString());
			request.put("QuoteNo", employeeList.get(0).get("QUOTE_NO")==null?"":employeeList.get(0).get("QUOTE_NO").toString());
			request.put("RequestReferenceNo", employeeList.get(0).get("REQUEST_REFERENCE_NO")==null?"":employeeList.get(0).get("REQUEST_REFERENCE_NO").toString());
			request.put("SectionId", employeeList.get(0).get("SECTION_ID")==null?"":employeeList.get(0).get("SECTION_ID").toString());
			request.put("ProductEmployeeSaveReq", parallelStream);
			log.info("Employee count "+parallelStream.size());
			String apiRequest =print.toJson(request);
			log.info("createEmployee ||employeeSaveRequest "+apiRequest);
			apiResponse=callApi(apiRequest, auth, mediaType, employeeMergeApi);
			log.info("createEmployee ||employeeSaveResponse "+apiResponse);
			List<Map<String,Object>> errors =apiResponse.get("ErrorMessage")==null?null:(List<Map<String,Object>>)apiResponse.get("ErrorMessage");
			String status ="N";
			if(CollectionUtils.isEmpty(errors))
				status ="Y";
			else
				status ="N";
			
			ewayEmployeeRepo.updateEmployeeStatus(status, null, null,null,request.get("InsuranceId").toString(), 
					request.get("ProductId").toString(), request.get("RequestReferenceNo").toString());
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return apiResponse;
	}
	
	@Async("fileUploadExeuter")
	public CompletableFuture<ProductEmployeeDetails> saveEmployeeList(ProductEmployeeSaveReq p,
			String sectionId,String companyId,String createdBy,String productId,String quoteNo,String
			refNo,String productName){
		ProductEmployeeDetails result = new ProductEmployeeDetails();
		try {
			System.out.println(Thread.currentThread().getName());
			ProductEmployeeDetails saveRes = new ProductEmployeeDetails();
			saveRes.setSectionId(sectionId);
			saveRes.setDateOfBirth( p.getDateOfBirth());
			saveRes.setDateOfJoiningYear(Integer.valueOf(p.getDateOfJoiningYear()) );
			saveRes.setDateOfJoiningMonth(p.getDateOfJoiningMonth());			
			saveRes.setCompanyId(companyId);
			saveRes.setCreatedBy(createdBy);
			saveRes.setEmployeeId(Long.valueOf(p.getEmployeeId()));
			saveRes.setEmployeeName(p.getEmployeeName());
			saveRes.setEntryDate(new Date());
			saveRes.setOccupationId(p.getOccupationId());
			saveRes.setOccupationDesc(p.getOccupationDesc());
			saveRes.setProductId(Integer.valueOf(productId));
			saveRes.setQuoteNo(quoteNo);
			saveRes.setRequestReferenceNo(refNo);
			saveRes.setRiskId(Integer.valueOf(p.getLocationId()));
			saveRes.setLocationId(Integer.valueOf(p.getLocationId()));				
			saveRes.setStatus("Y");
			saveRes.setSalary(new BigDecimal(p.getSalary()));
			saveRes.setNationalityId(p.getNationalityId());
			saveRes.setProductDesc(productName);
			saveRes.setAddress(p.getAddress());
			result =productRepo.saveAndFlush(saveRes);
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(result);
	}

	public Map<String,Object> createPassenger(List<Map<String, Object>> list,String auth) {
		Map<String,Object> apiResponse =null;
		try {
			LinkedHashMap<String,Object> map =new LinkedHashMap<String,Object>();
			List<LinkedHashMap<String,String>> passengerList =list.parallelStream().map(d ->{
				LinkedHashMap<String,String> data =new LinkedHashMap<String,String>();
				data.put("Dob", d.get("DATE_OF_BIRTH")==null?"":d.get("DATE_OF_BIRTH").toString()); 
				String gender =d.get("GENDER")==null?"M":d.get("GENDER").toString(); 
				data.put("GenderId", "Male".equalsIgnoreCase(gender)?"M":"F"); 
				data.put("Nationality", d.get("NATIONALITY_ID")==null?"TZA":d.get("NATIONALITY_ID").toString()); 
				data.put("PassengerFirstName", d.get("FIRST_NAME")==null?"":d.get("FIRST_NAME").toString()); 
				data.put("PassengerLastName", d.get("LAST_NAME")==null?"":d.get("LAST_NAME").toString()); 
				data.put("PassportNo",  d.get("PASSPORT_NO")==null?"":d.get("PASSPORT_NO").toString()); 
				data.put("RelationId", d.get("PASS_RELATION_ID")==null?"":d.get("PASS_RELATION_ID").toString()); 
				return data;
			}).collect(Collectors.toList());
			
			String productId =list.get(0).get("PRODUCT_ID")==null?"":list.get(0).get("PRODUCT_ID").toString();
			String quoteNo =list.get(0).get("QUOTE_NO")==null?"":list.get(0).get("QUOTE_NO").toString();
			String requestRefNo=list.get(0).get("REQUEST_REFERENCE_NO")==null?"":list.get(0).get("REQUEST_REFERENCE_NO").toString();
			String companyId =list.get(0).get("COMPANY_ID")==null?"":list.get(0).get("COMPANY_ID").toString();
			String createdBy =list.get(0).get("CREATED_BY")==null?"":list.get(0).get("CREATED_BY").toString();
			map.put("CreatedBy", createdBy);
			map.put("QuoteNo", quoteNo);
			map.put("PassengerList", passengerList);
			String apiRequest =print.toJson(map);
			log.info("createPassenger ||passengerSaveRequest count "+passengerList.size());
			apiResponse=callApi(apiRequest, auth, mediaType, travelSaveApi);
			log.info("createPassenger ||passenger SaveResponse "+apiResponse);
			List<Map<String,Object>> errors =apiResponse.get("ErrorMessage")==null?null:(List<Map<String,Object>>)apiResponse.get("ErrorMessage");
			String status ="N";
			if(CollectionUtils.isEmpty(errors))
				status ="Y";
			else
				status ="N";
			
			ewayEmployeeRepo.updateEmployeeStatus(status, null, null,null,companyId, 
					productId, requestRefNo);

		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return apiResponse ;
		
	}
	
}
