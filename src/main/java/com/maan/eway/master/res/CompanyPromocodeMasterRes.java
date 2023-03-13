package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyPromocodeMasterRes {

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
	
	@JsonProperty("PromocodeTypeDesc")
	 private String     promocodeTypeDesc ;
	
	@JsonProperty("CalcType")
	private String calcType;
	
	@JsonProperty("PromoRateOrAmt")
	private String promoRateOrAmt;
	
	@JsonProperty("DiscountCoverId")
	private String discountCoverId;
	
	@JsonProperty("DependentCoverYn")
    private String dependentCoverYn;
    
	@JsonProperty("DependentCoverId")
    private String dependentCoverId;
	
	@JsonProperty("IsSelectedYn")
    private String isSelectedYn;
	
	@JsonProperty("MultiSelectYn")
    private String multiSelectYn;
	
	@JsonProperty("CoverBasedOn")
	private String coverBasedOn;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonProperty("MinimumPremium")
	private String minPremium;
	
	@JsonProperty("FactorTypeId")
	private String factorTypeId; 
	
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("IsTaxExcempted")
	private String isTaxExcempted;
	
	@JsonProperty("TaxExcemptionReference")
	private String taxExcemptionReference;
	
	@JsonProperty("TaxExcemptionType")
	private String taxExcemptionType;
	
	@JsonProperty("CoverId")
	private String coverId;

}
