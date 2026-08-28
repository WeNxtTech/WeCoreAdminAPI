package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UploadDocDeductibleMasterSaveReq {

	private Integer deductId;
	private Integer deductStart;
	private Integer deductEnd;
	private Double rate;
	private String calcType;
	private String status;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateStart;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateEnd;
	private String remarks;
	private String branchCode;
	private String companyId;
	private Integer productId;
	private Integer sectionId;
}
