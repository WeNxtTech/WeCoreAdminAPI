package com.maan.eway.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name="raw_reference_table_details")
public class RawReferenceTableDetails {

	@Id
	@Column(name="PARENT_ID", nullable=false)
	private Integer parentId;
	
	@Column(name="TABLE_ID")
	private Integer tableId;
	
	@Column(name="TABLE_NAME",length=100)
	private String tableName;
	
	@Column(name="COLUMN_ID")
	private Integer columnId;
	
	@Column(name="COLUMN_NAME",length=100)
	private String columnName;
	
	@Column(name="KEY_NAME", length=100)
	private String keyName;
	
	@Column(name="DISPLAY_NAME",length=100)
	private String displayName;
	
	@Column(name="STATUS",length=1)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ENTRY_DATE")
	private Date entryDate;
}
