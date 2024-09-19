package com.maan.eway.factorrating.batch.configuration;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.eway.bean.FactorRateMaster;
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
	    String dynamic_errors="";

		try {
				SpringBatchMapperResponse factorData =new SpringBatchMapperResponse();
			    try {
			    	factorData = mapper.readValue(data, SpringBatchMapperResponse.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			    
			    
			    String[] discreate = StringUtils.isBlank(factorData.getDiscreteColumn())?null:factorData.getDiscreteColumn().split("~");
			    String[] range_cloumns = StringUtils.isBlank(factorData.getRangeColumns())?null:factorData.getRangeColumns().split("~");
			   
			    if(discreate!=null) {
			    	dynamic_errors = doDynamicValidation(discreate, item);
			    	if(StringUtils.isNotBlank(dynamic_errors))
			    		error_desc += dynamic_errors;
			    }
			    
			    if(range_cloumns!=null) {
			    	dynamic_errors = doDynamicValidation(range_cloumns, item);
			    	if(StringUtils.isNotBlank(dynamic_errors))
			    		error_desc += dynamic_errors;
			    }
			    
			    String min_premium = StringUtils.isBlank(item.getMinPremium())?"":item.getMinPremium().replace(",", "");			   
			    boolean rate_check = true;
			    if(StringUtils.isBlank(min_premium)) {
			    	error_desc+="MinPremium field cannot be empty or blank~";	
			    	rate_check = false;
			    }else if(!min_premium.matches("[0-9.]+")) {
			    	error_desc+="MinPremium field only allows number or decimal digits("+min_premium+")~";	
			    	rate_check = false;
			    }else if(!min_premium.matches("\\d+(\\.\\d+)?")) {
			    	error_desc+="Please enter valid minpremium("+min_premium+")~";	
			    	rate_check = false;
			    }
			    
			    
			    String rate = StringUtils.isBlank(item.getRate())?"":item.getRate().replace(",", "");
			    if(StringUtils.isBlank(rate)) {
			    	error_desc+="Rate field cannot be empty or blank~";			    	
			    }else if(!rate.matches("[0-9.]+")) {
			    	error_desc+="Rate field only allows number or decimal digits~";
			    }else if(!rate.matches("\\d+(\\.\\d+)?")) {
			    	error_desc+="Please enter valid rate("+rate+")~";	
			    	rate_check = false;
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
						  error_desc+="ExcessPercent field only allows number or decimal digits~";			    						  }
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
	            	LocalDate current_date = LocalDate.now();
	            	LocalDate effective_date =LocalDate.parse(factorData.getEffectiveDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));	        	            	
	            	error_desc+=effective_date.isBefore(current_date)?"EffectiveDate should be not pastdate or expirydate~":"";
	            	effectiveDate = Date.from(effective_date.atStartOfDay(ZoneId.systemDefault()).toInstant());

	            }
	            
	            if(StringUtils.isBlank(factorData.getFactorTypeId()))
	            	 error_desc+="FactorTypeId is empty in section_cover_master~";
	            
	         
	            // minimium_rate validation            
	            if("Y".equalsIgnoreCase(factorData.getMinimumRateYn())) {
	            	String minimum_rate =StringUtils.isBlank(item.getMinimumRate())?"":item.getMinimumRate();
	            	if(StringUtils.isBlank(minimum_rate)) {
	            		error_desc +="MinimumRate cannot be blank~";
	            	}else if(!minimum_rate.matches("[0-9.]+")) {
				    	error_desc+="MinimumRate field only allows number or decimal digits~";
	            	}else {	            		
	            		if(rate_check) { // checking whether rate is valid or not
	            			if(new BigDecimal(minimum_rate).compareTo(new BigDecimal(rate)) == 1) {
	            				error_desc += "MinimumRate should not be greaterthan Rate..!~";
	            			}
	            			
	            		}           			            		
	            	}
	            	
	            }
	            
	            
	            if(StringUtils.isBlank(item.getXlAgencyCode()) || "0".equalsIgnoreCase(item.getXlAgencyCode())) {
	            	
	            	error_desc =error_desc+"AgencyCode cannot be empty or blank or null";
	            }
	            
	            
			    item.setCompanyId(factorData.getInsuranceId());
			    item.setCoverId(Integer.valueOf(factorData.getCoverId()));
			    item.setSubCoverId(StringUtils.isBlank(factorData.getSubCoverId())?0:Integer.valueOf(factorData.getSubCoverId()));
			    item.setSectionId(StringUtils.isBlank(factorData.getSectionId())?0:Integer.valueOf(factorData.getSectionId()));
			    item.setAgencyCode(StringUtils.isBlank(factorData.getAgencyCode())?"99999": factorData.getAgencyCode());
			    item.setBranchCode(StringUtils.isBlank(factorData.getBranchCode())?"99999":factorData.getBranchCode());
			    item.setFactorTypeId(StringUtils.isBlank(factorData.getFactorTypeId())?0:Integer.valueOf(factorData.getFactorTypeId()));
			    item.setProductId(StringUtils.isBlank(factorData.getProductId())?0:Integer.valueOf(factorData.getProductId()));			   
			    item.setEffectiveDateStart(effectiveDate==null?new Date():effectiveDate);
			    item.setEffectiveDateEnd(formatter.parse("12/30/2050 00:00:00"));			   
			    item.setRemarks(StringUtils.isBlank(factorData.getRemarks())?"":factorData.getRemarks());
			    item.setCreatedBy(factorData.getCreatedBy());
			    item.setTranId(factorData.getTranId());
			    item.setEntryDate(new Date());
			    item.setAmendId(0);	
			    
			    if(StringUtils.isNotBlank(error_desc)) {
			    	item.setErrorDesc(error_desc);
			    	item.setErrorStatus("E");
			    }
			    
			    
			}catch (Exception e) {
				e.printStackTrace();
			}
		return item;
	}
	
	private String doDynamicValidation(String [] arr ,FactorRateRawInsert data) {
		StringJoiner joiner = new StringJoiner("~");
		try {
			
			for(String s : arr) {
				
				 Field field = FactorRateRawInsert.class.getDeclaredField(s);
                 field.setAccessible(true);
                 
                 String value =field.get(data)==null || String.valueOf(field.get(data)).isEmpty() ?null:String.valueOf(field.get(data));
                 
                 // Get the Field object for the specified field name
				 Field fields = FactorRateMaster.class.getDeclaredField(s);
                 String typeName =fields.getType().getSimpleName();
                 
                 if("Integer".equals(typeName) || "int".equals(typeName) || "Double".equalsIgnoreCase(typeName) || 
                		 "Long".equalsIgnoreCase(typeName) || "BigDecimal".equals(typeName)) {
                	 
                	 if(StringUtils.isBlank(value)) {
                		 joiner.add(s.toUpperCase(Locale.US)+" cannot be blank or empty"); 
                	 }else if(!value.matches("[0-9.]+")) {
                		 joiner.add(s.toUpperCase(Locale.US)+" should not allow any special characters except [0-9.]"); 

                	 }else if(!value.matches("[0-9.]+")) {
                		 joiner.add(s.toUpperCase(Locale.US)+" should not allow any special characters except [0-9.]"); 

                	 }else if(!value.matches("\\d+(\\.\\d+)?")) {
                		 joiner.add(s.toUpperCase(Locale.US)+" should be valid format("+value+")"); 

                	 }
                	 
                 } 
                 
                 else if("String".equals(typeName) && StringUtils.isBlank(value)) {
                	 joiner.add(s.toUpperCase(Locale.US)+" cannot be blank or empty"); 
                 }
                
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return joiner.toString();
	}
   
}