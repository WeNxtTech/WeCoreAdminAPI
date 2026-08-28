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
import com.maan.eway.req.StateMasterRequest;
import com.maan.eway.uploaddoc.dto.request.UploadDocStateMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocStateMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocStateMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocStateMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocStateMaster;
import com.maan.eway.uploaddoc.mapper.UploadDocStateMasterMapper;
import com.maan.eway.uploaddoc.repository.UploadDocStateMasterRepository;
import com.maan.eway.uploaddoc.service.UploadDocStateMasterService;

/**
 * <h2>UploadDocStateMasterServiceImpl</h2>
 *
 * Amend-Id / versioning approach:
 * <ul>
 * <li>The business key (everything in the composite PK except AMEND_ID)
 * identifies one logical "record family".</li>
 * <li>CREATE always starts a new family at AMEND_ID = 0. If a row already
 * exists for that business key, creation is rejected as a duplicate.</li>
 * <li>UPDATE never mutates an existing row. It looks up
 * {@code MAX(AMEND_ID)} for the business key inside the same transaction
 * (repo.findMaxAmendId), computes {@code newAmendId = max + 1} and inserts a
 * brand new row carrying that AMEND_ID, preserving full amendment
 * history.</li>
 * <li>GET / LIST always resolve the row whose AMEND_ID equals
 * {@code MAX(AMEND_ID)} for its business key (see
 * {@link UploadDocStateMasterRepository#findAllLatest()}), so callers only
 * ever see the latest version.</li>
 * <li>Concurrency: the max-then-insert sequence runs inside the
 * {@code @Transactional} boundary of this service method. Two concurrent
 * amendments of the same business key can still race between the MAX read
 * and the INSERT; the composite primary key (business key + AMEND_ID)
 * guarantees that a race produces a duplicate-key failure instead of a
 * silently lost amendment, so the client can safely retry.</li>
 * </ul>
 */
@Service
@Transactional
public class UploadDocStateMasterServiceImpl implements UploadDocStateMasterService {

	private static final Logger log = LogManager.getLogger(UploadDocStateMasterServiceImpl.class);

	@Autowired
	private UploadDocStateMasterRepository repo;

	@Autowired
	private UploadDocStateMasterMapper mapper;

