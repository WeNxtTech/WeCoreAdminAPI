package com.maan.eway.master.res;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGetallRes {

	@JsonProperty("CustomerId")
    private String     customerId;
	@JsonProperty("GstNo")
    private String    gstNo  ;
	
	@JsonProperty("CompanyId")
    private String     companyId;
	@JsonProperty("BranchCode")
    private String     branchCode;
	@JsonProperty("ClientName")
    private String     clientName;
	@JsonProperty("DateOfBirth")
    private Date       dateOfBirth  ;

	@JsonProperty("CreatedBy")
	private String createdBy;
	@JsonProperty("MobileNo1")
    private String     mobileNo1    ;
/*
	@JsonProperty("ProductCount")
	private String productCount;
*/

}
