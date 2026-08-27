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
import com.maan.eway.uploaddoc.dto.request.UploadDocAcExecutiveMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAcExecutiveMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAcExecutiveMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocAcExecutiveMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocAcExecutiveMaster;
import com.maan.eway.uploaddoc.mapper.UploadDocAcExecutiveMasterMapper;
import com.maan.eway.uploaddoc.repository.UploadDocAcExecutiveMasterRepository;
import com.maan.eway.uploaddoc.service.UploadDocAcExecutiveMasterService;

/**
 * <h2>UploadDocAcExecutiveMasterServiceImpl</h2>
 * Amend-Id / versioning approach: see {@link com.maan.eway.uploaddoc.service.impl.UploadDocStateMasterServiceImpl}.
 * Business key here: (acExecutiveId, branchCode, companyId, bankCode). STATUS and EFFECTIVE_DATE_END
 * are part of the physical primary key but are amendable attributes, not part of the logical business key.
 */
@Service
@Transactional
public class UploadDocAcExecutiveMasterServiceImpl implements UploadDocAcExecutiveMasterService {

	private static final Logger log = LogManager.getLogger(UploadDocAcExecutiveMasterServiceImpl.class);

	@Autowired
	private UploadDocAcExecutiveMasterRepository repo;

	@Autowired
	private UploadDocAcExecutiveMasterMapper mapper;

	@Override
	public List<Error> validateSave(UploadDocAcExecutiveMasterSaveReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getAcExecutiveId() == null) {
			errors.add(new Error("01", "acExecutiveId", "Please provide AcExecutiveId"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			errors.add(new Error("02", "branchCode", "Please provide BranchCode"));
		}
		if (StringUtils.isBlank(req.getCompanyId())) {
			errors.add(new Error("03", "companyId", "Please provide CompanyId"));
		}
		if (StringUtils.isBlank(req.getBankCode())) {
			errors.add(new Error("04", "bankCode", "Please provide BankCode"));
		}
		if (StringUtils.isBlank(req.getStatus())) {
			errors.add(new Error("05", "status", "Please provide Status"));
		} else if (req.getStatus().length() > 2) {
			errors.add(new Error("05", "status", "Status must not exceed 2 characters"));
		}
		if (req.getEffectiveDateStart() == null) {
			errors.add(new Error("06", "effectiveDateStart", "Please provide EffectiveDateStart"));
		}
		if (req.getEffectiveDateEnd() == null) {
			errors.add(new Error("07", "effectiveDateEnd", "Please provide EffectiveDateEnd"));
		}
		if (req.getAcExecutiveId() != null && StringUtils.isNotBlank(req.getBranchCode())
				&& StringUtils.isNotBlank(req.getCompanyId()) && StringUtils.isNotBlank(req.getBankCode())
				&& repo.findMaxAmendId(req.getAcExecutiveId(), req.getBranchCode(), req.getCompanyId(),
						req.getBankCode()).isPresent()) {
			errors.add(new Error("08", "acExecutiveId",
					"An Ac Executive Master record already exists for this business key. Use the update API to amend it."));
		}
		return errors;
	}

	@Override
	public UploadDocAcExecutiveMasterRes save(UploadDocAcExecutiveMasterSaveReq req) {
		Date now = new Date();
		UploadDocAcExecutiveMaster entity = UploadDocAcExecutiveMaster.builder()
				.acExecutiveId(req.getAcExecutiveId())
				.acExecutiveName(req.getAcExecutiveName())
				.oaCode(req.getOaCode())
				.branchCode(req.getBranchCode())
				.companyId(req.getCompanyId())
				.commissionPercent(req.getCommissionPercent())
				.status(req.getStatus())
				.effectiveDateStart(req.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd())
				.amendId(0)
				.entryDate(now)
				.bankCode(req.getBankCode())
				.build();
		entity = repo.saveAndFlush(entity);
		log.info("UploadDocAcExecutiveMaster created: {}", entity);
		return mapper.toRes(entity);
	}

	@Override
	public List<Error> validateUpdate(UploadDocAcExecutiveMasterUpdateReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getAcExecutiveId() == null) {
			errors.add(new Error("01", "acExecutiveId", "Please provide AcExecutiveId"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			errors.add(new Error("02", "branchCode", "Please provide BranchCode"));
		}
		if (StringUtils.isBlank(req.getCompanyId())) {
			errors.add(new Error("03", "companyId", "Please provide CompanyId"));
		}
		if (StringUtils.isBlank(req.getBankCode())) {
			errors.add(new Error("04", "bankCode", "Please provide BankCode"));
		}
		if (!errors.isEmpty()) {
			return errors;
		}
		if (repo.findMaxAmendId(req.getAcExecutiveId(), req.getBranchCode(), req.getCompanyId(), req.getBankCode())
				.isEmpty()) {
			errors.add(new Error("05", "acExecutiveId",
					"No existing Ac Executive Master record found for this business key. Use the save API to create it."));
		}
		return errors;
	}

	@Override
	public UploadDocAcExecutiveMasterRes update(UploadDocAcExecutiveMasterUpdateReq req) {
		int newAmendId = repo
				.findMaxAmendId(req.getAcExecutiveId(), req.getBranchCode(), req.getCompanyId(), req.getBankCode())
				.orElse(-1) + 1;
		UploadDocAcExecutiveMaster previous = repo
				.findByBusinessKeyAndAmendId(req.getAcExecutiveId(), req.getBranchCode(), req.getCompanyId(),
						req.getBankCode(), newAmendId - 1)
				.orElse(null);

		Date now = new Date();
		UploadDocAcExecutiveMaster entity = UploadDocAcExecutiveMaster.builder()
				.acExecutiveId(req.getAcExecutiveId())
				.branchCode(req.getBranchCode())
				.companyId(req.getCompanyId())
				.bankCode(req.getBankCode())
				.amendId(newAmendId)
				.acExecutiveName(req.getAcExecutiveName() != null ? req.getAcExecutiveName() : previous.getAcExecutiveName())
				.oaCode(req.getOaCode() != null ? req.getOaCode() : previous.getOaCode())
				.commissionPercent(req.getCommissionPercent() != null ? req.getCommissionPercent() : previous.getCommissionPercent())
				.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())
				.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart() : previous.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd() : previous.getEffectiveDateEnd())
				.entryDate(now)
				.build();

		entity = repo.saveAndFlush(entity);
		log.info("UploadDocAcExecutiveMaster amended to AMEND_ID={}: {}", newAmendId, entity);
		return mapper.toRes(entity);
	}

