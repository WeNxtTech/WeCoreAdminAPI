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
import com.maan.eway.uploaddoc.dto.request.UploadDocDeductibleMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocDeductibleMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocDeductibleMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocDeductibleMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocDeductibleMaster;
import com.maan.eway.uploaddoc.mapper.UploadDocDeductibleMasterMapper;
import com.maan.eway.uploaddoc.repository.UploadDocDeductibleMasterRepository;
import com.maan.eway.uploaddoc.service.UploadDocDeductibleMasterService;

/**
 * <h2>UploadDocDeductibleMasterServiceImpl</h2>
 * Amend-Id / versioning approach: see {@link com.maan.eway.uploaddoc.service.impl.UploadDocStateMasterServiceImpl}.
 * Business key here: (deductId, companyId, productId, sectionId, branchCode).
 */
@Service
@Transactional
public class UploadDocDeductibleMasterServiceImpl implements UploadDocDeductibleMasterService {

	private static final Logger log = LogManager.getLogger(UploadDocDeductibleMasterServiceImpl.class);

	@Autowired
	private UploadDocDeductibleMasterRepository repo;

	@Autowired
	private UploadDocDeductibleMasterMapper mapper;

	@Override
	public List<Error> validateSave(UploadDocDeductibleMasterSaveReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getDeductId() == null) {
			errors.add(new Error("01", "deductId", "Please provide DeductId"));
		}
		if (StringUtils.isBlank(req.getCompanyId())) {
			errors.add(new Error("02", "companyId", "Please provide CompanyId"));
		}
		if (req.getProductId() == null) {
			errors.add(new Error("03", "productId", "Please provide ProductId"));
		}
		if (req.getSectionId() == null) {
			errors.add(new Error("04", "sectionId", "Please provide SectionId"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			errors.add(new Error("05", "branchCode", "Please provide BranchCode"));
		}
		if (req.getRate() == null) {
			errors.add(new Error("06", "rate", "Please provide Rate"));
		}
		if (req.getDeductId() != null && StringUtils.isNotBlank(req.getCompanyId()) && req.getProductId() != null
				&& req.getSectionId() != null && StringUtils.isNotBlank(req.getBranchCode())
				&& repo.findMaxAmendId(req.getDeductId(), req.getCompanyId(), req.getProductId(), req.getSectionId(),
						req.getBranchCode()).isPresent()) {
			errors.add(new Error("07", "deductId",
					"A Deductible Master record already exists for this business key. Use the update API to amend it."));
		}
		return errors;
	}

	@Override
	public UploadDocDeductibleMasterRes save(UploadDocDeductibleMasterSaveReq req) {
		Date now = new Date();
		UploadDocDeductibleMaster entity = UploadDocDeductibleMaster.builder()
				.deductId(req.getDeductId())
				.deductStart(req.getDeductStart())
				.deductEnd(req.getDeductEnd())
				.rate(req.getRate())
				.calcType(req.getCalcType())
				.amendId(0)
				.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())
				.entryDate(now)
				.effectiveDateStart(req.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd())
				.remarks(req.getRemarks())
				.branchCode(req.getBranchCode())
				.companyId(req.getCompanyId())
				.productId(req.getProductId())
				.sectionId(req.getSectionId())
				.build();
		entity = repo.saveAndFlush(entity);
		log.info("UploadDocDeductibleMaster created: {}", entity);
		return mapper.toRes(entity);
	}

