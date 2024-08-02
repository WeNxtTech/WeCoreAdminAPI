package com.maan.eway.factorrating.batch.configuration;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.springbatch.FactorRateRawInsert;
import com.maan.eway.springbatch.SpringBatchMapperResponse;

public class RawDataItemProcessor  implements ItemProcessor<FactorRateRawInsert, FactorRateRawInsert> {
	
	
	//@Value("#{jobParameters[ewayBatchData]}")
	private String data;
	
	static int totalrecordCount =0;
	
	public static final DecimalFormat df2 = new DecimalFormat( "#.##" );	
	
	public RawDataItemProcessor(String data) {
		this.data=data;
	}
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
			    
			}catch (Exception e) {
				e.printStackTrace();
			}
		return item;
	}
   
}