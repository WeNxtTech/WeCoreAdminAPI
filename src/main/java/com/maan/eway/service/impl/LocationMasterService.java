package com.maan.eway.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.batch.req.LocationMasterReq;
import com.maan.eway.bean.LocationMaster;
import com.maan.eway.common.res.LocationMasterRes;
import com.maan.eway.repository.LocationMasterRepository;

@Service
public class LocationMasterService {

	@Autowired
	private LocationMasterRepository repository;

	// 1. Initial Insert (amendId = 0)
	@Transactional
	public LocationMasterRes saveOrUpdateLocation(LocationMasterReq req) {

		// 1. Check if an active record already exists for Company & CoreAppCode
		List<LocationMaster> existList = repository.findByCompanyIdAndStatus(req.getCompanyId(), "Y");

		LocationMaster newRecord = new LocationMaster();

		if (!existList.isEmpty()) {
			LocationMaster activeRecord = existList.get(0);

			activeRecord.setStatus("N");
			repository.save(activeRecord);

			Integer maxAmendId = repository.findMaxAmendId(req.getCompanyId(), req.getCoreAppCode()).orElse(0);
			newRecord.setAmendId(maxAmendId + 1);

		} else {
			// --- INSERT LOGIC ---
			newRecord.setAmendId(0);
		}

		// Map request fields to new entity instance
		newRecord.setCompanyId(req.getCompanyId());
		newRecord.setBranchId(req.getBranchId() != null ? req.getBranchId() : "99999");
		newRecord.setCountry(req.getCountry());
		newRecord.setRegionState(req.getRegionState());
		newRecord.setCityLocationName(req.getCityLocationName());
		newRecord.setAddress(req.getAddress());
		newRecord.setCoreAppCode(req.getCoreAppCode());
		newRecord.setRegulatoryCode(req.getRegulatoryCode());
		newRecord.setEffectiveDate(req.getEffectiveDate());
		newRecord.setRemarks(req.getRemarks());
		newRecord.setStatus(req.getStatus() != null ? req.getStatus() : "Y");

		LocationMaster saved = repository.save(newRecord);

		// Convert and return response
		return mapToResponse(saved);
	}

	private LocationMasterRes mapToResponse(LocationMaster entity) {
		LocationMasterRes res = new LocationMasterRes();
		res.setId(entity.getId());
		res.setCompanyId(entity.getCompanyId());
		res.setBranchId(entity.getBranchId());
		res.setAmendId(entity.getAmendId());
		res.setCountry(entity.getCountry());
		res.setRegionState(entity.getRegionState());
		res.setCityLocationName(entity.getCityLocationName());
		res.setAddress(entity.getAddress());
		res.setCoreAppCode(entity.getCoreAppCode());
		res.setRegulatoryCode(entity.getRegulatoryCode());
		res.setEffectiveDate(entity.getEffectiveDate());
		res.setRemarks(entity.getRemarks());
		res.setStatus(entity.getStatus());
		return res;
	}

	// 3. Fetch All Active Records by Company ID
	public List<LocationMaster> getByCompanyId(String companyId) {
		return repository.findByCompanyIdAndStatus(companyId, "Y");
	}

	// 4. Fetch Active Records by Company ID and Branch ID (including '99999' or
	// specific)
	public List<LocationMaster> getByCompanyAndBranchId(String companyId, String branchId) {
		return repository.findByCompanyIdAndBranchWithGlobal(companyId, branchId);
	}
}