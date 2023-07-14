package com.maan.eway.springbatch;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
					factor.setSno(p.getSNo()==null?"0":p.getSNo().toString());
					factor.setStatus("Y");
					factor.setRegulatoryCode(StringUtils.isBlank(p.getRegulatoryCode())?"":p.getRegulatoryCode());
					factor.setRate(p.getRate()==null?"":p.getRate().toString());
					factor.setApiUrl(StringUtils.isBlank(p.getApiUrl())?"":p.getApiUrl());
					factor.setCalType(StringUtils.isBlank(p.getCalcType())?"":p.getCalcType());
					factor.setMasterYn(StringUtils.isBlank(p.getMasterYn())?"":p.getMasterYn());
					factor.setMinimumPremium(p.getMinPremium()==null?"":p.getMinPremium().toString());
					factor.setParam1(p.getParam1()==null?"0":new BigDecimal(p.getParam1()).toPlainString());
					factor.setParam2(p.getParam2()==null?"0":new BigDecimal(p.getParam2()).toPlainString());
					factor.setParam3(p.getParam3()==null?"0":new BigDecimal(p.getParam3()).toPlainString());
					factor.setParam4(p.getParam4()==null?"0":new BigDecimal(p.getParam4()).toPlainString());
					factor.setParam5(p.getParam5()==null?"0":new BigDecimal(p.getParam5()).toPlainString());
					factor.setParam6(p.getParam6()==null?"0":new BigDecimal(p.getParam6()).toPlainString());
					factor.setParam7(p.getParam7()==null?"0":new BigDecimal(p.getParam7()).toPlainString());
					factor.setParam8(p.getParam8()==null?"0":new BigDecimal(p.getParam8()).toPlainString());
					factor.setParam9(p.getParam9()==null?"0":new BigDecimal(p.getParam9()).toPlainString());
					factor.setParam10(p.getParam10()==null?"0":new BigDecimal(p.getParam10()).toPlainString());
					factor.setParam11(p.getParam11()==null?"0":new BigDecimal(p.getParam11()).toPlainString());
					factor.setParam12(p.getParam12()==null?"0":new BigDecimal(p.getParam12()).toPlainString());
					return factor;
				}).collect(Collectors.toList());
				
				factorRateSaveReq.setFactorParams(factorParams);
				
				List<Error> errors =obj.factorRatingsValidation(factorRateSaveReq, dropDownList);
				
				serviceImpl.updateFactorRawRecords(factorRateSaveReq,errors,list.get(0).getTranId(),discreateColumns);
				
				return CompletableFuture.completedFuture("Success");
			}catch (Exception e) {
				 e.printStackTrace();
				
			}
			return CompletableFuture.completedFuture("Failed");

			
		}
	
	
	
	

}
