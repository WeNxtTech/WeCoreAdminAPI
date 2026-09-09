package com.maan.eway.vehicleupload;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.repository.MasterLookupRepository;

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
	
	@Autowired
	private MasterLookupRepository masterRepo;
	    
    private static Gson print =new Gson();
    
    
    private static  MediaType mediaType =MediaType.parse("application/json");
    
    private static final ConcurrentHashMap<String, String> rrnCache = new ConcurrentHashMap<>();
    
    private static final int RRN_WAIT_MAX_ATTEMPTS = 20;
    private static final long RRN_WAIT_SLEEP_MS    = 500;
	
    private String cachedSectionId;
    private String cachedMotorUsageId;
    private String cachedInsuranceTypeId;
    private String cachedInsuranceClassId;
    
    public void setPreFetchedIds(String sectionId, String motorUsageId, 
            String insuranceTypeId, String insuranceClassId) {
			this.cachedSectionId        = sectionId;
			this.cachedMotorUsageId     = motorUsageId;
			this.cachedInsuranceTypeId  = insuranceTypeId;
			this.cachedInsuranceClassId = insuranceClassId;
		}
    
    String auth;
	
	public APIItemProcessor(String Authorization) {
		this.auth =Authorization;
	}
	public static void clearRrnCache() {
        rrnCache.clear();
    }
	
	// In APIItemProcessor.java — update process() method
	// NOTE: process() is called once per item by Spring Batch (already partitioned),
	// so we run the async chain and block at the end to return result to the writer.

	@Override
	public EserviceMotorDetailsRaw process(EserviceMotorDetailsRaw p) throws Exception {
	    List<Map<String, Object>> errorList = new ArrayList<>();
	    try {
	        boolean isTiraSearchY   = "Y".equalsIgnoreCase(p.getTiraSearchByDesc());
	        boolean isCompany100002 = "100002".equals(p.getCompanyId().toString());

	        if (isTiraSearchY && isCompany100002) {
	            
	            p = processTiraFlowAsync(p).get(120, TimeUnit.SECONDS);
	        } else {
	            processExistingFlow(p, errorList);
	            if (!errorList.isEmpty()) {
	                p.setErrorDesc(print.toJson(errorList));
	                p.setStatus("E");
	            }
	        }
	    } catch (TimeoutException te) {
	        log.error("process || timeout sno={}", p.getSno());
	        p.setStatus("E");
	        p.setErrorDesc("Timeout after 120s");
	    } catch (Exception e) {
	        log.error("process || error sno={}", p.getSno(), e);
	        p.setStatus("E");
	        p.setErrorDesc(e.getMessage());
	    }
	    return p;
	}
	

	public EserviceMotorDetailsRaw processExistingFlow(EserviceMotorDetailsRaw p, List<Map<String, Object>> errorList ) {
		 errorList =new ArrayList<>();
		try {
			HashMap<String, Object> vehicleRequest =new LinkedHashMap<String,Object>();
			HashMap<String, Object> calcRequest =new LinkedHashMap<String,Object>();
			HashMap<String, Object> saveVehicleInfo =new LinkedHashMap<String,Object>();
			
			//save vehicle info 
			if(!"100002".equals(p.getCompanyId().toString())) {
				saveVehicleInfo.put("Insuranceid", p.getCompanyId());
				saveVehicleInfo.put("BranchCode", p.getBranchCode());
				saveVehicleInfo.put("AxelDistance", "1");
				saveVehicleInfo.put("Chassisnumber", p.getChassisNumber());
				saveVehicleInfo.put("Color", p.getColorId());
				saveVehicleInfo.put("CreatedBy", StringUtils.isBlank(p.getCreatedBy())?p.getLoginId():p.getCreatedBy());
				saveVehicleInfo.put("EngineNumber", p.getEngineNumber());
				saveVehicleInfo.put("FuelType", p.getFuelType());
				saveVehicleInfo.put("Grossweight", StringUtils.isBlank(p.getGrossWeight())?"500":p.getGrossWeight());
				saveVehicleInfo.put("MotorCategory", StringUtils.isBlank(p.getMotorCategoryId())?"1":p.getMotorCategoryId());
				saveVehicleInfo.put("Motorusage", StringUtils.isBlank(p.getMotorUsageDesc())?"Ambulance":p.getMotorUsageDesc());
				saveVehicleInfo.put("NumberOfAxels", "1");
				saveVehicleInfo.put("OwnerCategory",StringUtils.isBlank(p.getOwnerCategory())?"1":p.getOwnerCategory());
				saveVehicleInfo.put("Registrationnumber", StringUtils.isBlank(p.getSearchByData())?p.getChassisNumber():p.getSearchByData());
				saveVehicleInfo.put("ResEngineCapacity", StringUtils.isBlank(p.getResEngineCapacity())?"1":p.getResEngineCapacity());
				saveVehicleInfo.put("ResOwnerName", StringUtils.isBlank(p.getCustomerName())?"":p.getCustomerName());
				saveVehicleInfo.put("ResStatusCode", "Y");
				saveVehicleInfo.put("ResStatusDesc", "None");
				saveVehicleInfo.put("SeatingCapacity", p.getSeatingCapacity());
				saveVehicleInfo.put("Tareweight", p.getTareWeight());
				saveVehicleInfo.put("Vehcilemodel", StringUtils.isBlank(p.getVehicleModel())?"":p.getVehicleModel());
				saveVehicleInfo.put("VehicleType", StringUtils.isBlank(p.getBodyTypeDesc())?"":p.getBodyTypeDesc());
				saveVehicleInfo.put("HorsePower", StringUtils.isBlank(p.getHorsePower())?"":p.getHorsePower());
				saveVehicleInfo.put("RegistrationDate", StringUtils.isBlank(p.getRegistration_date())?"":p.getRegistration_date());
				saveVehicleInfo.put("NumberOfCylinders", StringUtils.isBlank(p.getNoOfCylinders())?"":p.getNoOfCylinders());
				saveVehicleInfo.put("Vehiclemake", StringUtils.isBlank(p.getVehicleMake())?"":p.getVehicleMake());

				String manufacture_year =100040==p.getCompanyId()?p.getRegistration_date().split("/")[2]
						:p.getManufactureYear();
				saveVehicleInfo.put("ManufactureYear", manufacture_year);

				
				String saveVehicleReq =print.toJson(saveVehicleInfo);
				log.info("callCreateQuote || saveVehicleInfoReq " + saveVehicleReq);
			
				Map<String,Object> vehResponse =service.callApi(saveVehicleReq,auth,mediaType,saveVehicleApi);
				log.info("callCreateQuote || saveVehicleInfoRes " + print.toJson(vehResponse));
				
				errorList=vehResponse.get("ErrorMessage")==null?null:(List<Map<String,Object>>)vehResponse.get("ErrorMessage");

			}
			
			if(("100019".equals(p.getCompanyId().toString()) && errorList.isEmpty()) || (100019!=p.getCompanyId())) {
				// vehicle request
				vehicleRequest.put("BrokerBranchCode", StringUtils.isBlank(p.getBrokerBranchcode())?"":p.getBrokerBranchcode());
				vehicleRequest.put("SectionId", p.getSectionId()==null?null:Arrays.asList(p.getSectionId().toString()));	
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
		
				String manufacture_year =100040==p.getCompanyId()?p.getRegistration_date().split("/")[2]
						:p.getManufactureYear();
				
				// sanlanm ivory mapping
				vehicleRequest.put("Deductibles", StringUtils.isBlank(p.getDeductiblesId())?"":p.getDeductiblesId());
				vehicleRequest.put("TransportHydro", StringUtils.isBlank(p.getTransportationOfHydrocarbons())?"":p.getTransportationOfHydrocarbons());
				vehicleRequest.put("MunicipalityTraffic", StringUtils.isBlank(p.getMunicipalityOfTrafficId())?"":p.getMunicipalityOfTrafficId());
				vehicleRequest.put("NumberOfCards", StringUtils.isBlank(p.getNoOfCards())?"":p.getNoOfCards());
				vehicleRequest.put("AggregatedValue", StringUtils.isBlank(p.getAggregatedValueId())?"":p.getAggregatedValueId());
				vehicleRequest.put("MarketValue", StringUtils.isBlank(p.getMarketValue())?"":p.getMarketValue());
				vehicleRequest.put("VehicleValueType", StringUtils.isBlank(p.getVehicleValueTypeId())?"":p.getVehicleValueTypeId());			
				vehicleRequest.put("NoOfPassengers", StringUtils.isBlank(p.getNoOfPassengers())?"":p.getNoOfPassengers());
				vehicleRequest.put("HorsePower", StringUtils.isBlank(p.getHorsePower())?"":p.getHorsePower());
				vehicleRequest.put("RegistrationDate", StringUtils.isBlank(p.getRegistration_date())?"":p.getRegistration_date());
				vehicleRequest.put("Vehiclemake",StringUtils.isBlank(p.getVehicleMake())?"":p.getVehicleMake());
				vehicleRequest.put("VehiclemakeId",StringUtils.isBlank(p.getVehicleMakeId())?"":p.getVehicleMakeId());
				vehicleRequest.put("VehcilemodelId",StringUtils.isBlank(p.getVehicleModelId())?"":p.getVehicleModelId());
				vehicleRequest.put("VehicleModel",StringUtils.isBlank(p.getVehicleModel())?"":p.getVehicleModel());

				
				vehicleRequest.put("EngineNumber", StringUtils.isBlank(p.getEngineNumber())?"":p.getEngineNumber());
				vehicleRequest.put("ManufactureYear",manufacture_year);
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
	
	

	// In APIItemProcessor.java

	/**
	 * Full async pipeline for one vehicle:
	 *   TIRA → Save1 → Save2 → Calc
	 * Each stage waits for the previous stage's response before starting.
	 * All vehicles run their pipelines concurrently with each other.
	 */
	public CompletableFuture<EserviceMotorDetailsRaw> processTiraFlowAsync(EserviceMotorDetailsRaw p) {

	    List<Map<String, Object>> errorList = new CopyOnWriteArrayList<>(); // thread-safe list

	    // ── STAGE 1: TIRA ─────────────────────────────────────────────────────────
	    HashMap<String, Object> tiraRequest = new LinkedHashMap<>();
	    tiraRequest.put("InsuranceId",      p.getCompanyId().toString());
	    tiraRequest.put("BranchCode",       StringUtils.isBlank(p.getBranchCode())       ? "" : p.getBranchCode());
	    tiraRequest.put("BrokerBranchCode", StringUtils.isBlank(p.getBrokerBranchcode()) ? "" : p.getBrokerBranchcode());
	    tiraRequest.put("ProductId",        p.getProductId() == null                     ? "" : p.getProductId().toString());
	    tiraRequest.put("CreatedBy",        StringUtils.isBlank(p.getCreatedBy())        ? p.getLoginId() : p.getCreatedBy());
	    tiraRequest.put("SavedFrom",        StringUtils.isBlank(p.getSavedFrom())        ? "API" : p.getSavedFrom());
	    tiraRequest.put("ReqRegNumber",     StringUtils.isBlank(p.getSearchByData())     ? "" : p.getSearchByData());
	    tiraRequest.put("ReqChassisNumber", "");
	    String tiraReqJson = print.toJson(tiraRequest);
	    log.info("processTiraFlowAsync || TIRA start sno={}", p.getSno());

	    long startTime = System.currentTimeMillis();
	    String sno = p.getSno().toString();
	    
	    log.info("⏱ [sno={}] PIPELINE START | thread={}", 
	        sno, Thread.currentThread().getName());
	    return service.callApiAsync(tiraReqJson, auth, mediaType, tiraApi)

	        // ── STAGE 2: after TIRA response Save1 ──────────────────────────────
	    		.thenCompose(tiraResponse -> {
	                log.info("⏱ [sno={}] TIRA DONE | elapsed={}ms | thread={}", 
	                    sno, System.currentTimeMillis() - startTime,
	                    Thread.currentThread().getName());

	            if (tiraResponse == null || !"Success".equalsIgnoreCase(getStr(tiraResponse, "Message"))) {
	                List<Map<String, Object>> tiraErrors = tiraResponse == null ? new ArrayList<>()
	                        : tiraResponse.get("ErrorMessage") == null ? new ArrayList<>()
	                        : (List<Map<String, Object>>) tiraResponse.get("ErrorMessage");
	                errorList.addAll(tiraErrors);
	                p.setTiraStatus("E");
	                p.setTiraErrorDesc(print.toJson(tiraErrors));
	                // short-circuit: return completed future with error state, skip Save1/Save2/Calc
	                return CompletableFuture.completedFuture(p);
	            }

	            p.setTiraStatus("Y");
	            Map<String, Object> tiraResult = (Map<String, Object>) tiraResponse.get("Result");

	            // extract TIRA fields
	            String chassisNumber   = getStr(tiraResult, "Chassisnumber");
	            String vehicleMake     = getStr(tiraResult, "Vehiclemake");
	            String vehicleModel    = getStr(tiraResult, "Vehcilemodel");
	            String vehicleType     = getStr(tiraResult, "VehicleType");
	            String color           = getStr(tiraResult, "Color");
	            String engineNumber    = getStr(tiraResult, "EngineNumber");
	            String engineCapacity  = getStr(tiraResult, "ResEngineCapacity");
	            String fuelType        = getStr(tiraResult, "FuelType");
	            String numberOfAxels   = getStr(tiraResult, "NumberOfAxels");
	            String axelDistance    = getStr(tiraResult, "AxelDistance");
	            String seatingCapacity = getStr(tiraResult, "SeatingCapacity");
	            String manufactureYear = getStr(tiraResult, "ManufactureYear");
	            String tareWeight      = getStr(tiraResult, "Tareweight");
	            String grossWeight     = getStr(tiraResult, "Grossweight");
	            String motorUsageDesc  = getStr(tiraResult, "Motorusage");
	            String ownerCategory   = getStr(tiraResult, "OwnerCategory");
	            String motorCategory   = getStr(tiraResult, "MotorCategory");
	            String resOwnerName    = getStr(tiraResult, "ResOwnerName");

	            
	            String sectionId       = masterRepo.findSectionIdByDesc(p.getCompanyId(), p.getProductId(), p.getBranchCode(), p.getInsuranceTypeDesc());
	            String motorUsageId    = masterRepo.findMotorUsageIdByDesc(p.getCompanyId(), sectionId, p.getBranchCode(), p.getMotorUsageDesc());
	            String insuranceTypeId = masterRepo.findPolicyTypeByDesc(p.getCompanyId(), p.getProductId(), p.getBranchCode(), p.getInsuranceTypeDesc());
	            String insuranceClassId= masterRepo.findInsuranceClassIdByDesc(p.getCompanyId(), p.getProductId(), p.getBranchCode(), p.getInsuranceClassDesc());

	            // build Save1 request
	            HashMap<String, Object> save1Request = buildSaveMotorRequest(
	                    p, chassisNumber, vehicleMake, vehicleModel, vehicleType,
	                    color, engineNumber, engineCapacity, fuelType, numberOfAxels,
	                    axelDistance, seatingCapacity, manufactureYear, tareWeight,
	                    grossWeight, motorUsageDesc, motorUsageId, ownerCategory,
	                    motorCategory, resOwnerName, sectionId, insuranceTypeId, insuranceClassId);
	            save1Request.put("SaveOrSubmit",       "Save");
	            save1Request.put("RequestReferenceNo", p.getRequestReferenceNo());
	            save1Request.put("VehicleId",          p.getVehicleId().toString());

	            log.info("processTiraFlowAsync || Save1 start sno={}", p.getSno());

	            // carry context forward via a small holder record
	            TiraContext ctx = new TiraContext(chassisNumber, vehicleMake, vehicleModel,
	                    vehicleType, color, engineNumber, engineCapacity, fuelType,
	                    numberOfAxels, axelDistance, seatingCapacity, manufactureYear,
	                    tareWeight, grossWeight, motorUsageDesc, motorUsageId,
	                    ownerCategory, motorCategory, resOwnerName,
	                    sectionId, insuranceTypeId, insuranceClassId);

	            return service.callApiAsync(print.toJson(save1Request), auth, mediaType, vehicleApi)

	                // ── STAGE 3: after Save1 response → Save2 ─────────────────────
	            		.thenCompose(save1Response -> {
	                        log.info("⏱ TIMER || sno={} | Save1 done | elapsed={}ms", 
	                            p.getSno(), System.currentTimeMillis() - startTime);

	                    if (save1Response == null) {
	                        p.setMotorErrorDesc("Null response from save1");
	                        errorList.add(Map.of("Message", "Null response from save1"));
	                        return CompletableFuture.completedFuture(p);
	                    }
	                    List<Map<String, Object>> save1Errors = save1Response.get("ErrorMessage") == null
	                            ? new ArrayList<>()
	                            : (List<Map<String, Object>>) save1Response.get("ErrorMessage");
	                    if (!save1Errors.isEmpty() || !"Success".equalsIgnoreCase(getStr(save1Response, "Message"))) {
	                        errorList.addAll(save1Errors);
	                        p.setMotorErrorDesc(print.toJson(save1Errors));
	                        return CompletableFuture.completedFuture(p);
	                    }

	                    List<Map<String, Object>> save1ResultList = (List<Map<String, Object>>) save1Response.get("Result");
	                    Map<String, Object> save1Result   = save1ResultList.get(0);
	                    String updatedRrn                 = getStr(save1Result, "RequestReferenceNo");
	                    String vehicleIdFromSave1          = getStr(save1Result, "VehicleId");

	                    // DB lookup for Save2 enrichment (keep synchronous — fast query)
	                    Map<String, Object> motorIds = masterRepo.findVehicleIdsFromMotorDetails(updatedRrn, vehicleIdFromSave1);

	                    HashMap<String, Object> save2Request = buildSaveMotorRequest(
	                            p, ctx.chassisNumber, ctx.vehicleMake, ctx.vehicleModel, ctx.vehicleType,
	                            ctx.color, ctx.engineNumber, ctx.engineCapacity, ctx.fuelType,
	                            ctx.numberOfAxels, ctx.axelDistance, ctx.seatingCapacity,
	                            ctx.manufactureYear, ctx.tareWeight, ctx.grossWeight,
	                            ctx.motorUsageDesc, ctx.motorUsageId, ctx.ownerCategory,
	                            ctx.motorCategory, ctx.resOwnerName,
	                            ctx.sectionId, ctx.insuranceTypeId, ctx.insuranceClassId);
	                    save2Request.put("SaveOrSubmit",       "Submit");
	                    save2Request.put("RequestReferenceNo", updatedRrn);
	                    save2Request.put("VehicleId",          p.getVehicleId().toString());
	                    enrichSave2WithMotorIds(save2Request, motorIds, ctx.sectionId);

	                    log.info("processTiraFlowAsync || Save2 start sno={}", p.getSno());

	                    // carry updatedRrn + sectionId into next stage
	                    return service.callApiAsync(print.toJson(save2Request), auth, mediaType, vehicleApi)

	                        // ── STAGE 4: after Save2 response → Calc ──────────────
	                    		.thenCompose(save2Response -> {
	                                log.info("⏱ TIMER || sno={} | Save2 done | elapsed={}ms", 
	                                    p.getSno(), System.currentTimeMillis() - startTime);


	                            if (save2Response == null) {
	                                p.setMotorErrorDesc("Null response from save2");
	                                errorList.add(Map.of("Message", "Null response from save2"));
	                                return CompletableFuture.completedFuture(p);
	                            }
	                            List<Map<String, Object>> save2Errors = save2Response.get("ErrorMessage") == null
	                                    ? new ArrayList<>()
	                                    : (List<Map<String, Object>>) save2Response.get("ErrorMessage");
	                            if (!save2Errors.isEmpty() || !"Success".equalsIgnoreCase(getStr(save2Response, "Message"))) {
	                                errorList.addAll(save2Errors);
	                                p.setMotorErrorDesc(print.toJson(save2Errors));
	                                return CompletableFuture.completedFuture(p);
	                            }

	                            List<Map<String, Object>> save2ResultList = (List<Map<String, Object>>) save2Response.get("Result");
	                            Map<String, Object> save2Result = save2ResultList.get(0);
	                            String msRefNo    = getStr(save2Result, "MSRefNo");
	                            String cdRefNo    = getStr(save2Result, "CdRefNo");
	                            String vdRefNo    = getStr(save2Result, "VdRefNo");
	                            String ddRefNo    = getStr(save2Result, "DdRefNo");
	                            String locationId = getStr(save2Result, "LocationId");

	                            HashMap<String, Object> calcRequest = new LinkedHashMap<>();
	                            calcRequest.put("InsuranceId",        p.getCompanyId());
	                            calcRequest.put("BranchCode",         StringUtils.isBlank(p.getBranchCode()) ? "" : p.getBranchCode());
	                            calcRequest.put("AgencyCode",         StringUtils.isBlank(p.getAgencyCode()) ? "" : p.getAgencyCode());
	                            calcRequest.put("SectionId",          ctx.sectionId);
	                            calcRequest.put("ProductId",          p.getProductId());
	                            calcRequest.put("MSRefNo",            msRefNo);
	                            calcRequest.put("VehicleId",          p.getVehicleId().toString());
	                            calcRequest.put("CdRefNo",            cdRefNo);
	                            calcRequest.put("DdRefNo",            StringUtils.isBlank(ddRefNo) ? "0" : ddRefNo);
	                            calcRequest.put("VdRefNo",            vdRefNo);
	                            calcRequest.put("LocationId",         StringUtils.isBlank(locationId) ? "1" : locationId);
	                            calcRequest.put("CreatedBy",          StringUtils.isBlank(p.getCreatedBy()) ? p.getLoginId() : p.getCreatedBy());
	                            calcRequest.put("productId",          p.getProductId());
	                            calcRequest.put("RequestReferenceNo", updatedRrn);
	                            calcRequest.put("EffectiveDate",      StringUtils.isBlank(p.getPolicyStartDate()) ? "" : p.getPolicyStartDate());
	                            calcRequest.put("PolicyEndDate",      StringUtils.isBlank(p.getPolicyEndDate())   ? "" : p.getPolicyEndDate());
	                            calcRequest.put("CoverModification",  "N");
	                            p.setCalcRequest(print.toJson(calcRequest));

	                            log.info("processTiraFlowAsync || Calc start sno={}", p.getSno());

	                            return service.callApiAsync(print.toJson(calcRequest), auth, mediaType, calcApi)
	                            		.thenApply(calcResponse -> {
	                                        log.info("⏱ TIMER || sno={} | Calc done | elapsed={}ms | TOTAL={}ms", 
	                                            p.getSno(), System.currentTimeMillis() - startTime,
	                                            System.currentTimeMillis() - startTime);
	                                    
	                                    if (calcResponse == null) {
	                                        String msg = "Null response from calc API";
	                                        errorList.add(Map.of("Message", msg));
	                                        p.setCalcErrorDesc(msg);
	                                        return p;
	                                    }
	                                    List<Map<String, Object>> calcErrors = calcResponse.get("ErrorMessage") == null
	                                            ? new ArrayList<>()
	                                            : (List<Map<String, Object>>) calcResponse.get("ErrorMessage");
	                                    if (!calcErrors.isEmpty()) {
	                                        errorList.addAll(calcErrors);
	                                        p.setCalcErrorDesc(print.toJson(calcErrors));
	                                    } else {
	                                        p.setStatus("Y");
	                                        p.setApiStatus("SUCCESS");
	                                    }
	                                    return p;
	                                });
	                        });
	                });
	        })

	        // ── FINAL: flush errorList onto entity ────────────────────────────────
	        .thenApply(result -> {
	            if (!errorList.isEmpty() && !"Y".equals(result.getStatus())) {
	                result.setErrorDesc(print.toJson(new ArrayList<>(errorList)));
	                result.setStatus("E");
	            }
	            return result;
	        })

	        // ── SAFETY NET: catch any unchecked exception in the chain ────────────
	        .exceptionally(ex -> {
	            log.error("processTiraFlowAsync || unhandled error sno={}", p.getSno(), ex);
	            p.setStatus("E");
	            p.setErrorDesc(ex.getMessage());
	            return p;
	        });
	}

    // Polls rrnCache until sno=1 populates it, or times out after 10 seconds.
    private String waitForCachedRrn(String rawRrn, Integer sno) throws InterruptedException {
        for (int attempt = 0; attempt < RRN_WAIT_MAX_ATTEMPTS; attempt++) {
            String cached = rrnCache.get(rawRrn);
            if (!StringUtils.isBlank(cached)) {
                log.info("waitForCachedRrn || sno={} found cachedRrn={} on attempt={}", sno, cached, attempt + 1);
                return cached;
            }
            log.info("waitForCachedRrn || sno={} waiting for RRN | rawRrn={} | attempt={}/{}", 
                sno, rawRrn, attempt + 1, RRN_WAIT_MAX_ATTEMPTS);
            Thread.sleep(RRN_WAIT_SLEEP_MS);
        }
        return null; // timed out
    }

	
	// CHANGED: removed msRefNo, cdRefNo, vdRefNo params (never used inside builder)
	// CHANGED: removed req.put("RequestReferenceNo") — caller sets it after build
	private HashMap<String, Object> buildSaveMotorRequest(
	    EserviceMotorDetailsRaw p,
	    String chassisNumber, String vehicleMake, String vehicleModel, String vehicleType,
	    String color, String engineNumber, String engineCapacity, String fuelType,
	    String numberOfAxels, String axelDistance, String seatingCapacity,
	    String manufactureYear, String tareWeight, String grossWeight,
	    String motorUsageDesc, String motorUsageId, String ownerCategory,
	    String motorCategory, String resOwnerName,
	    String sectionId, String insuranceTypeId, String insuranceClassId
	) {
	    HashMap<String, Object> req = new LinkedHashMap<>();

	    // RequestReferenceNo intentionally NOT set here — caller sets it
	    req.put("InsuranceId",          p.getCompanyId());
	    req.put("BranchCode",           StringUtils.isBlank(p.getBranchCode())          ? "" : p.getBranchCode());
	    req.put("AgencyCode",           StringUtils.isBlank(p.getAgencyCode())          ? "" : p.getAgencyCode());
	    req.put("ProductId",            p.getProductId());
	    req.put("SectionId",            sectionId == null ? null : Arrays.asList(sectionId));
	    req.put("CustomerReferenceNo",  StringUtils.isBlank(p.getCustomerReferenceno()) ? "" : p.getCustomerReferenceno());
	    req.put("Idnumber",             StringUtils.isBlank(p.getIdNumber())            ? "" : p.getIdNumber());
	    req.put("VehicleId",            p.getSno());
	    req.put("LoginId",              StringUtils.isBlank(p.getLoginId())             ? "" : p.getLoginId());
	    req.put("SubUserType",          StringUtils.isBlank(p.getSubUsertype())         ? "" : p.getSubUsertype());
	    req.put("ApplicationId",        StringUtils.isBlank(p.getApplicationId())       ? "" : p.getApplicationId());
	    req.put("UserType",             StringUtils.isBlank(p.getUserType())            ? "" : p.getUserType());
	    req.put("BrokerCode",           StringUtils.isBlank(p.getBrokerCode())          ? "" : p.getBrokerCode());
	    req.put("BrokerBranchCode",     StringUtils.isBlank(p.getBrokerBranchcode())    ? "" : p.getBrokerBranchcode());
	    req.put("CustomerCode",         StringUtils.isBlank(p.getCustomerCode())        ? "" : p.getCustomerCode());
	    req.put("CustomerName",         StringUtils.isBlank(p.getCustomerName())        ? "" : p.getCustomerName());
	    req.put("BdmCode",              StringUtils.isBlank(p.getBdmCode())             ? "" : p.getBdmCode());
	    req.put("CreatedBy",            StringUtils.isBlank(p.getCreatedBy())           ? p.getLoginId() : p.getCreatedBy());
	    req.put("SavedFrom",            StringUtils.isBlank(p.getSavedFrom())           ? "API" : p.getSavedFrom());
	    req.put("SourceTypeId",         StringUtils.isBlank(p.getSourceTypeId())        ? "" : p.getSourceTypeId());
	    req.put("SourceType",           StringUtils.isBlank(p.getSourceType())          ? "" : p.getSourceType());
	    req.put("BusinessSourceId",     "");
	    req.put("BusinessSource",       "");

	    // Vehicle from TIRA
	    req.put("Chassisnumber",        chassisNumber);
	    req.put("Registrationnumber",   StringUtils.isBlank(p.getSearchByData()) ? chassisNumber : p.getSearchByData());
	    req.put("Vehiclemake",          vehicleMake);
	    req.put("Vehcilemodel",         vehicleModel);
	    req.put("VehicleType",          vehicleType);
	    req.put("Color",                color);
	    req.put("EngineNumber",         engineNumber);
	    req.put("CubicCapacity",        StringUtils.isBlank(engineCapacity) ? null : new BigDecimal(engineCapacity).intValue());
	    req.put("EngineCapacity",       StringUtils.isBlank(engineCapacity) ? null : new BigDecimal(engineCapacity).intValue());
	    req.put("FuelType",             fuelType);
	    req.put("NumberOfAxels",        numberOfAxels);
	    req.put("AxelDistance",         StringUtils.isBlank(axelDistance)    ? "0"  : axelDistance);
	    req.put("SeatingCapacity",      StringUtils.isBlank(seatingCapacity) ? null : Integer.parseInt(seatingCapacity));
	    req.put("ManufactureYear",      manufactureYear);
	    req.put("Tareweight",           StringUtils.isBlank(tareWeight)      ? null : new BigDecimal(tareWeight));
	    req.put("Grossweight",          StringUtils.isBlank(grossWeight)     ? null : new BigDecimal(grossWeight));
	    req.put("MotorCategory",        StringUtils.isBlank(motorCategory)   ? "1"  : motorCategory);
	    req.put("OwnerCategory",        ownerCategoryCode(ownerCategory));
	    req.put("DrivenByDesc",         "D");
	    req.put("MobileCode",           "255");
	    req.put("MobileNumber",         "");
	    req.put("HoldInsurancePolicy",  "N");
	    req.put("InsurerSettlement",    "");
	    req.put("InterestedCompanyDetails", "");
	    req.put("AccessoriesInformation",   "");
	    req.put("AdditionalCircumstances",  "");

	    // Insurance IDs
	    req.put("Insurancetype",        StringUtils.isBlank(insuranceTypeId)  ? "" : insuranceTypeId);
	    req.put("InsurancetypeDesc",    StringUtils.isBlank(p.getInsuranceTypeDesc())  ? "" : p.getInsuranceTypeDesc());
	    req.put("InsuranceClass",       StringUtils.isBlank(insuranceClassId) ? "" : insuranceClassId);
	    req.put("InsuranceClassDesc",   StringUtils.isBlank(p.getInsuranceClassDesc()) ? "" : p.getInsuranceClassDesc());
	    req.put("Motorusage",           motorUsageDesc);
	    req.put("MotorusageId",         StringUtils.isBlank(motorUsageId)     ? "" : motorUsageId);
	    req.put("PolicyType",           StringUtils.isBlank(insuranceClassId) ? "" : insuranceClassId);

	    // Sum insured
	    req.put("SumInsured",             StringUtils.isBlank(p.getVehicleSuminsured())      ? null : new BigDecimal(p.getVehicleSuminsured()));
	    req.put("AcccessoriesSumInsured", StringUtils.isBlank(p.getAccessoriesSuminsured())  ? null : new BigDecimal(p.getAccessoriesSuminsured()));
	    req.put("WindScreenSumInsured",   StringUtils.isBlank(p.getWindshieldSuminsured())   ? null : new BigDecimal(p.getWindshieldSuminsured()));
	    req.put("TppdIncreaeLimit",       StringUtils.isBlank(p.getExtendedSuminsured())     ? null : p.getExtendedSuminsured());

	    // Policy period
	    req.put("PolicyStartDate",    StringUtils.isBlank(p.getPolicyStartDate()) ? "" : p.getPolicyStartDate());
	    req.put("PolicyEndDate",      StringUtils.isBlank(p.getPolicyEndDate())   ? "" : p.getPolicyEndDate());
	    req.put("periodOfInsurance",  "365");
	    req.put("Currency",           StringUtils.isBlank(p.getCurrency())        ? "" : p.getCurrency());
	    req.put("ExchangeRate",       StringUtils.isBlank(p.getExchangeRate())    ? "1.0" : p.getExchangeRate());
	    req.put("QuoteExpiryDays",    "90");

	    // Collateral
	    String collateralYn = StringUtils.isBlank(p.getCollateralYn()) ? "N" : p.getCollateralYn();
	    if ("Yes".equalsIgnoreCase(collateralYn) || "Y".equalsIgnoreCase(collateralYn)) {
	        req.put("CollateralYn",    "Y");
	        req.put("BorrowerType",    StringUtils.isBlank(p.getBorrowerType()) ? null
	                                    : "Bank".equalsIgnoreCase(p.getBorrowerType()) ? "1" : "2");
	        req.put("CollateralName",  "Bank".equalsIgnoreCase(p.getBorrowerType()) ? p.getBankId() : p.getCollateral());
	        req.put("FirstLossPayee",  StringUtils.isBlank(p.getFirstLossPayee()) ? "" : p.getFirstLossPayee());
	    } else {
	        req.put("CollateralYn",   "N");
	        req.put("BorrowerType",   null);
	        req.put("CollateralName", null);
	        req.put("FirstLossPayee", null);
	    }

	    // Promo
	    String promoYn = StringUtils.isBlank(p.getHavePromocode()) ? "N" : p.getHavePromocode();
	    req.put("HavePromoCode", promoYn);
	    req.put("PromoCode",     "Y".equalsIgnoreCase(promoYn) ? p.getPromocode() : null);

	    // NCD / claim
	    String claimYn = StringUtils.isBlank(p.getClaimYn()) ? "N"
	                     : "yes".equalsIgnoreCase(p.getClaimYn()) ? "Y" : "N";
	    req.put("NcdYn",   claimYn);
	    req.put("ClaimYn", claimYn);
	    if ("Y".equals(claimYn)) req.put("accident", "Y");
	    else                     req.put("accident", null);

	    // GPS / fleet / misc
	    req.put("Gpstrackinginstalled", "yes".equalsIgnoreCase(p.getGpsTrackingEnabled()) ? "Y" : "N");
	    req.put("FleetOwnerYn",  "N");
	    req.put("NoOfVehicles",  "1");
	    req.put("Status",        "Y");
	    req.put("SearchFromApi", true);
	    req.put("Zone",          "1");

	    // Endorsement
	    String endorsementYn = StringUtils.isBlank(p.getEndorsementYn()) ? "N" : p.getEndorsementYn();
	    req.put("EndorsementYn", endorsementYn);
	    if ("Y".equalsIgnoreCase(endorsementYn)) {
	        req.put("EndorsementDate",          StringUtils.isBlank(p.getEndorsementDate())           ? "" : p.getEndorsementDate());
	        req.put("EndorsementEffectiveDate", StringUtils.isBlank(p.getEndorsementEffectiveDate())  ? "" : p.getEndorsementEffectiveDate());
	        req.put("EndorsementRemarks",       StringUtils.isBlank(p.getEndorsementRemarks())        ? "" : p.getEndorsementRemarks());
	        req.put("EndorsementType",          StringUtils.isBlank(p.getEndorsementType())           ? "" : p.getEndorsementType());
	        req.put("EndorsementTypeDesc",      StringUtils.isBlank(p.getEndorsementTypeDesc())       ? "" : p.getEndorsementTypeDesc());
	        req.put("EndtCategoryDesc",         StringUtils.isBlank(p.getEndtCategoryDesc())          ? "" : p.getEndtCategoryDesc());
	        req.put("EndtCount",                StringUtils.isBlank(p.getEndtCount())                 ? "" : p.getEndtCount());
	        req.put("EndtPrevPolicyNo",         StringUtils.isBlank(p.getEndtPrevPolicyno())          ? "" : p.getEndtPrevPolicyno());
	        req.put("EndtStatus",               StringUtils.isBlank(p.getEndtStatus())                ? "" : p.getEndtStatus());
	        req.put("IsFinanceEndt",            StringUtils.isBlank(p.getIsFinanceEndt())             ? "" : p.getIsFinanceEndt());
	        req.put("OrginalPolicyNo",          StringUtils.isBlank(p.getOrginalPolicyno())           ? "" : p.getOrginalPolicyno());
	    } else {
	        req.put("EndorsementDate", null); req.put("EndorsementEffectiveDate", null);
	        req.put("EndorsementRemarks", null); req.put("EndorsementType", null);
	        req.put("EndorsementTypeDesc", null); req.put("EndtCategoryDesc", null);
	        req.put("EndtCount", null); req.put("EndtPrevPolicyNo", null);
	        req.put("EndtStatus", null); req.put("IsFinanceEndt", null);
	        req.put("OrginalPolicyNo", null);
	    }

	    // Extra fields
	    req.put("Deductibles",         StringUtils.isBlank(p.getDeductiblesId())                ? null : p.getDeductiblesId());
	    req.put("TransportHydro",      StringUtils.isBlank(p.getTransportationOfHydrocarbons()) ? null : p.getTransportationOfHydrocarbons());
	    req.put("MunicipalityTraffic", StringUtils.isBlank(p.getMunicipalityOfTrafficId())      ? null : p.getMunicipalityOfTrafficId());
	    req.put("NumberOfCards",       StringUtils.isBlank(p.getNoOfCards())                    ? null : p.getNoOfCards());
	    req.put("AggregatedValue",     StringUtils.isBlank(p.getAggregatedValueId())            ? "null" : p.getAggregatedValueId());
	    req.put("MarketValue",         StringUtils.isBlank(p.getMarketValue())                  ? null : p.getMarketValue());
	    req.put("VehicleValueType",    StringUtils.isBlank(p.getVehicleValueTypeId())           ? "" : p.getVehicleValueTypeId());
	    req.put("NoOfPassengers",      StringUtils.isBlank(p.getNoOfPassengers())               ? null : p.getNoOfPassengers());
	    req.put("HorsePower",          StringUtils.isBlank(p.getHorsePower())                   ? "" : p.getHorsePower());

	    // Scenarios
	    Map<String, Object> oldScenario = new LinkedHashMap<>();
	    oldScenario.put("OldAcccessoriesSumInsured", null);
	    oldScenario.put("OldCurrency",           StringUtils.isBlank(p.getCurrency())     ? "TZS" : p.getCurrency());
	    oldScenario.put("OldExchangeRate",        StringUtils.isBlank(p.getExchangeRate()) ? "1.0" : p.getExchangeRate());
	    oldScenario.put("OldSumInsured",          null);
	    oldScenario.put("OldTppdIncreaeLimit",    null);
	    oldScenario.put("OldWindScreenSumInsured",null);
	    Map<String, Object> scenarios = new LinkedHashMap<>();
	    scenarios.put("ExchangeRateScenario", oldScenario);
	    req.put("Scenarios", scenarios);

	    return req;
	}

		private String getStr(Map<String, Object> map, String key) {
		    Object val = map.get(key);
		    return val == null ? "" : val.toString();
		}

		// Map TIRA owner category description → numeric code used by savemotordetails
		private String ownerCategoryCode(String desc) {
		    if (StringUtils.isBlank(desc)) return "1";
		    switch (desc.trim().toLowerCase()) {
		        case "sole proprietor":   return "1";
		        case "company":           return "2";
		        case "government":        return "3";
		        default:                  return "1";
		    }
		}
		
		// ── helper: enrich Save2 with DB-resolved IDs ─────────────────────────────────
		private void enrichSave2WithMotorIds(HashMap<String, Object> req,
		                                      Map<String, Object> motorIds,
		                                      String sectionId) {
		    if (motorIds == null || motorIds.isEmpty()) return;
		    req.put("VehiclemakeId",     motorIds.get("vehicleMakeId"));
		    req.put("VehcilemodelId",    motorIds.get("vehicleModelId"));
		    req.put("VehicleTypeId",     motorIds.get("vehicleTypeId"));
		    req.put("FuelTypeDesc",      motorIds.get("fuelTypeDesc"));
		    req.put("ColorDesc",         motorIds.get("colorDesc"));
		    req.put("OwnerCategory",     motorIds.get("ownerCategoryId"));
		    req.put("MotorusageId",      motorIds.get("motorUsageId"));
		    req.put("Insurancetype",     motorIds.get("insuranceTypeId"));
		    req.put("InsurancetypeDesc", motorIds.get("insuranceTypeDesc"));
		    req.put("InsuranceClass",    motorIds.get("insuranceClassId"));
		    req.put("InsuranceClassDesc",motorIds.get("insuranceClassDesc"));
		    req.put("PolicyType",        motorIds.get("insuranceClassId"));
		    req.put("SectionId",         motorIds.get("sectionId") == null ? null
		                                    : Arrays.asList(motorIds.get("sectionId").toString()));
		    Object cc = motorIds.get("cubicCapacity");
		    if (cc != null && !cc.toString().isBlank()) {
		        int ccInt = new BigDecimal(cc.toString()).intValue();
		        req.put("CubicCapacity",  ccInt);
		        req.put("EngineCapacity", ccInt);
		    }
		    req.put("PaCoverId",              "0");
		    req.put("ClaimType",              "0");
		    req.put("VehicleValueType",       "");
		    req.put("Inflation",              "");
		    req.put("Ncb",                    "0");
		    req.put("DefenceValue",           "");
		    req.put("BankingDelegation",      "");
		    req.put("LoanAmount",             0);
		    req.put("UsageId",                "");
		    req.put("VehicleTypeIvr",         "");
		    req.put("CollateralCompanyAddress","");
		    req.put("CollateralCompanyName",   "");
		    req.put("PreviousInsuranceYN",    "N");
		    req.put("PreviousLossRatio",      "");
		    req.put("PolicyRenewalYn",        "N");
		    req.put("DriverDetails",          null);
		    req.put("AboutVehicle",           null);
		}
		
		// Inside APIItemProcessor.java — at the bottom

		private static class TiraContext {
		    final String chassisNumber, vehicleMake, vehicleModel, vehicleType;
		    final String color, engineNumber, engineCapacity, fuelType;
		    final String numberOfAxels, axelDistance, seatingCapacity, manufactureYear;
		    final String tareWeight, grossWeight, motorUsageDesc, motorUsageId;
		    final String ownerCategory, motorCategory, resOwnerName;
		    final String sectionId, insuranceTypeId, insuranceClassId;

		    TiraContext(String chassisNumber, String vehicleMake, String vehicleModel,
		                String vehicleType, String color, String engineNumber,
		                String engineCapacity, String fuelType, String numberOfAxels,
		                String axelDistance, String seatingCapacity, String manufactureYear,
		                String tareWeight, String grossWeight, String motorUsageDesc,
		                String motorUsageId, String ownerCategory, String motorCategory,
		                String resOwnerName, String sectionId,
		                String insuranceTypeId, String insuranceClassId) {
		        this.chassisNumber   = chassisNumber;   this.vehicleMake    = vehicleMake;
		        this.vehicleModel    = vehicleModel;    this.vehicleType    = vehicleType;
		        this.color           = color;           this.engineNumber   = engineNumber;
		        this.engineCapacity  = engineCapacity;  this.fuelType       = fuelType;
		        this.numberOfAxels   = numberOfAxels;   this.axelDistance   = axelDistance;
		        this.seatingCapacity = seatingCapacity; this.manufactureYear= manufactureYear;
		        this.tareWeight      = tareWeight;      this.grossWeight    = grossWeight;
		        this.motorUsageDesc  = motorUsageDesc;  this.motorUsageId   = motorUsageId;
		        this.ownerCategory   = ownerCategory;   this.motorCategory  = motorCategory;
		        this.resOwnerName    = resOwnerName;    this.sectionId      = sectionId;
		        this.insuranceTypeId = insuranceTypeId; this.insuranceClassId = insuranceClassId;
		    }
		}
		
		public void setService(VehicleAsynchronousProcess service) {
		    this.service = service;
		}

		public void setMasterRepo(MasterLookupRepository masterRepo) {
		    this.masterRepo = masterRepo;
		}
		
		// Add these setters in APIItemProcessor.java
		// Keep all existing @Value annotations as-is (they work when Spring creates the bean,
		// but we need setters as fallback for manual construction)

		public void setTiraApi(String tiraApi) {
		    this.tiraApi = tiraApi;
		}

		public void setVehicleApi(String vehicleApi) {
		    this.vehicleApi = vehicleApi;
		}

		public void setCalcApi(String calcApi) {
		    this.calcApi = calcApi;
		}

		public void setSaveVehicleApi(String saveVehicleApi) {
		    this.saveVehicleApi = saveVehicleApi;
		}
	
}
