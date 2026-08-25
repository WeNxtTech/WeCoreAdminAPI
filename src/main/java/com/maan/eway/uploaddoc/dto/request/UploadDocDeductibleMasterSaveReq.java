package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocDeductibleMasterSaveReq {

	private Integer deductId;
	private Integer deductStart;
	private Integer deductEnd;
	private Double rate;
	private String calcType;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String remarks;
	private String branchCode;
	private String companyId;
	private Integer productId;
	private Integer sectionId;
}
