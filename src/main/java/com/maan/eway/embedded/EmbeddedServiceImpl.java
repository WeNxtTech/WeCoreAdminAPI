package com.maan.eway.embedded;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.persistence.Tuple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.fileupload.JpqlQueryServiceImpl;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.res.CommonRes;

@Service
public class EmbeddedServiceImpl implements EmbeddedService {
	
	Logger log =LogManager.getLogger(EmbeddedServiceImpl.class);

	private static Gson printReq = new Gson();
	
	private SimpleDateFormat sdf =JpqlQueryServiceImpl.sdf;
	
	@Autowired
	private JpqlQueryServiceImpl query;
	
	@Autowired
	private EmbeddedRepository  embeddedRepository;
	
	@Autowired
	private ListItemValueRepository listItemValueRepository;
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	@Override
	public CommonRes getEmbeddedDetails(EmbeddedReq req) {
		log.info("getEmbeddedDetails request : "+printReq.toJson(req));
		CommonRes response = new CommonRes();
		try {
			 String pattern = "#####0.00";
			 DecimalFormat df = new DecimalFormat(pattern);

			List<Tuple> list=query.getEmbeddedDetails(req);
			if(list.size()>0) {
				List<EmbeddedRes> resList =list.stream().map(p ->{
					EmbeddedRes embeddedRes =EmbeddedRes.builder()
							.companyId(p.get("companyId")==null?"":p.get("companyId").toString())
							.productId(p.get("productId")==null?"":p.get("productId").toString())
							.amountPaid(p.get("amountPaid")==null?"":df.format(p.get("amountPaid")))
							.customerName(p.get("customerName")==null?"":p.get("customerName").toString())
							.filePath(p.get("pdfPath")==null?"":p.get("pdfPath").toString())
							.loginId(p.get("loginId")==null?"":p.get("loginId").toString())
							.mobileNo(p.get("mobileNo")==null?"":p.get("mobileNo").toString())
							.nidaNo(p.get("nidaNo")==null?"":p.get("nidaNo").toString())
							.planType(p.get("planTypeDesc")==null?"":p.get("planTypeDesc").toString())
							.policyEndDate(p.get("expiryDate")==null?"":sdf.format(p.get("expiryDate")))
							.policyStartDate(p.get("inceptionDate")==null?"":sdf.format(p.get("inceptionDate")))
							.policyNo(p.get("policyNo")==null?"":p.get("policyNo").toString())
							.premium(p.get("premium")==null?"0":df.format(p.get("premium")))
							.requestReferenceNo(p.get("requestReferenceNo")==null?"":p.get("requestReferenceNo").toString())
							.taxPremium(p.get("taxPremium")==null?"0":df.format(p.get("taxPremium")))
							.transactionNo(p.get("clientTransactionNo")==null?"":p.get("clientTransactionNo").toString())
							.commissionAmt(p.get("commissionAmount")==null?"0":df.format(p.get("commissionAmount")))
							.taxPercentage(p.get("taxPercentage")==null?"":p.get("taxPercentage").toString())
							.commissionPercentage(p.get("commissionPercentage")==null?"0":p.get("commissionPercentage").toString())
							.responsePeriod(p.get("responsePeriod")==null?"":p.get("responsePeriod").toString())
							.mobileCode(p.get("mobileCode")==null?"":p.get("mobileCode").toString())
							.overAllPremium(p.get("overallPremium")==null?"":df.format(p.get("overallPremium")))
							.build();
					return embeddedRes;
				}).collect(Collectors.toList());
				response.setCommonResponse(resList);
				response.setErrorMessage(Collections.emptyList());
				response.setIsError(false);
				response.setMessage("SUCCESS");
			}else {
				response.setCommonResponse(Collections.emptyList());
				response.setErrorMessage(Collections.emptyList());
				response.setIsError(true);
				response.setMessage("FAILED");
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}

	@Override
	public CommonRes getSearchType() {
		CommonRes response = new CommonRes();
		try {
			List<ListItemValue> list =listItemValueRepository.findByItemTypeAndStatus("EMBEDDED_SEARCH_TYPES", "Y");
			if(list.size()>0) {
				List<Map<String,String>> resList=list.stream().map(p ->{
					Map<String,String> map = new HashMap<>();
					map.put("Code", p.getItemValue());
					map.put("Description", p.getItemValue());
					return map;
				}).collect(Collectors.toList());
				
				Collections.sort(resList, new MyComparater());
				response.setCommonResponse(resList);
				response.setErrorMessage(Collections.emptyList());
				response.setIsError(false);
				response.setMessage("SUCCESS");
			}else {
				response.setCommonResponse(Collections.emptyList());
				response.setErrorMessage(Collections.emptyList());
				response.setIsError(true);
				response.setMessage("FAILED");
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}

	
	
	@Override
	public CommonRes getProductDashBoard(EmbeddedDashBoardReq req) {
		CommonRes response = new CommonRes();
		try {
			String pattern = "#####0.00";
			DecimalFormat df = new DecimalFormat(pattern);
			List<Map<String,Object>> list =embeddedRepository.getCompanyBasedDashBoard(req.getCompanyId(), req.getProductId(), req.getStartDate(), req.getEndDate()); 
			
			Map<String,Object> overAll =list.stream()
			.filter( p-> p.get("typ").toString().equalsIgnoreCase("Overall"))
			.map( p->p.entrySet())
			.flatMap(p ->p.stream())
			.collect(Collectors.toMap(Entry :: getKey, Entry :: getValue));
			
			Map<String,Object> active =list.stream()
					.filter( p-> p.get("typ").toString().equalsIgnoreCase("Active"))
					.map( p->p.entrySet())
					.flatMap(p ->p.stream())
					.collect(Collectors.toMap(Entry :: getKey, Entry :: getValue));
			
			Map<String,Object> expiry =list.stream()
					.filter(  p-> p.get("typ").toString().equalsIgnoreCase("Expiry"))
					.map( p->p.entrySet())
					.flatMap(p ->p.stream())
					.collect(Collectors.toMap(Entry :: getKey, Entry :: getValue));
					
			
			EmbeddedDashBoardRes boardRes =EmbeddedDashBoardRes.builder()
					.activePremium(active.get("total_premium")==null?"0":df.format(active.get("total_premium")))
					.companyId(req.getCompanyId())
					.loginId(req.getLoginId())
					.overAllPremium(overAll.get("total_premium")==null?"0":df.format(overAll.get("total_premium")))
					.productId(req.getProductId())
					.totalPolicy(overAll.get("total_policy")==null?"0":overAll.get("total_policy").toString())
					.activePolicyCount(active.get("total_policy")==null?"0":active.get("total_policy").toString())
					.expiryPolicyCount(expiry.get("total_policy")==null?"0":expiry.get("total_policy").toString())
					.expiryPolicyPremium(expiry.get("total_premium")==null?"0":df.format(expiry.get("total_premium")))
					.build();
			response.setCommonResponse(boardRes);
			response.setMessage("SUCCESS");
			response.setErrorMessage(Collections.EMPTY_LIST);
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}

	@Override
	public CommonRes getProductPlanTypeDashBoard(EmbeddedDashBoardReq req) {
		CommonRes response = new CommonRes();
		try {
			String pattern = "#####0.00";
			DecimalFormat df = new DecimalFormat(pattern);
			List<Map<String,Object>> list =embeddedRepository.getPlanBasedDashBoard(req.getCompanyId(),req.getProductId(),req.getStartDate(),req.getEndDate());
			List<Map<String,Object>> filterList =null;
			if("OVERALL".equalsIgnoreCase(req.getPreimumType())) {
				filterList =list.stream()
						.filter(p ->p.get("typ").toString().equalsIgnoreCase(req.getPreimumType()))
						.collect(Collectors.toList());
			}else if("ACTIVE".equalsIgnoreCase(req.getPreimumType())) {
			
				filterList =list.stream()
						.filter(p ->p.get("typ").toString().equalsIgnoreCase(req.getPreimumType()))
						.collect(Collectors.toList());
			}else if("EXPIRED".equalsIgnoreCase(req.getPreimumType())) {
			
				filterList =list.stream()
						.filter(p ->p.get("typ").toString().equalsIgnoreCase(req.getPreimumType()))
						.collect(Collectors.toList());
			}
			
			
			List<EmbeddedPlanDashBoardRes> resList =filterList.stream()
					.map(p ->{
						EmbeddedPlanDashBoardRes boardRes = new EmbeddedPlanDashBoardRes();
						boardRes.setCompanyId(p.get("company_id")==null?"":p.get("company_id").toString());
						boardRes.setLoginId(p.get("login_id")==null?"":p.get("login_id").toString());
						boardRes.setPlanId(p.get("plan_opted")==null?"":p.get("plan_opted").toString());
						boardRes.setPremium(p.get("total_premium")==null?"0":df.format(p.get("total_premium")));
						boardRes.setProductId(req.getProductId());
						boardRes.setTotalPolicy(p.get("total_policy")==null?"0":p.get("total_policy").toString());
						boardRes.setPlanType(p.get("plan_desc")==null?"0":p.get("plan_desc").toString());
						boardRes.setCommissionPremium(p.get("total_commission_amount")==null?"0":p.get("total_commission_amount").toString());
						boardRes.setPremiumType(req.getPreimumType());
						return boardRes;
					}).collect(Collectors.toList());
		
			response.setCommonResponse(resList);
			response.setMessage("SUCCESS");
			response.setErrorMessage(Collections.EMPTY_LIST);
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}

	@Override
	public CommonRes getActivePolicy(EmbeddedDashBoardReq req) {
		CommonRes response = new CommonRes();
		try {
			
			String pattern = "#####0.00";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			DecimalFormat df = new DecimalFormat(pattern);
			List<Map<String,Object>> list =embeddedRepository.getActivePolicy(req.getLoginId(), req.getCompanyId(), req.getProductId() ,
					sdf.format(req.getStartDate()), sdf.format(req.getEndDate()), req.getPlanId());
			List<EmbeddedPolicyGridRes> resList = list.stream().map(premium ->{
				Date inceptionDate=premium.get("inception_date")==null?null:(Date)premium.get("inception_date");
				Date expiryDate=premium.get("expiry_date")==null?null:(Date)premium.get("expiry_date");
				String strDate1 =sdf1.format(inceptionDate);
				String strDate2 =sdf1.format(expiryDate);
				EmbeddedPolicyGridRes boardRes =EmbeddedPolicyGridRes.builder()
						.planName(premium.get("plan_desc")==null?"0":premium.get("plan_desc").toString())
						.planOpted(premium.get("plan_opted")==null?"0":premium.get("plan_opted").toString())
						.requestReferenceNumber(premium.get("request_reference_no")==null?"0":premium.get("request_reference_no").toString())
						.companyId(req.getCompanyId())
						.loginId(req.getLoginId())
						.policyNo(premium.get("policy_no")==null?"0":premium.get("policy_no").toString())
						.mobileCode(premium.get("mobile_code")==null?"0":premium.get("mobile_code").toString())
						.mobileNo(premium.get("mobile_no")==null?"0":premium.get("mobile_no").toString())
						.sectionId(premium.get("section_id")==null?"0":premium.get("section_id").toString())
						.clientTransactionNo(premium.get("client_transaction_no")==null?"0":premium.get("client_transaction_no").toString())
						.customerName(premium.get("customer_name")==null?"":premium.get("customer_name").toString())
						.productId(req.getProductId())
						.amountPaid(premium.get("amount_paid")==null?"0":df.format(premium.get("amount_paid")))
						.premium(premium.get("premium")==null?"0":df.format(premium.get("premium")))
						.commissionPercentage(premium.get("commission_percentage")==null?"0":df.format(premium.get("commission_percentage")))
						.commissionAmount(premium.get("commission_amount")==null?"0":df.format(premium.get("commission_amount")))
						.taxPercentage(premium.get("tax_percentage")==null?"0":df.format(premium.get("tax_percentage")))
						.taxPremium(premium.get("tax_premium")==null?"0":df.format(premium.get("tax_premium")))
						.overAllPremium(premium.get("overall_premium")==null?"0":df.format(premium.get("overall_premium")))
						.totalPolicy(premium.get("total_policy")==null?"0":premium.get("total_policy").toString())
						.overAllComiPremium(premium.get("total_commission_amount")==null?"0":df.format(premium.get("total_commission_amount")))
						.overAllTaxPremium(premium.get("total_tax_premium")==null?"0":df.format(premium.get("total_tax_premium")))
						.activePolicyCount(premium.get("activePolicyCount")==null?"0":premium.get("activePolicyCount").toString())
						.expiryPolicyCount(premium.get("expiryPolicyCount")==null?"0":premium.get("expiryPolicyCount").toString())
						.expiryPolicyPremium(premium.get("expiry_premium")==null?"0":df.format(premium.get("expiry_premium")))
						.activePremium(premium.get("active_premium")==null?"0":df.format(premium.get("active_premium")))
						.filePath(premium.get("pdf_path")==null?"0":(String)premium.get("pdf_path"))
						.startDate(strDate1)
						.endDate(strDate2)
						.noOfDays(getBetweenDays(strDate1,strDate2))
						.build();
				return boardRes;
			}).collect(Collectors.toList());
			
			response.setCommonResponse(resList);
			response.setMessage("SUCCESS");
			response.setErrorMessage(Collections.EMPTY_LIST);
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}

	@Override
	public CommonRes getAllPolicy(EmbeddedDashBoardReq req) {
		CommonRes response = new CommonRes();
		try {
			String pattern = "#####0.00";
			DecimalFormat df = new DecimalFormat(pattern);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			List<Map<String,Object>> list =embeddedRepository.getAllPolicy(req.getLoginId(), req.getCompanyId(), req.getProductId() 
					, sdf.format(req.getStartDate()), sdf.format(req.getEndDate()),req.getPlanId());
			System.out.println(new Gson().toJson(list));
			List<EmbeddedPolicyGridRes> resList = list.stream().map(premium ->{
				Date inceptionDate=premium.get("inception_date")==null?null:(Date)premium.get("inception_date");
				Date expiryDate=premium.get("expiry_date")==null?null:(Date)premium.get("expiry_date");
				String strDate1 =sdf1.format(inceptionDate);
				String strDate2 =sdf1.format(expiryDate);
				EmbeddedPolicyGridRes boardRes =EmbeddedPolicyGridRes.builder()
						.planName(premium.get("plan_desc")==null?"0":premium.get("plan_desc").toString())
						.planOpted(premium.get("plan_opted")==null?"0":premium.get("plan_opted").toString())
						.requestReferenceNumber(premium.get("request_reference_no")==null?"0":premium.get("request_reference_no").toString())
						.companyId(req.getCompanyId())
						.loginId(req.getLoginId())
						.policyNo(premium.get("policy_no")==null?"0":premium.get("policy_no").toString())
						.mobileCode(premium.get("mobile_code")==null?"0":premium.get("mobile_code").toString())
						.mobileNo(premium.get("mobile_no")==null?"0":premium.get("mobile_no").toString())
						.sectionId(premium.get("section_id")==null?"0":premium.get("section_id").toString())
						.clientTransactionNo(premium.get("client_transaction_no")==null?"0":premium.get("client_transaction_no").toString())
						.customerName(premium.get("customer_name")==null?"":premium.get("customer_name").toString())
						.productId(req.getProductId())
						.amountPaid(premium.get("amount_paid")==null?"0":df.format(premium.get("amount_paid")))
						.premium(premium.get("premium")==null?"0":df.format(premium.get("premium")))
						.commissionPercentage(premium.get("commission_percentage")==null?"0":df.format(premium.get("commission_percentage")))
						.commissionAmount(premium.get("commission_amount")==null?"0":df.format(premium.get("commission_amount")))
						.taxPercentage(premium.get("tax_percentage")==null?"0":df.format(premium.get("tax_percentage")))
						.taxPremium(premium.get("tax_premium")==null?"0":df.format(premium.get("tax_premium")))
						.overAllPremium(premium.get("overall_premium")==null?"0":df.format(premium.get("overall_premium")))
						.totalPolicy(premium.get("total_policy")==null?"0":premium.get("total_policy").toString())
						.overAllComiPremium(premium.get("total_commission_amount")==null?"0":df.format(premium.get("total_commission_amount")))
						.overAllTaxPremium(premium.get("total_tax_premium")==null?"0":df.format(premium.get("total_tax_premium")))
						.activePolicyCount(premium.get("activePolicyCount")==null?"0":premium.get("activePolicyCount").toString())
						.expiryPolicyCount(premium.get("expiryPolicyCount")==null?"0":premium.get("expiryPolicyCount").toString())
						.expiryPolicyPremium(premium.get("expiry_premium")==null?"0":df.format(premium.get("expiry_premium")))
						.activePremium(premium.get("active_premium")==null?"0":df.format(premium.get("active_premium")))
						.startDate(strDate1)
						.endDate(strDate2)
						.noOfDays(getBetweenDays(strDate1,strDate2))
						.filePath(premium.get("pdf_path")==null?"0":(String)premium.get("pdf_path"))
						.build();
					return boardRes;
			}).collect(Collectors.toList());
			
			response.setCommonResponse(resList);
			response.setMessage("SUCCESS");
			response.setErrorMessage(Collections.EMPTY_LIST);


			//embeddedRepository.findByCompanyIdAnd
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}

	@Override
	public CommonRes getExpiredPolicy(EmbeddedDashBoardReq req) {
		CommonRes response = new CommonRes();
		try {
			String pattern = "#####0.00";
			DecimalFormat df = new DecimalFormat(pattern);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			List<Map<String,Object>> list = embeddedRepository.getExpiredPolicy(req.getLoginId(), req.getCompanyId(), req.getProductId() ,
					sdf.format(req.getStartDate()), sdf.format(req.getEndDate()),req.getPlanId());
			List<EmbeddedPolicyGridRes> resList = list.stream().map(premium ->{
				Date inceptionDate=premium.get("inception_date")==null?null:(Date)premium.get("inception_date");
				Date expiryDate=premium.get("expiry_date")==null?null:(Date)premium.get("expiry_date");
				String strDate1 =sdf1.format(inceptionDate);
				String strDate2 =sdf1.format(expiryDate);
				EmbeddedPolicyGridRes boardRes =EmbeddedPolicyGridRes.builder()
						.planName(premium.get("plan_desc")==null?"":premium.get("plan_desc").toString())
						.planOpted(premium.get("plan_opted")==null?"0":premium.get("plan_opted").toString())
						.requestReferenceNumber(premium.get("request_reference_no")==null?"0":premium.get("request_reference_no").toString())
						.companyId(req.getCompanyId())
						.loginId(req.getLoginId())
						.policyNo(premium.get("policy_no")==null?"0":premium.get("policy_no").toString())
						.mobileCode(premium.get("mobile_code")==null?"0":premium.get("mobile_code").toString())
						.mobileNo(premium.get("mobile_no")==null?"0":premium.get("mobile_no").toString())
						.sectionId(premium.get("section_id")==null?"0":premium.get("section_id").toString())
						.clientTransactionNo(premium.get("client_transaction_no")==null?"0":premium.get("client_transaction_no").toString())
						.customerName(premium.get("customer_name")==null?"":premium.get("customer_name").toString())
						.productId(req.getProductId())
						.amountPaid(premium.get("amount_paid")==null?"0":df.format(premium.get("amount_paid")))
						.premium(premium.get("premium")==null?"0":df.format(premium.get("premium")))
						.commissionPercentage(premium.get("commission_percentage")==null?"0":df.format(premium.get("commission_percentage")))
						.commissionAmount(premium.get("commission_amount")==null?"0":df.format(premium.get("commission_amount")))
						.taxPercentage(premium.get("tax_percentage")==null?"0":df.format(premium.get("tax_percentage")))
						.taxPremium(premium.get("tax_premium")==null?"0":df.format(premium.get("tax_premium")))
						.overAllPremium(premium.get("overall_premium")==null?"0":df.format(premium.get("overall_premium")))
						.totalPolicy(premium.get("total_policy")==null?"0":premium.get("total_policy").toString())
						.overAllComiPremium(premium.get("total_commission_amount")==null?"0":df.format(premium.get("total_commission_amount")))
						.overAllTaxPremium(premium.get("total_tax_premium")==null?"0":df.format(premium.get("total_tax_premium")))
						.activePolicyCount(premium.get("activePolicyCount")==null?"0":premium.get("activePolicyCount").toString())
						.expiryPolicyCount(premium.get("expiryPolicyCount")==null?"0":premium.get("expiryPolicyCount").toString())
						.expiryPolicyPremium(premium.get("expiry_premium")==null?"0":df.format(premium.get("expiry_premium")))
						.activePremium(premium.get("active_premium")==null?"0":df.format(premium.get("active_premium")))
						.startDate(strDate1)
						.endDate(strDate2)
						.noOfDays(getBetweenDays(strDate1,strDate2))
						.filePath(premium.get("pdf_path")==null?"0":(String)premium.get("pdf_path"))
						.build();
					return boardRes;
			}).collect(Collectors.toList());
			
			response.setCommonResponse(resList);
			response.setMessage("SUCCESS");
			response.setErrorMessage(Collections.EMPTY_LIST);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}
	
	private Long getBetweenDays(String startDate,String endDate) {
		Long count =0L;
		try {
			LocalDate date1 = LocalDate.parse(startDate,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			LocalDate date2 = LocalDate.parse(endDate,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			count=ChronoUnit.DAYS.between(date1, date2);
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		
		return count;
	}

	@Override
	public Map<String,String>  getWhatsAppVehicle(Map<String, Object> req) {
		Map<String,String> response = new HashMap<>();
		try {
			String companyId=req.get("CompanyId")==null?"":req.get("CompanyId").toString();
			String vehicleUsageName=req.get("MotorUsageName")==null?"":req.get("MotorUsageName").toString();
			String bodyType=req.get("BodyType")==null?"":req.get("BodyType").toString();
			String bodyId =embeddedRepository.getBodyId(companyId, bodyType);
			String vehcileUsageId =embeddedRepository.getVehicleUsage(companyId, vehicleUsageName);
			response.put("BodyId", bodyId);
			response.put("MotorUsage", vehcileUsageId);
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return response;
	}
	
	
}
