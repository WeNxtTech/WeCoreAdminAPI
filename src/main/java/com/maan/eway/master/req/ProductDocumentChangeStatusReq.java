package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductDocumentChangeStatusReq {

	 @JsonProperty("DocumentId")
	 private String documentId;
	 
	 @JsonProperty("Status")
	 private String status;
}
