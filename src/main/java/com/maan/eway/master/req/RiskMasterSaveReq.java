package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RiskMasterSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("RiskId")
    private String     riskId     ;
    
	@JsonFormat(pattern ="dd/MM/yyyy")
	@JsonProperty("EffectiveDate")
	private Date effectiveDate;

	@JsonProperty("RiskName")
    private String     riskName ;
	
	@JsonProperty("ProductId")
    private String     productId ;
	
	@JsonProperty("InsuranceId")
    private String     companyId ;
	
	@JsonProperty("Status")
    private String     status ;
	
	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("AmendId")
	private Integer amendId;

	@JsonProperty("Remarks")
	private String remarks;


}
