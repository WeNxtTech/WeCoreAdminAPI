package com.maan.eway.excelconfig2;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class XLConfigMasterPK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "COMPANY_ID")
	private Integer companyId;
	
	@Column(name="PRODUCT_ID")
	private Integer productId;
	
	@Column(name="SECTION_ID")
	private Integer sectionId;
	
	@Column(name="TYPEID")
	private Integer typeId;
	
	@Column(name="FIELDID")
	private Integer fieldId;
	
}
