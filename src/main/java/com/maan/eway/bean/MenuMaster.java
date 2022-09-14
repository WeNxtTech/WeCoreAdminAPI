package com.maan.eway.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="MENU_MASTER")
public class MenuMaster {

	@Id
	@Column(name="MENU_ID", nullable = false)
	private Integer menuId;
	
	@Column(name="MENU_NAME", length=300)
	private String menuName;
	

	@Column(name="MENU_URL", length=4000)
	private String menuUrl;
	
	@Column(name="PARENT_MENU", length=10)
	private String parentMenu;
	
	@Column(name="BRANCH_CODE", length=10)
	private String branchCode;
	
	@Column(name="PRODUCT_ID")
	private Integer productId;
	
	@Column(name="STATUS", length=1, nullable = false)
	private String status;
	
	@Column(name="RSACODE", length=25)
	private String rsacode;
	
	@Column(name="USERTYPE", length=100)
	private String usertype;
	
	@Column(name="ISCLICK", length=100)
	private String isclick;
	
	@Column(name="DISPLAY_ORDER")
	private Integer displayOrder;

	@Column(name="DISPLAY_YN")
	private String displayYn;
}
