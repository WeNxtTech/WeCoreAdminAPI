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
import com.maan.eway.uploaddoc.dto.request.UploadDocAgricultureMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAgricultureMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocAgricultureMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocAgricultureMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocAgricultureMaster;
import com.maan.eway.uploaddoc.mapper.UploadDocAgricultureMasterMapper;
import com.maan.eway.uploaddoc.repository.UploadDocAgricultureMasterRepository;
import com.maan.eway.uploaddoc.service.UploadDocAgricultureMasterService;

/**
 * <h2>UploadDocAgricultureMasterServiceImpl</h2>
 * Amend-Id / versioning approach: see {@link com.maan.eway.uploaddoc.service.impl.UploadDocStateMasterServiceImpl}.
 * Business key here: (sno, companyId, productId).
 */
@Service
@Transactional
public class UploadDocAgricultureMasterServiceImpl implements UploadDocAgricultureMasterService {

	private static final Logger log = LogManager.getLogger(UploadDocAgricultureMasterServiceImpl.class);

	@Autowired
	private UploadDocAgricultureMasterRepository repo;

	@Autowired
	private UploadDocAgricultureMasterMapper mapper;

	@Override
	public List<Error> validateSave(UploadDocAgricultureMasterSaveReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getSno() == null) {
			errors.add(new Error("01", "sno", "Please provide Sno"));
		}
		if (req.getCompanyId() == null) {
			errors.add(new Error("02", "companyId", "Please provide CompanyId"));
		}
		if (req.getProductId() == null) {
			errors.add(new Error("03", "productId", "Please provide ProductId"));
		}
		if (StringUtils.isBlank(req.getCropDesc())) {
			errors.add(new Error("04", "cropDesc", "Please provide CropDesc"));
		}
//		if (req.getSno() != null && req.getCompanyId() != null && req.getProductId() != null
//				&& repo.findMaxAmendId(req.getSno(), req.getCompanyId(), req.getProductId()).isPresent()) {
//			errors.add(new Error("05", "sno",
//					"An Agriculture Master record already exists for this business key. Use the update API to amend it."));
//		}
		return errors;
	}

	@Override
	public UploadDocAgricultureMasterRes save(UploadDocAgricultureMasterSaveReq req) {
		Date now = new Date();
		UploadDocAgricultureMaster entity = UploadDocAgricultureMaster.builder()
				.sno(req.getSno())
				.companyId(req.getCompanyId())
				.productId(req.getProductId())
				.amendId(0)
				.provinceId(req.getProvinceId())
				.provinceDesc(req.getProvinceDesc())
				.districtId(req.getDistrictId())
				.districtDesc(req.getDistrictDesc())
				.aez(req.getAez())
				.cropId(req.getCropId())
				.cropDesc(req.getCropDesc())
				.yieldPercentage(req.getYieldPercentage())
				.perHaCost(req.getPerHaCost())
				.sectionId(req.getSectionId())
				.coreAppCode(req.getCoreAppCode())
				.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())
				.entryDate(now)
				.effectiveDateStart(req.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd())
				.remarks(req.getRemarks())
				.build();
		entity = repo.saveAndFlush(entity);
		log.info("UploadDocAgricultureMaster created: {}", entity);
		return mapper.toRes(entity);
	}

	@Override
	public List<Error> validateUpdate(UploadDocAgricultureMasterUpdateReq req) {
		List<Error> errors = new ArrayList<>();
		if (req.getSno() == null) {
			errors.add(new Error("01", "sno", "Please provide Sno"));
		}
		if (req.getCompanyId() == null) {
			errors.add(new Error("02", "companyId", "Please provide CompanyId"));
		}
		if (req.getProductId() == null) {
			errors.add(new Error("03", "productId", "Please provide ProductId"));
		}
		if (!errors.isEmpty()) {
			return errors;
		}
		if (repo.findMaxAmendId(req.getSno(), req.getCompanyId(), req.getProductId()).isEmpty()) {
			errors.add(new Error("04", "sno",
					"No existing Agriculture Master record found for this business key. Use the save API to create it."));
		}
		return errors;
	}

	@Override
	public UploadDocAgricultureMasterRes update(UploadDocAgricultureMasterUpdateReq req) {
		int newAmendId = repo.findMaxAmendId(req.getSno(), req.getCompanyId(), req.getProductId()).orElse(-1) + 1;
		UploadDocAgricultureMaster previous = repo
				.findBySnoAndCompanyIdAndProductIdAndAmendId(req.getSno(), req.getCompanyId(), req.getProductId(),
						newAmendId - 1)
				.orElse(null);

		Date now = new Date();
		UploadDocAgricultureMaster entity = UploadDocAgricultureMaster.builder()
				.sno(req.getSno())
				.companyId(req.getCompanyId())
				.productId(req.getProductId())
				.amendId(newAmendId)
				.provinceId(req.getProvinceId() != null ? req.getProvinceId() : previous.getProvinceId())
				.provinceDesc(req.getProvinceDesc() != null ? req.getProvinceDesc() : previous.getProvinceDesc())
				.districtId(req.getDistrictId() != null ? req.getDistrictId() : previous.getDistrictId())
				.districtDesc(req.getDistrictDesc() != null ? req.getDistrictDesc() : previous.getDistrictDesc())
				.aez(req.getAez() != null ? req.getAez() : previous.getAez())
				.cropId(req.getCropId() != null ? req.getCropId() : previous.getCropId())
				.cropDesc(req.getCropDesc() != null ? req.getCropDesc() : previous.getCropDesc())
				.yieldPercentage(req.getYieldPercentage() != null ? req.getYieldPercentage() : previous.getYieldPercentage())
				.perHaCost(req.getPerHaCost() != null ? req.getPerHaCost() : previous.getPerHaCost())
				.sectionId(req.getSectionId() != null ? req.getSectionId() : previous.getSectionId())
				.coreAppCode(req.getCoreAppCode() != null ? req.getCoreAppCode() : previous.getCoreAppCode())
				.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())
				.entryDate(previous.getEntryDate())
				.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart() : previous.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd() : previous.getEffectiveDateEnd())
				.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())
				.build();

		entity = repo.saveAndFlush(entity);
		log.info("UploadDocAgricultureMaster amended to AMEND_ID={}: {}", newAmendId, entity);
		return mapper.toRes(entity);
	}

	@Override
	public UploadDocAgricultureMasterRes getLatest(UploadDocAgricultureMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getSno(), req.getCompanyId(), req.getProductId());
		if (maxAmendId.isEmpty()) {
			return null;
		}
		return repo.findBySnoAndCompanyIdAndProductIdAndAmendId(req.getSno(), req.getCompanyId(), req.getProductId(),
				maxAmendId.get()).map(mapper::toRes).orElse(null);
	}

	@Override
	public List<UploadDocAgricultureMasterRes> getAll(
	        UploadDocAgricultureMasterGetReq req) {

	    return repo.findAllLatest()
	            .stream()
	            .map(mapper::toRes)
	            .collect(Collectors.toList());
	}

	@Override
	public boolean delete(UploadDocAgricultureMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getSno(), req.getCompanyId(), req.getProductId());
		if (maxAmendId.isEmpty()) {
			return false;
		}
		Optional<UploadDocAgricultureMaster> latest = repo.findBySnoAndCompanyIdAndProductIdAndAmendId(req.getSno(),
				req.getCompanyId(), req.getProductId(), maxAmendId.get());
		latest.ifPresent(repo::delete);
		return latest.isPresent();
	}
	
	@Override
	public UploadDocAgricultureMasterRes saveOrUpdate(UploadDocAgricultureMasterSaveReq req) {

		Date now = new Date();

		// Get latest AMEND_ID
		int newAmendId = repo.findMaxAmendId(req.getSno(), req.getCompanyId(), req.getProductId()).orElse(-1) + 1;

		// Get previous/latest record
		UploadDocAgricultureMaster previous = null;

		if (newAmendId > 0) {
			previous = repo.findBySnoAndCompanyIdAndProductIdAndAmendId(req.getSno(), req.getCompanyId(),
					req.getProductId(), newAmendId - 1).orElse(null);
		}

		UploadDocAgricultureMaster entity;

		if (previous == null) {

			// =========================
			// NEW RECORD
			// =========================
			entity = UploadDocAgricultureMaster.builder().sno(req.getSno()).companyId(req.getCompanyId())
					.productId(req.getProductId()).amendId(0)

					.provinceId(req.getProvinceId()).provinceDesc(req.getProvinceDesc()).districtId(req.getDistrictId())
					.districtDesc(req.getDistrictDesc()).aez(req.getAez()).cropId(req.getCropId())
					.cropDesc(req.getCropDesc()).yieldPercentage(req.getYieldPercentage()).perHaCost(req.getPerHaCost())
					.sectionId(req.getSectionId()).coreAppCode(req.getCoreAppCode())

					.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())

					.entryDate(now).effectiveDateStart(req.getEffectiveDateStart())
					.effectiveDateEnd(req.getEffectiveDateEnd()).remarks(req.getRemarks())

					.build();

		} else {

			// =========================
			// UPDATE / AMENDMENT
			// =========================
			entity = UploadDocAgricultureMaster.builder().sno(req.getSno()).companyId(req.getCompanyId())
					.productId(req.getProductId()).amendId(newAmendId)

					.provinceId(req.getProvinceId() != null ? req.getProvinceId() : previous.getProvinceId())

					.provinceDesc(req.getProvinceDesc() != null ? req.getProvinceDesc() : previous.getProvinceDesc())

					.districtId(req.getDistrictId() != null ? req.getDistrictId() : previous.getDistrictId())

					.districtDesc(req.getDistrictDesc() != null ? req.getDistrictDesc() : previous.getDistrictDesc())

					.aez(req.getAez() != null ? req.getAez() : previous.getAez())

					.cropId(req.getCropId() != null ? req.getCropId() : previous.getCropId())

					.cropDesc(req.getCropDesc() != null ? req.getCropDesc() : previous.getCropDesc())

					.yieldPercentage(
							req.getYieldPercentage() != null ? req.getYieldPercentage() : previous.getYieldPercentage())

					.perHaCost(req.getPerHaCost() != null ? req.getPerHaCost() : previous.getPerHaCost())

					.sectionId(req.getSectionId() != null ? req.getSectionId() : previous.getSectionId())

					.coreAppCode(req.getCoreAppCode() != null ? req.getCoreAppCode() : previous.getCoreAppCode())

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

		log.info("UploadDocAgricultureMaster saved/amended with AMEND_ID={}: {}", entity.getAmendId(), entity);

		return mapper.toRes(entity);
	}
}
