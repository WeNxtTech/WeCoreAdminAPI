package com.maan.eway.uploaddoc.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class UploadDocStateMasterRes {

	private Integer stateId;
	private String stateName;
	private String stateShortCode;
	private String countryId;
	private String regionCode;
	private Integer amendId;
	private String status;
	private Date entryDate;
	private Date effectiveDateStart;
	private Date effectiveDateEnd;
	private String coreAppCode;
	private String tiraCode;
	private String createdBy;
	private String remarks;
	private String regulatoryCode;
	private Date updatedDate;
	private String updatedBy;
	private String city;
	private String suburb;
	private Integer areaGroup;
	private Integer cityId;
	private Integer suburbId;
	private String suburbLocal;
	private String stateNameLocal;
	private String cityLocal;
}
