package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocAgricultureMasterRes {

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
	private Integer amendId;
	private String coreAppCode;
	private String status;
	private Date entryDate;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String remarks;
}
