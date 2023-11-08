package com.maan.eway.common.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.bean.PaymentDetail;

import lombok.Data;

@Data
public class TransactionCheckStatusRes {

	@JsonProperty("LatestTiraSearchedVehicle")
    private LastTiraSearchedVehicleRes latestTiraSearchedVehicle ;
	
	@JsonProperty("LatestSentMail")
    private LastMailSentRes latestSentMail ;
	
	@JsonProperty("LatestSentSms")
    private LastSmSSentRes latestSentSms ;
	
	@JsonProperty("LatestConvertedPolicy")
    private LastConvertedPolicyRes  latestConvertedPolicy ;
	
	@JsonProperty("LatestEndrosmentPolicy")
    private LastConvertedEndtPolicyRes LatestendtPolicyRes ;
	
	@JsonProperty("LatestOnlinePayment")
    private LastestOnlinePaymentRes latestOnlinePaymentRes ;
	
	@JsonProperty("LatestCashPayment")
    private LatestCashPaymentRes latestCashPaymentRes ;
	
	@JsonProperty("LatestChequePayment")
    private LatestChequePaymentRes latestChequePaymentRes ;
	
	@JsonProperty("LatestCreditPayment")
    private LatestCreditPaymentRes latestCreditPaymentRes ;
	
	@JsonProperty("LatestRefundPayment")
    private LatestRefundPaymentRes latestRefundPaymentRes ;
	
}
