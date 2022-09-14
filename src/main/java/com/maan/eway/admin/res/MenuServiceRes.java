package com.maan.eway.admin.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MenuServiceRes {


	@JsonProperty("UserList")
	private List<UserMenuListRes> userList;
	
	@JsonProperty("AdminList")
	private List<AdminListRes> adminlist;
	
}
