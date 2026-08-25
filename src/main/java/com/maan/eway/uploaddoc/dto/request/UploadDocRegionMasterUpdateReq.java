package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocRegionMasterUpdateReq {

	// business key
	private String regionCode;
	private String countryId;

	// amendable fields
	private String regionShortCode;
	private String regionName;
	private String status;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String coreAppCode;
	private String remarks;
	private String tiraCode;
	private String regulatoryCode;
	private String updatedBy;
	private String regionNameLocal;
}
