package com.maan.eway.master.req;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompaniesTaxSaveReq implements Serializable {

    private static final long serialVersionUID = 1L;


	@JsonProperty("InsuranceId")
	private String companyId;
	

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CountryId")
	private String countryId;
	
	@JsonProperty("TaxFor")
	private String taxFor;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonProperty("TaxList")
	private List<CompanyTaxMuiltiInsertReq> taxList;
	
	
	@JsonProperty("CreatedBy")
	private String createdBy;

}
