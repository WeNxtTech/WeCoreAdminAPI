package com.maan.eway.master.res;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.eway.master.req.DropdownTableDetailsSaveReq;

import lombok.Data;

@Data
public class ConstantTableDetailsCommonRes implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("ConstantTableDetails")
	private ConstantTableDetailsRes ConstantTableDetailsRes;
	
	
	@JsonProperty("DropdownTableDetails")
	private List<DropdownTableDetailsRes> DropdownTableDetailsRes;

}
