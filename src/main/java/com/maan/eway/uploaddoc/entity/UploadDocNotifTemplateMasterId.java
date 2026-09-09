package com.maan.eway.uploaddoc.entity;

import java.io.Serializable;

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
public class UploadDocNotifTemplateMasterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String notifTemplateCode;
	private String companyId;
	private Long productId;
	private Integer amendId;
}
