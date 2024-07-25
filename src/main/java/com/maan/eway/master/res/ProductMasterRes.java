package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductMasterRes implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("ProductId")
	private String productId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonProperty("ProductName")
	private String productName;

	@JsonProperty("PackageYn")
	private String packageYn;
	
	@JsonProperty("MotorYn")
	private String motorYn;

	@JsonProperty("CurrencyIds")
	private List<String> currencyIds;
	
	@JsonProperty("PaymentYn")
	private String paymentYn;

	@JsonProperty("PaymentRedirUrl")
	private String paymentRedirUrl;

	@JsonProperty("AppLoginUrl")
	private String appLoginUrl;

	@JsonProperty("Status")
	private String status;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonProperty("ProductDesc")
	private String productDesc;

	@JsonProperty("RegulatoryCode")
	private String regulatoryCode;
	
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("ProductIconId")
	private String productIconId;
	
	@JsonProperty("ProductIconName")
	private String productIconName;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
	@JsonProperty("SectionEndtYn")
	private String sectionEndtYn;
	
	
}
