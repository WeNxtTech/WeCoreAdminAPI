package com.maan.eway.uploaddoc.mapper;

import org.springframework.stereotype.Component;

import com.maan.eway.uploaddoc.dto.response.UploadDocStateMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocStateMaster;

@Component
public class UploadDocStateMasterMapper {

	public UploadDocStateMasterRes toRes(UploadDocStateMaster entity) {
		if (entity == null) {
			return null;
		}
		UploadDocStateMasterRes res = new UploadDocStateMasterRes();
		res.setStateId(entity.getStateId());
		res.setStateName(entity.getStateName());
		res.setStateShortCode(entity.getStateShortCode());
		res.setCountryId(entity.getCountryId());
		res.setRegionCode(entity.getRegionCode());
		res.setAmendId(entity.getAmendId());
		res.setStatus(entity.getStatus());
		res.setEntryDate(entity.getEntryDate());
		res.setEffectiveDateStart(entity.getEffectiveDateStart());
		res.setEffectiveDateEnd(entity.getEffectiveDateEnd());
		res.setCoreAppCode(entity.getCoreAppCode());
		res.setTiraCode(entity.getTiraCode());
		res.setCreatedBy(entity.getCreatedBy());
		res.setRemarks(entity.getRemarks());
		res.setRegulatoryCode(entity.getRegulatoryCode());
		res.setUpdatedDate(entity.getUpdatedDate());
		res.setUpdatedBy(entity.getUpdatedBy());
		res.setCity(entity.getCity());
		res.setSuburb(entity.getSuburb());
		res.setAreaGroup(entity.getAreaGroup());
		res.setCityId(entity.getCityId());
		res.setSuburbId(entity.getSuburbId());
		res.setSuburbLocal(entity.getSuburbLocal());
		res.setStateNameLocal(entity.getStateNameLocal());
		res.setCityLocal(entity.getCityLocal());
		return res;
	}
}
