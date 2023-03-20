package com.maan.eway.master.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CoverDocumentMasterSaveReq {

	@JsonProperty("DocumentId")
	private List<String> documentId;
	
	@JsonProperty("InsuranceId")
	private String companyId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("SectionId")
	private String sectionId;
			
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("DocumentType")
	private String documentType;
	
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
    

//	@JsonProperty("CoverId")
//	private String coverId;
}
