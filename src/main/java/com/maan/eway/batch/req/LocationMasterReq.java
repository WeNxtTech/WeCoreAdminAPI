package com.maan.eway.batch.req;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationMasterReq {

	@JsonProperty("CompanyId")
	private String companyId;

	@JsonProperty("BranchId")
	private String branchId;

	@JsonProperty("AmendId")
	private Integer amendId = 0;

	@JsonProperty("Country")
	private String country;

	@JsonProperty("RegionState")
	private String regionState;

	@JsonProperty("CityLocationName")
	private String cityLocationName;

	@JsonProperty("Address")
	private String address;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;

	@JsonProperty("EffectiveDate")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate effectiveDate;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("Status")
	private String status; // Active, DeActive, Pending

	// Default Constructor
	public LocationMasterReq() {
	}

	// Getters and Setters
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public Integer getAmendId() {
		return amendId;
	}

	public void setAmendId(Integer amendId) {
		this.amendId = amendId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegionState() {
		return regionState;
	}

	public void setRegionState(String regionState) {
		this.regionState = regionState;
	}

	public String getCityLocationName() {
		return cityLocationName;
	}

	public void setCityLocationName(String cityLocationName) {
		this.cityLocationName = cityLocationName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCoreAppCode() {
		return coreAppCode;
	}

	public void setCoreAppCode(String coreAppCode) {
		this.coreAppCode = coreAppCode;
	}

	public String getRegulatoryCode() {
		return regulatoryCode;
	}

	public void setRegulatoryCode(String regulatoryCode) {
		this.regulatoryCode = regulatoryCode;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}