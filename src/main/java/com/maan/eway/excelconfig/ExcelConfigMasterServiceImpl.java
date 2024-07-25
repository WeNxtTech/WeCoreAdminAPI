package com.maan.eway.excelconfig;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.batch.entity.EwayUploadTypeMaster;
import com.maan.eway.batch.entity.EwayXlconfigMaster;
import com.maan.eway.batch.repository.EwayUploadTypeMasterRepository;
import com.maan.eway.batch.repository.EwayXlconfigMasterRepository;
import com.maan.eway.res.SuccessRes;

@Service
public class ExcelConfigMasterServiceImpl implements ExcelConfigMasterService {

	@Autowired
	private EwayUploadTypeMasterRepository typeMasterRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private EwayXlconfigMasterRepository xlConfigRepository;

	public List<String> validate(UploadTypeSaveReq req) {

		List<String> eList = new ArrayList<String>();

		if (!StringUtils.isBlank(req.getCompanyId())) {
			if (!NumberUtils.isCreatable(req.getCompanyId())) {
//				eList.add(new Errors("1", "Company id", "Company id should be a numarical value"));
				eList.add("1448");
			}
		} else {
//			eList.add(new Errors("1", "Company id", "Company id is blank"));
			eList.add("1255");
		}

		if (!StringUtils.isBlank(req.getProductId())) {
			if (!NumberUtils.isCreatable(req.getProductId())) {
//				eList.add(new Errors("2", "Product id", "Product id should be a numerical value"));
				eList.add("1496");
			}
		} else {
//			eList.add(new Errors("2", "Product id", "Product id is blank"));
			eList.add("1313");
		}

		if (!StringUtils.isBlank(req.getSectionId())) {
			if (!NumberUtils.isCreatable(req.getSectionId())) {
//				eList.add(new Errors("3", "Section id", "Section id should be a numerical value"));
				eList.add("1551");
			}
		}


		if (!StringUtils.isBlank(req.getTypeId())) {
			if (!NumberUtils.isCreatable(req.getTypeId())) {
//				eList.add(new Errors("4", "Type id", "Type id should be a numerical value"));
				eList.add("1712");
			} else {
				EwayUploadTypeMaster upload = typeMasterRepository.findBy(req.getCompanyId(), req.getProductId(),
						req.getTypeId(), StringUtils.isBlank(req.getSectionId()) ? "0" : req.getSectionId());
				if (upload == null) {
//					eList.add(new Errors("4", "Type id", "You cannot change your Product here"/*"Cannot find any record for this given information"*/));
					eList.add("1974");
				}
			}
		}

		if (!StringUtils.isBlank(req.getTypeName())) {
			if (req.getTypeName().length() > 200)
//				eList.add(new Errors("5", "Sample File Name", "Sample File Name is too long"));
				eList.add("1975");
		} else {
//			eList.add(new Errors("5", "Sample File Name", "Sample File Name is blank"));
			eList.add("1976");
		}

		if (!StringUtils.isBlank(req.getStatus())) {
			if (req.getStatus().length() != 1) {
//				eList.add(new Errors("7", "Status", "Status should be 'Y' or 'N'"));
				eList.add("1265");
			} else if (!(req.getStatus().equalsIgnoreCase("y") || req.getStatus().equalsIgnoreCase("n"))) {
//				eList.add(new Errors("7", "Status", "Status should be 'Y' or 'N'"));
				eList.add("1265");
			}
		} else {
//			eList.add(new Errors("7", "Status", "Status is blank"));
			eList.add("1263");
			}
		if (!StringUtils.isBlank(req.getRawTableName())) {
			if (req.getRawTableName().length() > 200) {
//				eList.add(new Errors("6", "Raw table name", "Raw table name is too long"));
				eList.add("1977"); }
		} else {
//			eList.add(new Errors("6", "Raw table name", "Raw table name is blank"));
			eList.add("1978");
		}

		if (!StringUtils.isBlank(req.getProductDesc())) {
			if (req.getProductDesc().length() > 200)
//				eList.add(new Errors("8", "Product Description", "Product Description is too long"));
				eList.add("1979");
		} else {
//			eList.add(new Errors("8", "Product Description", "Product Description is blank"));
			eList.add("1980");
		}

		if (!StringUtils.isBlank(req.getApiName())) {
			if (req.getApiName().length() > 300) {
//				eList.add(new Errors("10", "API name", "API name is too long"));
				eList.add("1981"); }
		} else {
//			eList.add(new Errors("10", "API name", "API name is blank"));
			eList.add("1982");
		}

		if (!StringUtils.isBlank(req.getRawTableId())) {
			if (req.getApiName().length() > 300) {
//				eList.add(new Errors("11", "Raw Table id", "Raw Table id is too long"));
				eList.add("1983");}
		} else {
//			eList.add(new Errors("11", "Raw Table id", "Raw Table id is blank"));
			eList.add("1984");
		}

		if (!StringUtils.isBlank(req.getIsMainStatus())) {
			if (req.getIsMainStatus().length() != 1) {
//				eList.add(new Errors("12", "Is main status", "Is main status Status should be 'Y' or 'N'"));
				eList.add("1985");
			} else if (!(req.getIsMainStatus().equalsIgnoreCase("y") || req.getIsMainStatus().equalsIgnoreCase("n"))) {
//				eList.add(new Errors("12", "Is main status", "Is main status should be 'Y' or 'N'"));
				eList.add("1986");
			}
		} else {
//			eList.add(new Errors("12", "Is main status", "Is main status is blank"));
			eList.add("1987");
		}

		if (!StringUtils.isBlank(req.getApiMethod())) {
			if (req.getApiMethod().length() > 200) {
//				eList.add(new Errors("13", "API method", "API method is too long"));
				eList.add("1988");}
		} else {
//			eList.add(new Errors("13", "API method", "API method is blank"));
			eList.add("1989");
		}

		return eList;
	}