	@Override
	public List<Error> validateSave(UploadDocStateMasterSaveReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getStateId() == null) {
			errors.add(new Error("01", "stateId", "Please provide StateId"));
		}
		if (StringUtils.isBlank(req.getStateShortCode())) {
			errors.add(new Error("02", "stateShortCode", "Please provide StateShortCode"));
		} else if (req.getStateShortCode().length() > 20) {
			errors.add(new Error("02", "stateShortCode", "StateShortCode must not exceed 20 characters"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			errors.add(new Error("03", "countryId", "Please provide CountryId"));
		} else if (req.getCountryId().length() > 20) {
			errors.add(new Error("03", "countryId", "CountryId must not exceed 20 characters"));
		}
		if (StringUtils.isBlank(req.getRegionCode())) {
			errors.add(new Error("04", "regionCode", "Please provide RegionCode"));
		} else if (req.getRegionCode().length() > 20) {
			errors.add(new Error("04", "regionCode", "RegionCode must not exceed 20 characters"));
		}
		if (req.getCityId() == null) {
			errors.add(new Error("05", "cityId", "Please provide CityId"));
		}
		if (req.getSuburbId() == null) {
			errors.add(new Error("06", "suburbId", "Please provide SuburbId"));
		}
		if (StringUtils.isBlank(req.getCreatedBy())) {
			errors.add(new Error("07", "createdBy", "Please provide CreatedBy"));
		} else if (req.getCreatedBy().length() > 20) {
			errors.add(new Error("07", "createdBy", "CreatedBy must not exceed 20 characters"));
		}
		if (StringUtils.isBlank(req.getRegulatoryCode())) {
			errors.add(new Error("08", "regulatoryCode", "Please provide RegulatoryCode"));
		} else if (req.getRegulatoryCode().length() > 20) {
			errors.add(new Error("08", "regulatoryCode", "RegulatoryCode must not exceed 20 characters"));
		}
//		if (req.getStateId() != null && req.getCityId() != null && req.getSuburbId() != null
//				&& StringUtils.isNotBlank(req.getStateShortCode()) && StringUtils.isNotBlank(req.getCountryId())
//				&& StringUtils.isNotBlank(req.getRegionCode())) {
//			boolean exists = repo
//					.findMaxAmendId(req.getStateId(), req.getStateShortCode(), req.getCountryId(), req.getRegionCode(),
//							req.getCityId(), req.getSuburbId())
//					.isPresent();
//			if (exists) {
//				errors.add(new Error("09", "stateId",
//						"A State Master record already exists for this business key. Use the update API to amend it."));
//			}
//		}
		return errors;
	}

	@Override
	public UploadDocStateMasterRes save(UploadDocStateMasterSaveReq req) {
		Date now = new Date();
		UploadDocStateMaster entity = UploadDocStateMaster.builder()
				.stateId(req.getStateId())
				.stateShortCode(req.getStateShortCode())
				.countryId(req.getCountryId())
				.regionCode(req.getRegionCode())
				.cityId(req.getCityId())
				.suburbId(req.getSuburbId())
				.amendId(0)
				.stateName(req.getStateName())
				.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())
				.effectiveDateStart(req.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd())
				.coreAppCode(req.getCoreAppCode())
				.tiraCode(req.getTiraCode())
				.createdBy(req.getCreatedBy())
				.remarks(req.getRemarks())
				.regulatoryCode(req.getRegulatoryCode())
				.city(req.getCity())
				.suburb(req.getSuburb())
				.areaGroup(req.getAreaGroup())
				.suburbLocal(req.getSuburbLocal())
				.stateNameLocal(req.getStateNameLocal())
				.cityLocal(req.getCityLocal())
				.entryDate(now)
				.updatedBy(req.getCreatedBy())
				.updatedDate(now)
				.build();

		entity = repo.saveAndFlush(entity);
		log.info("UploadDocStateMaster created: {}", entity);
		return mapper.toRes(entity);
	}

	@Override
	public List<Error> validateUpdate(UploadDocStateMasterUpdateReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getStateId() == null) {
			errors.add(new Error("01", "stateId", "Please provide StateId"));
		}
		if (StringUtils.isBlank(req.getStateShortCode())) {
			errors.add(new Error("02", "stateShortCode", "Please provide StateShortCode"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			errors.add(new Error("03", "countryId", "Please provide CountryId"));
		}
		if (StringUtils.isBlank(req.getRegionCode())) {
			errors.add(new Error("04", "regionCode", "Please provide RegionCode"));
		}
		if (req.getCityId() == null) {
			errors.add(new Error("05", "cityId", "Please provide CityId"));
		}
		if (req.getSuburbId() == null) {
			errors.add(new Error("06", "suburbId", "Please provide SuburbId"));
		}
		if (StringUtils.isBlank(req.getUpdatedBy())) {
			errors.add(new Error("07", "updatedBy", "Please provide UpdatedBy"));
		}
		if (!errors.isEmpty()) {
			return errors;
		}
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getStateId(), req.getStateShortCode(),
				req.getCountryId(), req.getRegionCode(), req.getCityId(), req.getSuburbId());
		if (maxAmendId.isEmpty()) {
			errors.add(new Error("08", "stateId",
					"No existing State Master record found for this business key. Use the save API to create it."));
		}
		return errors;
	}

