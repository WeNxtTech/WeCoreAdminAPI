package com.maan.eway.excelconfig2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.batch.entity.EwayXlconfigMaster;
import com.maan.eway.batch.repository.EwayXlconfigMasterRepository;
import com.maan.eway.bean.OneTimeTableDetails;
import com.maan.eway.master.req.ColumnNameDropDownlReq;
import com.maan.eway.res.DropDownRes;

import net.bytebuddy.asm.Advice.Return;

@Service
public class XLConfigMasterServiceImpl {

	@Autowired
	EwayXlconfigMasterRepository configRepo;

	public List<Errors> validate(List<XLConfigMasterSaveReq> xlConfigList) {

		List<Errors> eList = new ArrayList<>();

		Integer code = 1;

		for (int i = 0; i < xlConfigList.size(); i++) {

			for (int j = i + 1; j < xlConfigList.size(); j++) {

				String eCode = String.valueOf(j + 1);

				if (xlConfigList.get(i).getFieldId() != null && xlConfigList.get(j).getFieldId() != null
						&& !xlConfigList.get(i).getFieldId().equalsIgnoreCase("99999")) {

					if (xlConfigList.get(i).getFieldId().trim()
							.equalsIgnoreCase(xlConfigList.get(j).getFieldId().trim())) {
						xlConfigList.get(j).setFieldId("99999");
						eList.add(new Errors(eCode, "Field Id", "Field Id is already exist"));
					}
				}

				if (xlConfigList.get(i).getExcelHeaderName() != null && xlConfigList.get(j).getExcelHeaderName() != null
						&& !xlConfigList.get(i).getExcelHeaderName().equalsIgnoreCase("zzzzzz")) {
					if (xlConfigList.get(i).getExcelHeaderName().trim()
							.equalsIgnoreCase(xlConfigList.get(j).getExcelHeaderName().trim())) {
						xlConfigList.get(j).setExcelHeaderName("zzzzzz");
						eList.add(new Errors(eCode, "ExcelHeader Name", "ExcelHeader Name is already present"));
					}
				}

			}

		}

		for (XLConfigMasterSaveReq req : xlConfigList) {

			if (!StringUtils.isBlank(req.getCompanyId())) {
				if (!NumberUtils.isCreatable(req.getCompanyId())) {
					eList.add(new Errors(code.toString(), "Company id",
							code.toString() + " Company id should be a numarical value"));
				}
			} else {
				eList.add(new Errors(code.toString(), "Company id", code.toString() + " Company id is blank"));
			}

			if (!StringUtils.isBlank(req.getProductId())) {
				if (!NumberUtils.isCreatable(req.getProductId())) {
					eList.add(new Errors(code.toString(), "Product id",
							code.toString() + " Product id should be a numerical value"));
				}
			} else {
				eList.add(new Errors(code.toString(), "Product id", code.toString() + " Product id is blank"));
			}

			if (!StringUtils.isBlank(req.getSectionId())) {
				if (!NumberUtils.isCreatable(req.getSectionId())) {
					eList.add(new Errors(code.toString(), "Section id",
							code.toString() + " Section id should be a numerical value"));
				}
			}
			// else
			// eList.add(new Errors("3", "Section Id", "Section Id is blank"));

			if (!StringUtils.isBlank(req.getTypeId())) {
				if (!NumberUtils.isCreatable(req.getTypeId()))
					eList.add(new Errors(code.toString(), "Type id",
							code.toString() + " Type id should be a numerical value"));
			} else {
				eList.add(new Errors(code.toString(), "Type id", code.toString() + " Type id is blank"));

//						if(upload==null) {
//							eList.add(new Errors("4", "Type id", "Cannot find any record for this given information"));
//						}
			}

			if (!StringUtils.isBlank(req.getFieldId())) {
				if (!NumberUtils.isCreatable(req.getFieldId())) {
					eList.add(new Errors(code.toString(), "Field Id",
							code.toString() + " Field Id should be a numerical value"));
				}
//				else {
//					EwayXlconfigMaster xlConfig = configRepo.findByPK(req.getCompanyId(), req.getProductId(),
//							req.getTypeId(), req.getFieldId());
//					if (xlConfig != null) {
//						eList.add(new Errors(code.toString(), "Field Id", "Field Id already exists"));
//					}
//				}
			} else
				eList.add(new Errors("5", "Field Id", code.toString() + " Field Id is blank"));

//				if (!StringUtils.isBlank(req.getDateFormat())) {
//					if (req.getDateFormat().length() > 200)
//						eList.add(new Errors("9", "Date Format", "Date Format is too long"));
//				} else {
//					eList.add(new Errors("9", "Date Format", "Date Format is blank"));
//				}
			//
			if (!StringUtils.isBlank(req.getStatus())) {
				if (req.getStatus().length() != 1) {
					eList.add(new Errors("10", "Status", code.toString() + " Status should be 'Y' or 'N'"));
				} else if (!(req.getStatus().equalsIgnoreCase("y") || req.getStatus().equalsIgnoreCase("n"))) {
					eList.add(new Errors("10", "Status", code.toString() + " Status should be 'Y' or 'N'"));
				} else if (req.getStatus().equalsIgnoreCase("y")) {

					if (!StringUtils.isBlank(req.getExcelHeaderName())) {
						if (req.getExcelHeaderName().length() > 200)
							eList.add(new Errors(code.toString(), "Excel Header Name",
									code.toString() + " Excel Header Name is too long"));
					} else {
						eList.add(new Errors(code.toString(), "Excel Header Name",
								code.toString() + " Excel Header Name is blank"));
					}

					if (!StringUtils.isBlank(req.getFieldNameRaw())) {
						if (req.getFieldNameRaw().length() > 200)
							eList.add(new Errors(code.toString(), "RawColumnName",
									code.toString() + " RawColumnName is too long"));
					} else {
						eList.add(new Errors(code.toString(), "RawColumnName",
								code.toString() + " RawColumnName is blank"));
					}

					if (!StringUtils.isBlank(req.getMandatoryYn())) {
						if (req.getMandatoryYn().length() != 1) {
							eList.add(new Errors(code.toString(), "Mandatory YN",
									code.toString() + " Mandatory YN should be 'Y' or 'N'"));
						} else if (!(req.getMandatoryYn().equalsIgnoreCase("y")
								|| req.getMandatoryYn().equalsIgnoreCase("n"))) {
							eList.add(new Errors(code.toString(), "Mandatory YN",
									code.toString() + " Mandatory YN should be 'Y' or 'N'"));
						}
					} else
						eList.add(new Errors(code.toString(), "Mandatory YN",
								code.toString() + " Mandatory YN is blank"));

					if (!StringUtils.isBlank(req.getDataType())) {
						if (req.getDataType().length() > 200)
							eList.add(new Errors(code.toString(), "Data Type",
									code.toString() + " Data Type is too long"));
					} else {
						eList.add(new Errors(code.toString(), "Data Type", code.toString() + " Data Type is blank"));
					}

					if (!StringUtils.isBlank(req.getFieldLength())) {
						if (!NumberUtils.isCreatable(req.getFieldLength())) {
							eList.add(new Errors(code.toString(), "Field Length",
									code.toString() + " Field Length should be a numerical value"));
						}
					} else
						eList.add(new Errors(code.toString(), "Field Length",
								code.toString() + " Field Length is blank"));

					if (!StringUtils.isBlank(req.getDataRange())) {
						if (req.getDataRange().length() > 200)
							eList.add(new Errors(code.toString(), "Data Range",
									code.toString() + " Data Range is too long"));
					} else {
						eList.add(new Errors(code.toString(), "Data Range ", code.toString() + " Data Range is blank"));
					}

					// ISOBJECTYN
					if (!StringUtils.isBlank(req.getIsObject())) {
						if (req.getIsObject().length() != 1) {
							eList.add(new Errors(code.toString(), "Is Object",
									code.toString() + " Is Object should be 'Y' or 'N'"));
						} else if (!(req.getIsObject().equalsIgnoreCase("y") || req.getIsObject().equalsIgnoreCase("n")
								|| req.getIsObject().equalsIgnoreCase("l"))) {
							eList.add(new Errors(code.toString(), "Is Object",
									code.toString() + " Is Object should be 'Y' or 'N' or 'L'"));
						} else if (req.getIsObject().equalsIgnoreCase("y") || req.getIsObject().equalsIgnoreCase("l")) {

							// ObjectJsonKey
							if (!StringUtils.isBlank(req.getObjApijsonKey())) {
								if (req.getObjApijsonKey().length() > 200)
									eList.add(new Errors(code.toString(), "ObjectJsonKey",
											code.toString() + " ObjectJsonKey is too long"));
							} else {
								eList.add(new Errors(code.toString(), "ObjectJsonKey",
										code.toString() + " ObjectJsonKey is blank"));
							}

							// ObjectTableColumn
							if (!StringUtils.isBlank(req.getObjSelcolKey())) {
								if (req.getObjSelcolKey().length() > 200)
									eList.add(new Errors(code.toString(), "ObjectTableColumn",
											code.toString() + " ObjectTableColumn is too long"));
							} else {
								eList.add(new Errors(code.toString(), "ObjectTableColumn",
										code.toString() + " ObjectTableColumn is blank"));
							}

							// ObjectDefaultCol
							if (!StringUtils.isBlank(req.getObjDefaulVal())) {
								if (req.getObjDefaulVal().length() != 1) {
									eList.add(new Errors(code.toString(), "ObjectDefaultCol",
											code.toString() + " ObjectDefaultCol should be one character"));
								} else if (!(req.getObjDefaulVal().equalsIgnoreCase("y")
										|| req.getObjDefaulVal().equalsIgnoreCase("n"))) {
									eList.add(new Errors(code.toString(), "ObjectDefaultCol",
											code.toString() + " ObjectDefaultCol should be 'Y' or 'N'"));
								}
							} else {
								eList.add(new Errors(code.toString(), "ObjectDefaultCol",
										code.toString() + " ObjectDefaultCol is blank"));
							}
						}
					} else
						eList.add(new Errors(code.toString(), "Is Object", code.toString() + " Is Object is blank"));

					// ISARRAYYN
					if (!StringUtils.isBlank(req.getIsArray())) {
						if (req.getIsArray().length() != 1) {
							eList.add(new Errors(code.toString(), "Is Array",
									code.toString() + " Is Array should be 'Y' or 'N'"));
						} else if (!(req.getIsArray().equalsIgnoreCase("y")
								|| req.getIsArray().equalsIgnoreCase("n"))) {
							eList.add(new Errors(code.toString(), "Is Array",
									code.toString() + " Is Array should be 'Y' or 'N'"));
						} else if (req.getIsArray().equalsIgnoreCase("y")) {

							// ArrayJsonKey
							if (!StringUtils.isBlank(req.getApiJsonKey())) {
								if (req.getApiJsonKey().length() > 200)
									eList.add(new Errors(code.toString(), "ArrayJsonKey",
											code.toString() + " ArrayJsonKey is too long"));
							} else {
								eList.add(new Errors(code.toString(), "ArrayJsonKey",
										code.toString() + " ArrayJsonKey is blank"));
							}

							// ArrayTableColumn
							if (!StringUtils.isBlank(req.getSelColName())) {
								if (req.getSelColName().length() > 200)
									eList.add(new Errors(code.toString(), "ArrayTableColumn",
											code.toString() + " ArrayTableColumn is too long"));
							} else {
								eList.add(new Errors(code.toString(), "ArrayTableColumn",
										code.toString() + " ArrayTableColumn is blank"));
							}

							// ArrayDefaultVal
							if (!StringUtils.isBlank(req.getIsMainDefauVal())) {
								if (req.getIsMainDefauVal().length() != 1) {
									eList.add(new Errors(code.toString(), "ArrayDefaultVal",
											code.toString() + " ArrayDefaultVal should be 'Y' or 'N'"));
								} else if (!(req.getIsMainDefauVal().equalsIgnoreCase("y")
										|| req.getIsMainDefauVal().equalsIgnoreCase("n"))) {
									eList.add(new Errors(code.toString(), "ArrayDefaultVal",
											code.toString() + " ArrayDefaultVal should be 'Y' or 'N'"));
								}
							} else
								eList.add(new Errors(code.toString(), "ArrayDefaultVal",
										code.toString() + " IsMainDefauVal is blank"));
						}

					} else
						eList.add(new Errors(code.toString(), "Is Array", code.toString() + " Is Array is blank"));

				} else if (req.getStatus().equalsIgnoreCase("n")) {

					if (!StringUtils.isBlank(req.getExcelHeaderName())) {
						if (req.getExcelHeaderName().length() > 200)
							eList.add(new Errors(code.toString(), "Excel Header Name",
									code.toString() + " Excel Header Name is too long"));
					}

					if (!StringUtils.isBlank(req.getFieldNameRaw())) {
						if (req.getFieldNameRaw().length() > 200)
							eList.add(new Errors(code.toString(), "RawColumnName",
									code.toString() + " RawColumnName is too long"));
					}

					if (!StringUtils.isBlank(req.getMandatoryYn())) {
						if (req.getMandatoryYn().length() != 1) {
							eList.add(new Errors(code.toString(), "Mandatory YN",
									code.toString() + " Mandatory YN should be 'Y' or 'N'"));
						} else if (!(req.getMandatoryYn().equalsIgnoreCase("y")
								|| req.getMandatoryYn().equalsIgnoreCase("n"))) {
							eList.add(new Errors(code.toString(), "Mandatory YN",
									code.toString() + " Mandatory YN should be 'Y' or 'N'"));
						}
					}

					if (!StringUtils.isBlank(req.getDataType())) {
						if (req.getDataType().length() > 200)
							eList.add(new Errors(code.toString(), "Data Type",
									code.toString() + " Data Type is too long"));
					}

					if (!StringUtils.isBlank(req.getFieldLength())) {
						if (!NumberUtils.isCreatable(req.getFieldLength())) {
							eList.add(new Errors(code.toString(), "Field Length",
									code.toString() + " Field Length should be a numerical value"));
						}
					}

					if (!StringUtils.isBlank(req.getDataRange())) {
						if (req.getDataRange().length() > 200)
							eList.add(new Errors(code.toString(), "Data Range",
									code.toString() + " Data Range is too long"));
					}

					// ISOBJECTYN
					if (!StringUtils.isBlank(req.getIsObject())) {
						if (req.getIsObject().length() != 1) {
							eList.add(new Errors(code.toString(), "Is Object",
									code.toString() + " Is Object should be 'Y' or 'N'"));
						} else if (!(req.getIsObject().equalsIgnoreCase("y") || req.getIsObject().equalsIgnoreCase("n")
								|| req.getIsObject().equalsIgnoreCase("l"))) {
							eList.add(new Errors(code.toString(), "Is Object",
									code.toString() + " Is Object should be 'Y' or 'N' or 'L'"));
						} else if (req.getIsObject().equalsIgnoreCase("y") || req.getIsObject().equalsIgnoreCase("l")) {

						}
					}

					// ObjectJsonKey
					if (!StringUtils.isBlank(req.getObjApijsonKey())) {
						if (req.getObjApijsonKey().length() > 200)
							eList.add(new Errors(code.toString(), "ObjectJsonKey",
									code.toString() + " ObjectJsonKey is too long"));
					}

					// ObjectTableColumn
					if (!StringUtils.isBlank(req.getObjSelcolKey())) {
						if (req.getObjSelcolKey().length() > 200)
							eList.add(new Errors(code.toString(), "ObjectTableColumn",
									code.toString() + " ObjectTableColumn is too long"));
					}

					// ObjectDefaultCol
					if (!StringUtils.isBlank(req.getObjDefaulVal())) {
						if (req.getObjDefaulVal().length() != 1) {
							eList.add(new Errors(code.toString(), "ObjectDefaultCol",
									code.toString() + " ObjectDefaultCol should be one character"));
						} else if (!(req.getObjDefaulVal().equalsIgnoreCase("y")
								|| req.getObjDefaulVal().equalsIgnoreCase("n"))) {
							eList.add(new Errors(code.toString(), "ObjectDefaultCol",
									code.toString() + " ObjectDefaultCol should be 'Y' or 'N'"));
						}
					}

					// ISARRAYYN
					if (!StringUtils.isBlank(req.getIsArray())) {
						if (req.getIsArray().length() != 1) {
							eList.add(new Errors(code.toString(), "Is Array",
									code.toString() + " Is Array should be 'Y' or 'N'"));
						} else if (!(req.getIsArray().equalsIgnoreCase("y")
								|| req.getIsArray().equalsIgnoreCase("n"))) {
							eList.add(new Errors(code.toString(), "Is Array",
									code.toString() + " Is Array should be 'Y' or 'N'"));
						} else if (req.getIsArray().equalsIgnoreCase("y")) {

						}

					}

					// ArrayJsonKey
					if (!StringUtils.isBlank(req.getApiJsonKey())) {
						if (req.getApiJsonKey().length() > 200)
							eList.add(new Errors(code.toString(), "ArrayJsonKey",
									code.toString() + " ArrayJsonKey is too long"));
					}

					// ArrayTableColumn
					if (!StringUtils.isBlank(req.getSelColName())) {
						if (req.getSelColName().length() > 200)
							eList.add(new Errors(code.toString(), "ArrayTableColumn",
									code.toString() + " ArrayTableColumn is too long"));
					}

					// ArrayDefaultVal
					if (!StringUtils.isBlank(req.getIsMainDefauVal())) {
						if (req.getIsMainDefauVal().length() != 1) {
							eList.add(new Errors(code.toString(), "ArrayDefaultVal",
									code.toString() + " ArrayDefaultVal should be 'Y' or 'N'"));
						} else if (!(req.getIsMainDefauVal().equalsIgnoreCase("y")
								|| req.getIsMainDefauVal().equalsIgnoreCase("n"))) {
							eList.add(new Errors(code.toString(), "ArrayDefaultVal",
									code.toString() + " ArrayDefaultVal should be 'Y' or 'N'"));
						}
					}

				}
			} else
				eList.add(new Errors("10", "Status", code.toString() + " Status is blank"));
			//
//				if (!StringUtils.isBlank(req.getExcelColumnIndex())) {
//					if (!NumberUtils.isCreatable(req.getExcelColumnIndex())) {
//						eList.add(new Errors("11", "Excel Column Index", "Excel Column Index should be a numerical value"));
//					}
//				} else
//					eList.add(new Errors("11", "Excel Column Index", "Excel Column Index is blank"));

//				if (!StringUtils.isBlank(req.getFieldNameMain())) {
//					if (req.getFieldNameMain().length() > 200)
//						eList.add(new Errors("13", "Field Name Main", "Field Name Main is too long"));
//				} else {
//					eList.add(new Errors("13", "Field Name Main", "Field Name Main is blank"));
//				}
			//
//				if (!StringUtils.isBlank(req.getFieldNameError())) {
//					if (req.getFieldNameError().length() > 200)
//						eList.add(new Errors("14", "Field Name Error", "Field Name Error is too long"));
//				} else {
//					eList.add(new Errors("14", "Field Name Error", "Field Name Error is blank"));
//				}
			//
//				if (!StringUtils.isBlank(req.getExcelColumnYn())) {
//					if (req.getExcelColumnYn().length() > 200)
//						eList.add(new Errors("15", "Excel Column Yn", "Excel Column Yn is too long"));
//				} else {
//					eList.add(new Errors("15", "Excel Column Yn", "Excel Column Yn is blank"));
//				}
			//
//				if (!StringUtils.isBlank(req.getDublicateCheck())) {
//					if (req.getDublicateCheck().length() > 200)
//						eList.add(new Errors("16", "Duplicate Check", "Duplicate Check is too long"));
//				} else {
//					eList.add(new Errors("16", "Duplicate Check", "Duplicate Check is blank"));
//				}

//				if (!StringUtils.isBlank(req.getMasterCheck())) {
//					if (req.getMasterCheck().length() > 200)
//						eList.add(new Errors("18", "Master Check", "Master Check is too long"));
//				} else {
//					eList.add(new Errors("18", "Master Check", "Master Check is blank"));
//				}
			//
//				if (!StringUtils.isBlank(req.getMasterCheckField())) {
//					if (req.getMasterCheckField().length() > 200)
//						eList.add(new Errors("19", "Master Check Field", "Master Check Field is too long"));
//				} else {
//					eList.add(new Errors("19", "Master Check Field", "Master Check Field is blank"));
//				}

//				if (!StringUtils.isBlank(req.getIsMainColIdx())) {
//					if (!NumberUtils.isCreatable(req.getIsMainColIdx())) {
//						eList.add(new Errors("24", "IsMainColIdx", "IsMainColIdx should be a numerical value"));
//					}
//				} else
//					eList.add(new Errors("24", "IsMainColIdx", "IsMainColIdx is blank"));

			code++;
		}

		return eList;
		// return null;

	}

