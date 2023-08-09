package com.maan.eway.master.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertPlanBenefitsReq {
	@JsonProperty("PlanTypeId")
    private String planTypeId ;
	
	@JsonProperty("PlanTypeDesc")
    private String planTypeDesc ;
	
	@JsonProperty("PolicyTypeId")
    private String PolicyTypeId ;
	
	@JsonProperty("PolicyTypeDesc")
    private String PolicyTypeDesc ;

    @JsonProperty("InsuranceId")
    private String    companyId ;
  
    @JsonProperty("ProductId")
    private String    productId ;
    
    @JsonProperty("BranchCode")
    private String    branchCode ;
  
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("EffectiveDateStart")
    private Date       effectiveDateStart ;

//    @JsonFormat(pattern = "dd/MM/yyyy")
//    @JsonProperty("EffectiveDateEnd")
//    private Date       effectiveDateEnd ;
    
	@JsonProperty("Remarks")
    private String  remarks;

    
    @JsonProperty("CreatedBy")
    private String     createdBy ;
    
//    @JsonProperty("InsertPlanBenefits")
//    private List<InsertPlanBenefits>     insertPlanBenefits;
    
    @JsonProperty("CoverDetails")
    private List<TravelPolicyTypeCoverRes> travelCover ;
    
}
