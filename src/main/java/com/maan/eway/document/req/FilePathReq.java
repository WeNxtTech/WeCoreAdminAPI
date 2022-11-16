package com.maan.eway.document.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FilePathReq {

	@JsonProperty("RequestRefNo")
	private String requestRefNo;
	@JsonProperty("DocumentReferenceNumber")
	private String reqrefno;
	@JsonProperty("DocumentTypeId")
	private String doctypeid;
}
