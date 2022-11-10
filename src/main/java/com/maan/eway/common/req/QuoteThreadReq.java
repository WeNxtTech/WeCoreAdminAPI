package com.maan.eway.common.req;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.bean.EserviceMotorDetails;

import lombok.Data;

@Data
public class QuoteThreadReq {

	@JsonProperty("RequestReferenceNo")
	private String requestReferenceNo ;
	
	@JsonProperty("QuoteNo")
	private String    quoteNo ;

	 @JsonProperty("CustomerId")
	 private String    customerId ;
	
	 @JsonProperty("CreatedBy")
		private String createdBy ;
	 
	@JsonProperty("AgencyCode")
	private String agencyCode ;
	
	@JsonProperty("Vehicles")
	private List<VehicleIdsReq> VehicleIdsList;

}
