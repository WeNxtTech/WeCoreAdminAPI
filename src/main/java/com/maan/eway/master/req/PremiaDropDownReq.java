package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PremiaDropDownReq {

	@JsonProperty("SourceType")
	private String sourcetype;
	@JsonProperty("DivisionCode")
	private String divisioncode;
	@JsonProperty("InsuranceId")
	private String companyId;
	@JsonProperty("ChannelType")
	private String channeltype;
	@JsonProperty("SearchBy")
	private String searchby;
	@JsonProperty("SearchValue")
	private String searchvalue;
	@JsonProperty("LoginId")
	private String loginid;
	@JsonProperty("UserType")
	private String usertype;
	@JsonProperty("ProductId")
	private String productid;

	@JsonProperty("productId")
	private String product_id;

}
