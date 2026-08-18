package com.maan.eway.bean;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "location_master")
public class LocationMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "company_id", nullable = false)
	private String companyId;

	@Column(name = "branch_id", nullable = false)
	private String branchId;

	@Column(name = "amend_id", nullable = false)
	private Integer amendId = 0;

	@Column(name = "country", nullable = false)
	private String country;

	@Column(name = "region_state", nullable = false)
	private String regionState;

	@Column(name = "city_location_name", nullable = false)
	private String cityLocationName;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "core_app_code", nullable = false)
	private String coreAppCode;

	@Column(name = "regulatory_code", nullable = false)
	private String regulatoryCode;

	@Column(name = "effective_date")
	private LocalDate effectiveDate;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "status", nullable = false)
	private String status; // Active, DeActive, Pending

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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