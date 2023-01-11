package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EmiMasterChangeStatusReq {

	 @JsonProperty("EmiId")
	 private String emiId;
	 
	 @JsonProperty("Status")
	 private String status;
	 
	 @JsonProperty("InsuranceId")
	 private String companyId;

}
