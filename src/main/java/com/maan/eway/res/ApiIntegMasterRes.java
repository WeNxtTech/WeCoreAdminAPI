/**
 * @author : Ashok Kumar S 
 * @since  : 10-02-2025
 */
package com.maan.eway.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ApiIntegMasterRes {
	
	@JsonProperty("companyId")
	private String companyId;

	@JsonProperty("productId")
	@JsonFormat(shape = Shape.STRING)
	private Integer productId;

	@JsonProperty("apiType")
	private String apiType;

	@JsonProperty("status")
	private String status;
	
	@JsonProperty("apiDesc")
	private String apiDesc;
	
	@JsonProperty("apiUrl")
	private String apiUrl;

	//Not in ApiIntegMaster taken from FlowFieldDetails
	@JsonProperty("flowCount")
	private int flowCount;
}
