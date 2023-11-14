package com.maan.eway.embedded;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EmbeddedDashBoardReq {
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("StartDate")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date startDate;
	
	@JsonProperty("EndDate")
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date endDate;
	
	@JsonProperty("PreimumType")
	private String preimumType;
	
	@JsonProperty("PlanId")
	private String planId;
	

}
