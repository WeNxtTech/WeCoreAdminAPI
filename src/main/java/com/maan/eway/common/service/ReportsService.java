package com.maan.eway.common.service;

import java.util.List;
import java.util.Map;

import com.maan.eway.common.req.DataManipulationReq;
import com.maan.eway.common.req.DeleteTiraSearchedVehicleReq;
import com.maan.eway.common.req.GetAllTirraErrorHistory;
import com.maan.eway.common.req.GetTirraEorrorHistoryReq;
import com.maan.eway.common.req.PolicyRevertReq;
import com.maan.eway.common.req.TiraGetReq;
import com.maan.eway.common.req.TiraPushedDetailsReq;
import com.maan.eway.common.req.TransactionCheckStatusReq;
import com.maan.eway.common.res.GetTirraEorrorHistoryRes;
import com.maan.eway.common.res.TiraErrorHistoryTotalRes;
import com.maan.eway.common.res.TiraPushedDetailsRes;
import com.maan.eway.common.res.TiraPushedListDetailsRes;
import com.maan.eway.common.res.TransactionCheckStatusRes;
import com.maan.eway.error.Error;
import com.maan.eway.res.LogDetailsRes;
import com.maan.eway.res.SuccessRes;
import com.maan.eway.res.SuccessRes2;

public interface ReportsService {

	TiraErrorHistoryTotalRes getTirraEorrorHistory(GetTirraEorrorHistoryReq req);

	TiraErrorHistoryTotalRes getAllTirraErrorHistory(GetAllTirraErrorHistory req);

	TiraPushedListDetailsRes getallTiraIntegrationPushedDetails(TiraPushedDetailsReq req);

	List<TiraPushedDetailsRes> getTiraIntegrationPushedDetails(TiraGetReq req);

	SuccessRes2 deleteTiraSearchVehicle(DeleteTiraSearchedVehicleReq req);

	TransactionCheckStatusRes getTransactionCheckStatusDetails(TransactionCheckStatusReq req);

	SuccessRes2 policyRevertToQuote(PolicyRevertReq req);

	List<Map<String, Object>> dataManipulation(DataManipulationReq req);

	List<Error> validatedataManipulation(DataManipulationReq req);

	List<LogDetailsRes> LogDetails(String filePath);

}
