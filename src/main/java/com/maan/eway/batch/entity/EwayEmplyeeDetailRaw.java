package com.maan.eway.batch.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@Column(name ="DATE_OF_JOINING")
	private String dateOfJoining;
	
	@Column(name ="DATE_OF_JOIN_MONTH")
	private String dateOfJoiningMonth;
	
	@Column(name ="OCCUPATION_ID")
	private String occupationId;
	
	@Column(name ="OCCUPATION_DESC")
	private String occupatonDesc;
	
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
	
	@Column(name ="DATE_OF_JOIN_MONTH_YEAR")
	private String dateOfJoinMothYear;
	
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
	
	
	
}



