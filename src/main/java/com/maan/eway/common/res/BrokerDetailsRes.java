package com.maan.eway.common.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BrokerDetailsRes {

	@JsonProperty("status_msg")
	private String status;

	@JsonProperty("Data")
	private List<BrokerDetails> data;

}
