package com.maan.eway.document.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FilePathReq {

	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("DocumentId")
	private String documentId;
	
	@JsonProperty("Id")
	private String id;
	
	@JsonProperty("DocumentReferenceNo")
	private String documentReferenceNo;
	
}
