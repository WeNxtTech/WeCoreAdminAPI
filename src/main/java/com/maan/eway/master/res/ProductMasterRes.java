package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductMasterRes implements Serializable {

    private static final long serialVersionUID = 1L;

	@JsonProperty("ProductId")
	private String productId;

	@JsonProperty("InsuranceId")
	private String companyId;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateStart")
	private Date effectiveDateStart;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EffectiveDateEnd")
	private Date effectiveDateEnd;

	@JsonProperty("ProductName")
	private String productName;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("InceptionDate")
	private Date inceptionDate;

	@JsonProperty("DisplayOrder")
	private String displayOrder;

	@JsonProperty("PaymentYn")
	private String paymentYn;

	@JsonProperty("PaymentRedirUrl")
	private String paymentRedirUrl;

	@JsonProperty("AppLoginUrl")
	private String appLoginUrl;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("Rsacode")
	private String rsacode;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("EntryDate")
	private Date entryDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("ExpiryDate")
	private Date expiryDate;

	@JsonProperty("ProductCategory")
	private String productCategory;

	@JsonProperty("CoreAppCode")
	private String coreAppCode;

	@JsonProperty("AmendId")
	private Integer amendId;

	@JsonProperty("Remarks")
	private String remarks;
}
