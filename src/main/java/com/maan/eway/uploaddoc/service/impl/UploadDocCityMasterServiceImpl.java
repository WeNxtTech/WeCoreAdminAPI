package com.maan.eway.uploaddoc.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.maan.eway.uploaddoc.dto.request.UploadDocCityMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocCityMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocCityMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocCityMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocCityMaster;
import com.maan.eway.uploaddoc.mapper.UploadDocCityMasterMapper;
import com.maan.eway.uploaddoc.repository.UploadDocCityMasterRepository;
import com.maan.eway.uploaddoc.service.UploadDocCityMasterService;

/**
 * <h2>UploadDocCityMasterServiceImpl</h2>
 * Amend-Id / versioning approach: see {@link com.maan.eway.uploaddoc.service.impl.UploadDocStateMasterServiceImpl}
 * for the full explanation. The business key here is (cityId, countryId, stateId).
 */
@Service
@Transactional
public class UploadDocCityMasterServiceImpl implements UploadDocCityMasterService {

	private static final Logger log = LogManager.getLogger(UploadDocCityMasterServiceImpl.class);

	@Autowired
	private UploadDocCityMasterRepository repo;

	@Autowired
	private UploadDocCityMasterMapper mapper;

	@Override
	public List<Error> validateSave(UploadDocCityMasterSaveReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getCityId() == null) {
			errors.add(new Error("01", "cityId", "Please provide CityId"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			errors.add(new Error("02", "countryId", "Please provide CountryId"));
		}
		if (StringUtils.isBlank(req.getStateId())) {
			errors.add(new Error("03", "stateId", "Please provide StateId"));
		}
		if (req.getEffectiveDateStart() == null) {
			errors.add(new Error("04", "effectiveDateStart", "Please provide EffectiveDateStart"));
		}
//		if (req.getEffectiveDateEnd() == null) {
//			errors.add(new Error("05", "effectiveDateEnd", "Please provide EffectiveDateEnd"));
//		}
//		if (StringUtils.isBlank(req.getCreatedBy())) {
//			errors.add(new Error("06", "createdBy", "Please provide CreatedBy"));
//		} else if (req.getCreatedBy().length() > 100) {
//			errors.add(new Error("06", "createdBy", "CreatedBy must not exceed 100 characters"));
//		}
		if (StringUtils.isBlank(req.getRegulatoryCode())) {
			errors.add(new Error("07", "regulatoryCode", "Please provide RegulatoryCode"));
		}
		if (req.getCityId() != null && StringUtils.isNotBlank(req.getCountryId())
				&& StringUtils.isNotBlank(req.getStateId() ) && StringUtils.isNotBlank(req.getCityName() )
				&& repo.findMaxAmendId(req.getCityId(), req.getCountryId(), req.getCityName()).isPresent()) {
			errors.add(new Error("08", "cityId",
					"A City Master record already exists for this business key. Use the update API to amend it."));
		}
		return errors;
	}

	@Override
	public UploadDocCityMasterRes save(UploadDocCityMasterSaveReq req) {
		Date now = new Date();
		UploadDocCityMaster entity = UploadDocCityMaster.builder()
				.cityId(req.getCityId())
				.countryId(req.getCountryId())
				.stateId(req.getStateId())
				.amendId(0)
				.effectiveDateEnd(req.getEffectiveDateEnd())
				.effectiveDateStart(req.getEffectiveDateStart())
				.cityName(req.getCityName())
				.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())
				.remarks(req.getRemarks())
				.coreAppCode(req.getCoreAppCode())
				.tiraCode(req.getTiraCode())
				.createdBy(req.getCreatedBy())
				.regulatoryCode(req.getRegulatoryCode())
				.cityNameLocal(req.getCityNameLocal())
				.entryDate(now)
				.updatedBy(req.getCreatedBy())
				.updatedDate(now)
				.build();
		entity = repo.saveAndFlush(entity);
		log.info("UploadDocCityMaster created: {}", entity);
		return mapper.toRes(entity);
	}

