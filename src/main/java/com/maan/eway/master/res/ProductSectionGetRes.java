package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductSectionGetRes {

	@JsonProperty("SectionId")
    private String sectionId    ;
	
	@JsonProperty("SectionName")
    private String    sectionName ;
	
	@JsonProperty("RegulatoryCode")
    private String    regulatoryCode;
}
