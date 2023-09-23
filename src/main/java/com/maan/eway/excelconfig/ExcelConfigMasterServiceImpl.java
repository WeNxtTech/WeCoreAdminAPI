package com.maan.eway.excelconfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.maan.eway.excelconfig2.DataType;

@Service
public class ExcelConfigMasterServiceImpl implements ExcelConfigMasterService {

	@Autowired
	UploadTypeRepo repo;

	public List<Errors> validate(UploadTypeSaveReq req) {

		List<Errors> eList = new ArrayList<>();

		if (!StringUtils.isBlank(req.getCompanyId())) {
			if (!NumberUtils.isCreatable(req.getCompanyId())) {
				eList.add(new Errors("1", "Company id", "Company id should be a numarical value"));
			}
		} else {
			eList.add(new Errors("1", "Company id", "Company id is blank"));
		}

		if (!StringUtils.isBlank(req.getProductId())) {
			if (!NumberUtils.isCreatable(req.getProductId())) {
				eList.add(new Errors("2", "Product id", "Product id should be a numerical value"));
			}
		} else {
			eList.add(new Errors("2", "Product id", "Product id is blank"));
		}

		if (!StringUtils.isBlank(req.getSectionId())) {
			if (!NumberUtils.isCreatable(req.getSectionId())) {
				eList.add(new Errors("3", "Section id", "Section id should be a numerical value"));
			}
		} 
//			else
//			eList.add(new Errors("3", "Section Id", "Section Id is blank"));

		if (!StringUtils.isBlank(req.getTypeId())) {
			if (!NumberUtils.isCreatable(req.getTypeId())) {
				eList.add(new Errors("4", "Type id", "Type id should be a numerical value"));
			} else {
				UploadType upload = repo.findBy(req.getCompanyId(), req.getProductId(), req.getTypeId(),
						StringUtils.isBlank(req.getSectionId())?"0":req.getSectionId());
				if (upload == null) {
					eList.add(new Errors("4", "Type id", "Cannot find any record for this given information"));
				}
			}
		}

		if (!StringUtils.isBlank(req.getTypeName())) {
			if (req.getTypeName().length() > 200)
				eList.add(new Errors("5", "Type name", "Type name is too long"));
		} else {
			eList.add(new Errors("5", "Type name", "Type name is blank"));
		}
		

		if (!StringUtils.isBlank(req.getStatus())) {
			if (req.getStatus().length() != 1) {
				eList.add(new Errors("7", "Status", "Status should be 'Y' or 'N'"));
			} else if (!(req.getStatus().equalsIgnoreCase("y") || req.getStatus().equalsIgnoreCase("n"))) {
				eList.add(new Errors("7", "Status", "Status should be 'Y' or 'N'"));
			}
		} else
			eList.add(new Errors("7", "Status", "Status is blank"));

		if (!StringUtils.isBlank(req.getRawTableName())) {
			if (req.getRawTableName().length() > 200)
				eList.add(new Errors("6", "Raw table name", "Raw table name is too long"));
		} else {
			eList.add(new Errors("6", "Raw table name", "Raw table name is blank"));
		}

		if (!StringUtils.isBlank(req.getProductDesc())) {
			if (req.getProductDesc().length() > 200)
				eList.add(new Errors("8", "Product Desc", "Product Desc is too long"));
		} else {
			eList.add(new Errors("8", "Product Desc", "Product Desc is blank"));
		}

		if (!StringUtils.isBlank(req.getFilePath())) {
			if (req.getFilePath().length() > 200)
				eList.add(new Errors("9", "File Path", "File Path is too long"));
		} else {
			eList.add(new Errors("9", "File Path", "File Path is blank"));
		}

		if (!StringUtils.isBlank(req.getApiName())) {
			if (req.getApiName().length() > 300)
				eList.add(new Errors("10", "API name", "API name is too long"));
		} else {
			eList.add(new Errors("10", "API name", "API name is blank"));
		}

		if (!StringUtils.isBlank(req.getRawTableId())) {
			if (req.getApiName().length() > 300)
				eList.add(new Errors("11", "Raw Table id", "Raw Table id is too long"));
		} else {
			eList.add(new Errors("11", "Raw Table id", "Raw Table id is blank"));
		}

		if (!StringUtils.isBlank(req.getIsMainStatus())) {
			if (req.getIsMainStatus().length() != 1) {
				eList.add(new Errors("12", "Is main status", "Is main status Status should be 'Y' or 'N'"));
			} else if (!(req.getIsMainStatus().equalsIgnoreCase("y") || req.getIsMainStatus().equalsIgnoreCase("n"))) {
				eList.add(new Errors("12", "Is main status", "Is main status should be 'Y' or 'N'"));
			}
		} else {
			eList.add(new Errors("12", "Is main status", "Is main status is blank"));
		}

		if (!StringUtils.isBlank(req.getApiMethod())) {
			if (req.getApiMethod().length() > 200)
				eList.add(new Errors("13", "API method", "API method is too long"));
		} else {
			eList.add(new Errors("13", "API method", "API method is blank"));
		}

		return eList;
	}

