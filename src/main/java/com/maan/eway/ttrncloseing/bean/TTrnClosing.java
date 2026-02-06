package com.maan.eway.ttrncloseing.bean;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table(name = "t_trn_closing")
public class TTrnClosing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CLO_TRAN_CODE", nullable = false)
	private Integer tranCode;

	@Column(name = "CLO_DATE_CLOSED")
	private LocalDate dateClosed;

	@Column(name = "CLO_REMARKS", length = 120)
	private String remarks;

	@Column(name = "CLO_PREPARED_BY")
	private String preparedBy;

	@Column(name = "CLO_PREPARED_DT")
	private LocalDate preparedDt;

	@Column(name = "CLO_MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "CLO_MONTHEND_DT")
	private LocalDate monthendDt;

	@Column(name = "CLO_DATE_OPENED")
	private LocalDate dateOpened;

	@Column(name = "BRANCH_CODE", length = 8)
	private String branchCode;

	@Column(name = "SET_UP_MONTH")
	private LocalDate setUpMonth;

	@Column(name = "PRODUCT_ID", length = 25)
	private String productId;

	@Column(name = "COMPANY_ID")
	private String companyid;

	@Column(name = "CLO_YEAR")
	private String year;
}

