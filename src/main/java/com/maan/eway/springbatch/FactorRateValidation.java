package com.maan.eway.springbatch;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.FactorParamsInsert;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.master.service.impl.FactorRateMasterServiceImpl;
import com.maan.eway.res.DropDownRes;

@Component
public class FactorRateValidation {
	
	@Autowired
	private UtilityServiceImpl serviceImpl;
	
	public static DozerBeanMapper mapper =new DozerBeanMapper();
	
	
	
	  
    @Autowired
    private FactorRateMasterServiceImpl obj;
	
   
	@Async("fileUploadExeuter")
	@Transactional
	 public CompletableFuture<String>  callValidationApi(List<FactorRateRawInsert> list, String discreateColumns, String auth, Map<String, List<DropDownRes>> dropDownList)  {
			try {
				FactorRateSaveReq factorRateSaveReq=mapper.map(list.get(0), FactorRateSaveReq.class);
				List<FactorParamsInsert> factorParams =list.parallelStream().map(p ->{
					FactorParamsInsert factor =new FactorParamsInsert();
					factor.setSno(p.getSno()==null?"0":p.getSno().toString());
					factor.setStatus(p.getStatus());
					factor.setRegulatoryCode(StringUtils.isBlank(p.getRegulatoryCode())?"":p.getRegulatoryCode());
					factor.setRate(p.getRate()==null?"":new BigDecimal(p.getRate()).toPlainString());
					factor.setApiUrl(StringUtils.isBlank(p.getApiUrl())?"":p.getApiUrl());
					factor.setCalType(StringUtils.isBlank(p.getCalcType())?"":p.getCalcType());
					factor.setMasterYn(StringUtils.isBlank(p.getMasterYn())?"":p.getMasterYn());
					factor.setMinimumPremium(p.getMinPremium()==null?"":new BigDecimal(p.getMinPremium()).toPlainString());
					factor.setParam1(p.getParam1()==null?"0":new BigDecimal(p.getParam1()).toPlainString());
					factor.setParam2(p.getParam2()==null?"0":new BigDecimal(p.getParam2()).toPlainString());
					factor.setParam3(p.getParam3()==null?"0":new BigDecimal(p.getParam3()).toPlainString());
					factor.setParam4(p.getParam4()==null?"0":new BigDecimal(p.getParam4()).toPlainString());
					factor.setParam5(p.getParam5()==null?"0":new BigDecimal(p.getParam5()).toPlainString());
					factor.setParam6(p.getParam6()==null?"0":new BigDecimal(p.getParam6()).toPlainString());
					factor.setParam7(p.getParam7()==null?"0":new BigDecimal(p.getParam7()).toPlainString());
					factor.setParam8(p.getParam8()==null?"0":new BigDecimal(p.getParam8()).toPlainString());
					factor.setParam9(StringUtils.isBlank(p.getParam9())?"0":p.getParam9());
					factor.setParam10(StringUtils.isBlank(p.getParam10())?"0":p.getParam10());
					factor.setParam11(StringUtils.isBlank(p.getParam11())?"0":p.getParam11());
					factor.setParam12(StringUtils.isBlank(p.getParam12())?"0":p.getParam12());
					factor.setXlAgencyCode(StringUtils.isBlank(p.getXlAgencyCode())?"":p.getXlAgencyCode());
					factor.setParam13(StringUtils.isBlank(p.getParam13())?null:new BigDecimal(p.getParam13()).toPlainString());
					factor.setParam14(StringUtils.isBlank(p.getParam14())?null:new BigDecimal(p.getParam14()).toPlainString());
					factor.setParam15(StringUtils.isBlank(p.getParam15())?null:new BigDecimal(p.getParam15()).toPlainString());
					factor.setParam16(StringUtils.isBlank(p.getParam16())?null:new BigDecimal(p.getParam16()).toPlainString());
					factor.setParam17(StringUtils.isBlank(p.getParam17())?null:new BigDecimal(p.getParam17()).toPlainString());
					factor.setParam18(StringUtils.isBlank(p.getParam18())?null:new BigDecimal(p.getParam18()).toPlainString());
					factor.setParam19(StringUtils.isBlank(p.getParam19())?null:new BigDecimal(p.getParam19()).toPlainString());
					factor.setParam20(StringUtils.isBlank(p.getParam20())?null:new BigDecimal(p.getParam20()).toPlainString());
					factor.setParam21(p.getParam21()==null?"0":p.getParam21());
					factor.setParam22(p.getParam22()==null?"0":p.getParam22());
					factor.setParam23(p.getParam23()==null?"0":p.getParam23());
					factor.setParam24(p.getParam24()==null?"0":p.getParam24());
					factor.setParam25(p.getParam25()==null?"0":p.getParam25());
					factor.setParam26(p.getParam26()==null?"0":p.getParam26());
					factor.setParam27(p.getParam27()==null?"0":p.getParam27());
					factor.setParam28(p.getParam28()==null?"0":p.getParam28());
					factor.setExcessAmount(p.getExcessAmount()==null?"0":p.getExcessAmount());
					factor.setExcessPercent(p.getExcessPercent()==null?"0":p.getExcessPercent());
					factor.setExcessDesc(StringUtils.isBlank(p.getExcessDesc())?"":p.getExcessDesc());
					return factor;
				}).collect(Collectors.toList());
				
				factorRateSaveReq.setFactorParams(factorParams);
				
				System.out.println("GROUP "+factorRateSaveReq.getFactorParams().size());
				
				List<Error> errors =obj.factorRatingsValidation(factorRateSaveReq, dropDownList);
				
				serviceImpl.updateFactorRawRecords(factorRateSaveReq,errors,list.get(0).getTranId(),discreateColumns);
				
				return CompletableFuture.completedFuture("Success");
			}catch (Exception e) {
				 e.printStackTrace();
				
			}
			return CompletableFuture.completedFuture("Failed");

			
		}
	
	
	
	

}