	@Override
	public List<Error> validateUpdate(UploadDocCityMasterUpdateReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getCityId() == null) {
			errors.add(new Error("01", "cityId", "Please provide CityId"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			errors.add(new Error("02", "countryId", "Please provide CountryId"));
		}
		if (StringUtils.isBlank(req.getStateId())) {
			errors.add(new Error("03", "stateId", "Please provide StateId"));
		}
		if (StringUtils.isBlank(req.getUpdatedBy())) {
			errors.add(new Error("04", "updatedBy", "Please provide UpdatedBy"));
		}
		if (!errors.isEmpty()) {
			return errors;
		}
		if (repo.findMaxAmendId(req.getCityId(), req.getCountryId(), req.getStateId()).isEmpty()) {
			errors.add(new Error("05", "cityId",
					"No existing City Master record found for this business key. Use the save API to create it."));
		}
		return errors;
	}

		@Override
		public UploadDocCityMasterRes update(UploadDocCityMasterUpdateReq req) {
			int newAmendId = repo.findMaxAmendId(req.getCityId(), req.getCountryId(), req.getStateId()).orElse(-1) + 1;
			UploadDocCityMaster previous = repo
			        .findByCityIdAndCountryIdAndStateIdAndAmendId(
			                req.getCityId(), req.getCountryId(), req.getStateId(), newAmendId - 1)
			        .flatMap(list -> list.stream().findFirst())
			        .orElse(null);
	
			Date now = new Date();
			UploadDocCityMaster entity = UploadDocCityMaster.builder()
					.cityId(req.getCityId())
					.countryId(req.getCountryId())
					.stateId(req.getStateId())
					.amendId(newAmendId)
					.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd() : previous.getEffectiveDateEnd())
					.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart() : previous.getEffectiveDateStart())
					.cityName(req.getCityName() != null ? req.getCityName() : previous.getCityName())
					.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())
					.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())
					.coreAppCode(req.getCoreAppCode() != null ? req.getCoreAppCode() : previous.getCoreAppCode())
					.tiraCode(req.getTiraCode() != null ? req.getTiraCode() : previous.getTiraCode())
					.createdBy(previous.getCreatedBy())
					.regulatoryCode(req.getRegulatoryCode() != null ? req.getRegulatoryCode() : previous.getRegulatoryCode())
					.cityNameLocal(req.getCityNameLocal() != null ? req.getCityNameLocal() : previous.getCityNameLocal())
					.entryDate(previous.getEntryDate())
					.updatedBy(req.getUpdatedBy())
					.updatedDate(now)
					.build();
	
			entity = repo.saveAndFlush(entity);
			log.info("UploadDocCityMaster amended to AMEND_ID={}: {}", newAmendId, entity);
			return mapper.toRes(entity);
		}

	@Override
	public List<UploadDocCityMasterRes> getLatest(UploadDocCityMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getCityId(), req.getCountryId(), req.getStateId());
		if (maxAmendId.isEmpty()) {
			return null;
		}
		
		return repo.findByCityIdAndCountryIdAndStateIdAndAmendId(
	               req.getCityId(), req.getCountryId(), req.getStateId(), maxAmendId.get())
	           .map(list -> list.stream().map(mapper::toRes).collect(Collectors.toList()))
	           .orElse(null);
	}

	@Override
	public List<UploadDocCityMasterRes> getAll(UploadDocCityMasterGetReq req) {
	    List<UploadDocCityMaster> list = repo.findLatestAmendmentPerCity(
	            req.getCountryId(), req.getStateId());

	    if (list == null || list.isEmpty()) {
	        return null;
	    }
	    return list.stream()
	               .map(mapper::toRes)
	               .collect(Collectors.toList());
	}
	@Override
	public boolean delete(UploadDocCityMasterGetReq req) {
	    Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getCityId(), req.getCountryId(), req.getStateId());
	    if (maxAmendId.isEmpty()) {
	        return false;
	    }
	    Optional<List<UploadDocCityMaster>> latest = repo.findByCityIdAndCountryIdAndStateIdAndAmendId(
	            req.getCityId(), req.getCountryId(), req.getStateId(), maxAmendId.get());

	    if (latest.isEmpty() || latest.get().isEmpty()) {
	        return false;
	    }
	    repo.deleteAll(latest.get());
	    return true;
	}
	private Date calculateEffectiveDateEnd(Date effectiveDateStart) {
	    if (effectiveDateStart == null) {
	        return null;
	    }
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(effectiveDateStart);
	    cal.add(Calendar.YEAR, 25);
	    cal.add(Calendar.DATE, -1); // so the 25-year window ends the day before the next cycle starts
	    return cal.getTime();
	}

	@Override
	public UploadDocCityMasterRes saveOrUpdate(UploadDocCityMasterSaveReq req) {

		Date now = new Date();

		// Get latest amend ID
		int newAmendId = repo.findMaxAmendId(req.getCityId(), req.getCountryId(), req.getStateId()).orElse(-1) + 1;

		// Get previous/latest record
		UploadDocCityMaster previous = null;

		if (newAmendId > 0) {
			previous = repo.findByCityIdAndCountryIdAndStateIdAndAmendId(req.getCityId(), req.getCountryId(),
					req.getStateId(), newAmendId - 1).flatMap(list -> list.stream().findFirst()).orElse(null);
		}

		UploadDocCityMaster entity;

		if (previous == null) {

			// =========================
			// NEW RECORD
			// =========================
			Date resolvedEffectiveDateEnd = req.getEffectiveDateEnd() != null
		            ? req.getEffectiveDateEnd()
		            : calculateEffectiveDateEnd(req.getEffectiveDateStart());
			entity = UploadDocCityMaster.builder().cityId(req.getCityId()).countryId(req.getCountryId())
					.stateId(req.getStateId()).amendId(0)
					.effectiveDateEnd(resolvedEffectiveDateEnd)
		            .effectiveDateStart(req.getEffectiveDateStart())
					.cityName(req.getCityName())
					.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())
					.remarks(req.getRemarks()).coreAppCode(req.getCoreAppCode()).tiraCode(req.getTiraCode())
					.createdBy(req.getCreatedBy()).regulatoryCode(req.getRegulatoryCode())
					.cityNameLocal(req.getCityNameLocal())
					.entryDate(now).updatedBy(req.getCreatedBy()).updatedDate(now)
					.build();

		} else {

			// =========================
			// AMEND / UPDATE
			// =========================
			entity = UploadDocCityMaster.builder().cityId(req.getCityId()).countryId(req.getCountryId())
					.stateId(req.getStateId()).amendId(newAmendId)

					.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd()
							: previous.getEffectiveDateEnd())

					.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart()
							: previous.getEffectiveDateStart())

					.cityName(req.getCityName() != null ? req.getCityName() : previous.getCityName())

					.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())

					.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())

					.coreAppCode(req.getCoreAppCode() != null ? req.getCoreAppCode() : previous.getCoreAppCode())

					.tiraCode(req.getTiraCode() != null ? req.getTiraCode() : previous.getTiraCode())

					.createdBy(previous.getCreatedBy())

					.regulatoryCode(
							req.getRegulatoryCode() != null ? req.getRegulatoryCode() : previous.getRegulatoryCode())

					.cityNameLocal(
							req.getCityNameLocal() != null ? req.getCityNameLocal() : previous.getCityNameLocal())

					.entryDate(previous.getEntryDate())

					.updatedDate(now)

					.build();
		}

		entity = repo.saveAndFlush(entity);

		log.info("UploadDocCityMaster saved/amended with AMEND_ID={}: {}", entity.getAmendId(), entity);

		return mapper.toRes(entity);
	}
}
