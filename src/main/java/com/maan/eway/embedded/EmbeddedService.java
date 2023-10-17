package com.maan.eway.embedded;

import com.maan.eway.res.CommonRes;

public interface EmbeddedService {

	CommonRes getEmbeddedDetails(EmbeddedReq req);

	CommonRes getSearchType();

	CommonRes getProductDashBoard(EmbeddedDashBoardReq req);

	CommonRes getProductPlanTypeDashBoard(EmbeddedDashBoardReq req);

}
