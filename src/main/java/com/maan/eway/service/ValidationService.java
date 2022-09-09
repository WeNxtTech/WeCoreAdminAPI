package com.maan.eway.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.CustomerSaveReq;
import com.maan.eway.master.req.ProductsRiskSaveReq;
import com.maan.eway.master.req.RiskDomesticDetailsSaveReq;

import com.maan.eway.req.RawPersonalAccidentSaveReq;

import com.maan.eway.req.RawAllRisksSaveReq;
import com.maan.eway.req.RawBuildingsDetailsSaveReq;
import com.maan.eway.req.RawContentsDetailsSaveReq;


public interface ValidationService {

	List<Error> validateCustomerSave(CustomerSaveReq req) ;

	List<Error> validateCustomerUpdate(CustomerSaveReq req);

	List<Error> validateRiskDetails(RiskDomesticDetailsSaveReq req);


 List<Error> validatePersonalAccident(RawPersonalAccidentSaveReq req);

	List<Error> validateProductSections(ProductsRiskSaveReq req);

	List<Error> validateProductSections(RiskDomesticDetailsSaveReq req);

	List<Error> validateRawBuildingDetails(RawBuildingsDetailsSaveReq req);

	List<Error> validateRawContentDetails(RawContentsDetailsSaveReq req);

	List<Error> validateRawAllRisk(RawAllRisksSaveReq req);



}
