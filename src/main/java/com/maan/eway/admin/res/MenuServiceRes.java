package com.maan.eway.admin.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MenuServiceRes {

	@JsonProperty("MenuId")
	private Integer menuId;
	

	@JsonProperty("MenuName")
	private String menuName;
	

	@JsonProperty("MenuUrl")
	private String menuUrl;


	@JsonProperty("ParentMenu")
	private String parentMenu;
	
	@JsonProperty("DisplayOrder")
	private Integer displayOrder;
	
	
	@JsonProperty("AdminList")
	private List<AdminListRes> adminlist;
	
}
