package com.maan.eway.document.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FilePathRes {

	@JsonProperty("RequestRefNo")
	private String requestRefNo;

	@JsonProperty("DocumentReferenceNumber")
	private String reqrefno;

	@JsonProperty("FilePathName")
	private String filepathname ;
	@JsonProperty("UploadedTime")
	private String uploadedtime;
	
	@JsonProperty("Status")
	private String status;
	@JsonProperty("FileName")
	private String filename;
	
	@JsonProperty("DocumentId")
	private String documentId;
	
	@JsonProperty("DocDesc")
	private String docDesc;
	
	@JsonProperty("DocApplicable")
	private String docApplicable;
	@JsonProperty("DocApplicableId")
	private String docApplicableId;
	
	@JsonProperty("CommonFilePath")
	private String commonfilepath ;
	@JsonProperty("Errorres")
	private String errorres ;
	
	@JsonProperty("InsuranceId")
	private String insid;
	
	@JsonProperty("CreatedBy")
	private String createdby ;
	@JsonProperty("ImgUrl")
	private String imgurl;
}
