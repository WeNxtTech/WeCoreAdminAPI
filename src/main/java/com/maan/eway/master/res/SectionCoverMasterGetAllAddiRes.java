package com.maan.eway.master.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionCoverMasterGetAllAddiRes {

    @JsonProperty("CoverId")
    private String coverId;

    @JsonProperty("ProductId")
    private String productId;

    @JsonProperty("SectionId")
    private String sectionId;

    @JsonProperty("InsuranceId")
    private String companyId;

    @JsonProperty("EffectiveDateStart")
    private String effectiveDateStart;

    @JsonProperty("CoverName")
    private String coverName;

    @JsonProperty("CoverDesc")
    private String coverDesc;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("AdditionalInfoYN")
    private String additionalInfoYN;
}

