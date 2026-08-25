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
public class UploadDocAgricultureMasterId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer sno;
	private Integer companyId;
	private Integer productId;
	private Integer amendId;
}
