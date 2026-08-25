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
 * Entity mapped to table "eway_city_master".
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
@IdClass(UploadDocCityMasterId.class)
@Table(name = "eway_city_master")
public class UploadDocCityMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CITY_ID", nullable = false)
	private Integer cityId;

	@Id
	@Column(name = "COUNTRY_ID", nullable = false, length = 20)
	private String countryId;

	@Id
	@Column(name = "STATE_ID", nullable = false, length = 20)
	private String stateId;

	@Id
	@Column(name = "AMEND_ID", nullable = false)
	private Integer amendId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_END", nullable = false)
	private Date effectiveDateEnd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_START", nullable = false)
	private Date effectiveDateStart;

	@Column(name = "CITY_NAME", length = 100)
	private String cityName;

	@Column(name = "STATUS", length = 10)
	private String status;

	@Column(name = "REMARKS", length = 100)
	private String remarks;

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "CORE_APP_CODE", length = 20)
	private String coreAppCode;

	@Column(name = "TIRA_CODE", length = 20)
	private String tiraCode;

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	private String createdBy;

	@Column(name = "REGULATORY_CODE", nullable = false, length = 20)
	private String regulatoryCode;

	@Column(name = "UPDATED_BY", length = 100)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@Column(name = "CITY_NAME_LOCAL", length = 100)
	private String cityNameLocal;
}
