package com.maan.eway.bean;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "eway_screen_field_master")
@Builder
@ToString
@Getter
@Setter
public class ScreenFieldMaster implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "FIELD_ID")
	private Integer fieldId;
	
	@Column(name = "PRODUCT_ID")
	private Integer productId;
	
	@Column(name = "SECTION_ID")
	private Integer sectionId;
	
	@Column(name = "COMPANY_ID")
	private String companyId;
	
	@Column(name = "FIELDS")
	private String fields;
	
	@Column(name = "FIELD_NAME")
	private String fieldName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE")
	private Date effectiveDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE")
	private Date entryDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;
	
	@Column(name = "STATUS")
	private String status;

}
