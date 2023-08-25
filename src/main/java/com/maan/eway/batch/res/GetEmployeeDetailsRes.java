package com.maan.eway.batch.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetEmployeeDetailsRes {
	
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
	
	@JsonProperty("DateOfJoiningYear")
	private String dateOfJoiningYear;
	
	@JsonProperty("OccupationDesc")
	private String occupationDesc;
	
	@JsonProperty("OccupationId")
	private String occupationId;
	
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
	
	@JsonProperty("RiskId")
	private String riskId;
	
	@JsonProperty("FirstName")
	private String firstName;
	
	@JsonProperty("LastName")
	private String lastName;
	
	@JsonProperty("PassportNumber")
	private String passportNumber;
	
	@JsonProperty("Relation")
	private String relation;
	
	@JsonProperty("DateOfJoiningMonth")
	private String dateOfJoiningMonth;
}
