package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionGetRes {

	
	@JsonProperty("SectionId")
    private Integer    sectionId    ;
	@JsonProperty("RiskId")
    private Integer    riskId       ;
	@JsonProperty("ProductId")
    private Integer    productId    ;
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("EffectiveDateStart")
    private Date       effectiveDateStart ;
	@JsonProperty("EffectiveDateEnd")
    private Date       effectiveDateEnd ;
	@JsonProperty("SectionName")
    private String     sectionName  ;
	@JsonProperty("EntryDate")
    private Date       entryDate    ;
	@JsonProperty("Status")
    private String     status       ;
	@JsonProperty("CoreAppCode")
    private String     coreAppCode      ;
	@JsonProperty("AmendId")
    private Integer amendId      ;

	@JsonProperty("Remarks")
    private String remarks;
}