	public SuccessResponse saveXLConfig(XLConfigMasterSaveReq req) {

		DozerBeanMapper mapper = new DozerBeanMapper();

//		EwayXlconfigMaster xlConfig = new EwayXlconfigMaster();

		SuccessResponse sRes = new SuccessResponse();

		// save
//		if (StringUtils.isBlank(req.getFieldId())) {
//
//			Integer maxOfFieldId = configRepo.findMaxOfFieldId(req.getCompanyId(), req.getProductId(), req.getTypeId());

//		mapper.map(req, pk);

//			pk.setFieldId(maxOfFieldId + 1);

//		pk.setSectionId(Integer.valueOf(StringUtils.isBlank(req.getSectionId()) ? "0" : req.getSectionId()));
//
//		xlConfig.setPk(pk);

//		mapper.map(req, xlConfig);

//		xlConfig.setSectionId(Integer.valueOf(req.getSectionId()));

//		xlConfig.setSectionId();

//		xlConfig.setTypeid(Integer.valueOf(req.getTypeId()));

//		xlConfig.setFieldid(Integer.valueOf(req.getFieldId()));

//		xlConfig.setExcelColumnIndex(Integer.valueOf(req.getFieldId()));
//
//		xlConfig.setIsMainColIdx();

		EwayXlconfigMaster xlConfig = EwayXlconfigMaster.builder().companyId(Integer.valueOf(req.getCompanyId()))
				.productId(Integer.valueOf(req.getProductId()))
				.sectionId(Integer.valueOf(StringUtils.isBlank(req.getSectionId()) ? "0" : req.getSectionId()))
				.typeid(Integer.valueOf(req.getTypeId())).fieldid(Integer.valueOf(req.getFieldId().trim()))
				.excelColumnIndex(
						StringUtils.isBlank(req.getFieldId()) ? null : Integer.valueOf(req.getFieldId().trim()))
				.isMainColIdx(StringUtils.isBlank(req.getFieldId()) ? null : Integer.valueOf(req.getFieldId().trim()))
				.excelheaderName(StringUtils.isBlank(req.getExcelHeaderName()) ? null : req.getExcelHeaderName().trim())
				.mandatoryyn(StringUtils.isBlank(req.getMandatoryYn()) ? "N" : req.getMandatoryYn())
				.dataType(StringUtils.isBlank(req.getDataType()) ? null : req.getDataType())
				.status(StringUtils.isBlank(req.getStatus()) ? "N" : req.getStatus())
				.fieldNameRaw(StringUtils.isBlank(req.getFieldNameRaw()) ? null : req.getFieldNameRaw())
				.fieldLength(
						StringUtils.isBlank(req.getFieldLength()) ? null : Integer.valueOf(req.getFieldLength().trim()))
				.dataRange(StringUtils.isBlank(req.getDataRange()) ? null : req.getDataRange().trim())
				.isMainDefauVal(StringUtils.isBlank(req.getIsMainDefauVal()) ? "N" : req.getIsMainDefauVal())
				.apiJsonKey(StringUtils.isBlank(req.getApiJsonKey()) ? null : req.getApiJsonKey().trim())
				.selColName(StringUtils.isBlank(req.getSelColName()) ? null : req.getSelColName())
				.isObject(StringUtils.isBlank(req.getIsObject()) ? "N" : req.getIsObject())
				.isArray(StringUtils.isBlank(req.getIsArray()) ? "N" : req.getIsArray())
				.objApijsonKey(StringUtils.isBlank(req.getObjApijsonKey()) ? null : req.getObjApijsonKey().trim())
				.objSelcolKey(StringUtils.isBlank(req.getObjSelcolKey()) ? null : req.getObjSelcolKey())
				.objDefaulVal(StringUtils.isBlank(req.getObjDefaulVal()) ? "N" : req.getObjDefaulVal()).build();

		configRepo.save(xlConfig);

		sRes.setSuccessCode(req.getCompanyId());

		sRes.setSuccessMessage("Inserted Successfully");

		return sRes;
//		} else {
//
//			XLConfigMaster config = configRepo.findXLConfigMasterByPk(req.getCompanyId(), req.getProductId(),
//					req.getTypeId(), req.getFieldId());
//
//			// mapper.map(req, pk);
//
////			xlConfig.setPk(pk);
//
//			mapper.map(req, config);
//
//			configRepo.save(config);
//
//			sRes.setSuccessCode(req.getCompanyId());
//
//			sRes.setSuccessMessage("Updated Successfully");
//
//			return sRes;
//		}

	}

//	public List<XLConfigMasterSaveReq> getList(List<Map<String, Object>> reqList) {
//
//		List<XLConfigMasterSaveReq> xlConfigList = new ArrayList<>();
//
//		if (reqList.size() > 0) {
//
//			for (Map<String, Object> map : reqList) {
//
//				XLConfigMasterSaveReq save = new XLConfigMasterSaveReq();
//
//				save.setCompanyId((String) map.get("CompanyId"));
//				save.setProductId((String) map.get("ProductId"));
//				save.setSectionId((String) map.get("SectionId"));
//				save.setTypeId((String) map.get("TypeId"));
//				save.setFieldId((String) map.get("FieldId"));
//				save.setExcelHeaderName((String) map.get("ExcelHeaderName"));
//				save.setMandatoryYn((String) map.get("MandatoryYn"));
//				save.setDataType((String) map.get("DataType"));
//				save.setFieldNameRaw((String) map.get("FieldNameRaw"));
//				save.setFieldLength((String) map.get("FieldLength"));
//				save.setDataRange((String) map.get("DataRange"));
//				save.setIsMainDefauVal((String) map.get("IsMainDefauVal"));
//				save.setApiJsonKey((String) map.get("ApiJsonKey"));
//				save.setSelColName((String) map.get("SelColName"));
//				save.setIsObject((String) map.get("IsObject"));
//				save.setIsArray((String) map.get("IsArray"));
//				save.setObjApijsonKey((String) map.get("ObjApijsonKey"));
//				save.setObjSelcolKey((String) map.get("ObjSelcolKey"));
//				save.setObjDefaulVal((String) map.get("ObjDefaulVal"));
//
//				xlConfigList.add(save);
//			}
//			return xlConfigList;
//		} else
//			return null;
//	}

//	public List<List<Errors>> validateList(List<XLConfigMasterSaveReq> xlConfigList) {
//
//		List<List<Errors>> massError = new ArrayList<>();
//
//		for (XLConfigMasterSaveReq req : xlConfigList) {
//			List<Errors> elist = validate(req);
//			massError.add(elist);
//		}
//
//		return massError;
//	}

