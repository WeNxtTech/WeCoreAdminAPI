package com.maan.eway.document.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FilePathRes {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo;

	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("DocumentReferenceNo")
	private String documentReferenceNo;

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
	
	@JsonProperty("DocumentDesc")
	private String documentDesc;
	
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
