package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PremiaCustomerDetailsCriteriaRes {

	@JsonProperty("CustomerCode")
	private String customercode;

	@JsonProperty("CustomerName")
	private String customername;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	

}
