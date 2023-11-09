package com.maan.eway.embedded;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.Tuple;

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
							.premium(p.get("premium")==null?"":df.format(p.get("premium")))
							.requestReferenceNo(p.get("requestReferenceNo")==null?"":p.get("requestReferenceNo").toString())
							.taxPremium(p.get("taxPremium")==null?"":df.format(p.get("taxPremium")))
							.transactionNo(p.get("clientTransactionNo")==null?"":p.get("clientTransactionNo").toString())
							.commissionAmt(p.get("commissionAmount")==null?"":df.format(p.get("commissionAmount")))
							.taxPercentage(p.get("taxPercentage")==null?"":p.get("taxPercentage").toString())
							.commissionPercentage(p.get("commissionPercentage")==null?"":p.get("commissionPercentage").toString())
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
			Date date = new Date();
			 String pattern = "#####0.00";
			 DecimalFormat df = new DecimalFormat(pattern);
			 
			Map<String,Object> premium =embeddedRepository.getProductDashBoard(req.getCompanyId(),req.getProductId(), req.getStartDate(), req.getEndDate());
			Map<String,Object> activePremium =embeddedRepository.getActivePremium(req.getCompanyId(),req.getProductId(),req.getStartDate(), req.getEndDate());
			Map<String,Object> expiryPremium =embeddedRepository.getExpiryPremium(req.getCompanyId(), req.getProductId(), req.getStartDate(), req.getEndDate());
			EmbeddedDashBoardRes boardRes =EmbeddedDashBoardRes.builder()
					.activePremium(activePremium.get("active_premium")==null?"":df.format(activePremium.get("active_premium")))
					.companyId(req.getCompanyId())
					.loginId(req.getLoginId())
					.overAllComiPremium(premium.get("total_commission_amount")==null?"":df.format(premium.get("total_commission_amount")))
					.overAllPremium(premium.get("total_premium")==null?"":df.format(premium.get("total_premium")))
					.overAllTaxPremium(premium.get("total_tax_premium")==null?"":df.format(premium.get("total_tax_premium")))
					.productId(req.getProductId())
					.totalPolicy(premium.get("total_policy")==null?"":premium.get("total_policy").toString())
					.activePolicyCount(activePremium.get("activePolicyCount")==null?"":activePremium.get("activePolicyCount").toString())
					.expiryPolicyCount(expiryPremium.get("activePolicyCount")==null?"":expiryPremium.get("activePolicyCount").toString())
					.expiryPolicyPremium(expiryPremium.get("expiry_premium")==null?"":df.format(expiryPremium.get("expiry_premium")))
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
			Date date = new Date();
			 String pattern = "#####0.00";
			 DecimalFormat df = new DecimalFormat(pattern);
			List<Map<String,Object>> list =embeddedRepository.getProductPlanDashBoard(req.getLoginId(),req.getCompanyId(),req.getProductId(),req.getStartDate(),req.getEndDate());
			List<EmbeddedDashBoardRes> resList = list.stream().map(premium ->{
				String plan_opted =premium.get("plan_opted")==null?"":premium.get("plan_opted").toString();
				String activePremium=embeddedRepository.getActivePremiumBasedPlan(req.getLoginId(),req.getCompanyId(),req.getProductId(),plan_opted,req.getStartDate(),req.getEndDate());
				//Double activeP=Double.parseDouble(df.format(activePremium).toString());
				EmbeddedDashBoardRes boardRes =EmbeddedDashBoardRes.builder()
						.planOpted(plan_opted)
						.activePremium(activePremium)
						.companyId(req.getCompanyId())
						.loginId(req.getLoginId())
						.overAllComiPremium(premium.get("total_commission_amount")==null?"":df.format(premium.get("total_commission_amount")))
						.overAllPremium(premium.get("total_premium")==null?"":df.format(premium.get("total_premium")))
						.overAllTaxPremium(premium.get("total_tax_premium")==null?"":df.format(premium.get("total_tax_premium")))
						.productId(req.getProductId())
						.totalPolicy(premium.get("total_policy")==null?"":premium.get("total_policy").toString())
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
	public CommonRes getActivePolicy(EmbeddedDashBoardReq req) {
		try {
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CommonRes getAllPolicy(EmbeddedDashBoardReq req) {
		try {
			//embeddedRepository.findByCompanyIdAnd
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
