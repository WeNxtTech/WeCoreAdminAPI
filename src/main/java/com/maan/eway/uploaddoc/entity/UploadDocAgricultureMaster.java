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
 * Entity mapped to table "agriculture_master".
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
@IdClass(UploadDocAgricultureMasterId.class)
@Table(name = "agriculture_master")
public class UploadDocAgricultureMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SNO", nullable = false)
	private Integer sno;

	@Id
	@Column(name = "COMPANY_ID", nullable = false)
	private Integer companyId;

	@Id
	@Column(name = "PRODUCT_ID", nullable = false)
	private Integer productId;

	@Id
	@Column(name = "AMEND_ID", nullable = false)
	private Integer amendId;

	@Column(name = "PROVINCE_ID")
	private Integer provinceId;

	@Column(name = "PROVINCE_DESC", length = 100)
	private String provinceDesc;

	@Column(name = "DISTRICT_ID")
	private Integer districtId;

	@Column(name = "DISTRICT_DESC", length = 100)
	private String districtDesc;

	@Column(name = "AEZ")
	private Integer aez;

	@Column(name = "CROP_ID")
	private Integer cropId;

	@Column(name = "CROP_DESC", length = 100)
	private String cropDesc;

	@Column(name = "YIELD_PRECENTAGE")
	private Integer yieldPercentage;

	@Column(name = "PER_HA_COST")
	private Double perHaCost;

	@Column(name = "SECTION_ID")
	private Integer sectionId;

	@Column(name = "CORE_APP_CODE", length = 100)
	private String coreAppCode;

	@Column(name = "STATUS", length = 5)
	private String status;

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_DATE_START")
	private Date effectiveDateStart;

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_DATE_END")
	private Date effectiveDateEnd;

	@Column(name = "REMARKS", length = 200)
	private String remarks;
}
