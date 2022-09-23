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
@IdClass(MotorMakeModelMasterId.class)
@Table(name = "MOTOR_MAKEMODEL_MASTER")
public class MotorMakeModelMaster implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// --- ENTITY PRIMARY KEY

	@Id
	@Column(name = "MAKE_ID", nullable = false)
	private Integer makeId;

	@Id
	@Column(name = "MODEL_ID", nullable = false)
	private Integer modelId;
	
	@Id
	@Column(name = "BODY_ID", nullable = false)
	private Integer bodyId;
	
	@Id
	@Column(name = "INSCOMPANYID", length=100,nullable = false)
	private String insCompanyId;
	
	@Id
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "en-IN", timezone = "Asia/Calcutta")
	@Column(name = "EFFECTIVE_DATE", nullable = false, length = 100)
	private Date effectiveDate;

	@Id
	@Column(name = "AMEND_ID", nullable = false)
	private Integer amendId;

	// --- ENTITY DATA FIELDS
	@Column(name = "VEHICLEMODELCODE")
	private Integer vehiclemodelcode;

	@Column(name = "STATUS", length = 100)
	private String status;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "en-IN", timezone = "Asia/Calcutta")
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "REMARKS", length = 100)
	private String remarks;
	
	@Column(name = "MAKE_NAME_EN", length = 100)
	private String makeNameEn;
	
	@Column(name = "MODEL_NAME_EN", length = 100)
	private String modelNameEn;
	
	@Column(name = "BODY_NAME_EN", length = 100)
	private String bodyNameEn;

	@Column(name = "VEH_MANF_COUNTRY")
	private Integer vehManfCountry;
	
	@Column(name = "VEH_CC")
	private Integer vehCc;
	
	@Column(name = "VEH_WEIGHT")
	private Integer vehWeight;
	
	@Column(name = "VEH_FUELTYPE")
	private Integer vehFueltype;
	
	@Column(name = "MAKE_NAME_AR", length = 100)
	private String makeNameAr;
	
	@Column(name = "MODEL_NAME_AR", length = 100)
	private String modelNameAr;
	
	@Column(name = "TPLRATE")
	private Integer tplrate;
	
	@Column(name = "BASERATE")
	private Integer baserate;
	
	@Column(name = "NETRATE")
	private Integer netrate;
	
}