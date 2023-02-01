package com.maan.eway.springbatch;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FactorRateRawMasterId.class)
@Entity
@Table(name = "factor_rate_raw_master")
public class FactorRateRawInsert {
	
	
		@Id
	    @Column(name="FACTOR_TYPE_ID", nullable=false)
	    private Integer    factorTypeId ;

	    @Id
	    @Column(name="S_NO", nullable=false)
	    private Integer    sNo ;

	    @Id
	    @Column(name="COMPANY_ID", nullable=false, length=100)
	    private String     companyId ;

	    @Id
	    @Column(name="PRODUCT_ID", nullable=false)
	    private Integer    productId ;

	    @Id
	    @Column(name="BRANCH_CODE", nullable=false, length=20)
	    private String     branchCode ;

	    @Id
	    @Column(name="AGENCY_CODE", nullable=false, length=20)
	    private String     agencyCode ;

	    @Id
	    @Column(name="SECTION_ID", nullable=false)
	    private Integer    sectionId ;

	    @Id
	    @Column(name="COVER_ID", nullable=false)
	    private Integer    coverId ;

	    @Id
	    @Column(name="SUB_COVER_ID", nullable=false)
	    private Integer    subCoverId ;

	    @Id
	    @Column(name="AMEND_ID", nullable=false)
	    private Integer    amendId ;
	    
	    // created new columns by baskar
	    
	    @Column(name="TRAN_ID", length=20)
	    private String     tranId;
	    
	    @Column(name="GROUP_ID", length=20)
	    private String groupId;
	    
	    @Column(name="ERROR_DESC", length=20)
	    private String  errorDesc;
	    
	    @Column(name="GROUPING_COLUMN", length=20)
	    private String  groupingColumn;
	    
	    

	    //--- ENTITY DATA FIELDS 
	    @Column(name="FACTOR_TYPE_NAME", length=100)
	    private String     factorTypeName ;

	    @Column(name="FACTOR_TYPE_DESC", length=200)
	    private String     factorTypeDesc ;

	    @Column(name="CREATED_BY", length=100)
	    private String     createdBy ;

	    @Column(name="COVER_NAME", length=100)
	    private String     coverName ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="ENTRY_DATE")
	    private Date       entryDate ;

	    
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="EFFECTIVE_DATE_START", nullable=false)
	    private Date       effectiveDateStart ;

	    @Column(name="COVER_DESC", length=200)
	    private String     coverDesc ;

	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="EFFECTIVE_DATE_END", nullable=false)
	    private Date       effectiveDateEnd ;

	    @Column(name="SUB_COVER_NAME", length=100)
	    private String     subCoverName ;

	    @Column(name="SUB_COVER_DESC", length=200)
	    private String     subCoverDesc ;

	    @Column(name="STATUS", length=1)
	    private String     status ;

	    @Column(name="REMARKS", length=100)
	    private String     remarks ;

	    @Column(name="PARAM_1")
	    private Double     param1 ;

	    @Column(name="PARAM_2")
	    private Double     param2 ;

	    @Column(name="PARAM_3")
	    private Double     param3 ;

	    @Column(name="PARAM_4")
	    private Double     param4 ;

	    @Column(name="PARAM_5")
	    private Double     param5 ;

	    @Column(name="PARAM_6")
	    private Double     param6 ;

	    @Column(name="PARAM_7")
	    private Double     param7 ;

	    @Column(name="PARAM_8")
	    private Double     param8 ;

	    @Column(name="PARAM_9", length=100)
	    private String     param9 ;

	    @Column(name="PARAM_10", length=100)
	    private String     param10 ;

	    @Column(name="PARAM_11", length=100)
	    private String     param11 ;

	    @Column(name="PARAM_12", length=100)
	    private String     param12 ;

	        
	    @Column(name="UPDATED_BY", length=100)
	    private String     updatedBy ;
	   
	    @Temporal(TemporalType.TIMESTAMP)
	    @Column(name="UPDATED_DATE")
	    private Date updatedDate ;
	 
	        
	    @Column(name="RATE")
	    private Double     rate ;

	    @Column(name="CALC_TYPE", length=1)
	    private String     calcType ;

	    @Column(name="CALC_TYPE_DESC", length=100)
	    private String     calcTypeDesc ;

	    @Column(name="MIN_PREMIUM")
	    private Double     minPremium ;

	    @Column(name="REGULATORY_CODE", length=20)
	    private String     regulatoryCode ;

	    @Column(name="CORE_APP_CODE", length=20)
	    private String     coreAppCode ;

	    @Column(name="MASTER_YN", length=20)
	    private String     masterYn;
	    
	    @Column(name="API_URL", length=20)
	    private String     apiUrl;
	    
	    @Column(name="ERROR_STATUS", length=20)
	    private String   errorStatus;
    
   

}
