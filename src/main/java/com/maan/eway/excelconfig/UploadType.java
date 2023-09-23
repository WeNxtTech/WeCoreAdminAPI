package com.maan.eway.excelconfig;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eway_upload_type_master")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadType {

	@EmbeddedId
	private UploadTypePK pk;

	@Column(name = "TYPENAME")
	private String typeName;
	
	@Column(name = "STATUS")
	private Character status;
	
	@Column(name ="RAW_TABLE_NAME")
	private String rawTableName;
	
	@Column(name = "API_NAME")
	private String apiName;
	
	@Column(name = "PRODUCT_DESC")
	private String productDesc;
	
	@Column(name = "FILE_PATH")
	private String filePath;
	
	@Column(name="RAW_TABLE_ID")
	private String rawTableId;
	
	@Column(name = "IS_MAIN_STATUS")
	private Character isMainStatus;
	
	@Column(name="API_METHOD")
	private String apiMethod;
	
}
