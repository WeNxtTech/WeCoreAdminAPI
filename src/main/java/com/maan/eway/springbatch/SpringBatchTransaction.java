package com.maan.eway.springbatch;

import java.sql.Blob;
import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "spring_batch_transaction")
public class SpringBatchTransaction {
	
	@Id
	@Column(name = "TRAN_ID")
	private Integer tranId;
	
	@Column(name = "FILENAME")
	private String fileName;
	@Column(name = "EXCEL_FILE_PATH")
	private String excelFilepath;
	@Column(name = "CSV_FILE_PATH")
	private String csvFilePath;
	@Column(name = "ENTRY_DATE")
	private Date entryDate;
	@Column(name = "ERROR_DESC")
	private String errorDesc;
	@Column(name = "PROGRESS_DESC")
	private String progressDesc;
	@Column(name = "BATCH_STATUS")
	private String batchStatus;
	@Column(name = "TOTAL_ROWS")
	private Long totalRows;
	@Column(name = "ERROR_RECORD")
	private Long errorRecord;
	@Column(name = "VALID_RECORD")
	private Long validRecord;
	@Column(name = "VALIDATION_ERROR")
	private Clob vaildationError;
	
	@Column(name = "PROGRESS_STATUS")
	private String progrsessStatus;
	
	
}
