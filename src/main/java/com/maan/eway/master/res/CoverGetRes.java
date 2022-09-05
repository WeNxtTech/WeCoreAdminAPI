package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverGetRes {

	@JsonProperty("Coverid")
    private Integer    coverId      ;
	@JsonProperty("Sectionid")
    private Integer    sectionId    ;
	@JsonProperty("Productid")
    private Integer    productId    ;
	@JsonProperty("Companyid")
    private String     companyId    ;
	@JsonProperty("Effectivedatestart")
    private Date       effectiveDateStart ;
	@JsonProperty("Effectivedateend")
    private Date       effectiveDateEnd ;

	@JsonProperty("Covername")
    private String     coverName    ;
	@JsonProperty("Coverdesc")
    private String     coverDesc    ;
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
