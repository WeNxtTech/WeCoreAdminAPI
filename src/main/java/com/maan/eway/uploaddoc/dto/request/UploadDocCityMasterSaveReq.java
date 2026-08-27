package com.maan.eway.uploaddoc.dto.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UploadDocCityMasterSaveReq {

	private Integer cityId;
	private String countryId;
	private String stateId;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateStart;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date effectiveDateEnd;
	private String cityName;
	private String status;
	private String remarks;
	private String coreAppCode;
	private String tiraCode;
	private String createdBy;
	private String regulatoryCode;
	private String cityNameLocal;
}
