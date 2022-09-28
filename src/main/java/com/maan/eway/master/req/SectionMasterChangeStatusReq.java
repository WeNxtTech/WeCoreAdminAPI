package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SectionMasterChangeStatusReq {
	 
	 @JsonProperty("SectionId")
	 private String sectionId;
	 
	 @JsonProperty("Status")
	 private String status;
}
