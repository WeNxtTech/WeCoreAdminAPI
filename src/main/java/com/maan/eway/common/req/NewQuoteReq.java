package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NewQuoteReq {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo ;
	
	@JsonProperty("VehicleIdsList")
	private List<VehicleIdsReq> vehicleIdsList;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
}