	public SuccessRes saveUploadType(UploadTypeSaveReq req) {

		SuccessRes sRes = new SuccessRes();

		DozerBeanMapper mapper = new DozerBeanMapper();

		// insert
		if (StringUtils.isBlank(req.getTypeId())) {

			EwayUploadTypeMaster upload = new EwayUploadTypeMaster();

			Integer typeId = typeMasterRepository.getLastNo(req.getCompanyId());

			mapper.map(req, upload);

			upload.setTypeid(typeId != null ? typeId + 1 : 101);
			upload.setCompanyId(Integer.valueOf(req.getCompanyId()));
			upload.setProductId(Integer.valueOf(req.getProductId()));
			upload.setSectionId(Integer.valueOf(StringUtils.isBlank(req.getSectionId()) ? "0" : req.getSectionId()));

			upload.setTypename(req.getTypeName().trim());
			upload.setStatus(req.getStatus().toUpperCase());
			upload.setRawTableName(req.getRawTableName());
			upload.setApiName(req.getApiName().trim());
			upload.setProductDesc(req.getProductDesc());
			upload.setFilePath(null);
			upload.setRawTableId(req.getRawTableId());
			// upload.setIsMainStatus(req.getIsMainStatus().toUpperCase().charAt(0));
			upload.setApiMethod(req.getApiMethod());

			// UploadType.builder().pk(pk).section_id(Integer.valueOf(req.getSectionid())).typename(req.getTypename()).status(req.getStatus()).build();
			typeMasterRepository.save(upload);

			sRes.setResponse("Saved Successfully");
			sRes.setSuccessId(req.getCompanyId());
			return sRes;

		} // update
		else {

			EwayUploadTypeMaster lastUpload = typeMasterRepository.findBy(req.getCompanyId(), req.getProductId(),
					req.getTypeId(), StringUtils.isBlank(req.getSectionId()) ? "0" : req.getSectionId());

			if (lastUpload != null) {

//				UploadType upload = new UploadType();

//				UploadTypePK pk = new UploadTypePK();

//				pk.setTypeid(Integer.valueOf(req.getTypeid()));
//				pk.setCompany_id(Integer.valueOf(req.getCompanyid()));
//				pk.setProduct_id(Integer.valueOf(req.getProductid()));
				//
//				upload.setPk(pk);

//				lastUpload.setSectionId(Integer.valueOf(StringUtils.isBlank(req.getSectionId())?"0":req.getSectionId()));
				lastUpload.setTypename(req.getTypeName().trim());
				lastUpload.setStatus(req.getStatus().toUpperCase());
				lastUpload.setRawTableName(req.getRawTableName());
				lastUpload.setApiName(req.getApiName());
				lastUpload.setProductDesc(req.getProductDesc());
				lastUpload.setFilePath(req.getFilePath());
				lastUpload.setRawTableId(req.getRawTableId());
				// lastUpload.setIsMainStatus(req.getIsMainStatus().toUpperCase().charAt(0));
				lastUpload.setApiMethod(req.getApiMethod());
				typeMasterRepository.save(lastUpload);

				sRes.setResponse("Updated Successfully");
				sRes.setSuccessId(req.getCompanyId());

				return sRes;

			} else
				return null;
		}
	}

