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
import com.maan.eway.uploaddoc.dto.request.UploadDocNotifTemplateMasterGetReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocNotifTemplateMasterSaveReq;
import com.maan.eway.uploaddoc.dto.request.UploadDocNotifTemplateMasterUpdateReq;
import com.maan.eway.uploaddoc.dto.response.UploadDocNotifTemplateMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocNotifTemplateMaster;
import com.maan.eway.uploaddoc.mapper.UploadDocNotifTemplateMasterMapper;
import com.maan.eway.uploaddoc.repository.UploadDocNotifTemplateMasterRepository;
import com.maan.eway.uploaddoc.service.UploadDocNotifTemplateMasterService;

/**
 * <h2>UploadDocNotifTemplateMasterServiceImpl</h2>
 * Named with an "UploadDoc" prefix to avoid a Spring bean-name collision with
 * the pre-existing {@code com.maan.eway.master.service.impl.NotifTemplateMasterServiceImpl}.
 *
 * Amend-Id / versioning approach: see {@link com.maan.eway.uploaddoc.service.impl.StateMasterServiceImpl}.
 * Business key here: (notifTemplateCode, companyId, productId).
 */
@Service
@Transactional
public class UploadDocNotifTemplateMasterServiceImpl implements UploadDocNotifTemplateMasterService {

	private static final Logger log = LogManager.getLogger(UploadDocNotifTemplateMasterServiceImpl.class);

	@Autowired
	private UploadDocNotifTemplateMasterRepository repo;

	@Autowired
	private UploadDocNotifTemplateMasterMapper mapper;

	@Override
	public List<Error> validateSave(UploadDocNotifTemplateMasterSaveReq req) {
		List<Error> errors = new ArrayList<>();
		if (StringUtils.isBlank(req.getNotifTemplateCode())) {
			errors.add(new Error("01", "notifTemplateCode", "Please provide NotifTemplateCode"));
		} else if (req.getNotifTemplateCode().length() > 20) {
			errors.add(new Error("01", "notifTemplateCode", "NotifTemplateCode must not exceed 20 characters"));
		}
		if (StringUtils.isBlank(req.getCompanyId())) {
			errors.add(new Error("02", "companyId", "Please provide CompanyId"));
		} else if (req.getCompanyId().length() > 10) {
			errors.add(new Error("02", "companyId", "CompanyId must not exceed 10 characters"));
		}
		if (req.getProductId() == null) {
			errors.add(new Error("03", "productId", "Please provide ProductId"));
		}
		if (req.getEffectiveDateStart() == null) {
			errors.add(new Error("04", "effectiveDateStart", "Please provide EffectiveDateStart"));
		}
		if (req.getEffectiveDateEnd() == null) {
			errors.add(new Error("05", "effectiveDateEnd", "Please provide EffectiveDateEnd"));
		}
		if (StringUtils.isBlank(req.getSmsRequired())) {
			errors.add(new Error("06", "smsRequired", "Please provide SmsRequired"));
		}
		if (StringUtils.isBlank(req.getCoreAppCode())) {
			errors.add(new Error("07", "coreAppCode", "Please provide CoreAppCode"));
		}
		if (StringUtils.isBlank(req.getRegulatoryCode())) {
			errors.add(new Error("08", "regulatoryCode", "Please provide RegulatoryCode"));
		}
		if (StringUtils.isBlank(req.getCreatedBy())) {
			errors.add(new Error("09", "createdBy", "Please provide CreatedBy"));
		}
		if (StringUtils.isNotBlank(req.getNotifTemplateCode()) && StringUtils.isNotBlank(req.getCompanyId())
				&& req.getProductId() != null
				&& repo.findMaxAmendId(req.getNotifTemplateCode(), req.getCompanyId(), req.getProductId())
						.isPresent()) {
			errors.add(new Error("10", "notifTemplateCode",
					"A Notif Template Master record already exists for this business key. Use the update API to amend it."));
		}
		return errors;
	}

