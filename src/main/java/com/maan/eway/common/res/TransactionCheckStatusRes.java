package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransactionCheckStatusRes {

	@JsonProperty("LatestTiraSearchedVehicle")
    private LastTiraSearchedVehicleRes lastestTiraSearchedVehicle ;
	
	@JsonProperty("LatestSentMail")
    private LastMailSentRes lastestSentMail ;
	
	@JsonProperty("LatestSentSms")
    private LastSmSSentRes lastestSentSms ;
	
	@JsonProperty("LatestConvertedPolicy")
    private LastConvertedPolicyRes  lastestConvertedPolicy ;
	
	@JsonProperty("LatestEndrosmentPolicy")
    private LastConvertedEndtPolicyRes LatestendtPolicyRes ;
	
	
}
