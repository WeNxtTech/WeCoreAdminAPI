package com.maan.eway.springbatch;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.util.DoubleFormat;
import lombok.extern.slf4j.Slf4j;


@Component
@Configuration
@JobScope
@Slf4j
public class EwayItemProcessor implements ItemProcessor<FactorRateRawInsert, FactorRateRawInsert> {
	
	
	@Value("#{jobParameters[ewayBatchData]}")
	private String data;
	
	static int totalrecordCount =0;
	
	public static final DecimalFormat df2 = new DecimalFormat( "#.##" );	
	@Override
	public FactorRateRawInsert process(FactorRateRawInsert item) throws Exception {
		try {
				SpringBatchMapperResponse factorData =new SpringBatchMapperResponse();
			    ObjectMapper mapper = new ObjectMapper();
			    try {
			    	factorData = mapper.readValue(data, SpringBatchMapperResponse.class);
				    totalrecordCount=Integer.valueOf(factorData.getTotalRecordsCount());
				} catch (Exception e) {
					e.printStackTrace();
				}
			    item.setCompanyId(factorData.getInsuranceId());
			    item.setCoverId(Integer.valueOf(factorData.getCoverId()));
			    item.setSubCoverId(StringUtils.isBlank(factorData.getSubCoverId())?0:Integer.valueOf(factorData.getSubCoverId()));
			    item.setSectionId(StringUtils.isBlank(factorData.getSectionId())?0:Integer.valueOf(factorData.getSectionId()));
			    item.setAgencyCode(StringUtils.isBlank(factorData.getAgencyCode())?"99999": factorData.getAgencyCode());
			    item.setBranchCode(StringUtils.isBlank(factorData.getBranchCode())?"99999":factorData.getBranchCode());
			    item.setFactorTypeId(StringUtils.isBlank(factorData.getFactorTypeId())?0:Integer.valueOf(factorData.getFactorTypeId()));
			    item.setProductId(StringUtils.isBlank(factorData.getProductId())?0:Integer.valueOf(factorData.getProductId()));
			    item.setEffectiveDateStart(StringUtils.isBlank(factorData.getEffectiveDate())?null:new SimpleDateFormat("dd/MM/yyyy hh:MM:ss").parse(factorData.getEffectiveDate()));
			    item.setEffectiveDateEnd(new SimpleDateFormat("dd/mm/yyyy").parse("12/30/2050"));
			    // item.setSubCoverYn(StringUtils.isBlank(factorData.getSubCoverId()) || factorData.getSubCoverId().equals("0")?"N":"Y" );
			    item.setRemarks(StringUtils.isBlank(factorData.getRemarks())?"":factorData.getRemarks());
			    item.setCreatedBy(StringUtils.isBlank(factorData.getCreatedBy())?"":factorData.getCreatedBy());
			    item.setTranId(factorData.getTranId());
			    item.setEntryDate(new Date());
			    item.setAmendId(0);
			   // String rate= df2.format(item.getRate()).replaceAll("\\.00$", "");
			   // item.setRate(Double.valueOf(rate));
			   // System.out.println(rate);
			    
			    
			}catch (Exception e) {
				e.printStackTrace();
			}
		return item;
	}
   
	
	

}