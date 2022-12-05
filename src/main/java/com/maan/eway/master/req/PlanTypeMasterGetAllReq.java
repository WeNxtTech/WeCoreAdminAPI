package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import lombok.Data;

@Data
public class PlanTypeMasterGetAllReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("InsuranceId")
	private String insuranceId;

	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("ProductId")
	private String productId;
}
