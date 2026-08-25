package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocRegionMasterRes {

	private String regionCode;
	private String countryId;
	private Integer amendId;
	private String regionShortCode;
	private String regionName;
	private Date entryDate;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String coreAppCode;
	private String remarks;
	private String createdBy;
	private String tiraCode;
	private String regulatoryCode;
	private String updatedBy;
	private Date updatedDate;
	private String regionNameLocal;
}
