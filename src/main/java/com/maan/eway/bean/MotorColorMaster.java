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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(MotorColorMasterId.class)
@Table( name ="MOTOR_COLOR_MASTER")
public class MotorColorMaster implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//--- ENTITY PRIMARY KEY 
  
	@Id
    @Column(name="COLOR_ID",nullable=false)
    private Integer colorId;  
    
	@Id
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "en-IN", timezone = "Asia/Calcutta")
    @Column(name="EFFECTIVE_DATE",nullable=false)
    private Date effectiveDate;
  
    @Id
    @Column(name= "AMEND_ID", nullable=false)
    private Integer amendId ;
    
   
    //--- ENTITY DATA FIELDS 
    @Column(name="COLOR_CODE", length=100)
    private String    colorCode ;
    
    @Column(name="COLOR_DESC", length=100)
    private String    colorDesc ;
    
    @Column(name="CORE_APP_CODE", length=100)
    private String    coreAppCode ;

    @Column(name="STATUS", length=100)
    private String    status ;
    
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "en-IN", timezone = "Asia/Calcutta")
    @Column(name="ENTRY_DATE")
    private Date      entryDate ;


}