package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionCoverChangeStatusReq {

	 @JsonProperty("InsuranceId")
	 private String companyId;
	 
	 @JsonProperty("ProductId")
	 private String productId;
	 
	 @JsonProperty("CoverId")
	 private String coverId;

	 @JsonProperty("SectionId")
	 private String sectionId;

	 @JsonProperty("Status")
	 private String status;

}
