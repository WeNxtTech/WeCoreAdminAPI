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
 * Entity mapped to table "eway_region_master".
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
@IdClass(UploadDocRegionMasterId.class)
@Table(name = "eway_region_master")
public class UploadDocRegionMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REGION_CODE", nullable = false, length = 20)
	private String regionCode;

	@Id
	@Column(name = "COUNTRY_ID", nullable = false, length = 20)
	private String countryId;

	@Id
	@Column(name = "AMEND_ID", nullable = false)
	private Integer amendId;

	@Column(name = "REGION_SHORT_CODE", length = 20)
	private String regionShortCode;

	@Column(name = "REGION_NAME", length = 100)
	private String regionName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "STATUS", length = 1)
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_START", nullable = false)
	private Date effectiveDateStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_END", nullable = false)
	private Date effectiveDateEnd;

	@Column(name = "CORE_APP_CODE", length = 20)
	private String coreAppCode;

	@Column(name = "REMARKS", length = 100)
	private String remarks;

	@Column(name = "CREATED_BY", nullable = false, length = 50)
	private String createdBy;

	@Column(name = "TIRA_CODE", length = 20)
	private String tiraCode;

	@Column(name = "REGULATORY_CODE", nullable = false, length = 20)
	private String regulatoryCode;

	@Column(name = "UPDATED_BY", length = 20)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@Column(name = "REGION_NAME_LOCAL", length = 100)
	private String regionNameLocal;
}
