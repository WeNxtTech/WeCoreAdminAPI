package com.maan.eway.master.res;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	
	@JsonProperty("StatusDesc")
	private String statusDesc;
	
	@JsonFormat(pattern ="dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern ="dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;
	
	@JsonProperty("SectionDeatils")
	private List<ProductSectionConfigRes> sectionRes;

	@JsonProperty("FactorTypeDetails")
	private List<CommonConfigRes> factorTypeDetails;
	
	@JsonProperty("PolicyTypeDetails")
	private List<CommonConfigRes>  policyTypeDetails;
	
	@JsonProperty("EmiDetails")
	private List<CommonConfigRes>  emiConfigRes;
	
	@JsonProperty("TaxSetupDetails")
	private List<CommonConfigRes>  taxSetUpDetails;
	
	@JsonProperty("DocumentDetails")
	private List<SectionDocumentConfigRes>  documentDetails;
	
	
	@JsonProperty("RefferalDetails")
	private List<CommonConfigRes>  refferalDetails;
	
	
	@JsonProperty("PaymentDetails")
	private List<PaymentConfigRes>  paymentDetails;
	
	@JsonProperty("ProRataDetails")
	private List<ProRataConfigRes>  proRataDetails;
	
	

}
