package com.maan.eway.master.req;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PlanTypeMasterChangeStatusReq {

		@JsonProperty("PlanTypeId")
		private String planTypeId;

		@JsonProperty("BranchCode")
		private String branchCode;

		@JsonProperty("InsuranceId")
		private String insuranceId;

		@JsonProperty("SectionId")
		private String sectionId;
		
		@JsonProperty("ProductId")
		private String productId;
		
		 @JsonProperty("Status")
		 private String status;
}
