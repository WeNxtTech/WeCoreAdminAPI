package com.maan.eway.uploaddoc.mapper;

import org.springframework.stereotype.Component;

import com.maan.eway.uploaddoc.dto.response.UploadDocAgricultureMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocAgricultureMaster;

@Component
public class UploadDocAgricultureMasterMapper {

	public UploadDocAgricultureMasterRes toRes(UploadDocAgricultureMaster entity) {
		if (entity == null) {
			return null;
		}
		UploadDocAgricultureMasterRes res = new UploadDocAgricultureMasterRes();
		res.setSno(entity.getSno());
		res.setProvinceId(entity.getProvinceId());
		res.setProvinceDesc(entity.getProvinceDesc());
		res.setDistrictId(entity.getDistrictId());
		res.setDistrictDesc(entity.getDistrictDesc());
		res.setAez(entity.getAez());
		res.setCropId(entity.getCropId());
		res.setCropDesc(entity.getCropDesc());
		res.setYieldPercentage(entity.getYieldPercentage());
		res.setPerHaCost(entity.getPerHaCost());
		res.setCompanyId(entity.getCompanyId());
		res.setProductId(entity.getProductId());
		res.setSectionId(entity.getSectionId());
		res.setAmendId(entity.getAmendId());
		res.setCoreAppCode(entity.getCoreAppCode());
		res.setStatus(entity.getStatus());
		res.setEntryDate(entity.getEntryDate());
		res.setEffectiveDateStart(entity.getEffectiveDateStart());
		res.setEffectiveDateEnd(entity.getEffectiveDateEnd());
		res.setRemarks(entity.getRemarks());
		return res;
	}
}