	@Override
	public UploadDocStateMasterRes update(UploadDocStateMasterUpdateReq req) {
		int newAmendId = repo
				.findMaxAmendId(req.getStateId(), req.getStateShortCode(), req.getCountryId(), req.getRegionCode(),
						req.getCityId(), req.getSuburbId())
				.orElse(-1) + 1;

		UploadDocStateMaster previous = repo
				.findByStateIdAndStateShortCodeAndCountryIdAndRegionCodeAndCityIdAndSuburbIdAndAmendId(
						req.getStateId(), req.getStateShortCode(), req.getCountryId(), req.getRegionCode(),
						req.getCityId(), req.getSuburbId(), newAmendId - 1)
				.orElse(null);

		Date now = new Date();
		UploadDocStateMaster entity = UploadDocStateMaster.builder()
				.stateId(req.getStateId())
				.stateShortCode(req.getStateShortCode())
				.countryId(req.getCountryId())
				.regionCode(req.getRegionCode())
				.cityId(req.getCityId())
				.suburbId(req.getSuburbId())
				.amendId(newAmendId)
				.stateName(req.getStateName() != null ? req.getStateName() : previous.getStateName())
				.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())
				.effectiveDateStart(
						req.getEffectiveDateStart() != null ? req.getEffectiveDateStart() : previous.getEffectiveDateStart())
				.effectiveDateEnd(
						req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd() : previous.getEffectiveDateEnd())
				.coreAppCode(req.getCoreAppCode() != null ? req.getCoreAppCode() : previous.getCoreAppCode())
				.tiraCode(req.getTiraCode() != null ? req.getTiraCode() : previous.getTiraCode())
				.createdBy(previous.getCreatedBy())
				.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())
				.regulatoryCode(
						req.getRegulatoryCode() != null ? req.getRegulatoryCode() : previous.getRegulatoryCode())
				.city(req.getCity() != null ? req.getCity() : previous.getCity())
				.suburb(req.getSuburb() != null ? req.getSuburb() : previous.getSuburb())
				.areaGroup(req.getAreaGroup() != null ? req.getAreaGroup() : previous.getAreaGroup())
				.suburbLocal(req.getSuburbLocal() != null ? req.getSuburbLocal() : previous.getSuburbLocal())
				.stateNameLocal(
						req.getStateNameLocal() != null ? req.getStateNameLocal() : previous.getStateNameLocal())
				.cityLocal(req.getCityLocal() != null ? req.getCityLocal() : previous.getCityLocal())
				.entryDate(previous.getEntryDate())
				.updatedBy(req.getUpdatedBy())
				.updatedDate(now)
				.build();

		entity = repo.saveAndFlush(entity);
		log.info("UploadDocStateMaster amended to AMEND_ID={}: {}", newAmendId, entity);
		return mapper.toRes(entity);
	}

	@Override
	public UploadDocStateMasterRes getLatest(UploadDocStateMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getStateId(), req.getStateShortCode(),
				req.getCountryId(), req.getRegionCode(), req.getCityId(), req.getSuburbId());
		if (maxAmendId.isEmpty()) {
			return null;
		}
		return repo
				.findByStateIdAndStateShortCodeAndCountryIdAndRegionCodeAndCityIdAndSuburbIdAndAmendId(
						req.getStateId(), req.getStateShortCode(), req.getCountryId(), req.getRegionCode(),
						req.getCityId(), req.getSuburbId(), maxAmendId.get())
				.map(mapper::toRes)
				.orElse(null);
	}

	@Override
	public List<UploadDocStateMasterRes> getAll(StateMasterRequest req) {
		if(req.getRegionCode()==null)
		{
	    return repo.findAllLatest(req.getCountryId())
	            .stream()
	            .map(mapper::toRes)
	            .collect(Collectors.toList());
		}
		else{
			 return repo.findAllLatest(req.getCountryId(),req.getRegionCode())
			            .stream()
			            .map(mapper::toRes)
			            .collect(Collectors.toList());
		}
	}

	@Override
	public boolean delete(UploadDocStateMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getStateId(), req.getStateShortCode(),
				req.getCountryId(), req.getRegionCode(), req.getCityId(), req.getSuburbId());
		if (maxAmendId.isEmpty()) {
			return false;
		}
		Optional<UploadDocStateMaster> latest = repo
				.findByStateIdAndStateShortCodeAndCountryIdAndRegionCodeAndCityIdAndSuburbIdAndAmendId(
						req.getStateId(), req.getStateShortCode(), req.getCountryId(), req.getRegionCode(),
						req.getCityId(), req.getSuburbId(), maxAmendId.get());
		latest.ifPresent(repo::delete);
		return latest.isPresent();
	}

	@Override
	public UploadDocStateMasterRes saveOrUpdate(UploadDocStateMasterSaveReq req) {

		Date now = new Date();

		// Get latest amendment ID
		int newAmendId = repo.findMaxAmendId(req.getStateId(), req.getStateShortCode(), req.getCountryId(),
				req.getRegionCode(), req.getCityId(), req.getSuburbId()).orElse(-1) + 1;

		// Get previous/latest record
		UploadDocStateMaster previous = null;

		if (newAmendId > 0) {

			previous = repo.findByStateIdAndStateShortCodeAndCountryIdAndRegionCodeAndCityIdAndSuburbIdAndAmendId(
					req.getStateId(), req.getStateShortCode(), req.getCountryId(), req.getRegionCode(), req.getCityId(),
					req.getSuburbId(), newAmendId - 1).orElse(null);
		}

		UploadDocStateMaster entity;

		if (previous == null) {

			// =========================
			// FIRST RECORD
			// =========================

			entity = UploadDocStateMaster.builder().stateId(req.getStateId()).stateShortCode(req.getStateShortCode())
					.countryId(req.getCountryId()).regionCode(req.getRegionCode()).cityId(req.getCityId())
					.suburbId(req.getSuburbId()).amendId(0)

					.stateName(req.getStateName()).status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())

					.effectiveDateStart(req.getEffectiveDateStart()).effectiveDateEnd(req.getEffectiveDateEnd())
					.coreAppCode(req.getCoreAppCode()).tiraCode(req.getTiraCode()).createdBy(req.getCreatedBy())
					.remarks(req.getRemarks()).regulatoryCode(req.getRegulatoryCode()).city(req.getCity())
					.suburb(req.getSuburb()).areaGroup(req.getAreaGroup()).suburbLocal(req.getSuburbLocal())
					.stateNameLocal(req.getStateNameLocal()).cityLocal(req.getCityLocal())

					.entryDate(now).updatedBy(req.getCreatedBy()).updatedDate(now)

					.build();

		} else {

			// =========================
			// NEW AMENDMENT RECORD
			// =========================

			entity = UploadDocStateMaster.builder().stateId(req.getStateId()).stateShortCode(req.getStateShortCode())
					.countryId(req.getCountryId()).regionCode(req.getRegionCode()).cityId(req.getCityId())
					.suburbId(req.getSuburbId())

					// IMPORTANT
					.amendId(newAmendId)

					.stateName(req.getStateName() != null ? req.getStateName() : previous.getStateName())

					.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())

					.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart()
							: previous.getEffectiveDateStart())

					.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd()
							: previous.getEffectiveDateEnd())

					.coreAppCode(req.getCoreAppCode() != null ? req.getCoreAppCode() : previous.getCoreAppCode())

					.tiraCode(req.getTiraCode() != null ? req.getTiraCode() : previous.getTiraCode())

					.createdBy(previous.getCreatedBy())

					.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())

					.regulatoryCode(
							req.getRegulatoryCode() != null ? req.getRegulatoryCode() : previous.getRegulatoryCode())

					.city(req.getCity() != null ? req.getCity() : previous.getCity())

					.suburb(req.getSuburb() != null ? req.getSuburb() : previous.getSuburb())

					.areaGroup(req.getAreaGroup() != null ? req.getAreaGroup() : previous.getAreaGroup())

					.suburbLocal(req.getSuburbLocal() != null ? req.getSuburbLocal() : previous.getSuburbLocal())

					.stateNameLocal(
							req.getStateNameLocal() != null ? req.getStateNameLocal() : previous.getStateNameLocal())

					.cityLocal(req.getCityLocal() != null ? req.getCityLocal() : previous.getCityLocal())

					.entryDate(previous.getEntryDate()).updatedBy(req.getCreatedBy()).updatedDate(now)

					.build();
		}

		// INSERT new amendment row
		entity = repo.saveAndFlush(entity);

		log.info("UploadDocStateMaster saved with AMEND_ID={}", entity.getAmendId());

		return mapper.toRes(entity);
	}
}
