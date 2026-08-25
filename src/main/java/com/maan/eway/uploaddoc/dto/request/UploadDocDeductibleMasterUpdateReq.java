package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocDeductibleMasterUpdateReq {

	// business key
	private Integer deductId;
	private String companyId;
	private Integer productId;
	private Integer sectionId;
	private String branchCode;

	// amendable fields
	private Integer deductStart;
	private Integer deductEnd;
	private Double rate;
	private String calcType;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String remarks;
}
