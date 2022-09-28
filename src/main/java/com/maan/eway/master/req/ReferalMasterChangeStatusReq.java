package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReferalMasterChangeStatusReq {
	 
	 @JsonProperty("ReferalId")
	 private String referalId;
	 
	 @JsonProperty("Status")
	 private String status;
}
