package com.maan.eway.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name="list_item_value")
public class ListITemValue implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	 
	    //--- ENTITY PRIMARY KEY 
	    @Id
	    @Column(name="ITEM_ID", nullable=false)
	    private Integer     itemId ;

	
	    //--- ENTITY DATA FIELDS 
	    @Column(name="ITEM_TYPE", length=50)
	    private String     itemType;
	    
	    @Column(name="ITEM_CODE", length=20)
	    private String   itemCode ;


	    @Column(name="ITEM_VALUE", length = 100)
	    private String itemValue ;


	    @Column(name="STATUS", length = 5)
	    private String 	status ;


	    @Temporal(TemporalType.DATE)
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "en-IN", timezone = "Asia/Calcutta")
	    @Column(name="ENTRY_DATE")
	    private Date       entryDate ;


}
