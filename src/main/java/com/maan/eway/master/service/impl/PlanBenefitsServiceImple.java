package com.maan.eway.master.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.bean.TravelPolicyType;
import com.maan.eway.error.Error;
import com.maan.eway.master.req.DuplicationCheck;
import com.maan.eway.master.req.InsertPlanBenefitsReq;
import com.maan.eway.master.req.SuccessResponse;
import com.maan.eway.master.req.TravelPolicyTypeCoverRes;
import com.maan.eway.master.req.TravelPolicyTypeSubCoverRes;
import com.maan.eway.master.service.PlanBenefitsService;
import com.maan.eway.repository.TravelPolicyTypeRepository;

@Service
@Transactional
public class PlanBenefitsServiceImple implements PlanBenefitsService {

	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private 	TravelPolicyTypeRepository travelRepo;
	
	
	@Override
	public List<Error> validatePlanBenefits(InsertPlanBenefitsReq req) {
		List<Error> errorList = new ArrayList<Error>();
		try {
			
			
			// Date Validation 
			Calendar cal = new GregorianCalendar();
			Date today = new Date();
			cal.setTime(today);cal.add(Calendar.DAY_OF_MONTH, -1);cal.set(Calendar.HOUR_OF_DAY, 23);cal.set(Calendar.MINUTE, 50);
			today = cal.getTime();
			if (req.getEffectiveDateStart() == null ) {
				errorList.add(new Error("04", "EffectiveDateStart", "Please Enter Effective Date"));
	
			} else if (req.getEffectiveDateStart().before(today)) {
				errorList.add(new Error("04", "EffectiveDateStart", "Please Enter Effective Date as Future Date"));
			} 
			
		
			if (StringUtils.isBlank(req.getCreatedBy())) {
				errorList.add(new Error("06", "CreatedBy", "Please Enter CreatedBy"));
			}else if (req.getCreatedBy().length() > 50) {
				errorList.add(new Error("06", "CreatedBy", "Please Enter CreatedBy within 100 Characters"));
			} 
						
			// Other Errors	
			if (StringUtils.isBlank(req.getCompanyId())) {
				errorList.add(new Error("01", "Insurance Id", "Please Enter InsuranceId"));
				
			}
			if (StringUtils.isBlank(req.getBranchCode())) {
				errorList.add(new Error("01", "BranchCode", "Please Enter BranchCode"));
				
			}
			if (StringUtils.isBlank(req.getProductId())) {
				errorList.add(new Error("02", "ProductId", "Please Enter ProductId"));
			}
			if (StringUtils.isBlank(req.getPolicyTypeId())) {
				errorList.add(new Error("02", "PolicyTypeId", "Please Enter PolicyTypeId"));
			}
			if (StringUtils.isBlank(req.getPlanTypeId())) {
				errorList.add(new Error("02", "PlanTypeId", "Please Enter PlanTypeId"));
			}
			
			if (StringUtils.isBlank(req.getRemarks()) ) {
				errorList.add(new Error("03", "Remark", "Please Select Remark "));
			}else if (req.getRemarks().length() > 100){
				errorList.add(new Error("03","Remark", "Please Enter Remark within 100 Characters")); 
			}
			
		//	List<TravelPolicyType> poltype = travelRepo.findByPolicyTypeDescAndPlanTypeDesc(req.getPolicyTypeDesc(), req.getPlanTypeDesc());
			
			List<DuplicationCheck> dup = new ArrayList<DuplicationCheck>();
			int row = 0 ;
			if(req.getTravelCover().size()>0) {
				
				for(TravelPolicyTypeCoverRes data : req.getTravelCover()) {
					
					
					if(data.getTravelSubCover().size()>0) {
						
						for(TravelPolicyTypeSubCoverRes sub : data.getTravelSubCover()) {
							
							DuplicationCheck add = new DuplicationCheck();
							row = row + 1; 
							if (StringUtils.isBlank(data.getCoverDesc())) {
								errorList.add(new Error("02", "CoverDesc", "Please Enter CoverDesc in row "+ row));
								
							} else {
								add.setCoverDesc(data.getCoverDesc());
							}
							if (StringUtils.isBlank(data.getCoverStatus())) {
								errorList.add(new Error("05", "Cover Status", "Please Select Cover Status"));
							} else if (data.getCoverStatus().length() > 1) {
								errorList.add(new Error("05", "Cover Status", "Please Select Valid Cover Status - 1 Character Only Allwed"));
							}
							
							
							if (StringUtils.isBlank(sub.getCurrency())) {
								errorList.add(new Error("02", "Currency", "Please Enter Currency in row "+ row));
							}
							if (StringUtils.isBlank(sub.getExcess())) {
								errorList.add(new Error("02", "ExcessAmt", "Please Enter ExcessAmt in row "+ row));
							}
							if (StringUtils.isBlank(sub.getSubCoverDesc())) {
								errorList.add(new Error("02", "SubCoverDesc", "Please Enter SubCoverDesc in row "+ row));
							} else {
								add.setSubCoverDesc(sub.getSubCoverDesc());
							}
							dup.add(add);
						
							
							
							//Duplicate
							List<DuplicationCheck> dupfilter = dup.stream().filter(o -> ( (o.getCoverDesc().equalsIgnoreCase(data.getCoverDesc())) &&
									(o.getSubCoverDesc().equalsIgnoreCase(sub.getSubCoverDesc())) ) ).collect(Collectors.toList());
							
							if(dupfilter.size()>1) {
								errorList.add(new Error("02", "Duplicate", "Remove Duplicate Data in row "+ row));
							}
							
//							List<TravelPolicyType> poltypefilter = poltype.stream().filter(o -> ( (o.getCoverDesc().equalsIgnoreCase(data.getCoverDesc())) &&
//									(o.getSubCoverDesc().equalsIgnoreCase(sub.getSubCoverDesc())) && (o.getStatus().equalsIgnoreCase("Y")) ) ).collect(Collectors.toList());
//							
//							  Optional<Integer> maxValue = poltypefilter.stream()
//						                .map(TravelPolicyType::getAmendId)
//						                .max(Comparator.naturalOrder());
//							  
//							  List<TravelPolicyType> poltypefil = poltypefilter.stream().filter(o -> ( (o.getCoverDesc().equalsIgnoreCase(data.getCoverDesc())) &&
//										(o.getSubCoverDesc().equalsIgnoreCase(sub.getSubCoverDesc())) && (o.getStatus().equalsIgnoreCase("Y")) &&
//									  (o.getAmendId().equals(maxValue.get()))  ) ).collect(Collectors.toList());
//							
//							if(poltypefil.size()>0) {
//								errorList.add(new Error("02", "Duplicate", "Data Already Presents. Remove Duplicate in row "+ row));
//							}
							
							
							
							if (StringUtils.isBlank(sub.getSumInsured())) {
								errorList.add(new Error("02", "SumInsured", "Please Enter SumInsured in row "+ row));
							}
							if (StringUtils.isBlank(sub.getStatus())) {
								errorList.add(new Error("05", "SubCover Status", "Please Select SubCover Status"));
							} else if (sub.getStatus().length() > 1) {
								errorList.add(new Error("05", "SubCover Status", "Please Select Valid SubCover Status - 1 Character Only Allwed"));
							}
							
							
						}
					} else {
						errorList.add(new Error("05", "SubCover", "Please Enter SubCover Details"));
					}
					
				}
				
			} else {
				errorList.add(new Error("05", "Cover", "Please Enter Cover Details"));
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return errorList;
					 
		
	}

	@Override
	public SuccessResponse insertPlanBenefits(InsertPlanBenefitsReq req) {
		SuccessResponse res = new SuccessResponse();
		try {
			// Update Old Records
			Integer amendId = upadateOldPlanBenefits(req ) ;
		
			// Insert New Records
			res = insertNewPlanBenefits(req , amendId ) ;
		
		} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	return res;
	}

	private SuccessResponse insertNewPlanBenefits(InsertPlanBenefitsReq req, Integer amendId) {
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/YYYY");
		SuccessResponse res = new SuccessResponse();
		try {
			String end = "31/12/2050";
			Date endDate = sdformat.parse(end);
		
			TravelPolicyType topcover = travelRepo.findTopByOrderByCoverIdDesc();
			TravelPolicyType topsub = travelRepo.findTopByOrderBySubCoverIdDesc();
			int maxcoverid = topcover.getCoverId();
			int maxsubcoverid = topsub.getSubCoverId();
			
			List<TravelPolicyType> saveList = new ArrayList<TravelPolicyType>();
			
			
			
			if(req.getTravelCover().size()>0) {
			for ( TravelPolicyTypeCoverRes data :  req.getTravelCover() ) {
			
			
				if(data.getTravelSubCover().size() > 0) {
					
					for(TravelPolicyTypeSubCoverRes sub : data.getTravelSubCover()) {
						TravelPolicyType saveData = new TravelPolicyType();
						// Save New Records
						
						maxcoverid = maxcoverid + 1;
						maxsubcoverid = maxsubcoverid + 1;
						
						saveData.setAmendId(amendId);
						saveData.setBranchCode(req.getBranchCode());
						saveData.setCompanyId(req.getCompanyId());
						saveData.setCoverDesc(data.getCoverDesc());
						saveData.setCoverId(maxcoverid);
					
						saveData.setEffectiveEnddate(endDate);
						saveData.setEffectiveStartdate(req.getEffectiveDateStart());
						saveData.setEntryDate(new Date());
						
					
						saveData.setCoverStatus(data.getCoverStatus());
						
						saveData.setSubCoverId(maxsubcoverid);
						saveData.setCurrency(sub.getCurrency());
						saveData.setExcessAmt(sub.getExcess());
						saveData.setRemarks(req.getRemarks());
						saveData.setStatus(sub.getStatus());
						saveData.setSubCoverDesc(sub.getSubCoverDesc());
						saveData.setSumInsured(sub.getSumInsured());
						
						saveData.setPlanTypeDesc(req.getPlanTypeDesc());
						saveData.setPlanTypeId(Integer.valueOf(req.getPlanTypeId()));
						saveData.setPolicyTypeDesc(req.getPolicyTypeDesc());
						saveData.setPolicyTypeId(Integer.valueOf(req.getPolicyTypeId()));
						saveData.setProductId(Integer.valueOf(req.getProductId()));
					
						
						
						saveList.add(saveData);
					}
					
				}
				
				
			}
			}
			travelRepo.saveAllAndFlush(saveList);
			res.setResponse("Updated Successfully");
			res.setSuccessId("");
		
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
	}
	return res;
	}

	private Integer upadateOldPlanBenefits(InsertPlanBenefitsReq req) {
		List<TravelPolicyType> list = new ArrayList<TravelPolicyType>();
		Integer amendId = 0 ;
		try {
			long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
			Date oldEndDate = new Date(req.getEffectiveDateStart().getTime() - MILLIS_IN_A_DAY);
			Date entryDate = null ;
		
			entryDate = new Date();
			
			// Get Sno Record For Amend ID
			
			// FInd Old Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<TravelPolicyType> query = cb.createQuery(TravelPolicyType.class);
			
			Root<TravelPolicyType> b = query.from(TravelPolicyType.class);

			query.select(b);
			
			
			Subquery<Long> maxAmendId = query.subquery(Long.class);
			Root<TravelPolicyType> ocpm1 = maxAmendId.from(TravelPolicyType.class);
			maxAmendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a2 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			Predicate a3 = cb.equal(ocpm1.get("policyTypeId"), b.get("policyTypeId"));
			Predicate a4 = cb.equal(ocpm1.get("planTypeId"), b.get("planTypeId"));
			Predicate a5 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
			maxAmendId.where(a1,a2,a3,a4,a5);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("amendId")));
			
			// Where
			Predicate n1 = cb.equal(b.get("companyId"), req.getCompanyId());
			Predicate n2 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n3 = cb.equal(b.get("policyTypeId"), req.getPolicyTypeId());
			Predicate n4 = cb.equal(b.get("planTypeId"), req.getPlanTypeId());
			Predicate n5 = cb.equal(b.get("branchCode"), req.getBranchCode());
			Predicate n6 = cb.equal(b.get("branchCode"), "99999");
			Predicate n7 = cb.or(n5,n6);
			Predicate n8 = cb.equal(b.get("amendId"), maxAmendId);
			
			query.where(n1,n2,n3,n4,n7,n8).orderBy(orderList);
			
			// Get Result 
			TypedQuery<TravelPolicyType> result = em.createQuery(query);
			list = result.getResultList();
			
			if(list.size()>0) {
				Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
			
				if ( list.get(0).getEffectiveStartdate().before(beforeOneDay)  ) {
					amendId = list.get(0).getAmendId() + 1 ;
					entryDate = new Date() ;
					
					//UPDATE
					CriteriaBuilder cb2 = em.getCriteriaBuilder();
					// create update
					CriteriaUpdate<TravelPolicyType> update = cb2.createCriteriaUpdate(TravelPolicyType.class);
					// set the root class
					Root<TravelPolicyType> m = update.from(TravelPolicyType.class);
					// set update and where clause
					update.set("updatedBy", req.getCreatedBy());
					update.set("updatedDate", entryDate);
					update.set("effectiveEnddate", oldEndDate);
					
					n1 = cb.equal(m.get("companyId"), req.getCompanyId());
					n2 = cb.equal(m.get("productId"), req.getProductId());
					n3 = cb.equal(m.get("policyTypeId"), req.getPolicyTypeId());
					n4 = cb.equal(m.get("planTypeId"), req.getPlanTypeId());
					n5 = cb.equal(m.get("branchCode"), req.getBranchCode());
					n6 = cb.equal(m.get("branchCode"), "99999");
					n7 = cb.or(n5,n6);
					n8 = cb.equal(m.get("amendId"), list.get(0).getAmendId());
					update.where(n1,n2,n3,n4,n7,n8);
					// perform update
					em.createQuery(update).executeUpdate();
					
				} else {
					amendId = list.get(0).getAmendId() ;
					travelRepo.deleteAll(list);
			    }
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		
			return null;
	}
	return amendId;
	}


}
