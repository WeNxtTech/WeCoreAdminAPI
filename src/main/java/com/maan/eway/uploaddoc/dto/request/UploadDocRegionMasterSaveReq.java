package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UploadDocRegionMasterSaveReq {

	private String regionCode;
	private String countryId;
	private String regionShortCode;
	private String regionName;
	private String status;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateStart;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateEnd;
	private String coreAppCode;
	private String remarks;
	private String createdBy;
	private String tiraCode;
	private String regulatoryCode;
	private String regionNameLocal;
}
