package com.maan.eway.chart;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.maan.eway.bean.HomePositionMaster;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.error.Error;
import com.maan.eway.fileupload.JpqlQueryServiceImpl;
import com.maan.eway.repository.HomePositionMasterRepository;
import com.maan.eway.repository.ListItemValueRepository;
import com.maan.eway.res.CommonRes;
import com.maan.eway.res.SuccessRes;

@Service
@Transactional
public class ChartParentMasterServiceImpl implements ChartParentMasterService {
	
	
	@Autowired
	private JpqlQueryServiceImpl jpqlQuery;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private ChartParentMasterRepository chatParentMasterRepo;
	
	@Autowired
	private ChartAccountChildMasterRepository childRepo;
	
	@Autowired
	private ListItemValueRepository listItemValRepo;
	
	@Autowired
	private HomePositionMasterRepository hpmRepo;;
	
	
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public CommonRes parentSave(ChartParentRequest req) {
		CommonRes response = new CommonRes();
		try {
			
			List<Error> error = validateChartInputs(req);
			
			if(error.isEmpty()) {
				Integer amendId =jpqlQuery.getParentChartAmendId(req);
				ChartParentMasterId chartParentMasterId = ChartParentMasterId.builder()
						.companyId(Integer.valueOf(req.getCompanyId()))
						.chartId(StringUtils.isBlank(req.getChartId())?chatParentMasterRepo.findByChatParentIdCompanyId(Integer.valueOf(req.getCompanyId())).size()+1
								:Integer.valueOf(req.getChartId()))
						.amendId(amendId)
						.build();
				
				
				ChartParentMaster chartParentMaster = ChartParentMaster.builder()
						.accountType(req.getAccountType())
						.charactersticType(req.getCharactersticType())
						.chartAccountCode(Integer.valueOf(req.getChartAccountCode()))
						.chartAccountDesc(req.getChartAccountDesc())
						.chatParentId(chartParentMasterId)
						.displayOrder(Integer.valueOf(req.getDisplayOrder()))
						.effectiveStartDate(sdf.parse(req.getEffectiveStartDate()))
						.chartAccountDesc(req.getChartAccountDesc())
						.effectiveEndDate(sdf.parse("31/12/2050"))
						.entryDate(new Date())
						.narationDesc(req.getNarationDesc())
						.status(StringUtils.isBlank(req.getStatus())?"Y":req.getStatus())
						.updatedBy(req.getUpdatedBy())
						.updatedDate(new Date())
						.build();
				
				chatParentMasterRepo.save(chartParentMaster);
				
				SuccessRes res = new SuccessRes();
				 
				res.setResponse("Data saved successfully");
				response.setCommonResponse(res);
				response.setIsError(false);
				response.setErrorMessage(Collections.emptyList());
				response.setMessage("Success");
			}else {
				response.setCommonResponse(null);
				response.setIsError(false);
				response.setErrorMessage(error);
				response.setMessage("Failed");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private List<Error> validateChartInputs(ChartParentRequest req) {
		List<Error> list = new ArrayList<>();
		if(StringUtils.isBlank(req.getAccountType())) {
			list.add(new Error("101","AccountType","Please enter AccountType"));
		}
		if(StringUtils.isBlank(req.getCharactersticType())) {
			list.add(new Error("101","CharactersticType"," Please enter CharactersticType"));
		}
		if(StringUtils.isBlank(req.getChartAccountCode())) {
			list.add(new Error("101","ChartAccountCode","Please enter ChartAccountCode"));
		}
		if(StringUtils.isBlank(req.getDisplayOrder())) {
			list.add(new Error("101","DisplayOrder","Please enter DisplayOrder"));
		}
		if(StringUtils.isBlank(req.getEffectiveEndDate())) {
			//list.add(new Error("101","EffectiveEndDate","Please enter EffectiveEndDate"));
		}
		if(StringUtils.isBlank(req.getEffectiveStartDate())) {
			list.add(new Error("101","EffectiveStartDate","Please enter EffectiveStartDate"));
		}else {
			LocalDate localDate =LocalDate.now();
			LocalDate effectiveStartDate =LocalDate.parse(req.getEffectiveStartDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			
			if(effectiveStartDate.isBefore(localDate)) {
				list.add(new Error("101","EffectiveStartDate","EffectiveStartDate should be graterthan todaydate or equal"));
			}
		}
		if(StringUtils.isBlank(req.getNarationDesc())) {
			//list.add(new Error("101","NarationDesc","Please enter NarationDesc"));
		}
		if(StringUtils.isBlank(req.getStatus())) {
			list.add(new Error("101","Status","Please enter Status"));
		}
		if(StringUtils.isBlank(req.getUpdatedBy())) {
			list.add(new Error("101","UpdatedBy","Please enter UpdatedBy"));
		}
		if(StringUtils.isBlank(req.getChartAccountDesc())) {
			list.add(new Error("101","ChartAccountDesc","Please enter ChartAccountDesc"));
		}
		
		return list;
	}

	@Override
	public CommonRes getAllChartByCompanyId(Integer companyId) {
		CommonRes response = new CommonRes();
		try {
			List<ChartParentMaster> list =chatParentMasterRepo.findParentData(companyId);
			if(!list.isEmpty()) {
				List<Map<String,String>> listMap = new ArrayList<>();
					for(ChartParentMaster cpm :list) {	 	
							HashMap<String, String> map = new HashMap<>();
				 			map.put("CompanyId", cpm.getChatParentId().getCompanyId().toString());
				 			map.put("ChartId", cpm.getChatParentId().getChartId().toString());
				 			map.put("AmendId", cpm.getChatParentId().getAmendId().toString());
				 			map.put("ChartAccountCode", cpm.getChartAccountCode().toString());
				 			map.put("ChartAccountDesc", cpm.getChartAccountDesc());
				 			map.put("Status", cpm.getStatus());
				 			map.put("AccountType", cpm.getAccountType());
				 			map.put("CharactersticType", cpm.getCharactersticType());
				 			map.put("DisplayOrder", cpm.getDisplayOrder().toString());
				 			map.put("EffectiveStartDate", sdf.format(cpm.getEffectiveStartDate()));
				 			map.put("EffectiveEndDate", sdf.format(cpm.getEffectiveEndDate()));
				 			map.put("updatedDate", sdf.format(cpm.getUpdatedDate()));
				 			map.put("updatedBy", cpm.getUpdatedBy());
				 			listMap.add(map);
				 		}

				 response.setCommonResponse(listMap);
				 response.setErrorMessage(Collections.EMPTY_LIST);
				 response.setMessage("Succcess");
				
			}else {
				
				 response.setCommonResponse(null);
				 response.setErrorMessage(Collections.EMPTY_LIST);
				 response.setMessage("Failed");
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public CommonRes editChartDetails(EditChartDetailsReq req) {
		CommonRes response = new CommonRes();
		try {
			ChartParentMasterId chartParentMasterId = ChartParentMasterId.builder()
					.amendId(Integer.valueOf(req.getAmendId()))
					.chartId(Integer.valueOf(req.getCharId()))
					.companyId(Integer.valueOf(req.getCompanyId()))
					.build();
			ChartParentMaster cpm = chatParentMasterRepo.findById(chartParentMasterId).get();
			if(cpm!=null) {
				HashMap<String, String> map = new HashMap<>();
	 			map.put("CompanyId", cpm.getChatParentId().getCompanyId().toString());
	 			map.put("ChartId", cpm.getChatParentId().getChartId().toString());
	 			map.put("AmendId", cpm.getChatParentId().getAmendId().toString());
	 			map.put("ChartAccountCode", cpm.getChartAccountCode().toString());
	 			map.put("ChartAccountDesc", cpm.getChartAccountDesc());
	 			map.put("Status", cpm.getStatus());
	 			map.put("AccountType", cpm.getAccountType());
	 			map.put("CharactersticType", cpm.getCharactersticType());
	 			map.put("DisplayOrder", cpm.getDisplayOrder().toString());
	 			map.put("EffectiveStartDate", sdf.format(cpm.getEffectiveStartDate()));
	 			map.put("EffectiveEndDate", sdf.format(cpm.getEffectiveEndDate()));
	 			map.put("updatedDate", sdf.format(cpm.getUpdatedDate()));
	 			map.put("updatedBy", cpm.getUpdatedBy());
	 			response.setCommonResponse(map);
				response.setErrorMessage(Collections.EMPTY_LIST);
				response.setMessage("Succcess");
			}else {
				 response.setCommonResponse(null);
				 response.setErrorMessage(Collections.EMPTY_LIST);
				 response.setMessage("Failed");
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public CommonRes accountType() {
		CommonRes response = new CommonRes();
		try {
			List<ListItemValue> itemValues =listItemValRepo.findByItemTypeAndStatusOrderByItemCodeAsc("ACCOUNT_TYPE", "Y");
			if(!itemValues.isEmpty()) {
				List<Map<String,String>> mapList =new ArrayList<>();
				itemValues.forEach(p ->{
					HashMap<String, String> map = new HashMap<>();
					map.put("Code", p.getItemCode());
					map.put("CodeDesc", p.getItemValue());
					mapList.add(map);
				});
				response.setCommonResponse(mapList);
				response.setMessage("Success");
				response.setIsError(false);
				response.setErrorMessage(Collections.EMPTY_LIST);
			}else {
				response.setMessage("Failed");
				response.setIsError(true);
				response.setErrorMessage(Collections.EMPTY_LIST);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public CommonRes characterstic() {
		CommonRes response = new CommonRes();
		try {
			List<ListItemValue> itemValues =listItemValRepo.findByItemTypeAndStatusOrderByItemCodeAsc("CHARACTERSTIC", "Y");
			if(!itemValues.isEmpty()) {
				List<Map<String,String>> mapList =new ArrayList<>();
				itemValues.forEach(p ->{
					HashMap<String, String> map = new HashMap<>();
					map.put("Code", p.getItemCode());
					map.put("CodeDesc", p.getItemValue());
					mapList.add(map);
				});
				response.setCommonResponse(mapList);
				response.setMessage("Success");
				response.setIsError(false);
				response.setErrorMessage(Collections.EMPTY_LIST);
			}else {
				response.setMessage("Failed");
				response.setIsError(true);
				response.setErrorMessage(Collections.EMPTY_LIST);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public CommonRes childChartInsert(List<ChartChildRequest> reqList) {
		CommonRes response = new CommonRes();
		try {
			
			List<Error> error =validateChildChartInput(reqList);
			if(error.isEmpty()) {
				List<ChartAccountChildMaster> saveList =new ArrayList<>();
				for(ChartChildRequest req :reqList) {
				
					Integer amendId =jpqlQuery.getChildChartMaxOfAmendId(req);
					
						ChartAccountChildMasterId masterId = ChartAccountChildMasterId.builder()
								.companyId(Integer.valueOf(req.getCompanyId()))
								.productId(Integer.valueOf(req.getProductId()))
								.sectionId(Integer.valueOf(req.getSectionId()))
								.chartId(Integer.valueOf(req.getChartId()))
								.coverId(Integer.valueOf(req.getCoverId()))
								.amendId(amendId)
								.build();
						
						ChartAccountChildMaster childMaster =ChartAccountChildMaster.builder()
								.id(masterId)
								.accountType(req.getAccountType())
								.effectiveEndDate(sdf.parse("31/12/2050"))
								.effectiveStartDate(sdf.parse(req.getEffectiveStartDate()))
								.entryDate(new Date())
								.updatedBy(req.getUpdatedBy())
								.updatedDate(new Date())
								.status(StringUtils.isBlank(req.getStatus())?"Y":req.getStatus())
								.build();
						
						saveList.add(childMaster);
				}
				
				childRepo.saveAll(saveList);
				
				response.setMessage("Success");
				response.setIsError(true);
				response.setErrorMessage(Collections.EMPTY_LIST);
				response.setCommonResponse("Data saved successfully");
			}else {
				response.setMessage("Failed");
				response.setIsError(true);
				response.setErrorMessage(error);
				response.setCommonResponse(null);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
			response.setErrorMessage(Collections.EMPTY_LIST);
			response.setCommonResponse(null);
			return response;
		}
		return response;
	}

	private List<Error> validateChildChartInput(List<ChartChildRequest> reqList) {
		List<Error> error = new ArrayList<>();
		
		int index=1;
		for(ChartChildRequest req :reqList) {
			
			if(StringUtils.isBlank(req.getCompanyId())) {
				error.add(new Error("RowNo : "+index,"CompanyId","Please enter CompanyId"));
			}
			if(StringUtils.isBlank(req.getProductId())) {
				error.add(new Error("RowNo : "+index,"ProductId","Please enter ProductId"));
			}
			if(StringUtils.isBlank(req.getSectionId())) {
				error.add(new Error("RowNo : "+index,"SectionId","Please enter SectionId"));
			}
			if(StringUtils.isBlank(req.getChartId())) {
				error.add(new Error("RowNo : "+index,"ChartId","Please enter ChartId"));
			}
			if(StringUtils.isBlank(req.getCoverId())) {
				error.add(new Error("RowNo : "+index,"CoverId","Please enter CoverId"));
			}
			if(StringUtils.isBlank(req.getAccountType())) {
				error.add(new Error("RowNo : "+index,"AccountType","Pleae enter AccountType"));
			}
			if(StringUtils.isBlank(req.getStatus())) {
				error.add(new Error("RowNo : "+index,"Status","Please enter Status"));
			}
			if(StringUtils.isBlank(req.getEffectiveStartDate())) {
				error.add(new Error("RowNo : "+index,"EffectiveStartDate","Please enter EffectiveStartDate"));
			}else {
				LocalDate localDate =LocalDate.now();
				LocalDate effectiveStartDate =LocalDate.parse(req.getEffectiveStartDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				
				if(effectiveStartDate.isBefore(localDate)) {
					error.add(new Error("RowNo : "+index,"EffectiveStartDate","EffectiveStartDate should be graterthan todaydate or equal"));
				}
			}
			if(StringUtils.isBlank(req.getUpdatedBy())) {
				error.add(new Error("RowNo : "+index,"UpdatedBy","Please enter UpdatedBy"));
			}
			
			index++;
		}
		return error;
	}

	@Override
	public CommonRes getAllChildChartInsert(GetAllChildChartRequest req) {
		CommonRes response = new CommonRes();
		try {
			List<ChartAccountChildMaster> childMasters =childRepo.findChildData(Integer.valueOf(req.getCompanyId())
					,Integer.valueOf(req.getProductId()),Integer.valueOf(req.getSectionId()),Integer.valueOf(req.getChartId()));
		
			List<Map<String,String>> mapList =new ArrayList<>();
			if(!childMasters.isEmpty()) {
				for (ChartAccountChildMaster cac :childMasters) {
							HashMap<String, String> map =new HashMap<>();
							map.put("CompanyId", cac.getId().getCompanyId().toString());
							map.put("ProductId", cac.getId().getProductId().toString());
							map.put("SectionId",cac.getId().getSectionId().toString());
							map.put("ChartId", cac.getId().getChartId().toString());
							map.put("CoverId", cac.getId().getCoverId().toString());
							map.put("AccountType", cac.getAccountType());
							map.put("EffectiveStartDate", sdf.format(cac.getEffectiveStartDate()));
							map.put("EffectiveEndDate", sdf.format(cac.getEffectiveEndDate()));
							map.put("Status", cac.getStatus());
							map.put("UpdatedBy", cac.getUpdatedBy());
							map.put("UpdatedDate", sdf.format(cac.getUpdatedDate()));
							mapList.add(map);
				}
				
				
				response.setCommonResponse(mapList);
				response.setMessage("Success");
				response.setIsError(false);
				response.setErrorMessage(null);
			}else {
				
				response.setCommonResponse(Collections.EMPTY_LIST);
				response.setMessage("Failed");
				response.setIsError(true);
				response.setErrorMessage(null);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public CommonRes editChildChart(EditChartChildRequest req) {
		CommonRes response = new CommonRes();
		try {
			List<ChartAccountChildMaster> list =childRepo.findByIdCompanyIdAndIdProductIdAndIdSectionIdAndIdChartIdAndIdCoverId(Integer.valueOf(req.getCompanyId()),Integer.valueOf(req.getProductId()),
					Integer.valueOf(req.getSectionId()),Integer.valueOf(req.getChartId()),Integer.valueOf(req.getCoverId()));
					
	
					
			
			if(!list.isEmpty()) {
				
				ChartAccountChildMaster cac =list.stream().max((a,b)->a.getId().getAmendId().compareTo(b.getId().getAmendId())).get();
				HashMap<String, String> map =new HashMap<>();
				map.put("CompanyId", cac.getId().getCompanyId().toString());
				map.put("ProductId", cac.getId().getProductId().toString());
				map.put("SectionId",cac.getId().getSectionId().toString());
				map.put("ChartId", cac.getId().getChartId().toString());
				map.put("CoverId", cac.getId().getCoverId().toString());
				map.put("AccountType", cac.getAccountType());
				map.put("EffectiveStartDate", sdf.format(cac.getEffectiveStartDate()));
				map.put("EffectiveEndDate", sdf.format(cac.getEffectiveEndDate()));
				map.put("Status", cac.getStatus());
				map.put("UpdatedBy", cac.getUpdatedBy());
				map.put("UpdatedDate", sdf.format(cac.getUpdatedDate()));
				
				response.setCommonResponse(map);
				response.setMessage("Success");
				response.setIsError(false);
				response.setErrorMessage(null);
			}else {
				response.setCommonResponse(null);
				response.setMessage("Failed");
				response.setIsError(true);
				response.setErrorMessage(null);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public CommonRes policyCredittDebitEntry(String quoteNo) {
		CommonRes response = new CommonRes();
		try {
			HomePositionMaster hpm =hpmRepo.findByQuoteNo(quoteNo);
			Integer companyId =Integer.valueOf(hpm.getCompanyId());
			Integer productId =hpm.getProductId();
			Integer sectionId =hpm.getSectionId();
			BigDecimal brokerCommision =hpm.getCommissionPercentage();
			
			String endorsmentType =StringUtils.isBlank(hpm.getEndtStatus())?"":hpm.getEndtStatus();
			List<ChartParentMaster> cpm =jpqlQuery.getChartParentMasterDetails(companyId);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			

			// POSITIVE PREMIUM  CREDIT DEBIT BLOCK  START
			int index =1;
			
			
			if("P".equalsIgnoreCase(hpm.getStatus())) {
				
				for(ChartParentMaster c : cpm) {
					
					List<ChartAccountChildMaster> charAccount =jpqlQuery.getChildChartAccountData(companyId,productId,sectionId,c);
					
					Double premiumLc =null;
					Double premiumFc =null;
					String documentType ="";
					String docId ="";
					String  documentNo ="";
					Double premiumFcWithT=0D;
					Double premiumLcWithT=0D;
				
					List<Integer> coverIds =charAccount.stream().map(c1 -> c1.getId().getCoverId()).collect(Collectors.toList());
										 
					if("DR".equalsIgnoreCase(c.getAccountType()) && "C".equalsIgnoreCase(c.getCharactersticType())) {
								
												
						 List<PolicyCoverData> pcdList =jpqlQuery.getPolicyCoverDataPremium(quoteNo, coverIds);
						
						 
						 if(StringUtils.isBlank(endorsmentType)) {
						 
							 premiumFc =pcdList.stream().filter(p->p.getTaxId()==0 )
									 .filter(p ->p.getDiscLoadId()==0)
									 .map(p ->Double.valueOf(p.getPremiumExcludedTaxFc().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
									 
							 premiumLc =pcdList.stream().filter(p->p.getTaxId()==0 )
									 .filter(p ->p.getDiscLoadId()==0)
									 .map(p ->Double.valueOf(p.getPremiumExcludedTaxLc().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
						 }else {
							 
							 premiumFc =pcdList.stream().filter(p->p.getTaxId()==0 )
									 .filter(p ->p.getDiscLoadId()!=0)
									 .filter(p ->p.getCoverageType().equals("E"))
									 .map(p ->Double.valueOf(p.getPremiumExcludedTaxFc().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
									 
							 premiumLc =pcdList.stream().filter(p->p.getTaxId()==0 )
									 .filter(p ->p.getDiscLoadId()!=0)
									 .filter(p ->p.getCoverageType().equals("E"))
									 .map(p ->Double.valueOf(p.getPremiumExcludedTaxLc().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
						 }
						 	
						 docId=hpm.getCustomerId();
						 documentType="C";
						 documentNo ="";
						
					}else if("DR".equalsIgnoreCase(c.getAccountType()) && "T".equalsIgnoreCase(c.getCharactersticType())) {
						
						
						 List<PolicyCoverData> pcdList =jpqlQuery.getPolicyCoverDataTax(quoteNo, coverIds);
						 
						 
						 if(StringUtils.isBlank(endorsmentType)) {
						 
							 premiumFc=pcdList.stream().filter(p ->p.getTaxId()!=0)
									 .filter(p ->p.getDiscLoadId()==0)
									 .map(p ->Double.valueOf(p.getTaxAmount().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
							 
						 }else {
							 
							 premiumFc=pcdList.stream().filter(p ->p.getTaxId()!=0)
									 .filter(p ->p.getDiscLoadId()!=0)
									 .filter(p ->p.getCoverageType().equals("E"))
									 .map(p ->Double.valueOf(p.getTaxAmount().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
						 }
						
						 
						 
						docId=hpm.getCustomerId();
						documentType="C";
						documentNo ="";
					
					}else if("CR".equalsIgnoreCase(c.getAccountType()) && "C".equalsIgnoreCase(c.getCharactersticType())) {
						
						
						 List<PolicyCoverData> pcdList =jpqlQuery.getPolicyCoverDataPremium(quoteNo, coverIds);
							
						
						
						 if(StringUtils.isBlank(endorsmentType)) {
							 
							 premiumFcWithT =pcdList.stream().filter(p->p.getTaxId()==0 )
									 .filter(p ->p.getDiscLoadId()==0)
									 .map(p ->Double.valueOf(p.getPremiumExcludedTaxFc().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
									 
							 premiumLcWithT =pcdList.stream().filter(p->p.getTaxId()==0 )
									 .filter(p ->p.getDiscLoadId()==0)
									 .map(p ->Double.valueOf(p.getPremiumExcludedTaxLc().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
						 }else {
							 
							 premiumFcWithT =pcdList.stream().filter(p->p.getTaxId()==0 )
									 .filter(p ->p.getDiscLoadId()!=0)
									 .filter(p ->p.getCoverageType().equals("E"))
									 .map(p ->Double.valueOf(p.getPremiumExcludedTaxFc().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
									 
							 premiumLcWithT =pcdList.stream().filter(p->p.getTaxId()==0 )
									 .filter(p ->p.getDiscLoadId()!=0)
									 .filter(p ->p.getCoverageType().equals("E"))
									 .map(p ->Double.valueOf(p.getPremiumExcludedTaxLc().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
						 }
						 	 
						 premiumFc =premiumFcWithT * Double.valueOf(brokerCommision.toPlainString()) /100;
						 premiumLc =premiumLcWithT * Double.valueOf(brokerCommision.toPlainString()) /100;
						 
						 
						docId=hpm.getLoginId();
						documentType="B";
						documentNo ="";
						
					
					}else if("CR".equalsIgnoreCase(c.getAccountType()) && "T".equalsIgnoreCase(c.getCharactersticType())) {
													
						 List<PolicyCoverData> pcdList =jpqlQuery.getPolicyCoverDataTax(quoteNo, coverIds);
						 
						 if(StringUtils.isBlank(endorsmentType)) {
							 
							 premiumFc=pcdList.stream().filter(p ->p.getTaxId()!=0)
									 .filter(p ->p.getDiscLoadId()==0)
									 .map(p ->Double.valueOf(p.getTaxAmount().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
							 
						 }else {
							 
							 premiumFc=pcdList.stream().filter(p ->p.getTaxId()!=0)
									 .filter(p ->p.getDiscLoadId()!=0)
									 .filter(p ->p.getCoverageType().equals("E"))
									 .map(p ->Double.valueOf(p.getTaxAmount().toPlainString()))
									 .collect(Collectors.summingDouble(p ->p));
						 }
						
						 premiumFc =premiumFc * Double.valueOf(brokerCommision.toPlainString()) /100;
						
						docId=hpm.getLoginId();
						documentType="B";
						documentNo ="";
					}
						
					HashMap<String,Object> saveReq =new HashMap<>();
					saveReq.put("PolicyNo", hpm.getPolicyNo());
					saveReq.put("QuoteNo", hpm.getQuoteNo());
					saveReq.put("ChargeCode", c.getChartAccountCode());
					saveReq.put("ChargeId", index);
					saveReq.put("DocNo", documentNo);
					saveReq.put("DocType", documentType);
					saveReq.put("DocId", docId);
					saveReq.put("AmountLc",premiumLc);
					saveReq.put("CompanyId", companyId);
					saveReq.put("ProductId", productId);
					saveReq.put("BranchCode", hpm.getBranchCode());
					saveReq.put("DRCRFlag", c.getAccountType());
					saveReq.put("AmountFC", premiumFc);
					saveReq.put("ChargeAccountDesc", c.getChartAccountDesc());
					saveReq.put("Narration", "");
					saveReq.put("DisplayOrder",c.getDisplayOrder());
					
					list.add(saveReq);
	
					
					index++;
					
				}
				
			// POSITIVE PREMIUM  CREDIT DEBIT BLOCK  END
				
			
			// NEGATIVE PREMIUM  CREDIT DEBIT BLOCK  START
				
			}else if("C".equalsIgnoreCase(hpm.getStatus())) {
				
				for(ChartParentMaster c : cpm) {
					
					List<ChartAccountChildMaster> charAccount =jpqlQuery.getChildChartAccountData(companyId,productId,sectionId,c);
					
					Double premiumLc =null;
					Double premiumFc =null;
					String documentType ="";
					String docId ="";
					String documentNo="";
					
					List<Integer> coverIds =charAccount.stream().map(c1 -> c1.getId().getCoverId()).collect(Collectors.toList());
					
					//Integer [] coverIdArray = new Integer[coverIds.size()];
					 
					if("CR".equalsIgnoreCase(c.getAccountType()) && "C".equalsIgnoreCase(c.getCharactersticType())) {
								
												
						 List<PolicyCoverData> pcdList =jpqlQuery.getPolicyCoverDataPremium(quoteNo, coverIds);
						
						 premiumFc =pcdList.stream().map(p ->Double.valueOf(p.getPremiumExcludedTaxFc().toPlainString()))
								 .collect(Collectors.summingDouble(p ->p));
								 
						 premiumLc =pcdList.stream().map(p ->Double.valueOf(p.getPremiumExcludedTaxLc().toPlainString()))
								 .collect(Collectors.summingDouble(p ->p));
						 	
						 docId=hpm.getCustomerId();
						 
						 documentType="C";
						 
						 documentNo ="";
						
					}else if("CR".equalsIgnoreCase(c.getAccountType()) && "T".equalsIgnoreCase(c.getCharactersticType())) {
						
						
						 List<PolicyCoverData> pcdList =jpqlQuery.getPolicyCoverDataTax(quoteNo, coverIds);
						 
						 premiumFc=pcdList.stream().map(p ->Double.valueOf(p.getTaxAmount().toPlainString()))
								 .collect(Collectors.summingDouble(p ->p));
						 
						 premiumLc =pcdList.stream().map(p ->Double.valueOf(p.getTaxAmount().toPlainString()))
								 .collect(Collectors.summingDouble(p ->p));
						
						docId=hpm.getCustomerId();
						documentType="C";
						documentNo ="";
					
					}else if("DR".equalsIgnoreCase(c.getAccountType()) && "C".equalsIgnoreCase(c.getCharactersticType())) {
						
						
						 List<PolicyCoverData> pcdList =jpqlQuery.getPolicyCoverDataPremium(quoteNo, coverIds);
							
						 Double premiumFcWithT =pcdList.stream().map(p ->Double.valueOf(p.getPremiumExcludedTaxFc().toPlainString()))
								 .collect(Collectors.summingDouble(p ->p));
								 
						 Double premiumLcWithT =pcdList.stream().map(p ->Double.valueOf(p.getPremiumExcludedTaxLc().toPlainString()))
								 .collect(Collectors.summingDouble(p ->p));
						 
						 premiumFc =premiumFcWithT * Double.valueOf(brokerCommision.toPlainString()) /100;
						 premiumLc =premiumLcWithT * Double.valueOf(brokerCommision.toPlainString()) /100;
						 
						 
						docId=hpm.getLoginId();
						documentType="B";
						documentNo ="";
						
					
					}else if("DR".equalsIgnoreCase(c.getAccountType()) && "T".equalsIgnoreCase(c.getCharactersticType())) {
						
							
						 List<PolicyCoverData> pcdList =jpqlQuery.getPolicyCoverDataTax(quoteNo, coverIds);
						 
						 premiumFc=pcdList.stream().map(p ->Double.valueOf(p.getTaxAmount().toPlainString()))
								 .collect(Collectors.summingDouble(p ->p));
						 
						 premiumLc =pcdList.stream().map(p ->Double.valueOf(p.getTaxAmount().toPlainString()))
								 .collect(Collectors.summingDouble(p ->p));
						
						 premiumFc =premiumFc * Double.valueOf(brokerCommision.toPlainString()) /100;
						 premiumLc =premiumLc * Double.valueOf(brokerCommision.toPlainString()) /100;
						
						docId=hpm.getLoginId();
						documentType="B";
						documentNo ="";
					}
						
					HashMap<String,Object> saveReq =new HashMap<>();
					saveReq.put("PolicyNo", hpm.getPolicyNo());
					saveReq.put("QuoteNo", hpm.getQuoteNo());
					saveReq.put("ChargeCode", c.getChartAccountCode());
					saveReq.put("ChargeId", index);
					saveReq.put("DocNo", "");
					saveReq.put("DocType", documentType);
					saveReq.put("DocId", docId);
					saveReq.put("AmountLc",premiumFc);
					saveReq.put("CompanyId", companyId);
					saveReq.put("ProductId", productId);
					saveReq.put("BranchCode", "");
					saveReq.put("DRCRFlag", c.getAccountType());
					saveReq.put("AmountFC", premiumFc);
					saveReq.put("ChargeAccountDesc", c.getChartAccountDesc());
					saveReq.put("Narration", "");
					saveReq.put("DisplayOrder",c.getDisplayOrder());
					
					list.add(saveReq);
	
					
					index++;
					
				}
				
			}
		// NEGATIVE PREMIUM  CREDIT DEBIT BLOCK  START

			
			System.out.println(new Gson().toJson(list));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public CommonRes childChartInsert1(ChildChartInfoReq req) {
		CommonRes response = new CommonRes();
		try {
			
			List<Error> errors =validateChildReq1(req);
			
			if(errors.isEmpty()) {
				Integer amendId =jpqlQuery.getChildChartMaxOfAmendId1(req);
				
				List<ChartAccountChildMaster> saveList= new ArrayList<>();
				
				for (ChildCoverList cover: req.getCoverList()) {
					
					ChartAccountChildMasterId masterId = ChartAccountChildMasterId.builder()
							.companyId(Integer.valueOf(req.getCompanyId()))
							.productId(Integer.valueOf(req.getProductId()))
							.sectionId(Integer.valueOf(req.getSectionId()))
							.chartId(Integer.valueOf(req.getChartId()))
							.coverId(Integer.valueOf(cover.getCoverId()))
							.amendId(amendId)
							.build();
					
					ChartAccountChildMaster childMaster =ChartAccountChildMaster.builder()
							.id(masterId)
							.accountType(req.getAccountType())
							.effectiveEndDate(sdf.parse("31/12/2050"))
							.effectiveStartDate(sdf.parse(req.getEffectiveStartDate()))
							.entryDate(new Date())
							.updatedBy(req.getUpdatedBy())
							.updatedDate(new Date())
							.status(StringUtils.isBlank(cover.getStatus())?"Y":cover.getStatus())
							.build();
					
					saveList.add(childMaster);
				}
				
				childRepo.saveAll(saveList);
				
				response.setMessage("Success");
				response.setIsError(true);
				response.setErrorMessage(Collections.EMPTY_LIST);
				response.setCommonResponse("Data saved successfully");
			}else {
				response.setMessage("Failed");
				response.setIsError(true);
				response.setErrorMessage(errors);
				response.setCommonResponse(null);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private List<Error> validateChildReq1(ChildChartInfoReq req) {
		List<Error> error = new ArrayList<>();
		if(StringUtils.isBlank(req.getCompanyId())) {
			error.add(new Error("500","CompanyId","Please enter CompanyId"));
		}
		if(StringUtils.isBlank(req.getProductId())) {
			error.add(new Error("500","ProductId","Please enter ProductId"));
		}
		if(StringUtils.isBlank(req.getSectionId())) {
			error.add(new Error("500","SectionId","Please enter SectionId"));
		}
		if(StringUtils.isBlank(req.getChartId())) {
			error.add(new Error("500","ChartId","Please enter ChartId"));
		}
		
		if(StringUtils.isBlank(req.getAccountType())) {
			error.add(new Error("500","AccountType","Pleae enter AccountType"));
		}
		
		if(StringUtils.isBlank(req.getEffectiveStartDate())) {
			error.add(new Error("500","EffectiveStartDate","Please enter EffectiveStartDate"));
		}else {
			LocalDate localDate =LocalDate.now();
			LocalDate effectiveStartDate =LocalDate.parse(req.getEffectiveStartDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			
			if(effectiveStartDate.isBefore(localDate)) {
				error.add(new Error("500","EffectiveStartDate","EffectiveStartDate should be graterthan todaydate or equal"));
			}
		}
		if(StringUtils.isBlank(req.getUpdatedBy())) {
			error.add(new Error("500","UpdatedBy","Please enter UpdatedBy"));
		}
		
		
		if(req.getCoverList().isEmpty()) {
			error.add(new Error("500","CoverList","Please enter coverlist"));
		}
		return error;
	}
	

}
