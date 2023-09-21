package com.maan.eway.batch.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@IdClass(EwayUploadTypeMasterIdV1.class)
@Table(name="eway_upload_type_master_v2")
public class EwayUploadTypeMasterV1 {
	
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="PRODUCT_ID", nullable=false)
	    private Integer    productId ;

	    @Id
	    @Column(name="TYPEID", nullable=false)
	    private Integer    typeid ;
	    
	    @Id
	    @Column(name="COMPANY_ID")
	    private Integer    companyId ;
	  
	    //--- ENTITY DATA FIELDS 
	  
	    @Column(name="SECTION_ID")
	    private Integer    sectionId ;
	    

	    @Column(name="TYPENAME", length=200)
	    private String     typename ;

	    @Column(name="STATUS", length=1)
	    private String     status ;

	    @Column(name="RAW_TABLE_NAME", length=200)
	    private String     rawTableName ;

	    @Column(name="RAW_TABLE_ID", length=200)
	    private Integer     rawTableId ;
	    
	    @Column(name="API_NAME", length=200)
	    private String     apiName ;

	    @Column(name="PRODUCT_DESC", length=200)
	    private String     productDesc ;

	    @Column(name="FILE_PATH", length=100)
	    private String     filePath ;

	    @Column(name="ENTRY_DATE")
	    private Date    entryDate ;
	    //--- ENTITY LINKS ( RELATIONSHIP )

}
