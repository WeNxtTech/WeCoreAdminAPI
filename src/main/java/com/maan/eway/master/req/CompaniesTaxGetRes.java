package com.maan.eway.master.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompaniesTaxGetRes {


	@JsonProperty("InsuranceId")
	private String companyId;
	

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CountryId")
	private String countryId;
	
	@JsonProperty("TaxFor")
	private String taxFor;
	
	@JsonProperty("TaxForDesc")
	private String taxForDesc;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonProperty("TaxList")
	private List<CompanyTaxMuiltiInsertReq> taxList;
	
	
	@JsonProperty("CreatedBy")
	private String createdBy;
}
