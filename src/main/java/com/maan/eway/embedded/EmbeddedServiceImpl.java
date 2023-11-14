package com.maan.eway.embedded;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.Tuple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
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
					.activePremium(active.get("total_premium")==null?"":df.format(active.get("total_premium")))
					.companyId(req.getCompanyId())
					.loginId(req.getLoginId())
					.overAllPremium(overAll.get("total_premium")==null?"":df.format(overAll.get("total_premium")))
					.productId(req.getProductId())
					.totalPolicy(overAll.get("total_policy")==null?"":overAll.get("total_policy").toString())
					.activePolicyCount(active.get("total_policy")==null?"":active.get("total_policy").toString())
					.expiryPolicyCount(expiry.get("total_policy")==null?"":expiry.get("total_policy").toString())
					.expiryPolicyPremium(expiry.get("total_premium")==null?"":df.format(expiry.get("total_premium")))
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
			}else if("EXPIRY".equalsIgnoreCase(req.getPreimumType())) {
			
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
						boardRes.setPremium(p.get("total_premium")==null?"":df.format(p.get("total_premium")));
						boardRes.setProductId(req.getProductId());
						boardRes.setTotalPolicy(p.get("total_policy")==null?"":p.get("total_policy").toString());
						boardRes.setPlanType(p.get("plan_desc")==null?"":p.get("plan_desc").toString());
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
