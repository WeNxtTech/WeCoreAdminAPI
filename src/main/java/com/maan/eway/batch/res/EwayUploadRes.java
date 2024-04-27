package com.maan.eway.batch.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EwayUploadRes {
	
	@JsonProperty("SourceTypeId")
	private String sourceTypeId;
	@JsonProperty("CompanyId")
	private String companyId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("TypeId")
	private String typeId;
	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	@JsonProperty("ExcelFilePath")
	private String excelFilePath;
	@JsonProperty("TableColumns")
	private String tableColumns;
	@JsonProperty("TableColumnsDataType")
	private String tableColumnsDataType;
	@JsonProperty("ExcelHeaderColumns")
	private String excelHeaderColumns;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("ExcelFileName")
	private String excelFileName;
	@JsonProperty("CSVFileName")
	private String csvFileName;
	@JsonProperty("ToatalRows")
	private String toatalRows;
	@JsonProperty("Csvfilepath")
	private String csvfilepath;
	@JsonProperty("ProgressStatus")
	private String progressStatus;
	@JsonProperty("ProgressErrordesc")
	private String ProgressErrordesc;
	@JsonProperty("Progressdesc")
	private String progressdesc;
	@JsonProperty("Excelrawtablename")
	private String excelrawtablename;
	@JsonProperty("Excelrawtablefields")
	private String excelrawtablefields;
	@JsonProperty("Excelmandatorylist")
	private String excelmandatorylist;
	@JsonProperty("Exceldateformatlist")
	private String exceldateformatlist;
	@JsonProperty("Duplicatecheckcolumns")
	private String duplicatecheckcolumns;
	@JsonProperty("Duplicatecheckexcelcolumns")
	private String duplicatecheckexcelcolumns;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("UploadedBy")
	private String uploadedBy;
	@JsonProperty("ErrorRecords")
	private String errorRecords;
	@JsonProperty("ValidRecords")
	private String validRecords;
	@JsonProperty("DeletedRecords")
	private String deletedRecords;
	@JsonProperty("SectionId")
	private String sectionId;
	@JsonProperty("Token")
	private String token;
	@JsonProperty("XlConfigData")
	private List<XlConfigData> XlConfigData;
	@JsonProperty("BrokerBranchCode")
	private String brokerBranchCode;
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
	@JsonProperty("CustomerRefNo")
	private String customerRefNo;
	@JsonProperty("AgencyCode")
	private String agencyCode;
	@JsonProperty("Idnumber")
	private String idnumber;
	@JsonProperty("UserType")
	private String userType;
	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("RiskId")
	private String riskId;
	@JsonProperty("SourceType")
	private String sourceType;
	@JsonProperty("CustomerCode")
	private String customerCode;
	@JsonProperty("NcdYn")
	private String ncdYn;
	@JsonProperty("RelationId")
	private String relationId;
	@JsonProperty("StateCode")
	private String stateCode;
	
	@JsonProperty("UploadType")
	private String uploadType;
	
	@JsonProperty("DataFieldLength")
	private String dataFieldLength;
	
	@JsonProperty("DataRange")
	private String DataRange;
	@JsonProperty("OwnerCategory")
	private String ownerCategory;
	@JsonProperty("ResOwnerName")
	private String resOwnerName;
	@JsonProperty("CustomerName")
	private String customerName;
	@JsonProperty("BdmCode")
	private String bdmCode;
}
