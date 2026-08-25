package com.maan.eway.uploaddoc.mapper;

import org.springframework.stereotype.Component;

import com.maan.eway.uploaddoc.dto.response.UploadDocRegionMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocRegionMaster;

@Component
public class UploadDocRegionMasterMapper {

	public UploadDocRegionMasterRes toRes(UploadDocRegionMaster entity) {
		if (entity == null) {
			return null;
		}
		UploadDocRegionMasterRes res = new UploadDocRegionMasterRes();
		res.setRegionCode(entity.getRegionCode());
		res.setCountryId(entity.getCountryId());
		res.setAmendId(entity.getAmendId());
		res.setRegionShortCode(entity.getRegionShortCode());
		res.setRegionName(entity.getRegionName());
		res.setEntryDate(entity.getEntryDate());
		res.setStatus(entity.getStatus());
		res.setEffectiveDateStart(entity.getEffectiveDateStart());
		res.setEffectiveDateEnd(entity.getEffectiveDateEnd());
		res.setCoreAppCode(entity.getCoreAppCode());
		res.setRemarks(entity.getRemarks());
		res.setCreatedBy(entity.getCreatedBy());
		res.setTiraCode(entity.getTiraCode());
		res.setRegulatoryCode(entity.getRegulatoryCode());
		res.setUpdatedBy(entity.getUpdatedBy());
		res.setUpdatedDate(entity.getUpdatedDate());
		res.setRegionNameLocal(entity.getRegionNameLocal());
		return res;
	}
}
