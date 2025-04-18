package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmiDetailsReq {
	
	@JsonProperty("PremiumStart")
	private String premiumStart;

	@JsonProperty("PremiumEnd")
	private String premiumEnd;

	@JsonProperty("InterestPercent")
    private String  interestPercent ;
    
    @JsonProperty("AdvancePercent")
    private String     advancePercent ;
    
	@JsonProperty("InstallmentTypeId")
    private String installmentTypeId;
	
	@JsonProperty("InstallmentPeriod")
	private String installmentPeriod;
	
	@JsonProperty("EmiId")
	private String emiId;
}
