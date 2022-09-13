package com.maan.eway.master.res;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskDomesticCriteriaGetRes {

	@JsonProperty("CustomerId")
	private Integer     customerId ;

	@JsonProperty("RequestReferenceNo ")
	private String     requestReferenceNo ;
	
	@JsonProperty("LocationId")
	private Integer     locationId ;
	
	@JsonProperty("ProductId")
	private Integer     productId ;

    @JsonProperty("BranchCode")
	private String     branchCode;
	 
    @JsonProperty("OldProductName")
    private String     oldProductName ;

    @JsonProperty("InsuranceId")
    private String     companyId ;

    
    @JsonProperty("EntryDate")
    private Date       entryDate ;

    @JsonProperty("Status")
    private String     status ;
    
    @JsonProperty("OWN_HOUSE_YN")
    private String     ownHouseYn ;

    @JsonProperty("REMARKS")
    private String     remarks ;
    

    @JsonProperty("CREATED_BY")
    private String     createdBy;
    

    @JsonProperty("UpdatedBy")
    private String     updatedBy ;
    
    
    @JsonProperty("UpdatedDate")
    private Date       updatedDate ;
    
    @JsonProperty("ProductName")
    private String     productName ;
}
