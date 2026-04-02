package com.maan.eway.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class SectionDetailsRes {

	@JsonProperty("CoverNoteReferenceNo")
	private String coverNoteReferenceNo;
	@JsonProperty("StickerNumber")
	private String stickerNumber;
	@JsonProperty("TiraResponseId")
	private String tiraResponseId;
	@JsonProperty("ResponseStatusCode")
	private String responseStatusCode;
	@JsonProperty("QuoteNo")
	private String quoteNo;
	@JsonProperty("ProductId")
	private String productId;

}
