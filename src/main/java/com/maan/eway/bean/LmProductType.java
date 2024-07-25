package com.maan.eway.bean;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.hibernate.annotations.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@IdClass(LmProductTypeId.class)
@Table(name="lm_product_type")


public class LmProductType implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="PT_TYPE", nullable=false, length=20)
    private String     ptType ;

    //--- ENTITY DATA FIELDS 
    @Column(name="PT_TYPE_DESC", length=200)
    private String     ptTypeDesc ;

    @Column(name="EFFECTIVE_DATE_START")
    private Date       effectiveDateStart ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EFFECTIVE_DATE_END")
    private Date       effectiveDateEnd ;
    
    @Column(name="ENTRY_DATE")
    private Date       entryDate ;
    
    @Column(name="CREATED_BY")
    private String createdBy;
    
    @Column(name="UPDATED_BY")
    private String updatedBy;
    
    @Column(name="UPDATED_DATE")
    private Date updatedDate;
    
    @Column(name="AMEND_ID")
    private Integer amendId;
    
    @Column(name="STATUS")
    private String status;


    //--- ENTITY LINKS ( RELATIONSHIP )


}