	public SuccessResponse saveList(List<XLConfigMasterSaveReq> xlConfigList) {

		SuccessResponse sRes = new SuccessResponse();

		List<EwayXlconfigMaster> lastData = configRepo.findXLConfigMasterByPk(xlConfigList.get(0).getCompanyId(),
				xlConfigList.get(0).getProductId(),xlConfigList.get(0).getSectionId(), xlConfigList.get(0).getTypeId());

		if (lastData.size() > 0) {
			for (EwayXlconfigMaster xl : lastData) {
				configRepo.delete(xl);
			}
		}

		for (XLConfigMasterSaveReq save : xlConfigList) {
			sRes = saveXLConfig(save);
		}

		return sRes;
	}

	public XLConfigMasterResponse getByPK(XLConfigGetReq req) {

		EwayXlconfigMaster xlConfig = configRepo.findByPK(req.getCompanyId(), req.getProductId(), req.getTypeId(),
				req.getFieldId());

		XLConfigMasterResponse resp = new XLConfigMasterResponse();

		DozerBeanMapper mapper = new DozerBeanMapper();

		if (xlConfig != null) {

//			XLConfigMasterPK pk = new XLConfigMasterPK();

//			mapper.map(xlConfig.getPk(), pk);

			mapper.map(xlConfig, resp);

			resp.setCompanyId(xlConfig.getCompanyId().toString());
			resp.setProductId(xlConfig.getProductId().toString());
			resp.setSectionId(xlConfig.getSectionId().toString());
			resp.setTypeId(xlConfig.getTypeid().toString());
			resp.setFieldId(xlConfig.getFieldid().toString());

			return resp;
		} else
			return null;
	}

