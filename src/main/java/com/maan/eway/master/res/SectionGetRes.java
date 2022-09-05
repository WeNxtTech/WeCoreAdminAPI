package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionGetRes {

	
	@JsonProperty("Sectionid")
    private Integer    sectionId    ;
	@JsonProperty("Riskid")
    private Integer    riskId       ;
	@JsonProperty("Productid")
    private Integer    productId    ;
	@JsonProperty("Companyid")
    private String     companyId    ;
	@JsonProperty("Effectivedatestart")
    private Date       effectiveDateStart ;
	@JsonProperty("Effectivedateend")
    private Date       effectiveDateEnd ;
	@JsonProperty("Sectionname")
    private String     sectionName  ;
	@JsonProperty("Entrydate")
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
