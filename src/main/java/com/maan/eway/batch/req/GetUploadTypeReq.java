package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetUploadTypeReq {
	
	@JsonProperty("CompanyId")
	private String companyId;

}
