package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyProrataChangeStatusReq {

	 @JsonProperty("ProductId")
	 private String productId;
	 
	 @JsonProperty("Sno")
	 private String sno;
	 
	 @JsonProperty("InsuranceId")
	 private String companyId;

	 @JsonFormat(pattern = "dd/MM/yyyy")
	 @JsonProperty("EffectiveDateStart")
	 private Date effectiveDateStart;
	 @JsonProperty("PolicyTypeId")
	 private String   policyTypeId;
	 @JsonProperty("Status")
	 private String status;
}
