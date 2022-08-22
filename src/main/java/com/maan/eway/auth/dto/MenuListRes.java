package com.maan.eway.auth.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MenuListRes {

	@JsonProperty("MenuId")
	private String menuId ; 
	
	@JsonProperty("MenuName")
	private String menuName ;
	
	@JsonProperty("MenuUrl")
	private String menuUrl ;
	
	@JsonProperty("ParentId")
	private String parentId ;
	
	@JsonProperty("ChildList")
	private List<MenuListRes> childList ;
}
