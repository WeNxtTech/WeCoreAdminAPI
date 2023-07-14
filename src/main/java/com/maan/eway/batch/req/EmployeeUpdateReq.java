package com.maan.eway.batch.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateReq {
	
	@JsonProperty("NationalityId")
	private String nationalityId;
	
	@JsonProperty("EmployeeName")
	private String employeeName;
	
	@JsonProperty("DateOfJoiningMonth")
	private String DateOfJoiningMonth;
	
	@JsonProperty("DateOfJoiningYear")
	private String DateOfJoiningYear;
	
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
	

}
