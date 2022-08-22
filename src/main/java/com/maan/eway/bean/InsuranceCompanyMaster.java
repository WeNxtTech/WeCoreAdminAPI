package com.maan.eway.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(InsuranceCompanyMasterId.class)
@Table(name = "INSURANCE_COMPANY_MASTER")
public class InsuranceCompanyMaster implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// --- ENTITY PRIMARY KEY

	@Id
	@Column(name = "INS_ID", nullable = false)
	private String insId;

	// --- ENTITY DATA FIELDS
	@Column(name = "INS_NAME", length = 100)
	private String insName;

	@Column(name = "STATUS", length = 100)
	private String status;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "en-IN", timezone = "Asia/Calcutta")
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "REMARKS", length = 100)
	private String remarks;

	@Column(name = "INS_ADDRESS", length = 100)
	private String insAddress;
	
	@Column(name = "INS_COUNTRY")
	private Integer insCountry;
	
	@Column(name = "INS_CITY")
	private Integer insCity;
	
	@Column(name = "INS_ZIPCODE", length = 100)
	private String insZipcode;
	
	@Column(name = "INS_PHONE", length = 100)
	private String insPhone;
	
	@Column(name = "MOBILE_NO", length = 100)
	private String mobileNo;
	
	@Column(name = "INS_EMAIL", length = 100)
	private String insEmail;
	
	@Column(name="COMPANY_LOGO")
	private String  companyLogo ;
	    
	@Column(name="REGARDS")
	private String  regards ;
	
}