package com.maan.eway.document.res;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDocListRes {

	@JsonProperty("RequestRefNo")
	private String requestRefNo;

	@JsonProperty("DocumentReferenceNumber")
	private String documentRef;

	@Column(name = "FILE_PATH_ORGINAL")
	private String filePathOrginal;
	
	@JsonProperty("UploadedTime")
	private String uploadedtime;
	
	@JsonProperty("FileName")
	private String filename;
	
	@JsonProperty("DocumentId")
	private Integer documentId;
	
	@JsonProperty("DocDesc")
	private String docDesc;
	
	@JsonProperty("DocApplicableId")
	private String docApplicableId;
	
	@JsonProperty("DocApplicable")
	private String docApplicable;
	
    @JsonProperty("InsuranceId")
    private String CompanyId ;
	
    @JsonProperty("CreatedBy")
	private String createdby ;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonFormat(pattern = "dd/MM/YYYY")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonProperty("OrginalFileName")
	private String orginalFileName;
	
	 @JsonProperty("RequestedBy")
	private String requestedBy ;
	 
	 @JsonProperty("UplodedBy")
	private String uploadedBy ;

	
}
