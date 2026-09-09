package com.maan.eway.uploaddoc.mapper;

import org.springframework.stereotype.Component;

import com.maan.eway.uploaddoc.dto.response.UploadDocAcExecutiveMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocAcExecutiveMaster;

@Component
public class UploadDocAcExecutiveMasterMapper {

	public UploadDocAcExecutiveMasterRes toRes(UploadDocAcExecutiveMaster entity) {
		if (entity == null) {
			return null;
		}
		UploadDocAcExecutiveMasterRes res = new UploadDocAcExecutiveMasterRes();
		res.setAcExecutiveId(entity.getAcExecutiveId());
		res.setAcExecutiveName(entity.getAcExecutiveName());
		res.setOaCode(entity.getOaCode());
		res.setBranchCode(entity.getBranchCode());
		res.setCompanyId(entity.getCompanyId());
		res.setCommissionPercent(entity.getCommissionPercent());
		res.setStatus(entity.getStatus());
		res.setEffectiveDateStart(entity.getEffectiveDateStart());
		res.setEffectiveDateEnd(entity.getEffectiveDateEnd());
		res.setAmendId(entity.getAmendId());
		res.setEntryDate(entity.getEntryDate());
		res.setBankCode(entity.getBankCode());
		res.setCoreAppCode(entity.getCoreAppCode());
		return res;
	}
}
