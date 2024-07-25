package com.maan.eway.master.req;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyPromocodeSaveReq {
	
	@JsonProperty("PromocodeId")
	private String promocodeId;
	
	@JsonProperty("Promocode")
    private String     promocode ;
	
	@JsonProperty("PromocodeDesc")
	private String     promocodeDesc ;
    
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("AgencyCode")
	private String agencyCode;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("PeriodFrom")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("PeriodTo")
	private Date effectiveDateEnd;
	
	@JsonProperty("DiscountCoverId")
	private String discountCoverId;
	
	@JsonProperty("Status")
	private String status;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("Remarks")
	private String remarks; 
	
	@JsonProperty("CreatedBy")
	private String createdBy; 
	
	@JsonProperty("ToolTip")
	private String toolTip;  

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;
	
	@JsonProperty("PromocodeType")
	 private String     promocodeType ;
	
	@JsonProperty("CalcType")
	private String calcType;
	
	@JsonProperty("PromoRateOrAmt")
	private String promoRateOrAmt;
	
	@JsonProperty("MinimumPremium")
	private String minimumPremium;
	
	@JsonProperty("FactorTypeId")
	private String factorTypeId;
	
//	@JsonProperty("FactorParams")
//    private List<FactorParamsInsert>     factorParams;
//	
	@JsonProperty("IsTaxExcempted")
	private String isTaxExcempted;
	
	@JsonProperty("TaxExcemptionReference")
	private String taxExcemptionReference;
	
	@JsonProperty("TaxExcemptionType")
	private String taxExcemptionType;

}
