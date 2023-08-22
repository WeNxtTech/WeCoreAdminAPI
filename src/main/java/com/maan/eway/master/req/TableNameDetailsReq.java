package com.maan.eway.master.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TableNameDetailsReq {


    @JsonProperty("TableType")
    private String tableType;
}
