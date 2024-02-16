package com.maan.eway.admin.service;

import java.util.List;

import com.maan.eway.common.req.GetTableDropDownReq;
import com.maan.eway.common.req.LovDropDownReq;
import com.maan.eway.req.SubUserTypeReq;
import com.maan.eway.res.ColummnDropRes;
import com.maan.eway.res.DropDownRes;
import com.maan.eway.res.SubUserTypeDropDownRes;

public interface AdminDropDownService {

	List<DropDownRes> getUserType(LovDropDownReq req);

	List<SubUserTypeDropDownRes> getSubUserType(SubUserTypeReq req);

	List<DropDownRes> getProductIcons(LovDropDownReq req);

	List<DropDownRes> getCalcTypes(LovDropDownReq req);

	List<DropDownRes> getCoverageTypes(LovDropDownReq req);

	List<DropDownRes> getRangeParams(LovDropDownReq req);

	List<DropDownRes> getDiscreteParams(LovDropDownReq req);

	
	List<DropDownRes> getProductCategory(LovDropDownReq req);

	List<ColummnDropRes> getTableDetails(GetTableDropDownReq req);

	List<DropDownRes> getDocType(LovDropDownReq req);

	List<DropDownRes> getReferralType(LovDropDownReq req);

	List<DropDownRes> getQuestionType(LovDropDownReq req);

	List<DropDownRes> getSequenceType(LovDropDownReq req);

	List<DropDownRes> getEndtShortCodes(LovDropDownReq req);


}
