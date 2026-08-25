package com.maan.eway.uploaddoc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.error.Error;
import com.maan.eway.uploaddoc.dto.request.UploadDocRegionMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocRegionMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocRegionMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocRegionMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocRegionMaster;
import com.maan.eway.uploaddoc.mapper.UploadDocRegionMasterMapper;
import com.maan.eway.uploaddoc.repository.UploadDocRegionMasterRepository;
import com.maan.eway.uploaddoc.service.UploadDocRegionMasterService;

/**
 * <h2>UploadDocRegionMasterServiceImpl</h2>
 * Amend-Id / versioning approach: see {@link com.maan.eway.uploaddoc.service.impl.UploadDocStateMasterServiceImpl}.
 * Business key here: (regionCode, countryId).
 */
@Service
@Transactional
public class UploadDocRegionMasterServiceImpl implements UploadDocRegionMasterService {

	private static final Logger log = LogManager.getLogger(UploadDocRegionMasterServiceImpl.class);

	@Autowired
	private UploadDocRegionMasterRepository repo;

	@Autowired
	private UploadDocRegionMasterMapper mapper;

	@Override
	public List<Error> validateSave(UploadDocRegionMasterSaveReq req) {
		List<Error> errors = new ArrayList<>();
		if (StringUtils.isBlank(req.getRegionCode())) {
			errors.add(new Error("01", "regionCode", "Please provide RegionCode"));
		} else if (req.getRegionCode().length() > 20) {
			errors.add(new Error("01", "regionCode", "RegionCode must not exceed 20 characters"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			errors.add(new Error("02", "countryId", "Please provide CountryId"));
		} else if (req.getCountryId().length() > 20) {
			errors.add(new Error("02", "countryId", "CountryId must not exceed 20 characters"));
		}
		if (req.getEffectiveDateStart() == null) {
			errors.add(new Error("03", "effectiveDateStart", "Please provide EffectiveDateStart"));
		}
		if (req.getEffectiveDateEnd() == null) {
			errors.add(new Error("04", "effectiveDateEnd", "Please provide EffectiveDateEnd"));
		}
		if (StringUtils.isBlank(req.getCreatedBy())) {
			errors.add(new Error("05", "createdBy", "Please provide CreatedBy"));
		} else if (req.getCreatedBy().length() > 50) {
			errors.add(new Error("05", "createdBy", "CreatedBy must not exceed 50 characters"));
		}
		if (StringUtils.isBlank(req.getRegulatoryCode())) {
			errors.add(new Error("06", "regulatoryCode", "Please provide RegulatoryCode"));
		} else if (req.getRegulatoryCode().length() > 20) {
			errors.add(new Error("06", "regulatoryCode", "RegulatoryCode must not exceed 20 characters"));
		}
		if (StringUtils.isNotBlank(req.getRegionCode()) && StringUtils.isNotBlank(req.getCountryId())
				&& repo.findMaxAmendId(req.getRegionCode(), req.getCountryId()).isPresent()) {
			errors.add(new Error("07", "regionCode",
					"A Region Master record already exists for this business key. Use the update API to amend it."));
		}
		return errors;
	}

	@Override
	public UploadDocRegionMasterRes save(UploadDocRegionMasterSaveReq req) {
		Date now = new Date();
		UploadDocRegionMaster entity = UploadDocRegionMaster.builder()
				.regionCode(req.getRegionCode())
				.countryId(req.getCountryId())
				.amendId(0)
				.regionShortCode(req.getRegionShortCode())
				.regionName(req.getRegionName())
				.entryDate(now)
				.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())
				.effectiveDateStart(req.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd())
				.coreAppCode(req.getCoreAppCode())
				.remarks(req.getRemarks())
				.createdBy(req.getCreatedBy())
				.tiraCode(req.getTiraCode())
				.regulatoryCode(req.getRegulatoryCode())
				.updatedBy(req.getCreatedBy())
				.updatedDate(now)
				.regionNameLocal(req.getRegionNameLocal())
				.build();
		entity = repo.saveAndFlush(entity);
		log.info("UploadDocRegionMaster created: {}", entity);
		return mapper.toRes(entity);
	}

	@Override
	public List<Error> validateUpdate(UploadDocRegionMasterUpdateReq req) {
		List<Error> errors = new ArrayList<>();
		if (StringUtils.isBlank(req.getRegionCode())) {
			errors.add(new Error("01", "regionCode", "Please provide RegionCode"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			errors.add(new Error("02", "countryId", "Please provide CountryId"));
		}
		if (StringUtils.isBlank(req.getUpdatedBy())) {
			errors.add(new Error("03", "updatedBy", "Please provide UpdatedBy"));
		}
		if (!errors.isEmpty()) {
			return errors;
		}
		if (repo.findMaxAmendId(req.getRegionCode(), req.getCountryId()).isEmpty()) {
			errors.add(new Error("04", "regionCode",
					"No existing Region Master record found for this business key. Use the save API to create it."));
		}
		return errors;
	}

	@Override
	public UploadDocRegionMasterRes update(UploadDocRegionMasterUpdateReq req) {
		int newAmendId = repo.findMaxAmendId(req.getRegionCode(), req.getCountryId()).orElse(-1) + 1;
		UploadDocRegionMaster previous = repo
				.findByRegionCodeAndCountryIdAndAmendId(req.getRegionCode(), req.getCountryId(), newAmendId - 1)
				.orElse(null);

		Date now = new Date();
		UploadDocRegionMaster entity = UploadDocRegionMaster.builder()
				.regionCode(req.getRegionCode())
				.countryId(req.getCountryId())
				.amendId(newAmendId)
				.regionShortCode(req.getRegionShortCode() != null ? req.getRegionShortCode() : previous.getRegionShortCode())
				.regionName(req.getRegionName() != null ? req.getRegionName() : previous.getRegionName())
				.entryDate(previous.getEntryDate())
				.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())
				.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart() : previous.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd() : previous.getEffectiveDateEnd())
				.coreAppCode(req.getCoreAppCode() != null ? req.getCoreAppCode() : previous.getCoreAppCode())
				.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())
				.createdBy(previous.getCreatedBy())
				.tiraCode(req.getTiraCode() != null ? req.getTiraCode() : previous.getTiraCode())
				.regulatoryCode(req.getRegulatoryCode() != null ? req.getRegulatoryCode() : previous.getRegulatoryCode())
				.updatedBy(req.getUpdatedBy())
				.updatedDate(now)
				.regionNameLocal(req.getRegionNameLocal() != null ? req.getRegionNameLocal() : previous.getRegionNameLocal())
				.build();

		entity = repo.saveAndFlush(entity);
		log.info("UploadDocRegionMaster amended to AMEND_ID={}: {}", newAmendId, entity);
		return mapper.toRes(entity);
	}

	@Override
	public UploadDocRegionMasterRes getLatest(UploadDocRegionMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getRegionCode(), req.getCountryId());
		if (maxAmendId.isEmpty()) {
			return null;
		}
		return repo.findByRegionCodeAndCountryIdAndAmendId(req.getRegionCode(), req.getCountryId(), maxAmendId.get())
				.map(mapper::toRes).orElse(null);
	}

	@Override
	public List<UploadDocRegionMasterRes> getAll() {
		return repo.findAllLatest().stream().map(mapper::toRes).collect(Collectors.toList());
	}

	@Override
	public boolean delete(UploadDocRegionMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getRegionCode(), req.getCountryId());
		if (maxAmendId.isEmpty()) {
			return false;
		}
		Optional<UploadDocRegionMaster> latest = repo.findByRegionCodeAndCountryIdAndAmendId(req.getRegionCode(),
				req.getCountryId(), maxAmendId.get());
		latest.ifPresent(repo::delete);
		return latest.isPresent();
	}
}
