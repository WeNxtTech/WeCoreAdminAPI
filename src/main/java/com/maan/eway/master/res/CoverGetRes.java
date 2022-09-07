package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverGetRes {

	@JsonProperty("CoverId")
    private Integer    coverId      ;
	@JsonProperty("SectionId")
    private Integer    sectionId    ;
	@JsonProperty("ProductId")
    private Integer    productId    ;
	@JsonProperty("InsuranceId")
    private String     companyId    ;
	@JsonProperty("EffectiveDateStart")
    private Date       effectiveDateStart ;
	@JsonProperty("EffectiveDateEnd")
    private Date       effectiveDateEnd ;

	@JsonProperty("CoverName")
    private String     coverName    ;
	@JsonProperty("CoverDesc")
    private String     coverDesc    ;
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
