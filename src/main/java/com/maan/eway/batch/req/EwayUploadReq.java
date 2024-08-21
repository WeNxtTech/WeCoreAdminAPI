package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EwayUploadReq {
	@JsonProperty("CompanyId")
	private String companyId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("TypeId")
	private String typeId;
	@JsonProperty("BrokerBranchCode")
	private String brokerBranchCode;
	@JsonProperty("CustomerRefNo")
	private String customerRefNo;
	@JsonProperty("AcExecutiveId")
	private String acExecutiveId;
	@JsonProperty("BrokerCode")
	private String beokerCode;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("SubUserType")
	private String subUserType;
	@JsonProperty("ApplicationId")
	private String applicationId;
	@JsonProperty("EndorsementYn")
	private String endorsementYn;
	@JsonProperty("EndorsementDate")
	private String endorsementDate;
	@JsonProperty("EndorsementEffectiveDate")
	private String endorsementEffectiveDate;
	@JsonProperty("EndorsementRemarks")
	private String endorsementRemarks;
	@JsonProperty("EndorsementType")
	private String endorsementType;
	@JsonProperty("EndorsementTypeDesc")
	private String endorsementTypeDesc;
	@JsonProperty("EndtCategoryDesc")
	private String endtCategoryDesc;
	@JsonProperty("EndtCount")
	private String endtCount;
	@JsonProperty("EndtPrevPolicyNo")
	private String endtPrevPolicyNo;
	@JsonProperty("EndtPrevQuoteNo")
	private String endtPrevQuoteNo;
	@JsonProperty("EndtStatus")
	private String endtStatus;
	@JsonProperty("IsFinanceEndt")
	private String isFinanceEndt;
	@JsonProperty("OrginalPolicyNo")
	private String orginalPolicyNo;
	@JsonProperty("PolicyStartDate")
	private String policyStartDate;
	@JsonProperty("PolicyEndDate")
	private String policyEndDate;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	@JsonProperty("HavePromoCode")
	private String havePromoCode;
	@JsonProperty("PromoCode")
	private String promoCode;
	@JsonProperty("NoOfVehicles")
	private String noOfVehicles;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("AgencyCode")
	private String agencyCode;
	@JsonProperty("Idnumber")
	private String idnumber;
	@JsonProperty("UserType")
	private String userType;
	@JsonProperty("NcdYn")
	private String ncdYn;
	@JsonProperty("SourceType")
	private String sourceType;
	@JsonProperty("CustomerCode")
	private String customerCode;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("RelationId")
	private String relationId;
	@JsonProperty("StateCode")
	private String stateCode;
	@JsonProperty("UploadType")
	private String uploadType;
	@JsonProperty("CustomerName")
	private String customerName;
	@JsonProperty("BdmCode")
	private String bdmCode;
	@JsonProperty("OwnerCategory")
	private String ownerCategory;
	@JsonProperty("ResOwnerName")
	private String resOwnerName;
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	
	@JsonProperty("FactorId")
	private String FactorId;
	
	
}
