package com.maan.eway.bean;


import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "wecore_lob_master")
@Data
public class WecoreLobMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String companyId;
	private String branchCode;

	private String className;
	private String classCode;
	private String classDescription;
	private String regulatoryCode;
	private String coreAppCode;
	private String productIconId;
	private String status;
	private String remarks;
	private String productIconName;

	private Integer amendId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDateStart;

	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDateEnd;

	private String createdBy;
	private Date createdDate;

	private String updatedBy;
	private Date updatedDate;
}