	public List<XLConfigMasterResponse> getAll(XLConfigGetReq req) {

		List<EwayXlconfigMaster> xlConfigList = configRepo.findXLConfigMasterByPk(req.getCompanyId(),
				req.getProductId(), req.getSectionId(),req.getTypeId());

		List<XLConfigMasterResponse> respList = new ArrayList<XLConfigMasterResponse>();

		DozerBeanMapper mapper = new DozerBeanMapper();

		if (xlConfigList.size() != 0) {

			for (EwayXlconfigMaster xlConfig : xlConfigList) {

				XLConfigMasterResponse resp = new XLConfigMasterResponse();

//				XLConfigMasterPK pk = new XLConfigMasterPK();

//				mapper.map(xlConfig.getPk(), pk);

//				mapper.map(xlConfig, resp);

				resp.setExcelHeaderName(
						StringUtils.isBlank(xlConfig.getExcelheaderName()) ? "" : xlConfig.getExcelheaderName());
				resp.setMandatoryYn(StringUtils.isBlank(xlConfig.getMandatoryyn()) ? "" : xlConfig.getMandatoryyn());
				resp.setDataType(StringUtils.isBlank(xlConfig.getDataType()) ? "" : xlConfig.getDataType());
				resp.setStatus(StringUtils.isBlank(xlConfig.getStatus()) ? "" : xlConfig.getStatus());
				resp.setFieldNameRaw(StringUtils.isBlank(xlConfig.getFieldNameRaw()) ? "" : xlConfig.getFieldNameRaw());
				resp.setFieldLength((xlConfig.getFieldLength() == null) ? "" : xlConfig.getFieldLength().toString());
				resp.setDataRange(StringUtils.isBlank(xlConfig.getDataRange()) ? "" : xlConfig.getDataRange());
				resp.setIsMainDefauVal(
						StringUtils.isBlank(xlConfig.getIsMainDefauVal()) ? "" : xlConfig.getIsMainDefauVal());
				resp.setApiJsonKey(StringUtils.isBlank(xlConfig.getApiJsonKey()) ? "" : xlConfig.getApiJsonKey());
				resp.setSelColName(StringUtils.isBlank(xlConfig.getSelColName()) ? "" : xlConfig.getSelColName());
				resp.setIsObject(StringUtils.isBlank(xlConfig.getIsObject()) ? "" : xlConfig.getIsObject());
				resp.setIsArray(StringUtils.isBlank(xlConfig.getIsArray()) ? "" : xlConfig.getIsArray());
				resp.setObjApijsonKey(
						StringUtils.isBlank(xlConfig.getObjApijsonKey()) ? "" : xlConfig.getObjApijsonKey());
				resp.setObjSelcolKey(StringUtils.isBlank(xlConfig.getObjSelcolKey()) ? "" : xlConfig.getObjSelcolKey());
				resp.setObjDefaulVal(StringUtils.isBlank(xlConfig.getObjDefaulVal()) ? "" : xlConfig.getObjDefaulVal());

				resp.setCompanyId(xlConfig.getCompanyId().toString());
				resp.setProductId(xlConfig.getProductId().toString());
				resp.setSectionId(xlConfig.getSectionId().toString());
				resp.setTypeId(xlConfig.getTypeid().toString());
				resp.setFieldId(xlConfig.getFieldid().toString());

				respList.add(resp);
			}

			return respList;
		} else
			return null;
	}

