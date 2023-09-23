package com.maan.eway.excelconfig2;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "eway_xlconfig_master")
@Getter
@Setter
public class XLConfigMaster {

	@EmbeddedId
	private XLConfigMasterPK pk;
	
	@Column(name="EXCELHEADER_NAME")
	private String excelHeaderName;
	
	@Column(name="MANDATORYYN")
	private String mandatoryYn;
	
	@Column(name="DATA_TYPE")
	private String dataType;
	
	@Column(name="DATE_FORMAT")
	private String dateFormat;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="EXCEL_COLUMN_INDEX")
	private Integer excelColumnIndex;
	
	@Column(name="FIELD_NAME_RAW")
	private String fieldNameRaw;
	
	@Column(name="FIELD_NAME_MAIN")
	private String fieldNameMain;
	
	@Column(name="FIELD_NAME_ERROR")
	private String fieldNameError;
	
	@Column(name="EXCEL_COLUMN_YN")
	private String excelColumnYn;
	
	@Column(name = "DUBLICATE_CHECK")
	private String dublicateCheck;
	
	@Column(name="FIELD_LENGTH")
	private Integer fieldLength;
	
	@Column(name="MASTER_CHECK")
	private String masterCheck;
	
	@Column(name="MASTER_CHECK_FIELD")
	private String masterCheckField;
	
	@Column(name="DATA_RANGE")
	private String dataRange;
	
	@Column(name="ISMAIN_DEFAU_VAL")
	private String isMainDefauVal;
	
	@Column(name="API_JSON_KEY")
	private String apiJsonKey;
	
	@Column(name="SEL_COL_NAME")
	private String selColName;
	
	@Column(name="IS_MAIN_COL_IDX")
	private Integer isMainColIdx;
	
	@Column(name="IS_OBJECT")
	private String isObject;
	
	@Column(name="IS_ARRAY")
	private String isArray;
	
	@Column(name="OBJ_APIJSON_KEY")
	private String objApijsonKey;
	
	@Column(name="OBJ_SELCOL_KEY")
	private String objSelcolKey;
	
	@Column(name="OBJ_DEFAUL_VAL")
	private String objDefaulVal;
	
	@Column(name="ISMAIN_MOVE")
	private String ismainMove;
	
}