	public SuccessResponse saveUploadType(UploadTypeSaveReq req) {

		SuccessResponse sRes = new SuccessResponse();

		DozerBeanMapper mapper = new DozerBeanMapper();

		// insert
		if (StringUtils.isBlank(req.getTypeId())) {

			UploadType upload = new UploadType();

			UploadTypePK pk = new UploadTypePK();

			Integer typeId = repo.getLastNo(req.getCompanyId());

			pk.setTypeId(typeId != null ? typeId + 1 : 101);
			pk.setCompanyId(Integer.valueOf(req.getCompanyId()));
			pk.setProductId(Integer.valueOf(req.getProductId()));
			pk.setSectionId(Integer.valueOf(StringUtils.isBlank(req.getSectionId())?"0":req.getSectionId()));
			
			mapper.map(req, upload);

			upload.setPk(pk);

			upload.setTypeName(req.getTypeName().trim());
			upload.setStatus(req.getStatus().toUpperCase().charAt(0));
			upload.setRawTableName(req.getRawTableName());
			upload.setApiName(req.getApiName().trim());
			upload.setProductDesc(req.getProductDesc());
			upload.setFilePath(req.getFilePath().trim());
			upload.setRawTableId(req.getRawTableId());
			upload.setIsMainStatus(req.getIsMainStatus().toUpperCase().charAt(0));
			upload.setApiMethod(req.getApiMethod());

			// UploadType.builder().pk(pk).section_id(Integer.valueOf(req.getSectionid())).typename(req.getTypename()).status(req.getStatus()).build();
			repo.save(upload);

			sRes.setSuccessMessage("Saved Successfully");
			sRes.setSuccessCode(req.getCompanyId());
			return sRes;

		} // update
		else {

			UploadType lastUpload = repo.findBy(req.getCompanyId(), req.getProductId(), req.getTypeId(),
					StringUtils.isBlank(req.getSectionId())?"0":req.getSectionId());

			if (lastUpload != null) {

//				UploadType upload = new UploadType();

//				UploadTypePK pk = new UploadTypePK();

//				pk.setTypeid(Integer.valueOf(req.getTypeid()));
//				pk.setCompany_id(Integer.valueOf(req.getCompanyid()));
//				pk.setProduct_id(Integer.valueOf(req.getProductid()));
				//
//				upload.setPk(pk);

//				lastUpload.setSectionId(Integer.valueOf(StringUtils.isBlank(req.getSectionId())?"0":req.getSectionId()));
				lastUpload.setTypeName(req.getTypeName());
				lastUpload.setStatus(req.getStatus().toUpperCase().charAt(0));
				lastUpload.setRawTableName(req.getRawTableName());
				lastUpload.setApiName(req.getApiName());
				lastUpload.setProductDesc(req.getProductDesc());
				lastUpload.setFilePath(req.getFilePath());
				lastUpload.setRawTableId(req.getRawTableId());
				lastUpload.setIsMainStatus(req.getIsMainStatus().toUpperCase().charAt(0));
				lastUpload.setApiMethod(req.getApiMethod());
				repo.save(lastUpload);

				sRes.setSuccessMessage("Updated Successfully");
				sRes.setSuccessCode(req.getCompanyId());

				return sRes;

			} else
				return null;
		}
	}


	public UploadTypeResponse get(UploadTypeGetReq req) {

			DozerBeanMapper mapper = new DozerBeanMapper();

			UploadType upload = repo.findBy(req.getCompanyId(), req.getProductId(), req.getTypeId(),
					StringUtils.isBlank(req.getSectionId())?"0":req.getSectionId());

			if (upload != null) {

				UploadTypeResponse resp = new UploadTypeResponse();

				resp.setCompanyId(upload.getPk().getCompanyId().toString());
				resp.setProductId(upload.getPk().getProductId().toString());
				resp.setSectionId(upload.getPk().getProductId().toString());
				resp.setTypeId(upload.getPk().getTypeId().toString());

				mapper.map(upload, resp);

				return resp;

			} else
				return null;

		}
	

	public List<UploadTypeResponse> getAll(UploadTypeGetAllReq req) {

		List<UploadType> list = repo.findByCompanyId(req.getCompanyId());
		
		if (list.size() > 0) {
			
			List<UploadTypeResponse> resList = new ArrayList<>();

			DozerBeanMapper mapper = new DozerBeanMapper();

			for (UploadType upload : list) {

				UploadTypeResponse res = new UploadTypeResponse();

				res.setCompanyId(upload.getPk().getCompanyId().toString());
				res.setProductId(upload.getPk().getProductId().toString());
				res.setTypeId(upload.getPk().getTypeId().toString());

				mapper.map(upload, res);

				resList.add(res);
			}
			return resList;
		} else
			return null;
	}
	

	public SuccessResponse deleteUploadType(UploadTypeDeleteReq req) {

		SuccessResponse sRes = new SuccessResponse();

//		UploadTypePK pk = new UploadTypePK();

//		pk.setCompany_id(Integer.valueOf(req.getCompanyid()));
//		pk.setProduct_id(Integer.valueOf(req.getProductid()));
//		pk.setTypeid(Integer.valueOf(req.getTypeid()));

		UploadType upload = repo.findBy(req.getCompanyId(), req.getProductId(), req.getTypeId(), req.getSectionId());

		if (upload != null) {

			repo.delete(upload);

			sRes.setSuccessCode(upload.getPk().getCompanyId().toString());
			sRes.setSuccessMessage("Deleted Successfully");
			return sRes;

		} else {
			return null;
		}

	}



}
