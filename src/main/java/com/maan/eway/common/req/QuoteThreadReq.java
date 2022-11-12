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
	 
	 @JsonProperty("ProductId")
	 private String    productId ;
	
	 @JsonProperty("LoginId")
	 private String loginId ;
	 
	 @JsonProperty("ApplicationId")
	 private String applicationId ;
	 
	 @JsonProperty("BrokerCode")
	 private String brokerCode ;
	 
	 @JsonProperty("AcExecutiveId")
	 private String acExecutiveId ;
	 
	@JsonProperty("AgencyCode")
	private String agencyCode ;
	
	@JsonProperty("VehicleId")
	private Integer vehicleId ;

	
	@JsonProperty("Vehicles")
	private List<VehicleIdsReq> VehicleIdsList;

}