	public UploadTypeResponse get(UploadTypeGetReq req) {

		DozerBeanMapper mapper = new DozerBeanMapper();

		EwayUploadTypeMaster upload = typeMasterRepository.findBy(req.getCompanyId(), req.getProductId(),
				req.getTypeId(), StringUtils.isBlank(req.getSectionId()) ? "0" : req.getSectionId());

		if (upload != null) {

			UploadTypeResponse resp = new UploadTypeResponse();

			resp.setTypeName(upload.getTypename());
			resp.setCompanyId(upload.getCompanyId().toString());
			resp.setProductId(upload.getProductId().toString());
			resp.setSectionId(upload.getProductId().toString());
			resp.setTypeId(upload.getTypeid().toString());

			mapper.map(upload, resp);

			return resp;

		} else
			return null;

	}

	public List<UploadTypeResponse> getAll(UploadTypeGetAllReq req) {

		List<EwayUploadTypeMaster> list = typeMasterRepository.findByCompanyIdAndProductId(
				Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()));

		if (list.size() > 0) {

			List<UploadTypeResponse> resList = new ArrayList<>();

			DozerBeanMapper mapper = new DozerBeanMapper();

			for (EwayUploadTypeMaster upload : list) {

				UploadTypeResponse res = new UploadTypeResponse();

				res.setTypeName(upload.getTypename());
				res.setCompanyId(upload.getCompanyId().toString());
				res.setProductId(upload.getProductId().toString());
				res.setTypeId(upload.getTypeid().toString());

				mapper.map(upload, res);

				
				resList.add(res);
			}
			return resList;
		} else
			return null;
	}

	public SuccessResponse deleteUploadType(UploadTypeDeleteReq req) {

		SuccessResponse sRes = new SuccessResponse();

		EwayUploadTypeMaster upload = typeMasterRepository.findBy(req.getCompanyId(), req.getProductId(),
				req.getTypeId(), StringUtils.isBlank(req.getSectionId())?"0":req.getSectionId());

		if (upload != null) {

			List<EwayXlconfigMaster> list = xlConfigRepository.findXLConfigMasterByPk(upload.getCompanyId().toString(),
					upload.getProductId().toString(), upload.getSectionId().toString(), upload.getTypeid().toString());

//			for (EwayXlconfigMaster xl : list) {
//				xlConfigRepository.delete(xl);
//			}

			xlConfigRepository.deleteAll(list);
			
			typeMasterRepository.delete(upload);

			sRes.setSuccessCode(upload.getCompanyId().toString());
			sRes.setSuccessMessage("Deleted Successfully");
			
			return sRes;

		} else {
			return null;
		}

	}

//	public List<EwayUploadTypeMaster> getCriteria() {
//
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//
//		CriteriaQuery<EwayUploadTypeMaster> cq = cb.createQuery(EwayUploadTypeMaster.class);
//
//		Root<EwayUploadTypeMaster> root = cq.from(EwayUploadTypeMaster.class);
//
//		Predicate p = cb.equal(root.get("companyId"), "100002");
//
//		cq.where(p);
//
//		TypedQuery<EwayUploadTypeMaster> t = em.createQuery(cq);
//
//		return t.getResultList();
//	}
//
//	public List<EwayUploadTypeMaster> get2Criteria() {
//
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//
//		CriteriaQuery<EwayUploadTypeMaster> cq = cb.createQuery(EwayUploadTypeMaster.class);
//
//		Root<EwayUploadTypeMaster> root = cq.from(EwayUploadTypeMaster.class);
//
//		Predicate p1 = cb.equal(root.get("productId"), "15");
//
//		Predicate p2 = cb.equal(root.get("sectionId"), "0");
//
//		cq.where(p1, p2);
//
//		TypedQuery<EwayUploadTypeMaster> t = em.createQuery(cq);
//
//		return t.getResultList();
//
//	}

}
