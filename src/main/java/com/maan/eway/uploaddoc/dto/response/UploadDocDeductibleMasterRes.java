package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocDeductibleMasterRes {

	private Long deductMasterId;
	private Integer deductId;
	private Integer deductStart;
	private Integer deductEnd;
	private Double rate;
	private String calcType;
	private Integer amendId;
	private String status;
	private Date entryDate;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String remarks;
	private String branchCode;
	private String companyId;
	private Integer productId;
	private Integer sectionId;
}
