package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocAgricultureMasterUpdateReq {

	// business key
	private Integer sno;
	private Integer companyId;
	private Integer productId;

	// amendable fields
	private Integer provinceId;
	private String provinceDesc;
	private Integer districtId;
	private String districtDesc;
	private Integer aez;
	private Integer cropId;
	private String cropDesc;
	private Integer yieldPercentage;
	private Double perHaCost;
	private Integer sectionId;
	private String coreAppCode;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String remarks;
}
