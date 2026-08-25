package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocCityMasterRes {

	private Integer cityId;
	private String countryId;
	private String stateId;
	private Integer amendId;
	private Date effectiveDateEnd;
	private Date effectiveDateStart;
	private String cityName;
	private String status;
	private String remarks;
	private Date entryDate;
	private String coreAppCode;
	private String tiraCode;
	private String createdBy;
	private String regulatoryCode;
	private String updatedBy;
	private Date updatedDate;
	private String cityNameLocal;
}
