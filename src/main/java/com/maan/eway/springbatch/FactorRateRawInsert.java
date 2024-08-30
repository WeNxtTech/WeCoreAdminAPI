package com.maan.eway.springbatch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
public class FactorRateRawInsert implements Serializable{
	
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		@Id
	    @Column(name="FACTOR_TYPE_ID", nullable=false)
	    private Integer    factorTypeId ;

	    @Id
	    @Column(name="SNO", nullable=false)
	    private Integer    sno ;

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
	    
	    @Id
	    @Column(name="TRAN_ID", length=20)
	    private String     tranId;
	    
	    // created new columns by baskar
	    
	   
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
	    private String     param1 ;

	    @Column(name="PARAM_2")
	    private String     param2 ;

	    @Column(name="PARAM_3")
	    private String     param3 ;

	    @Column(name="PARAM_4")
	    private String     param4 ;

	    @Column(name="PARAM_5")
	    private String     param5 ;

	    @Column(name="PARAM_6")
	    private String     param6 ;

	    @Column(name="PARAM_7")
	    private String     param7 ;

	    @Column(name="PARAM_8")
	    private String     param8 ;

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
	    private String     rate ;
	    
	    @Column(name="MINIMUM_RATE")
	    private String     minimumRate ;

	    @Column(name="CALC_TYPE", length=1)
	    private String     calcType ;

	    @Column(name="CALC_TYPE_DESC", length=100)
	    private String     calcTypeDesc ;

	    @Column(name="MIN_PREMIUM")
	    private String     minPremium ;

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
    
	    @Column(name="XL_AGENCY_CODE", length=20)
	    private String   xlAgencyCode;
	    
	    @Column(name="PARAM_13", length=100)
	    private String     param13 ;
	    
	    @Column(name="PARAM_14", length=100)
	    private String     param14 ;
	    
	    @Column(name="PARAM_15", length=100)
	    private String     param15 ;
	    
	    @Column(name="PARAM_16", length=100)
	    private String     param16 ;
	    
	    @Column(name="PARAM_17", length=100)
	    private String     param17 ;
	    
	    @Column(name="PARAM_18", length=100)
	    private String     param18 ;
	    
	    @Column(name="PARAM_19", length=100)
	    private String     param19 ;
	    
	    @Column(name="PARAM_20", length=100)
	    private String     param20 ;
	    
	    @Column(name="PARAM_21")
	    private String     param21 ;
	    
	    @Column(name="PARAM_22")
	    private String     param22 ;
	    
	    @Column(name="PARAM_23")
	    private String     param23 ;
	    
	    @Column(name="PARAM_24")
	    private String     param24 ;
	    
	    @Column(name="PARAM_25")
	    private String     param25 ;
	    
	    @Column(name="PARAM_26")
	    private String     param26 ;
	    
	    @Column(name="PARAM_27")
	    private String     param27;
	   
	    @Column(name="PARAM_28")
	    private String     param28;
	    
	    @Column(name="EXCESS_AMOUNT")
	    private String     excessAmount ;
	    
	    @Column(name="EXCESS_PERCENT")
	    private String     excessPercent ;
	    
	    @Column(name="EXCESS_DESC")
	    private String     excessDesc ;
	    

}