	@Override
	public List<Error> validateUpdate(UploadDocDeductibleMasterUpdateReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getDeductId() == null) {
			errors.add(new Error("01", "deductId", "Please provide DeductId"));
		}
		if (StringUtils.isBlank(req.getCompanyId())) {
			errors.add(new Error("02", "companyId", "Please provide CompanyId"));
		}
		if (req.getProductId() == null) {
			errors.add(new Error("03", "productId", "Please provide ProductId"));
		}
		if (req.getSectionId() == null) {
			errors.add(new Error("04", "sectionId", "Please provide SectionId"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			errors.add(new Error("05", "branchCode", "Please provide BranchCode"));
		}
		if (!errors.isEmpty()) {
			return errors;
		}
		if (repo.findMaxAmendId(req.getDeductId(), req.getCompanyId(), req.getProductId(), req.getSectionId(),
				req.getBranchCode()).isEmpty()) {
			errors.add(new Error("06", "deductId",
					"No existing Deductible Master record found for this business key. Use the save API to create it."));
		}
		return errors;
	}

	@Override
	public UploadDocDeductibleMasterRes update(UploadDocDeductibleMasterUpdateReq req) {
		int newAmendId = repo.findMaxAmendId(req.getDeductId(), req.getCompanyId(), req.getProductId(),
				req.getSectionId(), req.getBranchCode()).orElse(-1) + 1;
		UploadDocDeductibleMaster previous = repo
				.findByDeductIdAndCompanyIdAndProductIdAndSectionIdAndBranchCodeAndAmendId(req.getDeductId(),
						req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getBranchCode(),
						newAmendId - 1)
				.orElse(null);

		Date now = new Date();
		UploadDocDeductibleMaster entity = UploadDocDeductibleMaster.builder()
				.deductId(req.getDeductId())
				.companyId(req.getCompanyId())
				.productId(req.getProductId())
				.sectionId(req.getSectionId())
				.branchCode(req.getBranchCode())
				.amendId(newAmendId)
				.deductStart(req.getDeductStart() != null ? req.getDeductStart() : previous.getDeductStart())
				.deductEnd(req.getDeductEnd() != null ? req.getDeductEnd() : previous.getDeductEnd())
				.rate(req.getRate() != null ? req.getRate() : previous.getRate())
				.calcType(req.getCalcType() != null ? req.getCalcType() : previous.getCalcType())
				.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())
				.entryDate(previous.getEntryDate())
				.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart() : previous.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd() : previous.getEffectiveDateEnd())
				.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())
				.build();

		entity = repo.saveAndFlush(entity);
		log.info("UploadDocDeductibleMaster amended to AMEND_ID={}: {}", newAmendId, entity);
		return mapper.toRes(entity);
	}

	@Override
	public UploadDocDeductibleMasterRes getLatest(UploadDocDeductibleMasterGetReq req) {
	    Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getDeductId(), req.getCompanyId(),
	            req.getProductId(), req.getSectionId(), req.getBranchCode());
	    if (maxAmendId.isEmpty()) {
	        return null;
	    }
	    Optional<UploadDocDeductibleMaster> result = repo
	            .findByDeductIdAndCompanyIdAndProductIdAndSectionIdAndBranchCodeAndAmendId(
	                    req.getDeductId(), req.getCompanyId(), req.getProductId(),
	                    req.getSectionId(), req.getBranchCode(), maxAmendId.get());

	    return result.map(mapper::toRes).orElse(null);
	}

	@Override
	public List<UploadDocDeductibleMasterRes> getAll(UploadDocDeductibleMasterGetReq req) {
	    List<UploadDocDeductibleMaster> list = repo.findLatestAmendmentPerDeduct(
	            req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getBranchCode());

	    if (list == null || list.isEmpty()) {
	        return null;
	    }
	    return list.stream()
	               .map(mapper::toRes)
	               .collect(Collectors.toList());
	}
	@Override
	public boolean delete(UploadDocDeductibleMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getDeductId(), req.getCompanyId(), req.getProductId(),
				req.getSectionId(), req.getBranchCode());
		if (maxAmendId.isEmpty()) {
			return false;
		}
		Optional<UploadDocDeductibleMaster> latest = repo.findByDeductIdAndCompanyIdAndProductIdAndSectionIdAndBranchCodeAndAmendId(
				req.getDeductId(), req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getBranchCode(),
				maxAmendId.get());
		latest.ifPresent(repo::delete);
		return latest.isPresent();
	}
	
	
	@Override
	public UploadDocDeductibleMasterRes saveOrUpdate(
			UploadDocDeductibleMasterSaveReq req) {

		Date now = new Date();

		// Get latest AMEND_ID
		int newAmendId = repo.findMaxAmendId(req.getDeductId(), req.getCompanyId(), req.getProductId(),
				req.getSectionId(), req.getBranchCode()).orElse(-1) + 1;

		// Get previous/latest record
		UploadDocDeductibleMaster previous = null;

		if (newAmendId > 0) {
			previous = repo.findByDeductIdAndCompanyIdAndProductIdAndSectionIdAndBranchCodeAndAmendId(req.getDeductId(),
					req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getBranchCode(), newAmendId - 1)
					.orElse(null);
		}

		UploadDocDeductibleMaster entity;

		if (previous == null) {

			// =========================
			// NEW RECORD
			// =========================
			entity = UploadDocDeductibleMaster.builder().deductId(req.getDeductId()).companyId(req.getCompanyId())
					.productId(req.getProductId()).sectionId(req.getSectionId()).branchCode(req.getBranchCode())

					.amendId(0)

					.deductStart(req.getDeductStart()).deductEnd(req.getDeductEnd()).rate(req.getRate())
					.calcType(req.getCalcType())

					.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())

					.entryDate(now)

					.effectiveDateStart(req.getEffectiveDateStart()).effectiveDateEnd(req.getEffectiveDateEnd())

					.remarks(req.getRemarks())

					.build();

		} else {

			// =========================
			// UPDATE / AMENDMENT
			// =========================
			entity = UploadDocDeductibleMaster.builder().deductId(req.getDeductId()).companyId(req.getCompanyId())
					.productId(req.getProductId()).sectionId(req.getSectionId()).branchCode(req.getBranchCode())

					.amendId(newAmendId)

					.deductStart(req.getDeductStart() != null ? req.getDeductStart() : previous.getDeductStart())

					.deductEnd(req.getDeductEnd() != null ? req.getDeductEnd() : previous.getDeductEnd())

					.rate(req.getRate() != null ? req.getRate() : previous.getRate())

					.calcType(req.getCalcType() != null ? req.getCalcType() : previous.getCalcType())

					.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())

					.entryDate(previous.getEntryDate())

					.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart()
							: previous.getEffectiveDateStart())

					.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd()
							: previous.getEffectiveDateEnd())

					.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())

					.build();
		}

		entity = repo.saveAndFlush(entity);

		log.info("UploadDocDeductibleMaster saved/amended with AMEND_ID={}: {}", entity.getAmendId(), entity);

		return mapper.toRes(entity);
	}
}