	@Override
	public UploadDocNotifTemplateMasterRes save(UploadDocNotifTemplateMasterSaveReq req) {
		Date now = new Date();
		UploadDocNotifTemplateMaster entity = UploadDocNotifTemplateMaster.builder()
				.notifTemplateCode(req.getNotifTemplateCode())
				.companyId(req.getCompanyId())
				.productId(req.getProductId())
				.amendId(0)
				.notifTemplateName(req.getNotifTemplateName())
				.toMessengerNo(req.getToMessengerNo())
				.toSmsNo(req.getToSmsNo())
				.toEmail(req.getToEmail())
				.effectiveDateStart(req.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd())
				.mailRequired(req.getMailRequired())
				.mailSubject(req.getMailSubject())
				.mailBody(req.getMailBody())
				.mailRegards(req.getMailRegards())
				.smsRequired(req.getSmsRequired())
				.smsSubject(req.getSmsSubject())
				.smsBodyEn(req.getSmsBodyEn())
				.smsRegards(req.getSmsRegards())
				.whatsappRequired(req.getWhatsappRequired())
				.whatsappSubject(req.getWhatsappSubject())
				.whatsappBodyEn(req.getWhatsappBodyEn())
				.whatsappRegards(req.getWhatsappRegards())
				.entryDate(now)
				.remarks(req.getRemarks())
				.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())
				.coreAppCode(req.getCoreAppCode())
				.regulatoryCode(req.getRegulatoryCode())
				.createdBy(req.getCreatedBy())
				.updatedBy(req.getCreatedBy())
				.updatedDate(now)
				.build();
		entity = repo.saveAndFlush(entity);
		log.info("UploadDocNotifTemplateMaster created: {}", entity);
		return mapper.toRes(entity);
	}

	@Override
	public List<Error> validateUpdate(UploadDocNotifTemplateMasterUpdateReq req) {
		List<Error> errors = new ArrayList<>();
		if (StringUtils.isBlank(req.getNotifTemplateCode())) {
			errors.add(new Error("01", "notifTemplateCode", "Please provide NotifTemplateCode"));
		}
		if (StringUtils.isBlank(req.getCompanyId())) {
			errors.add(new Error("02", "companyId", "Please provide CompanyId"));
		}
		if (req.getProductId() == null) {
			errors.add(new Error("03", "productId", "Please provide ProductId"));
		}
		if (StringUtils.isBlank(req.getUpdatedBy())) {
			errors.add(new Error("04", "updatedBy", "Please provide UpdatedBy"));
		}
		if (!errors.isEmpty()) {
			return errors;
		}
		if (repo.findMaxAmendId(req.getNotifTemplateCode(), req.getCompanyId(), req.getProductId()).isEmpty()) {
			errors.add(new Error("05", "notifTemplateCode",
					"No existing Notif Template Master record found for this business key. Use the save API to create it."));
		}
		return errors;
	}

	@Override
	public UploadDocNotifTemplateMasterRes update(UploadDocNotifTemplateMasterUpdateReq req) {
		int newAmendId = repo.findMaxAmendId(req.getNotifTemplateCode(), req.getCompanyId(), req.getProductId())
				.orElse(-1) + 1;
		UploadDocNotifTemplateMaster previous = repo
				.findByNotifTemplateCodeAndCompanyIdAndProductIdAndAmendId(req.getNotifTemplateCode(),
						req.getCompanyId(), req.getProductId(), newAmendId - 1)
				.orElse(null);

		Date now = new Date();
		UploadDocNotifTemplateMaster entity = UploadDocNotifTemplateMaster.builder()
				.notifTemplateCode(req.getNotifTemplateCode())
				.companyId(req.getCompanyId())
				.productId(req.getProductId())
				.amendId(newAmendId)
				.notifTemplateName(req.getNotifTemplateName() != null ? req.getNotifTemplateName() : previous.getNotifTemplateName())
				.toMessengerNo(req.getToMessengerNo() != null ? req.getToMessengerNo() : previous.getToMessengerNo())
				.toSmsNo(req.getToSmsNo() != null ? req.getToSmsNo() : previous.getToSmsNo())
				.toEmail(req.getToEmail() != null ? req.getToEmail() : previous.getToEmail())
				.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart() : previous.getEffectiveDateStart())
				.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd() : previous.getEffectiveDateEnd())
				.mailRequired(req.getMailRequired() != null ? req.getMailRequired() : previous.getMailRequired())
				.mailSubject(req.getMailSubject() != null ? req.getMailSubject() : previous.getMailSubject())
				.mailBody(req.getMailBody() != null ? req.getMailBody() : previous.getMailBody())
				.mailRegards(req.getMailRegards() != null ? req.getMailRegards() : previous.getMailRegards())
				.smsRequired(req.getSmsRequired() != null ? req.getSmsRequired() : previous.getSmsRequired())
				.smsSubject(req.getSmsSubject() != null ? req.getSmsSubject() : previous.getSmsSubject())
				.smsBodyEn(req.getSmsBodyEn() != null ? req.getSmsBodyEn() : previous.getSmsBodyEn())
				.smsRegards(req.getSmsRegards() != null ? req.getSmsRegards() : previous.getSmsRegards())
				.whatsappRequired(req.getWhatsappRequired() != null ? req.getWhatsappRequired() : previous.getWhatsappRequired())
				.whatsappSubject(req.getWhatsappSubject() != null ? req.getWhatsappSubject() : previous.getWhatsappSubject())
				.whatsappBodyEn(req.getWhatsappBodyEn() != null ? req.getWhatsappBodyEn() : previous.getWhatsappBodyEn())
				.whatsappRegards(req.getWhatsappRegards() != null ? req.getWhatsappRegards() : previous.getWhatsappRegards())
				.entryDate(previous.getEntryDate())
				.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())
				.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())
				.coreAppCode(req.getCoreAppCode() != null ? req.getCoreAppCode() : previous.getCoreAppCode())
				.regulatoryCode(req.getRegulatoryCode() != null ? req.getRegulatoryCode() : previous.getRegulatoryCode())
				.createdBy(previous.getCreatedBy())
				.updatedBy(req.getUpdatedBy())
				.updatedDate(now)
				.build();

		entity = repo.saveAndFlush(entity);
		log.info("UploadDocNotifTemplateMaster amended to AMEND_ID={}: {}", newAmendId, entity);
		return mapper.toRes(entity);
	}

	@Override
	public UploadDocNotifTemplateMasterRes getLatest(UploadDocNotifTemplateMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getNotifTemplateCode(), req.getCompanyId(),
				req.getProductId());
		if (maxAmendId.isEmpty()) {
			return null;
		}
		return repo.findByNotifTemplateCodeAndCompanyIdAndProductIdAndAmendId(req.getNotifTemplateCode(),
				req.getCompanyId(), req.getProductId(), maxAmendId.get()).map(mapper::toRes).orElse(null);
	}

	@Override
	public List<UploadDocNotifTemplateMasterRes> getAll(
	        UploadDocNotifTemplateMasterGetReq req) {

	    return repo.findAllLatest(
	            req.getCompanyId(),
	            req.getProductId()
	        )
	        .stream()
	        .map(mapper::toRes)
	        .collect(Collectors.toList());
	}

	@Override
	public boolean delete(UploadDocNotifTemplateMasterGetReq req) {
		Optional<Integer> maxAmendId = repo.findMaxAmendId(req.getNotifTemplateCode(), req.getCompanyId(),
				req.getProductId());
		if (maxAmendId.isEmpty()) {
			return false;
		}
		Optional<UploadDocNotifTemplateMaster> latest = repo.findByNotifTemplateCodeAndCompanyIdAndProductIdAndAmendId(
				req.getNotifTemplateCode(), req.getCompanyId(), req.getProductId(), maxAmendId.get());
		latest.ifPresent(repo::delete);
		return latest.isPresent();
	}
	
	@Override
	public UploadDocNotifTemplateMasterRes saveOrUpdate(
			UploadDocNotifTemplateMasterSaveReq req) {

		Date now = new Date();

		// Get latest AMEND_ID
		int newAmendId = repo.findMaxAmendId(req.getNotifTemplateCode(), req.getCompanyId(), req.getProductId())
				.orElse(-1) + 1;

		// Get previous/latest record
		UploadDocNotifTemplateMaster previous = null;

		if (newAmendId > 0) {
			previous = repo.findByNotifTemplateCodeAndCompanyIdAndProductIdAndAmendId(req.getNotifTemplateCode(),
					req.getCompanyId(), req.getProductId(), newAmendId - 1).orElse(null);
		}

		UploadDocNotifTemplateMaster entity;

		if (previous == null) {

			// =========================
			// NEW RECORD
			// =========================
			entity = UploadDocNotifTemplateMaster.builder().notifTemplateCode(req.getNotifTemplateCode())
					.companyId(req.getCompanyId()).productId(req.getProductId()).amendId(0)

					.notifTemplateName(req.getNotifTemplateName()).toMessengerNo(req.getToMessengerNo())
					.toSmsNo(req.getToSmsNo()).toEmail(req.getToEmail())

					.effectiveDateStart(req.getEffectiveDateStart()).effectiveDateEnd(req.getEffectiveDateEnd())

					.mailRequired(req.getMailRequired()).mailSubject(req.getMailSubject()).mailBody(req.getMailBody())
					.mailRegards(req.getMailRegards())

					.smsRequired(req.getSmsRequired()).smsSubject(req.getSmsSubject()).smsBodyEn(req.getSmsBodyEn())
					.smsRegards(req.getSmsRegards())

					.whatsappRequired(req.getWhatsappRequired()).whatsappSubject(req.getWhatsappSubject())
					.whatsappBodyEn(req.getWhatsappBodyEn()).whatsappRegards(req.getWhatsappRegards())

					.entryDate(now).remarks(req.getRemarks())

					.status(StringUtils.isBlank(req.getStatus()) ? "Y" : req.getStatus())

					.coreAppCode(req.getCoreAppCode()).regulatoryCode(req.getRegulatoryCode())

					.createdBy(req.getCreatedBy()).updatedBy(req.getCreatedBy()).updatedDate(now)

					.build();

		} else {

			// =========================
			// UPDATE / AMENDMENT
			// =========================
			entity = UploadDocNotifTemplateMaster.builder().notifTemplateCode(req.getNotifTemplateCode())
					.companyId(req.getCompanyId()).productId(req.getProductId()).amendId(newAmendId)

					.notifTemplateName(req.getNotifTemplateName() != null ? req.getNotifTemplateName()
							: previous.getNotifTemplateName())

					.toMessengerNo(
							req.getToMessengerNo() != null ? req.getToMessengerNo() : previous.getToMessengerNo())

					.toSmsNo(req.getToSmsNo() != null ? req.getToSmsNo() : previous.getToSmsNo())

					.toEmail(req.getToEmail() != null ? req.getToEmail() : previous.getToEmail())

					.effectiveDateStart(req.getEffectiveDateStart() != null ? req.getEffectiveDateStart()
							: previous.getEffectiveDateStart())

					.effectiveDateEnd(req.getEffectiveDateEnd() != null ? req.getEffectiveDateEnd()
							: previous.getEffectiveDateEnd())

					.mailRequired(req.getMailRequired() != null ? req.getMailRequired() : previous.getMailRequired())

					.mailSubject(req.getMailSubject() != null ? req.getMailSubject() : previous.getMailSubject())

					.mailBody(req.getMailBody() != null ? req.getMailBody() : previous.getMailBody())

					.mailRegards(req.getMailRegards() != null ? req.getMailRegards() : previous.getMailRegards())

					.smsRequired(req.getSmsRequired() != null ? req.getSmsRequired() : previous.getSmsRequired())

					.smsSubject(req.getSmsSubject() != null ? req.getSmsSubject() : previous.getSmsSubject())

					.smsBodyEn(req.getSmsBodyEn() != null ? req.getSmsBodyEn() : previous.getSmsBodyEn())

					.smsRegards(req.getSmsRegards() != null ? req.getSmsRegards() : previous.getSmsRegards())

					.whatsappRequired(req.getWhatsappRequired() != null ? req.getWhatsappRequired()
							: previous.getWhatsappRequired())

					.whatsappSubject(
							req.getWhatsappSubject() != null ? req.getWhatsappSubject() : previous.getWhatsappSubject())

					.whatsappBodyEn(
							req.getWhatsappBodyEn() != null ? req.getWhatsappBodyEn() : previous.getWhatsappBodyEn())

					.whatsappRegards(
							req.getWhatsappRegards() != null ? req.getWhatsappRegards() : previous.getWhatsappRegards())

					.entryDate(previous.getEntryDate())

					.remarks(req.getRemarks() != null ? req.getRemarks() : previous.getRemarks())

					.status(req.getStatus() != null ? req.getStatus() : previous.getStatus())

					.coreAppCode(req.getCoreAppCode() != null ? req.getCoreAppCode() : previous.getCoreAppCode())

					.regulatoryCode(
							req.getRegulatoryCode() != null ? req.getRegulatoryCode() : previous.getRegulatoryCode())

					.createdBy(previous.getCreatedBy())

					.updatedDate(now)

					.build();
		}

		entity = repo.saveAndFlush(entity);

		log.info("UploadDocNotifTemplateMaster saved/amended with AMEND_ID={}: {}", entity.getAmendId(), entity);

		return mapper.toRes(entity);
	}
}
