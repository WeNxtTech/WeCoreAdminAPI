package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocumentChangeStatusReq {

	 @JsonProperty("DocumentId")
	 private String documentId;
	 
	 @JsonProperty("Status")
	 private String status;
}
