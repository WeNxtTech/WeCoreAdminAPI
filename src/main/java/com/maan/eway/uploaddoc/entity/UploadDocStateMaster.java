package com.maan.eway.uploaddoc.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity mapped to table "eway_state_master".
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@IdClass(UploadDocStateMasterId.class)
@Table(name = "eway_state_master")
public class UploadDocStateMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	// --- COMPOSITE PRIMARY KEY
	@Id
	@Column(name = "STATE_ID", nullable = false)
	private Integer stateId;

	@Id
	@Column(name = "STATE_SHORT_CODE", nullable = false, length = 20)
	private String stateShortCode;

	@Id
	@Column(name = "COUNTRY_ID", nullable = false, length = 20)
	private String countryId;

	@Id
	@Column(name = "REGION_CODE", nullable = false, length = 20)
	private String regionCode;

	@Id
	@Column(name = "CITY_ID", nullable = false)
	private Integer cityId;

	@Id
	@Column(name = "SUBURB_ID", nullable = false)
	private Integer suburbId;

	@Id
	@Column(name = "AMEND_ID", nullable = false)
	private Integer amendId;

	// --- DATA FIELDS
	@Column(name = "STATE_NAME", length = 100)
	private String stateName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "STATUS", length = 1)
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_START")
	private Date effectiveDateStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_END")
	private Date effectiveDateEnd;

	@Column(name = "CORE_APP_CODE", length = 20)
	private String coreAppCode;

	@Column(name = "TIRA_CODE", length = 20)
	private String tiraCode;

	@Column(name = "CREATED_BY", nullable = false, length = 20)
	private String createdBy;

	@Column(name = "REMARKS", length = 100)
	private String remarks;

	@Column(name = "REGULATORY_CODE", nullable = false, length = 20)
	private String regulatoryCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@Column(name = "UPDATED_BY", length = 50)
	private String updatedBy;

	@Column(name = "CITY", length = 100)
	private String city;

	@Column(name = "SUBURB", length = 100)
	private String suburb;

	@Column(name = "AREA_GROUP")
	private Integer areaGroup;

	@Column(name = "SUBURB_LOCAL", length = 100)
	private String suburbLocal;

	@Column(name = "STATE_NAME_LOCAL", length = 100)
	private String stateNameLocal;

	@Column(name = "CITY_LOCAL", length = 100)
	private String cityLocal;
}
