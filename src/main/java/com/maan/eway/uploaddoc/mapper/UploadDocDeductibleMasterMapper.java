package com.maan.eway.uploaddoc.mapper;

import org.springframework.stereotype.Component;

import com.maan.eway.uploaddoc.dto.response.UploadDocDeductibleMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocDeductibleMaster;

@Component
public class UploadDocDeductibleMasterMapper {

	public UploadDocDeductibleMasterRes toRes(UploadDocDeductibleMaster entity) {
		if (entity == null) {
			return null;
		}
		UploadDocDeductibleMasterRes res = new UploadDocDeductibleMasterRes();
		
		res.setDeductId(entity.getDeductId());
		res.setDeductStart(entity.getDeductStart());
		res.setDeductEnd(entity.getDeductEnd());
		res.setRate(entity.getRate());
		res.setCalcType(entity.getCalcType());
		res.setAmendId(entity.getAmendId());
		res.setStatus(entity.getStatus());
		res.setEntryDate(entity.getEntryDate());
		res.setEffectiveDateStart(entity.getEffectiveDateStart());
		res.setEffectiveDateEnd(entity.getEffectiveDateEnd());
		res.setRemarks(entity.getRemarks());
		res.setBranchCode(entity.getBranchCode());
		res.setCompanyId(entity.getCompanyId());
		res.setProductId(entity.getProductId());
		res.setSectionId(entity.getSectionId());
		return res;
	}
}
