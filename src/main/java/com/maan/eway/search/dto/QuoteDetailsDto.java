package com.maan.eway.search.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuoteDetailsDto {
	
	@JsonProperty("Customerinfo")
	private CustomerinfoRes cusInfo;
	
	@JsonProperty("PolicyDetails")
	private PolicyDetailsRes policyDetails;
	
	@JsonProperty("SectionDetails")
	private  List<SectionDetailsRes> sectionList;
	
	@JsonProperty("MotorDetails")
	private  List<MotorDetailsRes> motorResList;
	
	@JsonProperty("BuildingDetails")
	private  List<BuildingRiskRes> buildinglist;
	
	@JsonProperty("CommonDetails")
	private  List<HumanDetailsRes> CommonList;
	

//	@JsonProperty("quoteNo")
//	private String quoteNo;
//	@JsonProperty("CustomerId")
//	private String customerId;
//	@JsonProperty("NoOfVehicles")
//	private Integer noOfVehicles;
//	@JsonProperty("CustomerName")
//	private String customerName;
//	@JsonProperty("PolicyNo")
//	private String policyNo;
//	@JsonProperty("OriginalPolicyNo")
//	private String originalPolicyNo;
//	@JsonProperty("Status")
//	private String status;
//	@JsonProperty("PolicyStartDate")
//	private Date inceptionDate;
//	@JsonProperty("ExpiryDate")
//	private Date expiryDate;
//	@JsonProperty("EffectiveDate")
//	private Date effectiveDate;
//	@JsonProperty("OverallPremiumLc")
//	private BigDecimal overallPremiumLc;
//	@JsonProperty("OverallPremiumFc")
//	private BigDecimal overallPremiumFc;
//	@JsonProperty("BranchName")
//	private String branchName;
//	@JsonProperty("PaymentMode")
//	private String paymentMode;
//	@JsonProperty("PaymentStatus")
//	private String paymentStatus;
//
//	@JsonProperty("ChassisNumber")
//	private String chassisNumber;
//	@JsonProperty("RegistrationNumber")
//	private String registrationNumber;
//	@JsonProperty("VehicleId")
//	private String vehicleId;
//	@JsonProperty("ProductId")
//	private Integer productId;
//	@JsonProperty("SectionId")
//	private Integer sectionId;
//
//	@JsonProperty("CoverNoteReferenceNo")
//	private String coverNoteReferenceNo;
//	@JsonProperty("StickerNumber")
//	private String stickerNumber;
//	@JsonProperty("TiraResponseId")
//	private String tiraResponseId;
//	@JsonProperty("ResponseStatusCode")
//	private String responseStatusCode;

}
