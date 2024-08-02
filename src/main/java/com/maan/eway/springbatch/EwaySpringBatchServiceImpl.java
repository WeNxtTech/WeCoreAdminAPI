package com.maan.eway.springbatch;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.maan.eway.batch.repository.TransactionControlDetailsRepository;
import com.maan.eway.bean.FactorRateMaster;
import com.maan.eway.bean.FactorTypeDetails;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.SectionCoverMaster;
import com.maan.eway.fileupload.FileUploadInputRequest;
import com.maan.eway.master.req.FactorRateSaveReq;
import com.maan.eway.repository.FactorRateMasterRepository;

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

@Service
public class EwaySpringBatchServiceImpl implements EwaySpringBatchService {

	Logger log =LogManager.getLogger(getClass());
	
	private Gson json = new Gson();

    @Autowired
    private UtilityServiceImpl utilService;
    @PersistenceContext
	private EntityManager em;
	
	@Autowired
	private FactorRateMasterRepository factorRepo;;
	
	@Autowired
	private TransactionControlDetailsRepository transactionConRepo;
	
	DozerBeanMapper dozerMapper = new  DozerBeanMapper();

   
	@Override
	public FileUploadInputRequest batchUpload(FileUploadInputRequest request, MultipartFile file) {
		log.info("batchUpload request "+json.toJson(request));
		try {
			request =utilService.saveExcelfile(request,file);
			utilService.saveUploadTransactionData(request);
			Thread_Batch_Upload batchUpload = new Thread_Batch_Upload(utilService,request,"Batch_upload");
			Thread thread = new Thread(batchUpload);
			thread.setDaemon(false);
			thread.setName("BATCH_UPLOAD");
			thread.start();
			
		}catch (Exception e) {
			log.error(e);
		}
		return request;
	}
	
