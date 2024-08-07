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
	
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static ObjectMapper mapper = new ObjectMapper();

	private String data;
		
	public static final DecimalFormat df2 = new DecimalFormat( "#.##" );	
	
	public RawDataItemProcessor(String data) {
		this.data=data;
	}
	
	@Override
	public FactorRateRawInsert process(FactorRateRawInsert item) throws Exception {
		String error_desc="";
		try {
				SpringBatchMapperResponse factorData =new SpringBatchMapperResponse();
			    try {
			    	factorData = mapper.readValue(data, SpringBatchMapperResponse.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    String min_premium =item.getMinPremium().replace(",", "");
			    if(!min_premium.matches("[0-9.]+")) {
			    	error_desc+="MinPremium field only allows number or decimal digits~";			    	
			    }
			    
			    String rate =item.getRate().replace(",", "");
			    if(!rate.matches("[0-9.]+")) {
			    	error_desc+="Rate field only allows number or decimal digits~";
			    }
			    
			    if(StringUtils.isBlank(item.getCalcType())) {
			    	error_desc+="CalcType should not be empty~";
			    }else if(item.getCalcType().length()>1) {
			    	error_desc+="CalcType should not be graterthan one character~";
			    }
			    
			    if (StringUtils.isBlank(item.getStatus())) {
			    	error_desc+="Status should not be empty~";
				} else if (item.getStatus().length() > 1) {
					error_desc+="Status should not be graterthan one character~";
				}else if(!("Y".equalsIgnoreCase(item.getStatus())||"N".equalsIgnoreCase(item.getStatus())||"R".equalsIgnoreCase(item.getStatus())|| "P".equalsIgnoreCase(item.getStatus()))) {
					error_desc+="Anyone of status is required - Active or Deactive or Pending or Referral~";
				}
			    
			    if (StringUtils.isBlank(item.getRegulatoryCode())) {
			    	error_desc+="RegulatoryCode should not be empty~";
			    }else if(item.getRegulatoryCode().length()>20) {
					error_desc+="RegulatoryCode within 20 Characters~";
				}
			    
			    if (StringUtils.isNotBlank(item.getExcessPercent())) {
					  if (!item.getExcessPercent().matches("[0-9.]+") ) {
						  error_desc+="ExcessPercent field only allows number or decimal digits~";			    	
					  }
				}
			    
				if (StringUtils.isNotBlank(item.getExcessAmount())) {
					if (! item.getExcessAmount().matches("[0-9.]+") ) {
						 error_desc+="ExcessAmount field only allows number or decimal digits~";	
					}
				}  
			    
				if (StringUtils.isNotBlank(item.getExcessDesc())) {
					 if (item.getExcessDesc().length() > 500) {
						 error_desc+="ExcessDesc should be  within 500 Characters~";
					}
				}
				
				Date effectiveDate=null;
	            if(StringUtils.isBlank(factorData.getEffectiveDate())) {
	            	error_desc+="EffectiveDate should not be null in section_cover_master table for this factor_id "+factorData.getFactorTypeId()+"~";
	            }else if(StringUtils.isNotBlank(factorData.getEffectiveDate())) {
	            	effectiveDate =formatter.parse(factorData.getEffectiveDate()); 
			        Date current_date = new Date();			        
	            	error_desc+=effectiveDate.before(current_date)?"EffectiveDate should be not pastdate or expirydate~":error_desc;
	            }
	            
	            if(StringUtils.isBlank(factorData.getFactorTypeId()))
	            	 error_desc+="FactorTypeId is empty in section_cover_master~";
	            
			    item.setCompanyId(factorData.getInsuranceId());
			    item.setCoverId(Integer.valueOf(factorData.getCoverId()));
			    item.setSubCoverId(StringUtils.isBlank(factorData.getSubCoverId())?0:Integer.valueOf(factorData.getSubCoverId()));
			    item.setSectionId(StringUtils.isBlank(factorData.getSectionId())?0:Integer.valueOf(factorData.getSectionId()));
			    item.setAgencyCode(StringUtils.isBlank(factorData.getAgencyCode())?"99999": factorData.getAgencyCode());
			    item.setBranchCode(StringUtils.isBlank(factorData.getBranchCode())?"99999":factorData.getBranchCode());
			    item.setFactorTypeId(StringUtils.isBlank(factorData.getFactorTypeId())?0:Integer.valueOf(factorData.getFactorTypeId()));
			    item.setProductId(StringUtils.isBlank(factorData.getProductId())?0:Integer.valueOf(factorData.getProductId()));			   
			    item.setEffectiveDateStart(effectiveDate);
			    item.setEffectiveDateEnd(formatter.parse("12/30/2050 00:00:00"));			   
			    item.setRemarks(StringUtils.isBlank(factorData.getRemarks())?"":factorData.getRemarks());
			    item.setCreatedBy(factorData.getCreatedBy());
			    item.setTranId(factorData.getTranId());
			    item.setEntryDate(new Date());
			    item.setAmendId(0);	
			    			    			    			    
			}catch (Exception e) {
				e.printStackTrace();
			}
		return item;
	}
   
}