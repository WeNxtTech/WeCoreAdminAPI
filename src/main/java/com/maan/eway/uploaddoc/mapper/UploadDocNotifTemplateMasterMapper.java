package com.maan.eway.uploaddoc.mapper;

import org.springframework.stereotype.Component;

import com.maan.eway.uploaddoc.dto.response.UploadDocNotifTemplateMasterRes;
import com.maan.eway.uploaddoc.entity.UploadDocNotifTemplateMaster;

@Component
public class UploadDocNotifTemplateMasterMapper {

	public UploadDocNotifTemplateMasterRes toRes(UploadDocNotifTemplateMaster entity) {
		if (entity == null) {
			return null;
		}
		UploadDocNotifTemplateMasterRes res = new UploadDocNotifTemplateMasterRes();
		res.setNotifTemplateCode(entity.getNotifTemplateCode());
		res.setNotifTemplateName(entity.getNotifTemplateName());
		res.setToMessengerNo(entity.getToMessengerNo());
		res.setToSmsNo(entity.getToSmsNo());
		res.setToEmail(entity.getToEmail());
		res.setEffectiveDateStart(entity.getEffectiveDateStart());
		res.setEffectiveDateEnd(entity.getEffectiveDateEnd());
		res.setMailRequired(entity.getMailRequired());
		res.setMailSubject(entity.getMailSubject());
		res.setMailBody(entity.getMailBody());
		res.setMailRegards(entity.getMailRegards());
		res.setSmsRequired(entity.getSmsRequired());
		res.setSmsSubject(entity.getSmsSubject());
		res.setSmsBodyEn(entity.getSmsBodyEn());
		res.setSmsRegards(entity.getSmsRegards());
		res.setWhatsappRequired(entity.getWhatsappRequired());
		res.setWhatsappSubject(entity.getWhatsappSubject());
		res.setWhatsappBodyEn(entity.getWhatsappBodyEn());
		res.setWhatsappRegards(entity.getWhatsappRegards());
		res.setEntryDate(entity.getEntryDate());
		res.setRemarks(entity.getRemarks());
		res.setStatus(entity.getStatus());
		res.setCoreAppCode(entity.getCoreAppCode());
		res.setRegulatoryCode(entity.getRegulatoryCode());
		res.setCreatedBy(entity.getCreatedBy());
		res.setUpdatedBy(entity.getUpdatedBy());
		res.setUpdatedDate(entity.getUpdatedDate());
		res.setCompanyId(entity.getCompanyId());
		res.setProductId(entity.getProductId());
		res.setAmendId(entity.getAmendId());
		return res;
	}
}
