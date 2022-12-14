package com.maan.eway.document.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocumentDeleteReq {

	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("Id")
	private String id;
	
	@JsonProperty("DocumentId")
	private String documentId;
	
	@JsonProperty("DocumentReferenceNo")
	private String documentReferenceNo;
	
	
	
	
}
