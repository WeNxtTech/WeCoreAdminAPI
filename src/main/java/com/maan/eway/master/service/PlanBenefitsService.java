package com.maan.eway.master.service;

import java.util.List;

import com.maan.eway.error.Error;
import com.maan.eway.master.req.InsertPlanBenefitsReq;
import com.maan.eway.master.req.SuccessResponse;

public interface PlanBenefitsService {

	List<Error> validatePlanBenefits(InsertPlanBenefitsReq req);

	SuccessResponse insertPlanBenefits(InsertPlanBenefitsReq req);

}
