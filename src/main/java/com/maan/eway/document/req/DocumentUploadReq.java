package com.maan.eway.document.req;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocumentUploadReq {
	

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("DocumentId")
	private String documentId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	/*@JsonProperty("CoverId")
	private String coverId;*/
	
	@JsonProperty("SectionId")
	private String sectionId;
	
	@JsonProperty("DocumentReferenceNo")
	private String documentReferenceNo;

	@JsonProperty("FileName")
	private String fileName;

	@JsonProperty("OriginalFileName")
	private String originalFileName;

	@JsonProperty("CreatedBy")
	private String createdBy;

//	@JsonProperty("DocumentType")
//	private String documentType;
//
//	@JsonProperty("DocApplicableId")
//	private String docApplicableId;

//	@JsonProperty("RequestedBy")
//	private String requesteBy;
//	
//	@JsonProperty("UplodedBy")
//	private String uploadedBy;
	
	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("Id")
	private String id;
    @JsonProperty("EndorsementDate") //EndorsementDate
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date       endorsementDate ;
    @JsonProperty("EndorsementRemarks") // EndorsementRemarks
    private String     endorsementRemarks ;    
    @JsonProperty("EndorsementEffectiveDate") // EndorsementEffectiveDate
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date       endorsementEffdate ;
    @JsonProperty("OrginalPolicyNo") // OrginalPolicyNo
    private String     originalPolicyNo ;
    @JsonProperty("EndtPrevPolicyNo") // EndtPrevPolicyNo
    private String     endtPrevPolicyNo ;
    @JsonProperty("EndtPrevQuoteNo") // EndtPrevQuoteNo
    private String     endtPrevQuoteNo ;
    @JsonProperty("EndtCount")  // EndtCount
    private String endtCount ;
    @JsonProperty("EndtStatus") //EndtStatus
    private String     endtStatus ;   
    @JsonProperty("IsFinanceEndt") //IsFinanceEndt
    private String     isFinaceYn ;  
    @JsonProperty("EndtCategoryDesc") //EndtCategoryDesc
    private String     endtCategDesc ;
    @JsonProperty("EndorsementType") //EndorsementType
    private String    endorsementType ;

    @JsonProperty("EndorsementTypeDesc") // EndorsementTypeDesc
    private String     endorsementTypeDesc ;

}
