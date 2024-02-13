package com.maan.eway.chart;

import java.util.List;

import com.maan.eway.res.CommonRes;

public interface ChartParentMasterService  {

	CommonRes parentSave(ChartParentRequest req);

	CommonRes getAllChartByCompanyId(Integer companyId);

	CommonRes editChartDetails(EditChartDetailsReq req);

	CommonRes accountType();

	CommonRes characterstic();

	CommonRes childChartInsert(List<ChartChildRequest> req);

	CommonRes getAllChildChartInsert(GetAllChildChartRequest req);

	CommonRes editChildChart(EditChartChildRequest req);

	CommonRes policyCredittDebitEntry(String quoteNo);

	CommonRes childChartInsert1(ChildChartInfoReq req);

}
