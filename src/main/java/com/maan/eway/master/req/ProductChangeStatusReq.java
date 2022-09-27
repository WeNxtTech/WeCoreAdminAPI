package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductChangeStatusReq {

	 @JsonProperty("ProductId")
	 private String productId;
	 
	 @JsonProperty("Status")
	 private String status;
}
