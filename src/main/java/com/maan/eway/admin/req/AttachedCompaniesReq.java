package com.maan.eway.admin.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AttachedCompaniesReq {

	
	
	@JsonProperty("AttachedProducts")
	private List<AttachedPreductReq> attachedProducts;  

}
