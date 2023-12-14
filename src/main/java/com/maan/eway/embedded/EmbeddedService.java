package com.maan.eway.embedded;

import java.util.Map;

import com.maan.eway.res.CommonRes;

public interface EmbeddedService {

	CommonRes getEmbeddedDetails(EmbeddedReq req);

	CommonRes getSearchType();

	CommonRes getProductDashBoard(EmbeddedDashBoardReq req);

	CommonRes getProductPlanTypeDashBoard(EmbeddedDashBoardReq req);

	CommonRes getActivePolicy(EmbeddedDashBoardReq req);

	CommonRes getAllPolicy(EmbeddedDashBoardReq req);

	CommonRes getExpiredPolicy(EmbeddedDashBoardReq req);

	Map<String,String>  getWhatsAppVehicle(Map<String, Object> req);

}