	public FactorRateCommonResponse saveFactorDetails(FactorRateSaveReq req) {
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/YYYY");
		FactorRateCommonResponse res = new FactorRateCommonResponse();
		List<FactorRateMaster> list = new ArrayList<FactorRateMaster>();
		Date endDate=null;
		try {
			Date startDate = req.getEffectiveDateStart() ;
			String end = "31/12/2050";
			endDate = sdformat.parse(end);
			long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
			Date oldEndDate = new Date(req.getEffectiveDateStart().getTime() - MILLIS_IN_A_DAY);
			Date entryDate = null ;
			String createdBy = "" ;
			
			String factorTypeId ="";
		
			factorTypeId = req.getFactorTypeId() ;
			entryDate = new Date();
			createdBy = req.getCreatedBy();
			//res.setResponse("Updated Successfully");
			//res.setSuccessId(factorTypeId);
			
			// COver SubCoverDetails
			List<SectionCoverMaster> coverlist = new ArrayList<SectionCoverMaster>();
			{
				// Find Latest Record
				CriteriaBuilder cb2 = em.getCriteriaBuilder();
				CriteriaQuery<SectionCoverMaster> query2 = cb2.createQuery(SectionCoverMaster.class);
	
				// Find All
				Root<SectionCoverMaster> b2 = query2.from(SectionCoverMaster.class);
	
				// Effective Date Max Filter
				
				Subquery<Timestamp> effectiveDate2 = query2.subquery(Timestamp.class);
				Root<SectionCoverMaster> ocpm2 = effectiveDate2.from(SectionCoverMaster.class);
				effectiveDate2.select(cb2.greatest(ocpm2.get("effectiveDateStart")));
				Predicate a10 = cb2.equal(ocpm2.get("coverId"), b2.get("coverId"));
				Predicate a11 = cb2.equal(ocpm2.get("sectionId"), b2.get("sectionId"));
				Predicate a12 = cb2.equal(ocpm2.get("productId"), b2.get("productId"));
				Predicate a13 = cb2.equal(ocpm2.get("companyId"), b2.get("companyId"));
			//	Predicate a14 = cb2.lessThanOrEqualTo(ocpm2.get("effectiveDateStart"), today);
				effectiveDate2.where(a10,a11,a12,a13);
	
				// Select
				query2.select(b2);
	
				// Order By
				List<Order> orderList2 = new ArrayList<Order>();
				orderList2.add(cb2.desc(b2.get("effectiveDateStart")));
	
				// Where
				Predicate n7 = cb2.equal(b2.get("effectiveDateStart"),effectiveDate2);
				Predicate n8 =cb2.equal(b2.get("coverId"), req.getCoverId());
				Predicate n9 = cb2.equal(b2.get("productId"), req.getProductId());
				Predicate n10 = cb2.equal(b2.get("companyId"), req.getCompanyId());
				Predicate n11 = cb2.equal(b2.get("sectionId"), req.getSectionId());
				Predicate n12 = cb2.equal(b2.get("status"), "Y");
				Predicate n13 = cb2.notEqual(b2.get("status"), "Y");
				Predicate n14 = cb2.or(n12,n13);
				Predicate n15 = null ; 
				if( StringUtils.isBlank(req.getSubCoverId()) ) {
					n15 = cb2.equal(b2.get("subCoverId"), "0");
				} else {
					n15 = cb2.equal(b2.get("subCoverId"), req.getSubCoverId());
				}
				
				query2.where(n7,n8,n9,n10,n11,n14,n15).orderBy(orderList2);
				// Get Result
				TypedQuery<SectionCoverMaster> result2 = em.createQuery(query2);
				coverlist  = result2.getResultList();
			}
			
			// Factor Type Name
			List<FactorTypeDetails> factorTypes = new ArrayList<FactorTypeDetails>();
			{
				// COver SubCoverDetails
				
				// Find Latest Record
				CriteriaBuilder cb2 = em.getCriteriaBuilder();
				CriteriaQuery<FactorTypeDetails> query2 = cb2.createQuery(FactorTypeDetails.class);
	
				// Find All
				Root<FactorTypeDetails> b2 = query2.from(FactorTypeDetails.class);
	
				// Effective Date Max Filter
				
				Subquery<Timestamp> effectiveDate2 = query2.subquery(Timestamp.class);
				Root<FactorTypeDetails> ocpm2 = effectiveDate2.from(FactorTypeDetails.class);
				effectiveDate2.select(cb2.greatest(ocpm2.get("effectiveDateStart")));
				Predicate a12 = cb2.equal(ocpm2.get("productId"), b2.get("productId"));
				Predicate a13 = cb2.equal(ocpm2.get("companyId"), b2.get("companyId"));
				Predicate a14 = cb2.lessThanOrEqualTo(ocpm2.get("effectiveDateStart"),req.getEffectiveDateStart());
				Predicate a15 = cb2.equal(ocpm2.get("factorTypeId"), b2.get("factorTypeId"));
				effectiveDate2.where(a12,a13,a14,a15);
	
				// Select
				query2.select(b2);
	
				// Order By
				List<Order> orderList2 = new ArrayList<Order>();
				orderList2.add(cb2.desc(b2.get("effectiveDateStart")));
	
				// Where
				Predicate n7 = cb2.equal(b2.get("effectiveDateStart"),effectiveDate2);
				Predicate n9 = cb2.equal(b2.get("productId"), req.getProductId());
				Predicate n10 = cb2.equal(b2.get("companyId"), req.getCompanyId());
				Predicate n12 = cb2.equal(b2.get("status"), "Y");
				Predicate n13 = cb2.notEqual(b2.get("status"), "Y");
				Predicate n14 = cb2.or(n12,n13);
				Predicate n15 = cb2.equal(b2.get("factorTypeId"), req.getFactorTypeId());
				
				query2.where(n7,n9,n10,n14,n15).orderBy(orderList2);
				// Get Result
				TypedQuery<FactorTypeDetails> result2 = em.createQuery(query2);
				factorTypes  = result2.getResultList();
			}
			
			List<ListItemValue> calcTypes =  getListItem("99999" , req.getBranchCode()   ,"CALCULATION_TYPE"); // listRepo.findByItemTypeAndStatus("CALCULATION_TYPE" , "Y");
		//	cal.setTime(today);  cal.set(Calendar.HOUR_OF_DAY, 23); cal.set(Calendar.MINUTE, 50) ;
		//	today = cal.getTime();
			
			// Get Sno Record For Amend ID
			// FInd Old Record
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FactorRateMaster> query = cb.createQuery(FactorRateMaster.class);
			//Find all
			Root<FactorRateMaster> b = query.from(FactorRateMaster.class);
			//Select 
			query.select(b);

			// Max AmendId
			Subquery<Long> maxAmendId = query.subquery(Long.class);
			Root<FactorRateMaster> ocpm1 = maxAmendId.from(FactorRateMaster.class);
			maxAmendId.select(cb.max(ocpm1.get("amendId")));
			Predicate a1 = cb.equal(ocpm1.get("companyId"), b.get("companyId"));
			Predicate a2 = cb.equal(ocpm1.get("productId"), b.get("productId"));
			Predicate a3 = cb.equal(ocpm1.get("sectionId"), b.get("sectionId"));
			Predicate a4 = cb.equal(ocpm1.get("coverId"), b.get("coverId"));
			Predicate a5 = cb.equal(ocpm1.get("subCoverId"), b.get("subCoverId") );
			Predicate a6 = cb.equal(ocpm1.get("branchCode"), b.get("branchCode"));
			Predicate a7 = cb.equal(ocpm1.get("factorTypeId"), b.get("factorTypeId"));
			Predicate a8 = cb.equal(ocpm1.get("sNo"), b.get("sNo"));
			Predicate a9 = cb.equal(ocpm1.get("agencyCode"), b.get("agencyCode"));
			maxAmendId.where(a1,a2,a3,a4,a5,a6,a7,a8,a9);

			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(b.get("amendId")));
			
			// Where
			Predicate n1 = cb.equal(b.get("companyId"), req.getCompanyId());
			Predicate n2 = cb.equal(b.get("productId"), req.getProductId());
			Predicate n3 = cb.equal(b.get("sectionId"), req.getSectionId());
			Predicate n4 = cb.equal(b.get("coverId"), req.getCoverId());
			Predicate n5 = cb.equal(b.get("subCoverId"), StringUtils.isBlank(req.getSubCoverId())?"0":req.getSubCoverId());
			Predicate n6 = cb.equal(b.get("branchCode"), req.getBranchCode());
			Predicate n7 = cb.equal(b.get("branchCode"), "99999");
			Predicate n8 = cb.or(n6,n7);
			Predicate n9 = cb.equal(b.get("factorTypeId"), factorTypeId);
			Predicate n10 = cb.equal(b.get("amendId"), maxAmendId);
			Predicate n11 = cb.equal(b.get("agencyCode"),StringUtils.isBlank(req.getAgencyCode())?"99999":req.getAgencyCode());
			
			query.where(n1,n2,n3,n4,n5,n8,n9,n10,n11).orderBy(orderList);
			
			// Get Result 
			TypedQuery<FactorRateMaster> result = em.createQuery(query);
			list = result.getResultList();
			
			Integer amendId = 0 ;
			
			if(list.size()>0) {
				Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
			
				if ( list.get(0).getEffectiveDateStart().before(beforeOneDay)  ) {
					amendId = list.get(0).getAmendId() + 1 ;
					entryDate = new Date() ;
					createdBy = req.getCreatedBy();

					//UPDATE
					CriteriaBuilder cb2 = em.getCriteriaBuilder();
					// create update
					CriteriaUpdate<FactorRateMaster> update = cb2.createCriteriaUpdate(FactorRateMaster.class);
					// set the root class
					Root<FactorRateMaster> m = update.from(FactorRateMaster.class);
					// set update and where clause
					update.set("updatedBy", req.getCreatedBy());
					update.set("updatedDate", entryDate);
					update.set("effectiveDateEnd", oldEndDate);
					
					n1 = cb.equal(m.get("companyId"), req.getCompanyId());
					n2 = cb.equal(m.get("productId"), req.getProductId());
					n3 = cb.equal(m.get("sectionId"), req.getSectionId());
					n4 = cb.equal(m.get("coverId"), req.getCoverId());
					n5 = cb.equal(m.get("subCoverId"), StringUtils.isBlank(req.getSubCoverId())?"0":req.getSubCoverId());
					n6 = cb.equal(m.get("branchCode"), req.getBranchCode());
					n7 = cb.equal(m.get("branchCode"), "99999");
					n8 = cb.or(n6,n7);
					n9 = cb.equal(m.get("factorTypeId"), factorTypeId);
					n10 = cb.equal(m.get("amendId"), list.get(0).getAmendId());
					n11 = cb.equal(b.get("agencyCode"),StringUtils.isBlank(req.getAgencyCode())?"99999":req.getAgencyCode());
					update.where(n1,n2,n3,n4,n5,n8,n9,n10,n11);
					// perform update
					em.createQuery(update).executeUpdate();
					
				} else {
					amendId = list.get(0).getAmendId() ;
					entryDate = list.get(0).getEntryDate() ;
					createdBy = list.get(0).getCreatedBy();
					factorRepo.deleteAll(list);
			    }
			}
			
			res.setEndate(endDate);
			res.setAmendId(amendId);
		} catch (Exception e) {
		e.printStackTrace();
		log.info("Exception is --->" + e.getMessage());
		return null;
	}
	return res;
	}
	
	public synchronized List<ListItemValue> getListItem(String insuranceId , String branchCode, String itemType) {
		List<ListItemValue> list = new ArrayList<ListItemValue>();
		try {
			Date today = new Date();
			Calendar cal = new GregorianCalendar();
			cal.setTime(today);
			today = cal.getTime();
			Date todayEnd = cal.getTime();
			
			// Criteria
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> query=  cb.createQuery(ListItemValue.class);
			// Find All
			Root<ListItemValue> c = query.from(ListItemValue.class);
			
			//Select
			query.select(c);
			// Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(c.get("branchCode")));
			
			
			// Effective Date Start Max Filter
			Subquery<Timestamp> effectiveDate = query.subquery(Timestamp.class);
			Root<ListItemValue> ocpm1 = effectiveDate.from(ListItemValue.class);
			effectiveDate.select(cb.greatest(ocpm1.get("effectiveDateStart")));
			Predicate a1 = cb.equal(c.get("itemId"),ocpm1.get("itemId"));
			Predicate a2 = cb.lessThanOrEqualTo(ocpm1.get("effectiveDateStart"), today);
			Predicate b1= cb.equal(c.get("branchCode"),ocpm1.get("branchCode"));
			Predicate b2 = cb.equal(c.get("companyId"),ocpm1.get("companyId"));
			effectiveDate.where(a1,a2,b1,b2);
			
			// Effective Date End Max Filter
			Subquery<Timestamp> effectiveDate2 = query.subquery(Timestamp.class);
			Root<ListItemValue> ocpm2 = effectiveDate2.from(ListItemValue.class);
			effectiveDate2.select(cb.greatest(ocpm2.get("effectiveDateEnd")));
			Predicate a3 = cb.equal(c.get("itemId"),ocpm2.get("itemId"));
			Predicate a4 = cb.greaterThanOrEqualTo(ocpm2.get("effectiveDateEnd"), todayEnd);
			Predicate b3= cb.equal(c.get("companyId"),ocpm2.get("companyId"));
			Predicate b4= cb.equal(c.get("branchCode"),ocpm2.get("branchCode"));
			effectiveDate2.where(a3,a4,b3,b4);
						
			// Where
			Predicate n1 = cb.equal(c.get("status"),"Y");
			Predicate n2 = cb.equal(c.get("effectiveDateStart"),effectiveDate);
			Predicate n3 = cb.equal(c.get("effectiveDateEnd"),effectiveDate2);	
			Predicate n4 = cb.equal(c.get("companyId"),insuranceId);
		//	Predicate n5 = cb.equal(c.get("companyId"), "99999");
			Predicate n6 = cb.equal(c.get("branchCode"),branchCode);
			Predicate n7 = cb.equal(c.get("branchCode"), "99999");
		//	Predicate n8 = cb.or(n4,n5);
			Predicate n9 = cb.or(n6,n7);
			Predicate n10 = cb.equal(c.get("itemType"),itemType);
			query.where(n1,n2,n3,n4,n9,n10).orderBy(orderList);
			// Get Result
			TypedQuery<ListItemValue> result = em.createQuery(query);
			list = result.getResultList();
			
			list = list.stream().filter(distinctByKey(o -> Arrays.asList(o.getItemCode()))).collect(Collectors.toList());
			list.sort(Comparator.comparing(ListItemValue :: getItemValue));
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception is ---> " + e.getMessage());
			return null;
		}
		return list ;
	}
	
	private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	
	public void factorInsert (FactorRateSaveReq req) {
		try {
			/*Integer sNo = 0;
			for ( FactorParamsInsert data :  req.getFactorParams() ) {
				FactorRateMaster saveData = new FactorRateMaster();
				// Save New Records
				sNo = sNo + 1 ;
				saveData = dozerMapper.map(req, FactorRateMaster.class );
				saveData.setFactorTypeId(Integer.valueOf(req.getFactorTypeId()));
				saveData.setEffectiveDateStart(req.getEffectiveDateStart());
				saveData.setEffectiveDateEnd(req.getEffectiveDateEnd());
				saveData.setEntryDate(new Date());
				saveData.setSubCoverId(req.getSubCoverYn().equalsIgnoreCase("Y") ? Integer.valueOf(req.getSubCoverId()) : 0 );
				saveData.setAmendId(req.getAmendId());
				saveData.setStatus(req.getStatus().equalsIgnoreCase("P")?"P" : data.getStatus());		
				saveData.setSNo(sNo);
				saveData.setParam1(StringUtils.isBlank(data.getParam1()) ? null :Double.valueOf(data.getParam1()) );
				saveData.setParam2(StringUtils.isBlank(data.getParam2()) ? null :Double.valueOf(data.getParam2()) );
				saveData.setParam3(StringUtils.isBlank(data.getParam3()) ? null :Double.valueOf(data.getParam3()) );
				saveData.setParam4(StringUtils.isBlank(data.getParam4()) ? null :Double.valueOf(data.getParam4()) );
				saveData.setParam5(StringUtils.isBlank(data.getParam5()) ? null :Double.valueOf(data.getParam5()) );
				saveData.setParam6(StringUtils.isBlank(data.getParam6()) ? null :Double.valueOf(data.getParam6()) );
				saveData.setParam7(StringUtils.isBlank(data.getParam7()) ? null :Double.valueOf(data.getParam7()) );
				saveData.setParam8(StringUtils.isBlank(data.getParam8()) ? null :Double.valueOf(data.getParam8()) );
				saveData.setParam9(data.getParam9());
				saveData.setParam10(data.getParam10());
				saveData.setParam11(data.getParam11());
				saveData.setParam12(data.getParam12());
				saveData.setRate(StringUtils.isBlank(data.getRate()) ? null :Double.valueOf(data.getRate()) );
				saveData.setMinPremium(StringUtils.isBlank(data.getMinimumPremium()) ? null :Double.valueOf(data.getMinimumPremium()) );
				saveData.setCalcType(data.getCalType() );
				//saveData.setCalcTypeDesc(calcTypes.stream().filter( o -> o.getItemCode().equalsIgnoreCase(data.getCalType()) ).collect(Collectors.toList()).get(0).getItemValue());
				saveData.setStatus(StringUtils.isBlank(data.getStatus()) ? req.getStatus()  : data.getStatus());
				saveData.setAgencyCode(StringUtils.isBlank(req.getAgencyCode()) ? "99999" : req.getAgencyCode());
				saveData.setBranchCode(StringUtils.isBlank(req.getBranchCode()) ? "99999"  : req.getBranchCode());
				//saveData.setCoverName(coverlist.size()>0 ? coverlist.get(0).getCoverName() : "") ;
				//saveData.setCoverDesc(coverlist.size()>0 ? coverlist.get(0).getCoverDesc() : "") ;
				//saveData.setSubCoverName(coverlist.size()>0 ? coverlist.get(0).getSubCoverName() : "") ;
				//saveData.setSubCoverDesc(coverlist.size()>0 ? coverlist.get(0).getSubCoverDesc() : "") ;
				//saveData.setFactorTypeName(factorTypes.size()>0 ? factorTypes.get(0).getFactorTypeName() : "");
				//saveData.setFactorTypeDesc(factorTypes.size()>0 ? factorTypes.get(0).getFactorTypeDesc(): "");
				saveData.setRegulatoryCode(data.getRegulatoryCode());
				factorRepo.saveAndFlush(saveData);
				//log.info("Saved Details is ---> " + json.toJson(saveData));
				
			}*/
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public com.maan.eway.res.CommonRes  getTranactionByTranId(String tranId) {
		com.maan.eway.res.CommonRes res = new com.maan.eway.res.CommonRes ();
		try {
			TransactionControlDetails transaction =transactionConRepo.findByRequestReferenceNo(tranId);
			TransactionResponse response =TransactionResponse.builder()
					.description(transaction.getProgressDescription())
					.status(transaction.getStatus())
					.errorRecord(transaction.getErrorRecords()==null?0:transaction.getErrorRecords())
					.validRecord(transaction.getValidRecords()==null?0:transaction.getValidRecords())
					.totalRecord(transaction.getTotalRecords()==null?0:transaction.getTotalRecords())
					.excelFilePath(StringUtils.isBlank(transaction.getFilePath())?"":transaction.getFilePath())
					.excelFileName(StringUtils.isBlank(transaction.getFileName())?"":transaction.getFileName())
					.tranId(tranId)
					.build();
			res.setCommonResponse(response);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public com.maan.eway.res.CommonRes doMainJob(String tranId) {
		try {
			return utilService.doMainJob(tranId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