	public List<DataType> getDataType() {

		List<Map<String, String>> list = configRepo.getDataType();

		List<DataType> dataTypeList = new ArrayList<>();

		if (list.size() != 0) {

			for (Map<String, String> map : list) {

				DataType dataType = new DataType();
				dataType.setItemCode(map.get("item_code"));
				dataType.setItemValue(map.get("item_value"));

				dataTypeList.add(dataType);
			}

			return dataTypeList;
		}
		return null;
	}

	public List<DropDownRes> columnName(ColumnNameDropDownlReq req) {
		List<DropDownRes> resList = new ArrayList<DropDownRes>();
		try {
			List<OneTimeTableDetails> getList = configRepo.findColumn(Integer.valueOf(req.getItemId()), "Y");
			for (OneTimeTableDetails data : getList) {
				if (!data.getItemType().equalsIgnoreCase("ONE_TIME_TABLE")) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getItemCode());
					res.setCodeDesc(data.getDisplayName());
					res.setStatus(data.getStatus());
					resList.add(res);
				}
			}
		} catch (Exception e) {

			return null;
		}
		return resList;
	}

	public SuccessResponse deleteByPk(XLConfigGetReq req) {

		EwayXlconfigMaster xlConfig = configRepo.findByPK(req.getCompanyId(), req.getProductId(), req.getTypeId(),
				req.getFieldId());

		SuccessResponse sRes = new SuccessResponse();

		if (xlConfig != null) {

			sRes.setSuccessCode(xlConfig.getFieldid().toString());

			configRepo.delete(xlConfig);

			sRes.setSuccessMessage("Deleted");

			return sRes;

		} else {

			return null;
		}

	}

	public List<Errors> validateConfigMaster(List<XLConfigMasterSaveReq> req) {
		List<Errors> error =new ArrayList<>();
		try {
	
				
				for(int i=0;i<req.size();i++) {
					
					
					XLConfigMasterSaveReq xl =req.get(i);
					
					if("Y".equalsIgnoreCase(xl.getStatus())) {
					
						if(StringUtils.isBlank(xl.getFieldId())) {
							error.add(new Errors(""+i+"","FieldId","Please enter FieldId"));
						}else if(!StringUtils.isNumeric(xl.getFieldId())) {
							error.add(new Errors(""+i+"","FieldId","FieldId does allow only numeric digits"));
	
						}
						
						if(StringUtils.isBlank(xl.getExcelHeaderName())) {
							error.add(new Errors(""+i+"","ExcelHeaderName","Please enter ExcelHeaderName"));
						}if(StringUtils.isBlank(xl.getMandatoryYn())) {
							error.add(new Errors(""+i+"","MandatoryYn","Please enter MandatoryYn"));
						}if(StringUtils.isBlank(xl.getDataType())) {
							error.add(new Errors(""+i+"","DataType","Please enter DataType"));
						}if(StringUtils.isBlank(xl.getFieldNameRaw())) {
							error.add(new Errors(""+i+"","FieldNameRaw","Please enter FieldNameRaw"));
						}if(StringUtils.isNotBlank(xl.getDataRange())) {
							
							if(!xl.getDataRange().matches("[0-9]+-[0-9]+")) {
								error.add(new Errors(""+i+"","DataRange","DataRange format should be like this :00-00"));
							}
						}
					}
				
				
				
				if("Y".equalsIgnoreCase(xl.getIsObject())) {
								
					if(StringUtils.isBlank(xl.getObjApijsonKey())) {
						error.add(new Errors(""+i+"","ObjApijsonKey","Please enter ObjApijsonKey"));
					}if(StringUtils.isBlank(xl.getObjSelcolKey())) {
						error.add(new Errors(""+i+"","ObjSelcolKey","Please enter ObjSelcolKey"));
					}if(StringUtils.isBlank(xl.getObjDefaulVal())) {
						error.add(new Errors(""+i+"","ObjDefaulVal","Please enter ObjDefaulVal"));
					}
					
				}
				
			
	
				if("Y".equalsIgnoreCase(xl.getIsArray())) {
										
					if(StringUtils.isBlank(xl.getSelColName())) {
						error.add(new Errors(""+i+"","ArrayTableColumn","Please enter ArrayTableColumn"));
					}if(StringUtils.isBlank(xl.getApiJsonKey())) {
						error.add(new Errors(""+i+"","ArrayJsonKey","Please enter ArrayJsonKey"));
					}if(StringUtils.isBlank(xl.getIsMainDefauVal())) {
						error.add(new Errors(""+i+"","ArrayDefaultValue","Please enter ArrayDefaultValue"));
					}
					
				}
			
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return error;
	}

}
