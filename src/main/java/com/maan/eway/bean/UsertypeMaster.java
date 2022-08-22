package com.maan.eway.bean;

import java.io.Serializable;
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


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UsertypeMasterId.class)
@Table( name ="USERTYPE_MASTER")
public class UsertypeMaster implements Serializable  {
	/**
	 * 
	 */
	        
	private static final long serialVersionUID = 1L;
	//--- ENTITY PRIMARY KEY 
  
	@Id
    @Column(name="USER_CODE",nullable=false)
    private String userCode;  
	
    @Id
    @Column(name="COMPANY_ID", nullable=false, length=100)
    private String    companyId ;
        
    @Column(name="USERTYPE", length=100)
    private String    usertype ;
    
    @Column(name="ORDER_ID")
    private Integer     orderId ;
    
    @Column(name="STATUS", length=2)
    private String     status ;
    
    @Column(name="REMARKS", length=100)
    private String     remarks ;
    
    @Temporal(TemporalType.DATE)
    @Column(name="ENTRY_DATE")
    private Date     entryDate;

    
}
