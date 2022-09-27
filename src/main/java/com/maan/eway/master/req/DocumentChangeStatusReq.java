package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocumentChangeStatusReq {

	 @JsonProperty("CoverId")
	 private String coverId;
	 
	 @JsonProperty("Status")
	 private String status;
}
