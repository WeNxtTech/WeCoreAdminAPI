package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BranchMasterRes implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDate")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonProperty("BranchName")
	private String branchName;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("RegionCode")
	private String regionCode;
	
	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("CreatedBy")
	private String createdBy;

	@Column(name="StateCode", length=50)
    private String stateCode ;

    @Column(name="StateName", length=50)
    private String     stateName ;

    @Column(name="CityCode" ,nullable=false )
    private String cityCode ;

    @Column(name="CityName", length=50)
    private String     cityName ;
	

}
