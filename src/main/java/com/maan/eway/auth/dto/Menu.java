package com.maan.eway.auth.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Builder
public class Menu {
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("name")
	private String name;
	
	
	@JsonProperty("icon")
	private String icon;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("parent")
	private String parent;
	
	@JsonProperty("orderby")
	private Long orderby;
	
	@JsonProperty("children")
	private List<Menu> children;
	
	
}
