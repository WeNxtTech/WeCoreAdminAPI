package com.maan.eway.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
@Table(name="uw_questions_details")
@IdClass(UwQuestionsDetailsId.class)
public class UwQuestionsDetails {

	@Id
	@Column(name="COMPANY_ID",length=20,nullable=false)
	private String companyId;
	
	@Id
	@Column(name="PRODUCT_ID",nullable=false)
	private Integer productId;
	
	@Id
	@Column(name="REQUEST_REFERENCE_NO",length=20,nullable=false)
	private String requestReferenceNo;
	
	@Id
	@Column(name="VEHICLE_ID",nullable=false)
	private Integer vehicleId;
	

	@Id
	@Column(name="UW_QUESTION_ID",nullable=false)
	private Integer uwQuestionId;
	
	@Column(name="UW_QUESTION_DESC",length=100)
	private String uwQuestionDesc;
	
	@Column(name="QUESTION_TYPE",length=100)
	private String questionType;
	

	@Column(name="VALUE",length=100)
	private String value;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date       entryDate ;

    @Column(name="STATUS", length=2)
    private String     status ;

    @Column(name="REMARKS", length=100)
    private String     remarks ;

    @Column(name="CREATED_BY", length=100)
    private String     createdBy;

    @Column(name="UPDATED_BY", length=100)
    private String     updatedBy;
    
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATED_DATE")
    private Date    updatedDate ;
	
	
}
