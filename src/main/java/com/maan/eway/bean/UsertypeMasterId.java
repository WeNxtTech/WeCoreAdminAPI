package com.maan.eway.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class UsertypeMasterId implements Serializable {

    private static final long serialVersionUID = 1L;

    //--- ENTITY KEY ATTRIBUTES 
    private String     code ;
    
    
    private String companyId;
}
