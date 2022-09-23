package com.maan.eway.common.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EserviceCustomerSaveReq {
	
	
	
	@JsonProperty("CustomerCommonDetails")
    private CustomerCommonSaveReq     customerCommonDetails ;
	
	@JsonProperty("CoverNoteDetails")
    private CoverNoteDetailsSaveReq coverNoteDetails;
	
	@JsonProperty("RiskDetails")
    private RiskDetailsSaveReq riskDetails;
	
	@JsonProperty("SubjectDetails")
    private SubjectDetailsSaveReq subjectDetails;
	
	@JsonProperty("CoverAddonDetails")
    private CoverAddOnDetailsSaveReq coverAddonDetails;
	
	@JsonProperty("OtherDetails")
    private CustomerOtherDetailsSaveReq otherDetails;
	

}
