package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UploadDocAgricultureMasterSaveReq {

	private Integer sno;
	private Integer provinceId;
	private String provinceDesc;
	private Integer districtId;
	private String districtDesc;
	private Integer aez;
	private Integer cropId;
	private String cropDesc;
	private Integer yieldPercentage;
	private Double perHaCost;
	private Integer companyId;
	private Integer productId;
	private Integer sectionId;
	private String coreAppCode;
	private String status;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateStart;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateEnd;
	private String remarks;
}
