package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocRegionMasterSaveReq {

	private String regionCode;
	private String countryId;
	private String regionShortCode;
	private String regionName;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String coreAppCode;
	private String remarks;
	private String createdBy;
	private String tiraCode;
	private String regulatoryCode;
	private String regionNameLocal;
}
