package com.maan.eway.ttrncloseing.service;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.ttrncloseing.dto.HomePositionReq;
import com.maan.eway.ttrncloseing.dto.TtrnReq;

public interface TTrnCloseingService {

	CommonRes insertTTRNDetails(TtrnReq req);

	CommonRes updateHPM(HomePositionReq req);

}
