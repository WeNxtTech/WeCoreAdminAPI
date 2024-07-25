package com.maan.eway.master.req;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CompanyPromocodeSchemeSaveReq {
	
	@JsonProperty("PromocodeId")
	private String promocodeId;	
    
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
	
	@JsonProperty("CoverId")
	private String coverId;

	@JsonProperty("FactorTypeId")
	private String factorTypeId;
	
	@JsonProperty("CoverBasedOn")
	private String coverBasedOn;
	
	@JsonProperty("CreatedBy")
	private String createdBy; 
	
	@JsonProperty("FactorParams")
    private List<FactorParamsInsert>     factorParams;

//	@JsonProperty("Promocode")
//  private String     promocode ;
//	
//	@JsonProperty("PromocodeDesc")
//	private String     promocodeDesc ;
	
//	@JsonFormat(pattern="dd/MM/yyyy")
//	@JsonProperty("PeriodFrom")
//	private Date effectiveDateStart;
//	
//	@JsonFormat(pattern="dd/MM/yyyy")
//	@JsonProperty("PeriodTo")
//	private Date effectiveDateEnd;
	
//	@JsonProperty("DiscountCoverId")
//	private String discountCoverId;
//	
//	@JsonProperty("Status")
//	private String status;
//
//	@JsonProperty("CoreAppCode")
//	private String coreAppCode;
//
//	@JsonProperty("Remarks")
//	private String remarks; 
//	
//	@JsonProperty("ToolTip")
//	private String toolTip;  
//
//	@JsonProperty("RegulatoryCode")
//	private String regulatoryCode;
//	
//	@JsonProperty("PromocodeType")
//	 private String     promocodeType ;
//	
//	@JsonProperty("CalcType")
//	private String calcType;
	
	
//	@JsonProperty("PromoRateOrAmt")
//	private String promoRateOrAmt;
//	
//	@JsonProperty("MinimumPremium")
//	private String minimumPremium;
//		
//	@JsonProperty("IsTaxExcempted")
//	private String isTaxExcempted;
//	
//	@JsonProperty("TaxExcemptionReference")
//	private String taxExcemptionReference;
//	
//	@JsonProperty("TaxExcemptionType")
//	private String taxExcemptionType;
//	
//	@JsonProperty("DependentCoverYn")
//    private String dependentCoverYn;
//    
//	@JsonProperty("DependentCoverId")
//    private String dependentCoverId;
	
//	@JsonProperty("IsSelectedYn")
//    private String isSelectedYn;
//	
//	@JsonProperty("MultiSelectYn")
//    private String multiSelectYn;
}
