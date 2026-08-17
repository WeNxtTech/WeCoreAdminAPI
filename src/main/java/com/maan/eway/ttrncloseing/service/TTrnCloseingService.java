package com.maan.eway.ttrncloseing.service;

import com.maan.eway.common.res.CommonRes;
import com.maan.eway.ttrncloseing.dto.HomePositionReq;
import com.maan.eway.ttrncloseing.dto.TtrnGetReq;
import com.maan.eway.ttrncloseing.dto.TtrnReq;

public interface TTrnCloseingService {

	CommonRes insertTTRNDetails(TtrnReq req);

	CommonRes updateHPM(HomePositionReq req);

	CommonRes getTTrnList(TtrnGetReq req);

	CommonRes getTTrnAllYear(HomePositionReq req);

	CommonRes getTTrnTran(HomePositionReq req);

}
