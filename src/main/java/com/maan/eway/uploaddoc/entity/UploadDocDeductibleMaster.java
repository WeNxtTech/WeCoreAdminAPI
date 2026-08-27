package com.maan.eway.uploaddoc.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
 * Entity mapped to table "eway_deductible_master".
 *
 * NOTE (documented assumption): the source table definition supplied in the
 * requirements document has no PRIMARY KEY / UNIQUE KEY at all. To make the
 * table safely usable through JPA and to support row-level amendment
 * (AMEND_ID) history, a surrogate auto-increment primary key
 * {@code DEDUCT_MASTER_ID} has been added. The business key used for
 * amendment/versioning purposes is (deductId, companyId, productId,
 * sectionId, branchCode).
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
@Table(name = "eway_deductible_master")
public class UploadDocDeductibleMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DEDUCT_ID")
	private Integer deductId;

	@Column(name = "DEDUCT_START")
	private Integer deductStart;

	@Column(name = "DEDUCT_END")
	private Integer deductEnd;

	@Column(name = "RATE")
	private Double rate;

	@Column(name = "CALC_TYPE", length = 20)
	private String calcType;

	@Column(name = "AMEND_ID")
	private Integer amendId;

	@Column(name = "STATUS", length = 10)
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_START")
	private Date effectiveDateStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_END")
	private Date effectiveDateEnd;

	@Column(name = "REMARKS", length = 500)
	private String remarks;

	@Column(name = "BRANCH_CODE", length = 10)
	private String branchCode;

	@Column(name = "COMPANY_ID", length = 20)
	private String companyId;

	@Column(name = "PRODUCT_ID")
	private Integer productId;

	@Column(name = "SECTION_ID")
	private Integer sectionId;
}