	@Override
	public UploadDocAcExecutiveMasterRes getLatest(UploadDocAcExecutiveMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getAcExecutiveId(), req.getBranchCode(),
				req.getCompanyId(), req.getBankCode());
		if (maxAmendId.isEmpty()) {
			return null;
		}
		return repo.findByBusinessKeyAndAmendId(req.getAcExecutiveId(), req.getBranchCode(), req.getCompanyId(),
				req.getBankCode(), maxAmendId.get()).map(mapper::toRes).orElse(null);
	}

	@Override
	public List<UploadDocAcExecutiveMasterRes> getAll(UploadDocAcExecutiveMasterGetReq req) {
	    List<UploadDocAcExecutiveMaster> list = repo.findAllLatest(
	             req.getBranchCode(),
	            req.getCompanyId(), req.getBankCode());
	    if (list == null || list.isEmpty()) {
	        return null;
	    }
	    return list.stream()
	               .map(mapper::toRes)
	               .collect(Collectors.toList());
	}

	@Override
	public boolean delete(UploadDocAcExecutiveMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getAcExecutiveId(), req.getBranchCode(),
				req.getCompanyId(), req.getBankCode());
		if (maxAmendId.isEmpty()) {
			return false;
		}
		Optional<UploadDocAcExecutiveMaster> latest = repo.findByBusinessKeyAndAmendId(req.getAcExecutiveId(),
				req.getBranchCode(), req.getCompanyId(), req.getBankCode(), maxAmendId.get());
		latest.ifPresent(repo::delete);
		return latest.isPresent();
	}
	
	
	public UploadDocAcExecutiveMasterRes saveOrUpdate(
	        UploadDocAcExecutiveMasterSaveReq req) {

	    Date now = new Date();

	    // Get latest amend ID
	    int newAmendId = repo.findMaxAmendId(
	            req.getAcExecutiveId(),
	            req.getBranchCode(),
	            req.getCompanyId(),
	            req.getBankCode()
	    ).orElse(-1) + 1;

	    // Fetch previous/latest record
	    UploadDocAcExecutiveMaster previous = null;

	    if (newAmendId > 0) {
	        previous = repo.findByBusinessKeyAndAmendId(
	                req.getAcExecutiveId(),
	                req.getBranchCode(),
	                req.getCompanyId(),
	                req.getBankCode(),
	                newAmendId - 1
	        ).orElse(null);
	    }

	    UploadDocAcExecutiveMaster entity;

	    if (previous == null) {

	        // =========================
	        // NEW RECORD
	        // =========================
	        entity = UploadDocAcExecutiveMaster.builder()
	                .acExecutiveId(req.getAcExecutiveId())
	                .acExecutiveName(req.getAcExecutiveName())
	                .oaCode(req.getOaCode())
	                .branchCode(req.getBranchCode())
	                .companyId(req.getCompanyId())
	                .commissionPercent(req.getCommissionPercent())
	                .status(req.getStatus())
	                .effectiveDateStart(req.getEffectiveDateStart())
	                .effectiveDateEnd(req.getEffectiveDateEnd())
	                .amendId(0)
	                .entryDate(now)
	                .bankCode(req.getBankCode())
	                .build();

	    } else {

	        // =========================
	        // AMEND / UPDATE
	        // =========================
	        entity = UploadDocAcExecutiveMaster.builder()
	                .acExecutiveId(req.getAcExecutiveId())
	                .branchCode(req.getBranchCode())
	                .companyId(req.getCompanyId())
	                .bankCode(req.getBankCode())
	                .amendId(newAmendId)

	                .acExecutiveName(
	                        req.getAcExecutiveName() != null
	                                ? req.getAcExecutiveName()
	                                : previous.getAcExecutiveName()
	                )

	                .oaCode(
	                        req.getOaCode() != null
	                                ? req.getOaCode()
	                                : previous.getOaCode()
	                )

	                .commissionPercent(
	                        req.getCommissionPercent() != null
	                                ? req.getCommissionPercent()
	                                : previous.getCommissionPercent()
	                )

	                .status(
	                        req.getStatus() != null
	                                ? req.getStatus()
	                                : previous.getStatus()
	                )

	                .effectiveDateStart(
	                        req.getEffectiveDateStart() != null
	                                ? req.getEffectiveDateStart()
	                                : previous.getEffectiveDateStart()
	                )

	                .effectiveDateEnd(
	                        req.getEffectiveDateEnd() != null
	                                ? req.getEffectiveDateEnd()
	                                : previous.getEffectiveDateEnd()
	                )

	                .entryDate(previous.getEntryDate())

	                .build();
	    }

	    entity = repo.saveAndFlush(entity);

	    log.info(
	            "UploadDocAcExecutiveMaster saved/amended with AMEND_ID={}: {}",
	            entity.getAmendId(),
	            entity
	    );

	    return mapper.toRes(entity);
	}
}
