package com.maan.eway.uploaddoc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AcExtDropDownRes {
	
	  @JsonProperty("Code")
	  private Integer acExecutiveId;
	  
	  @JsonProperty("CodeDesc")
	  private String acExecutiveName;
	  
	  @JsonProperty("CoreAppCode")
	   private String coreAppCode;
	  
	    @JsonProperty("CompanyId")
	    private String companyId;
	    
	    @JsonProperty("BranchCode")
	    private String branchCode;
	    
	    @JsonProperty("CommissionPercent")
	    private Double commissionPercent;

}
