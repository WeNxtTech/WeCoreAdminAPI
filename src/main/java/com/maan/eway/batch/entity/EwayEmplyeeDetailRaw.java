package com.maan.eway.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@IdClass(EwayEmplyeeDetailRawId.class)
@Table(name = "EWAY_EMPLOYEE_DETAILS_RAW")
public class EwayEmplyeeDetailRaw {
	
	@Id
	@Column(name ="ROWNUM_")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer rowNum;

	@Column(name ="NATIONALITY_ID")
	private String nationalityId;
		
	
	@Column(name ="REQUEST_REFERENCE_NO")
	private String requestReferenceNo;
	

	@Column(name ="RISK_ID")
	private Integer riskId;

	@Column(name ="QUOTE_NO")
	private String quoteNo;
	

	@Column(name ="PRODUCT_ID")
	private Integer productId;
	

	@Column(name ="COMPANY_ID")
	private Integer companyId;
	
	
	@Column(name ="CREATED_BY")
	private String createdBy;
	
	@Column(name ="EMPLOYEE_NAME")
	private String employeeName;
	
	@Column(name ="YEAR_OF_JOINING")
	private String yearOfJoining;
	
	@Column(name ="MONTH_OF_JOINING_DESC")
	private String monthOfJoiningDesc;
	
	@Column(name ="OCCUPATION_ID")
	private String occupationId;
	
	@Column(name ="OCCUPATION_DESC")
	private String occupationDesc;
	
	@Column(name ="SALARY")
	private String salary;
	
	@Column(name ="SNO")
	private Integer sno;
	
	@Column(name ="DATE_OF_BIRTH")
	private String dateOfBirth;
	
	@Column(name ="ERROR_DESC")
	private String errorDesc;
	
	@Column(name ="STATUS")
	private String status;
	
	@Column(name ="TYPEID")
	private Integer typeid;
	
	@Column(name ="API_STATUS")
	private String apiStatus;
	
	@Column(name ="EMPLOYEE_TYPE")
	private String employeeType;
	
	@Column(name ="ENDORSEMENT_TYPE")
	private String endorsmentType;
	
	@Column(name ="ADDRESS")
	private String address;
	
	@Column(name ="SECTION_ID")
	private String sectionId;

	@Column(name ="RELATION_DESC")
	private String relationDesc;
	
	@Column(name ="PASS_RELATION_ID")
	private String passRelationId;
	
	@Column(name ="LOCATION_ID")
	private String locationId;
	
	@Column(name ="MONTH_OF_JOINING")
	private String monthOfJoining;
	
	@Column(name ="LOCATION_DESC")
	private String locationDesc;
	
	@Column(name ="GENDER")
	private String gender;
	
	@Column(name ="PASSPORT_NO")
	private String passportNo;

	@Column(name ="FIRST_NAME")
	private String firstName;
	
	@Column(name ="LAST_NAME")
	private String lastName;
	
	@Column(name ="GENDER_ID")
	private String genderId;
	
	@Column(name ="PASSENGER_NATIONALITY")
	private String passengerNationality;
	
	@Column(name ="CONTENT_TYPE_DESC")
	private String contentTypeDesc;
	
	@Column(name ="CONTENT_TYPE_ID")
	private String contentTypeId;
	
	@Column(name ="DESCRIPTION")
	private String description;
	
	@Column(name ="SERIAL_NUMBER")
	private String serialNumber;
	
	@Column(name ="SUM_INSURED")
	private String sumInsured;
	
	
}



