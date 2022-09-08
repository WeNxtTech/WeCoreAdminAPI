package com.maan.eway.master.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductSectionsSaveReq {

	@JsonProperty("RiskId")
    private String     riskId     ;
    
	@JsonProperty("SectionList")
    private List<SectionListReq>     sectionList     ;
    
}
