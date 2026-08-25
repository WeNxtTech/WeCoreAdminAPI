package com.maan.eway.uploaddoc.mapper;

import org.springframework.stereotype.Component;

import com.maan.eway.uploaddoc.dto.response.UploadDocCityMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocCityMaster;

@Component
public class UploadDocCityMasterMapper {

	public UploadDocCityMasterRes toRes(UploadDocCityMaster entity) {
		if (entity == null) {
			return null;
		}
		UploadDocCityMasterRes res = new UploadDocCityMasterRes();
		res.setCityId(entity.getCityId());
		res.setCountryId(entity.getCountryId());
		res.setStateId(entity.getStateId());
		res.setAmendId(entity.getAmendId());
		res.setEffectiveDateEnd(entity.getEffectiveDateEnd());
		res.setEffectiveDateStart(entity.getEffectiveDateStart());
		res.setCityName(entity.getCityName());
		res.setStatus(entity.getStatus());
		res.setRemarks(entity.getRemarks());
		res.setEntryDate(entity.getEntryDate());
		res.setCoreAppCode(entity.getCoreAppCode());
		res.setTiraCode(entity.getTiraCode());
		res.setCreatedBy(entity.getCreatedBy());
		res.setRegulatoryCode(entity.getRegulatoryCode());
		res.setUpdatedBy(entity.getUpdatedBy());
		res.setUpdatedDate(entity.getUpdatedDate());
		res.setCityNameLocal(entity.getCityNameLocal());
		return res;
	}
}
