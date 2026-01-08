package com.maan.eway.ttrncloseing.bean;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "t_trn_closing")
public class TTrnClosing {

    @Id
    @Column(name = "CLO_TRAN_CODE", nullable = false)
    private Integer tranCode;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLO_DATE_CLOSED")
    private Date dateClosed;

    @Column(name = "CLO_REMARKS", length = 120)
    private String remarks;

    @Column(name = "CLO_PREPARED_BY")
    private Integer preparedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLO_PREPARED_DT")
    private Date preparedDt;

    @Column(name = "CLO_MODIFIED_BY")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLO_MONTHEND_DT")
    private Date monthendDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CLO_DATE_OPENED")
    private Date dateOpened;

    @Column(name = "BRANCH_CODE", length = 8)
    private String branchCode;

    @Column(name = "PRODUCT_CORE_CODE", length = 25)
    private String productCoreCode;
    
    @Column(name = "COMPANY_ID")
    private String company_id;
}

