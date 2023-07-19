package com.maan.eway.batch.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditEmployeeDetailsRes {
	
	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("ProductId")
    private String productId;
	
	@JsonProperty("RequestRefNo")
    private String requestRefNo;
	
	@JsonProperty("NationalityId")
	private String nationalityId;
	
	@JsonProperty("EmployeeName")
	private String employeeName;
	
	@JsonProperty("DateOfjoining")
	private String dateOfjoining;
	
	@JsonProperty("OccupationDesc")
	private String occupationDesc;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("ErrorDesc")
	private String errorDesc;

	@JsonProperty("RowNum")
    private String rowNum;
	
	@JsonProperty("Salary")
	private String salary;
	
	@JsonProperty("DateOfBirth")
	private String dateOfBirth;
	
	@JsonProperty("QuoteNo")
	private String quoteNo;
	
	@JsonProperty("RisId")
	private String riskId;
	

}
