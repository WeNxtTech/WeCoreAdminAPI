package com.maan.eway.master.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.bean.FactorTypeDetails;

import lombok.Data;

@Data
public class CompanyProductConfigRes {
	
	@JsonProperty("InsuranceId")
    private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("ProductName")
	private String productName;
	
	@JsonProperty("Status")
	private String status;
	
	
	@JsonProperty("SectionDeatils")
	private List<ProductSectionConfigRes> sectionRes;

	@JsonProperty("FactorTypeId")
	private List<CommonConfigRes> factorTypeDetails;
	
	@JsonProperty("PolicyTypeDetails")
	private List<CommonConfigRes>  policyTypeDetails;
	
	@JsonProperty("EmiDetails")
	private List<CommonConfigRes>  emiConfigRes;
	
	@JsonProperty("TaxSetupDetails")
	private List<CommonConfigRes>  taxSetUpDetails;
	
	@JsonProperty("DocumentDetails")
	private List<CommonConfigRes>  documentDetails;
	
	@JsonProperty("ProrateDetails")
	private List<CommonConfigRes>  prorateDetails;
	
	
	@JsonProperty("RefferalDetails")
	private List<CommonConfigRes>  refferalDetails;
	
	
	

}
