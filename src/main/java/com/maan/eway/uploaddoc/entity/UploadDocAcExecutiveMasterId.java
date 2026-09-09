package com.maan.eway.uploaddoc.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UploadDocAcExecutiveMasterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer acExecutiveId;
	private String branchCode;
	private String companyId;
	private String status;
	private Date effectiveDateEnd;
	private String bankCode;
}
