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
 * Entity mapped to table "ac_executive_master".
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
@IdClass(UploadDocAcExecutiveMasterId.class)
@Table(name = "ac_executive_master")
public class UploadDocAcExecutiveMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AC_EXECUTIVE_ID", nullable = false)
	private Integer acExecutiveId;

	@Id
	@Column(name = "BRANCH_CODE", nullable = false, length = 20)
	private String branchCode;

	@Id
	@Column(name = "COMPANY_ID", nullable = false, length = 20)
	private String companyId;

	@Id
	@Column(name = "STATUS", nullable = false, length = 2)
	private String status;

	@Id
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_END", nullable = false)
	private Date effectiveDateEnd;

	@Id
	@Column(name = "BANK_CODE", nullable = false, length = 20)
	private String bankCode;

	@Column(name = "AC_EXECUTIVE_NAME", length = 100)
	private String acExecutiveName;

	@Column(name = "OA_CODE", length = 20)
	private String oaCode;

	@Column(name = "COMMISSION_PERCENT")
	private Double commissionPercent;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE_START", nullable = false)
	private Date effectiveDateStart;

	@Column(name = "AMEND_ID")
	private Integer amendId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;
	
	@Column(name = "CORE_APP_CODE")
	private String coreAppCode;

}
