package com.maan.eway.common.service;

import java.util.List;

import com.maan.eway.common.req.GetAllTirraErrorHistory;
import com.maan.eway.common.req.GetTirraEorrorHistoryReq;
import com.maan.eway.common.res.GetTirraEorrorHistoryRes;

public interface ReportsService {

	List<GetTirraEorrorHistoryRes> getTirraEorrorHistory(GetTirraEorrorHistoryReq req);

	List<GetTirraEorrorHistoryRes> getAllTirraErrorHistory(GetAllTirraErrorHistory req);

}
