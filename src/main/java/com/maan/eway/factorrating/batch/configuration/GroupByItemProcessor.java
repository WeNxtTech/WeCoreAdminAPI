package com.maan.eway.factorrating.batch.configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.maan.eway.error.Error;
import com.maan.eway.factorrating.batch.FactorRatingBatchServiceImpl;
import com.maan.eway.master.req.FactorParamsInsert;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.master.service.impl.FactorRateMasterServiceImpl;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.vehicleupload.CustomStepExecutionListener;

@Component
public class GroupByItemProcessor implements ItemProcessor<List<FactorRateRawInsert>,List<FactorRateRawInsert>>{

	public GroupByItemProcessor() {};
	public static DozerBeanMapper mapper =new DozerBeanMapper();
	public static ObjectMapper map = new ObjectMapper();
	public static Gson print = new Gson();
	private String dropdown_data;
	private String range_columns;
	private String isDiscreate;
	private String factor_id;
	private Map<String, List<Map<String,Object>>> dropDownMap; 
	private Map<String, List<DropDownRes>> drop = new HashMap<>(); 
	private String minimum_rate_yn ;
	
	@Autowired
    private FactorRatingBatchServiceImpl validation;

	 @Autowired
	 @Qualifier("groupingStepJobListener")
	 private FactorStepExecutionListener stepExecutionListener;

	
	public GroupByItemProcessor(String rage_columns, String isDiscreate,String factor_id,String minimum_rate_yn) {
		this.range_columns=rage_columns;
		this.isDiscreate=isDiscreate;
		this.factor_id =factor_id;
		this.minimum_rate_yn = minimum_rate_yn;
	}


	/*public List<FactorRateRawInsert> process(List<FactorRateRawInsert> item) throws Exception {	  
	
		  return doFactorValidation(item);
		    		
	}
	
	private List<FactorRateRawInsert> doFactorValidation(List<FactorRateRawInsert> list){
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
			
			List<Error> errors =factorValidation.factorRatingsValidation(factorRateSaveReq, drop);
			
			if(errors.size()>0)
				return updateValidationError(list,errors);
			else
				return list;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;	
	}
	
	private List<FactorRateRawInsert> updateValidationError(List<FactorRateRawInsert> list,List<Error> errors){
		 List<FactorRateRawInsert> updateList = new ArrayList<>(list.size());
		try {
			list.forEach(p ->{
				p.setErrorDesc(errors.size()>0?print.toJson(errors).length()>10000?print.toJson(errors).substring(0,10000):print.toJson(errors)
						:null);
				p.setErrorStatus("E");
				
				updateList.add(p);
			});
		}catch (Exception e) {
			e.printStackTrace();
		}
		return updateList;
	}*/


	@Override
	public List<FactorRateRawInsert> process(List<FactorRateRawInsert> item) throws Exception {
		drop = stepExecutionListener.getDropDownList();
		return validation.factorRangeValidation(item, this.range_columns, this.isDiscreate,factor_id,drop,minimum_rate_yn);
	}

}
