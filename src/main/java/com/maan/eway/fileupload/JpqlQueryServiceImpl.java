package com.maan.eway.fileupload;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.maan.eway.bean.FactorRateMaster;
import com.maan.eway.bean.FactorTypeDetails;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.PolicyCoverData;
import com.maan.eway.bean.PremiaConfigDataMaster;
import com.maan.eway.bean.SectionCoverMaster;
import com.maan.eway.chart.ChartAccountChildMaster;
import com.maan.eway.chart.ChartAccountChildMasterRepository;
import com.maan.eway.chart.ChartChildRequest;
import com.maan.eway.chart.ChartParentMaster;
import com.maan.eway.chart.ChartParentMasterRepository;
import com.maan.eway.chart.ChartParentRequest;
import com.maan.eway.chart.ChildChartInfoReq;
import com.maan.eway.embedded.EmbeddedReq;
import com.maan.eway.embedded.GroupMedicalDetails;
import com.maan.eway.springbatch.FactorRateRawInsert;

@Component
public class JpqlQueryServiceImpl {
	
	Logger log =LogManager.getLogger(getClass());

	private  Query query =null;
	
	@PersistenceContext
	private EntityManager em;
	
	public static Gson json = new Gson();

	public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	private ChartParentMasterRepository chartParnetRepo;
	
	@Autowired
	private ChartAccountChildMasterRepository chartChildRepo;
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getFactorXlColumns(FileDownloadRequest req) {
		StringJoiner select_columns = new StringJoiner(",");
		StringJoiner display_columns = new StringJoiner(",");
		Map<String,Object> res =new HashMap<String,Object>();
		String factorId ="";
		try {
//			query =em.createQuery("select s from SectionCoverMaster s where s.companyId=:companyId and s.productId=:productId and s.coverId=:coverId and s.sectionId=:sectionId and s.subCoverId=:subCoverId and sysdate() between effectiveDateStart and effectiveDateEnd and s.amendId=(SELECT MAX(amendId) FROM SectionCoverMaster WHERE"
//					+ " companyId=s.companyId AND productId=s.productId AND coverId=s.coverId AND "
//					+ " sectionId=s.sectionId AND subCoverId=s.subCoverId and sysdate() between effectiveDateStart and effectiveDateEnd )");
			query = em.createQuery("select s from SectionCoverMaster s where s.companyId = :companyId and s.productId = :productId and s.coverId = :coverId and s.sectionId = :sectionId and s.subCoverId = :subCoverId and sysdate() between s.effectiveDateStart and s.effectiveDateEnd and s.amendId = (SELECT MAX(s2.amendId) FROM SectionCoverMaster s2 WHERE s2.companyId = s.companyId AND s2.productId = s.productId AND s2.coverId = s.coverId AND s2.sectionId = s.sectionId AND s2.subCoverId = s.subCoverId and sysdate() between s2.effectiveDateStart and s2.effectiveDateEnd)");
			query.setParameter("companyId", req.getCompanyId());
			query.setParameter("productId", Integer.valueOf(req.getProductId()));
			query.setParameter("coverId", Integer.valueOf(req.getCoverId()));
			query.setParameter("sectionId", Integer.valueOf(req.getSectionId()));
			query.setParameter("subCoverId", StringUtils.isBlank(req.getSubCoverId())?0:Integer.valueOf(req.getSubCoverId()));
			List<SectionCoverMaster> sectionMaster=query.getResultList(); 
			String minimum_rateyn ="N";
			if(!CollectionUtils.isEmpty(sectionMaster)) {
				factorId =StringUtils.isBlank(sectionMaster.get(0).getFactorTypeId().toString())?"":sectionMaster.get(0).getFactorTypeId().toString();
				minimum_rateyn = StringUtils.isBlank(sectionMaster.get(0).getMinimumRateYn())?"N":sectionMaster.get(0).getMinimumRateYn();
				log.info("getFactorXlColumns :: FactorTypeId : "+factorId+" && minimum_rateyn is "+minimum_rateyn+" ");
				query = em.createQuery("select f from FactorTypeDetails f where f.companyId = :companyId and f.productId = :productId and f.factorTypeId = :factorTypeId and sysdate() between f.effectiveDateStart and f.effectiveDateEnd"
				        + " and f.amendId = (select max(f2.amendId) from FactorTypeDetails f2 where f2.companyId = f.companyId and f2.productId = f.productId and f2.factorTypeId = f.factorTypeId and sysdate() between f2.effectiveDateStart and f2.effectiveDateEnd) order by f.columnsId asc");
//				query=em.createQuery("select f from FactorTypeDetails f where f.companyId=:companyId and f.productId=:productId and f.factorTypeId=:factorTypeId and sysdate() between effectiveDateStart and effectiveDateEnd"
//						+ " and f.amendId =(select max(amendId) from FactorTypeDetails where companyId=f.companyId and productId=f.productId and factorTypeId=f.factorTypeId and  sysdate() between effectiveDateStart and effectiveDateEnd ) order by columnsId asc");
				query.setParameter("companyId", req.getCompanyId());
				query.setParameter("productId", Integer.valueOf(req.getProductId()));
				query.setParameter("factorTypeId", Integer.valueOf(factorId));
				List<FactorTypeDetails> factorType=query.getResultList();
				if(!CollectionUtils.isEmpty(factorType)) {
					for (FactorTypeDetails fac :factorType) {
						String rangeYn =StringUtils.isBlank(fac.getRangeYn())?"":fac.getRangeYn();
						if(StringUtils.isNotBlank(rangeYn) && "Y".equalsIgnoreCase(rangeYn)) {
							select_columns.add(fac.getRangeFromColumn());
							select_columns.add(fac.getRangeToColumn());
							display_columns.add(fac.getFromDisplayName());
							display_columns.add(fac.getToDisplayName());
						}else if (StringUtils.isNotBlank(rangeYn) && "N".equalsIgnoreCase(rangeYn)) {
							select_columns.add(fac.getDiscreteColumn());
							display_columns.add(fac.getDiscreteDisplayName());
							
						}
					}
				}
			}
			res.put("XL_COLUMNS", display_columns.toString());
			res.put("QUERY_COLUMNS", select_columns.toString());
			res.put("FACTOR_ID", factorId);
			res.put("MINIMUM_RATEYN", minimum_rateyn);
			log.info("getFactorXlColumns Response || "+json.toJson(res));
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Object[][]> getFactorRateDetails(FileDownloadRequest req,String columns,String factorId){
		List<Object[][]> object =null;
		try {
			
			if(StringUtils.isNotBlank(req.getAgencyCode())) {
			query=em.createQuery("select "+columns+" from FactorRateMaster where  companyId=:companyId "
						+ "and productId=:productId and factorTypeId=:factorTypeId and coverId=:coverId and sectionId=:sectionId and agencyCode=:agencyCode and branchCode=:branchCode and "
						+ "sysdate() between effectiveDateStart and effectiveDateEnd and subCoverId=:subCoverId and amendId =(select max(amendId) from FactorRateMaster "
						+ "where companyId=:companyId and productId=:productId and factorTypeId=:factorTypeId and coverId=:coverId and sectionId=:sectionId and agencyCode=:agencyCode and branchCode=:branchCode and "
						+ "sysdate() between effectiveDateStart and effectiveDateEnd and subCoverId=:subCoverId)");
				
				query.setParameter("companyId", req.getCompanyId());
				query.setParameter("productId", Integer.valueOf(req.getProductId()));
				query.setParameter("factorTypeId", Integer.valueOf(factorId));
				query.setParameter("coverId", Integer.valueOf(req.getCoverId()));
				query.setParameter("sectionId", Integer.valueOf(req.getSectionId()));
				query.setParameter("agencyCode", req.getAgencyCode());
				query.setParameter("branchCode", req.getBranchCode());
				query.setParameter("subCoverId", Integer.valueOf(req.getSubCoverId()));
				object =query.getResultList();
			}else {
				query=em.createQuery("select "+columns+" from FactorRateMaster where  companyId=:companyId "
						+ "and productId=:productId and factorTypeId=:factorTypeId and coverId=:coverId and sectionId=:sectionId and branchCode=:branchCode and "
						+ "sysdate() between effectiveDateStart and effectiveDateEnd and subCoverId=:subCoverId and amendId =(select max(amendId) from FactorRateMaster "
						+ "where companyId=:companyId and productId=:productId and factorTypeId=:factorTypeId and coverId=:coverId and sectionId=:sectionId and branchCode=:branchCode and "
						+ "sysdate() between effectiveDateStart and effectiveDateEnd and subCoverId=:subCoverId)");
				
				query.setParameter("companyId", req.getCompanyId());
				query.setParameter("productId", Integer.valueOf(req.getProductId()));
				query.setParameter("factorTypeId", Integer.valueOf(factorId));
				query.setParameter("coverId", Integer.valueOf(req.getCoverId()));
				query.setParameter("sectionId", Integer.valueOf(req.getSectionId()));
				//query.setParameter("agencyCode", req.getAgencyCode());
				query.setParameter("branchCode", req.getBranchCode());
				query.setParameter("subCoverId", Integer.valueOf(req.getSubCoverId()));
				object =query.getResultList();
			}
			//log.info("getFactorRateDetails Response || "+json.toJson(object));
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public List<FactorTypeDetails> getFactorRateColumns(FileUploadInputRequest req,String factorTypeId){
		List<FactorTypeDetails> list =null;
		try {
//			query=em.createQuery("select f from FactorTypeDetails f where f.companyId=:companyId and f.productId=:productId and f.factorTypeId=:factorTypeId"
//					+ " and sysdate() between f.effectiveDateStart and f.effectiveDateEnd and f.amendId=(select max(amendId) from FactorTypeDetails where "
//					+ " companyId=f.companyId and productId=f.productId and factorTypeId=f.factorTypeId and ratingFieldId=f.ratingFieldId) order by f.columnsId asc");
			query = em.createQuery("select f from FactorTypeDetails f where f.status=:status and f.companyId = :companyId and f.productId = :productId and f.factorTypeId = :factorTypeId and sysdate() between f.effectiveDateStart and f.effectiveDateEnd and f.amendId = (select max(f2.amendId) from FactorTypeDetails f2 where f2.companyId = f.companyId and f2.productId = f.productId and f2.factorTypeId = f.factorTypeId and f2.ratingFieldId = f.ratingFieldId and sysdate() between f2.effectiveDateStart and f2.effectiveDateEnd) order by f.columnsId asc");
			query.setParameter("companyId", req.getInsuranceId());
			query.setParameter("status", "Y");
			query.setParameter("productId", Integer.valueOf(req.getProductId()));
			query.setParameter("factorTypeId", Integer.valueOf(factorTypeId));
		 list=query.getResultList();
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<FactorRateMaster> getFactorRateMasterDet(FileUploadInputRequest req){
		List<FactorRateMaster> list =null;
		try {
			
//			query =em.createQuery("select f  from FactorRateMaster f where f.companyId=:companyId and f.productId=:productId and f.coverId=:coverId and f.sectionId=:sectionId"
//					+ " and sysdate() between f.effectiveDateStart and f.effectiveDateEnd and f.subCoverId=:subCoverId and f.amendId=(select max(amendId) from FactorRateMaster where companyId=f.companyId and productId=f.productId"
//					+ " and coverId=f.coverId and sectionId=f.sectionId and sysdate() between effectiveDateStart and effectiveDateEnd and subCoverId=f.subCoverId)");
			query = em.createQuery("select f from FactorRateMaster f where f.companyId = :companyId and f.productId = :productId and f.coverId = :coverId and f.sectionId = :sectionId and sysdate() between f.effectiveDateStart and f.effectiveDateEnd and f.subCoverId = :subCoverId and f.amendId = (select max(f2.amendId) from FactorRateMaster f2 where f2.companyId = f.companyId and f2.productId = f.productId and f2.coverId = f.coverId and f2.sectionId = f.sectionId and sysdate() between f2.effectiveDateStart and f2.effectiveDateEnd and f2.subCoverId = f.subCoverId)");
			query.setParameter("companyId", req.getInsuranceId());
			query.setParameter("productId", Integer.valueOf(req.getProductId()));
			query.setParameter("coverId", Integer.valueOf(req.getCoverId()));
			query.setParameter("sectionId", Integer.valueOf(req.getSectionId()));
			query.setParameter("subCoverId", Integer.valueOf(req.getSubCoverId()));
			list =query.getResultList();
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<SectionCoverMaster> getSectionCoverMaster(FileUploadInputRequest req) {
		List<SectionCoverMaster> sectionMaster =null;
		try {
//			query =em.createQuery("select s from SectionCoverMaster s where s.companyId=:companyId and s.productId=:productId and s.coverId=:coverId and s.sectionId=:sectionId and s.subCoverId=:subCoverId and sysdate() between effectiveDateStart and effectiveDateEnd and s.amendId=(SELECT MAX(amendId) FROM SectionCoverMaster WHERE"
//					+ " companyId=s.companyId AND productId=s.productId AND coverId=s.coverId AND "
//					+ " sectionId=s.sectionId AND subCoverId=s.subCoverId and sysdate() between effectiveDateStart and effectiveDateEnd )");
			query = em.createQuery("select s from SectionCoverMaster s where s.companyId = :companyId and s.status=:status and s.productId = :productId and s.coverId = :coverId and s.sectionId = :sectionId and s.subCoverId = :subCoverId and sysdate() between s.effectiveDateStart and s.effectiveDateEnd and s.amendId = (SELECT MAX(s2.amendId) FROM SectionCoverMaster s2 WHERE s2.companyId = s.companyId AND s2.productId = s.productId AND s2.coverId = s.coverId AND s2.sectionId = s.sectionId AND s2.subCoverId = s.subCoverId and sysdate() between s2.effectiveDateStart and s2.effectiveDateEnd)");
			query.setParameter("companyId", req.getInsuranceId());
			query.setParameter("productId", Integer.valueOf(req.getProductId()));
			query.setParameter("coverId", Integer.valueOf(req.getCoverId()));
			query.setParameter("sectionId", Integer.valueOf(req.getSectionId()));
			query.setParameter("status", "Y");
			query.setParameter("subCoverId", StringUtils.isBlank(req.getSubCoverId())?Integer.valueOf("0"):Integer.valueOf(req.getSubCoverId()));
			sectionMaster=query.getResultList(); 
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return sectionMaster;
	}

	
	public List<Tuple> getEmbeddedDetails(EmbeddedReq req){
		List<Tuple> tupleList = null;
		try {
			List<Predicate> predicates = new ArrayList<>();
			CriteriaBuilder cb =em.getCriteriaBuilder();	
			CriteriaQuery<Tuple> query =cb.createTupleQuery();
			Root<GroupMedicalDetails> gmd =query.from(GroupMedicalDetails.class);
			
			//predicates.add(cb.equal(gmd.get("loginId"), req.getLoginId()));
			predicates.add(cb.equal(gmd.get("companyId"), req.getCompanyId()));
			predicates.add(cb.equal(gmd.get("productId"), Long.valueOf(req.getProductId())));
			predicates.add(cb.equal(gmd.get("status"), "Y"));
			
			if(StringUtils.isNotBlank(req.getStartDate()) && StringUtils.isNotBlank(req.getEndDate())) {
				 Date startDate= sdf.parse(req.getStartDate());
				 Date endDate =sdf.parse(req.getEndDate());
				 predicates.add(cb.between(cb.currentDate(), startDate, endDate));
			
			}else if(StringUtils.isNotBlank(req.getSearchType()) && StringUtils.isNotBlank(req.getSearchValue())) {
				if("CustomerName".equalsIgnoreCase(req.getSearchType())) {
					predicates.add(cb.like(cb.trim(cb.upper(gmd.get("customerName"))), "%"+req.getSearchValue()
					.toUpperCase().trim()+"%"));
				}else if("MobileNo".equalsIgnoreCase(req.getSearchType())) {
					predicates.add(cb.equal(gmd.get("mobileNo"), req.getSearchValue()));
				}else if("TransactionNo".equalsIgnoreCase(req.getSearchType())) {
					predicates.add(cb.equal(gmd.get("clientTransactionNo"), req.getSearchValue()));
				}else if("NidaNo".equalsIgnoreCase(req.getSearchType())) {
					predicates.add(cb.equal(gmd.get("nidaNo"), req.getSearchValue()));
				}else if("PoilcyNo".equalsIgnoreCase(req.getSearchType())) {
					predicates.add(cb.equal(gmd.get("policyNo"), req.getSearchValue()));
				}else if("PolicyStartDate".equalsIgnoreCase(req.getSearchType())) {
					Date startTime =getExpectedTimeWithDate(req.getSearchValue(),0,0,0);
					Date endTime =getExpectedTimeWithDate(req.getSearchValue(),23,59,59);
					log.info("PolicyDateWithStarttime : "+startTime+" || PolicyDateWithEndtime : "+endTime+"");
					predicates.add(cb.between(gmd.get("inceptionDate"), startTime, endTime));
				}else if("RequestReferenceNo".equalsIgnoreCase(req.getSearchType())) {
					predicates.add(cb.equal(gmd.get("requestReferenceNo"), req.getSearchValue()));
				}
			
			}
			
			Predicate [] predicateArray = new Predicate[predicates.size()];
			predicates.toArray(predicateArray);
			
			Subquery<String> planType =query.subquery(String.class);
			Root<ListItemValue> liv =planType.from(ListItemValue.class);
			
			query.multiselect(gmd.get("mobileNo").alias("mobileNo"),gmd.get("inceptionDate").alias("inceptionDate"),
					gmd.get("expiryDate").alias("expiryDate"),gmd.get("requestReferenceNo").alias("requestReferenceNo"),
					gmd.get("policyNo").alias("policyNo"),gmd.get("nidaNo").alias("nidaNo"),gmd.get("loginId").alias("loginId"),
					gmd.get("applicationId").alias("applicationId"),gmd.get("productId").alias("productId"),gmd.get("sectionId")
					.alias("sectionId"),gmd.get("companyId").alias("companyId"),gmd.get("status").alias("status"),gmd.get("entryDate")
					.alias("entryDate"),gmd.get("clientTransactionNo").alias("clientTransactionNo"),gmd.get("customerName")
					.alias("customerName"),gmd.get("responsePeriod").alias("responsePeriod"),gmd.get("amountPaid").alias("amountPaid"),
					gmd.get("pdfPath").alias("pdfPath"),gmd.get("premium").alias("premium"),gmd.get("commissionPercentage")
					.alias("commissionPercentage"),gmd.get("commissionAmount").alias("commissionAmount"),gmd.get("taxPercentage")
					.alias("taxPercentage"),gmd.get("taxPremium").alias("taxPremium"),gmd.get("overallPremium").alias("overallPremium"),
					gmd.get("mobileCode").alias("mobileCode"),planType.select(liv.get("itemValue")).where(
							cb.equal(liv.get("itemType"), "PLAN_OPTED"),cb.equal(gmd.get("planOpted"), liv.get("itemCode")),
							cb.equal(liv.get("status"), "Y")).alias("planTypeDesc")
					
					).where(predicateArray).orderBy(cb.desc(gmd.get("entryDate")));
			
		 tupleList=em.createQuery(query).getResultList();
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return tupleList;
	}
	
	private  Date getExpectedTimeWithDate(String input,Integer hours,Integer min,Integer sec) {
		Date output =null;
		try {
			Date date =sdf.parse(input);
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);

	        // Set the time components to zero (midnight)
	        calendar.set(Calendar.HOUR_OF_DAY, hours);
	        calendar.set(Calendar.MINUTE, min);
	        calendar.set(Calendar.SECOND, sec);
	        calendar.set(Calendar.MILLISECOND, 0);
	        output=calendar.getTime();
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return output;
	}
	
//Premia Integration Table
	@SuppressWarnings("unchecked")
	public Map<String,Object> getPremiaXlColumns(PremiaFileDownloadRequest req,Integer premiaId) {
		StringJoiner display_columns = new StringJoiner(",");
		StringJoiner select_columns = new StringJoiner(",");
		Map<String,Object> res =new HashMap<String,Object>();
		List<PremiaConfigDataMaster> premiaList=null;
		try {
			query =em.createQuery("SELECT p FROM PremiaConfigDataMaster p where"
					+ " p.companyId=:companyId and p.productId=:productId and p.premiaId=:premiaId");
			query.setParameter("companyId", req.getCompanyId());
			query.setParameter("productId", req.getProductId());
			query.setParameter("premiaId",premiaId );
			premiaList=query.getResultList(); 
			if(CollectionUtils.isEmpty(premiaList)) {
				query =em.createQuery("SELECT p FROM PremiaConfigDataMaster p where"
						+ " p.companyId=:companyId and p.productId=:productId and p.premiaId=:premiaId");
				query.setParameter("companyId", req.getCompanyId());
				query.setParameter("productId", "99999");
				query.setParameter("premiaId",premiaId );
				premiaList=query.getResultList(); 
			}
			if(!CollectionUtils.isEmpty(premiaList)) {
					for (PremiaConfigDataMaster fac :premiaList) {
							display_columns.add(fac.getColumnName());
							select_columns.add(fac.getColumnName());
						}
			}
			res.put("QUERY_COLUMNS", select_columns.toString());
			res.put("XL_COLUMNS", display_columns.toString());
			log.info("getPremiaXlColumns Response || "+json.toJson(res));
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Object[][]> getPremiaDetails(PremiaFileDownloadRequest req,String columns,String tableString) {
		List<Object[][]> object =null;
		try {
			if(tableString.equals("PgitPolRiskAddlInfo01")) {
				tableString="PgithPolRiskAddlInfo";
				query =em.createQuery("SELECT "+columns+" FROM "+tableString+" pol "
						+ "where quotationPolicyNo=:policyNo");
				query.setParameter("policyNo", req.getPolicyNo());
			}else if(tableString.equals("CreditLimitDetail")) {
				query =em.createQuery("SELECT "+columns+" FROM "+tableString+" pol "
						+ "where customerCode=:customerId");
				query.setParameter("customerId", req.getCustomerId());
			}else {
				query =em.createQuery("SELECT "+columns+" FROM "+tableString+" pol "
						+ "where quotationPolicyNo=:policyNo");
				query.setParameter("policyNo", req.getPolicyNo());
			}
		
			object =query.getResultList();
		log.info("getPremiaDetails Response || "+json.toJson(object));
	}catch (Exception e) {
		log.error(e);
		e.printStackTrace();
	}
	return object;
	}

	public Integer getParentChartAmendId(ChartParentRequest req) {
		Integer amendId =0;
		try {
			String queryString ="select cp from ChartParentMaster cp where cp.chatParentId.companyId=:companyId "
								+ "and cp.chatParentId.chartId=:chartId and cp.chatParentId.amendId=(select max(cpm.chatParentId.amendId) from ChartParentMaster "
								+ "cpm where cp.chatParentId.companyId=cpm.chatParentId.companyId and cp.chatParentId.chartId=cpm.chatParentId.chartId)";
			
			@SuppressWarnings("unchecked")
			List<ChartParentMaster> list =em.createQuery(queryString)
					.setParameter("companyId", Integer.valueOf(req.getCompanyId()))
					.setParameter("chartId", StringUtils.isBlank(req.getChartId())?0:Integer.valueOf(req.getChartId()))
					.getResultList();
			
			if(!list.isEmpty()) {
				
			
				long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
				
				Date oldEndDate =new Date(sdf.parse(req.getEffectiveStartDate()).getTime() -MILLIS_IN_A_DAY);

				Date todayDate =new Date();
				Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
			
				
				if(list.get(0).getEffectiveStartDate().before(beforeOneDay)) {
					
					// updated existing effectiveend date
					List<ChartParentMaster> cpmList =list.stream().map(p ->{
						p.setUpdatedBy(req.getUpdatedBy());
						p.setUpdatedDate(todayDate);
						p.setEffectiveEndDate(oldEndDate);
						return chartParnetRepo.save(p);
					}).collect(Collectors.toList());
					
					amendId=cpmList.get(0).getChatParentId().getAmendId() +1;
					
				}else {
					amendId=list.get(0).getChatParentId().getAmendId();
					chartParnetRepo.deleteAll(list);
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return amendId;
	}

	public Integer getChildChartMaxOfAmendId(ChartChildRequest req) {
		Integer amendId=0;
		try {
			
			String queryString ="select ccm from ChartAccountChildMaster ccm  where ccm.id.companyId=:companyId and ccm.id.productId=:productId and ccm.id.sectionId=:sectionId "
					+ "and ccm.id.chartId=:chartId and ccm.id.coverId=:coverId and ccm.id.amendId=(select max(ccmm.id.amendId) from ChartAccountChildMaster ccmm where ccm.id.companyId=ccmm.id.companyId "
					+ "and ccm.id.productId=ccmm.id.productId and ccm.id.sectionId=ccmm.id.sectionId and ccm.id.chartId=ccmm.id.chartId and ccm.id.coverId=ccmm.id.coverId)";
			
			@SuppressWarnings("unchecked")
			List<ChartAccountChildMaster> list =em.createQuery(queryString)
					.setParameter("companyId", Integer.valueOf(req.getCompanyId()))
					.setParameter("productId", Integer.valueOf(req.getProductId()))
					.setParameter("sectionId", Integer.valueOf(req.getSectionId()))
					.setParameter("chartId", Integer.valueOf(req.getChartId()))
					.setParameter("coverId", Integer.valueOf(req.getCoverId()))
					.getResultList();
			
			if(!list.isEmpty()) {
				
				LocalDate minusEffectiveStartDate =LocalDate.parse(req.getEffectiveStartDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).minusDays(1);
				
				Date todayDate = new Date();
				
				if(list.get(0).getEffectiveStartDate().before(todayDate)) {
					
					list.stream().map(p ->{
						p.setUpdatedDate(new Date());
						p.setUpdatedBy(req.getUpdatedBy());
						p.setEffectiveEndDate(Date.from(minusEffectiveStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
						return chartChildRepo.save(p);
					}).collect(Collectors.toList());
										
					amendId =list.get(0).getId().getAmendId()+1;
				}else {
					amendId =list.get(0).getId().getAmendId();
					chartChildRepo.deleteAll(list);
				}
								
			}
		   
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);

		}
		
		return amendId;
	}

	public List<ChartAccountChildMaster> getChildChartAccountData(Integer companyId, Integer productId, Integer sectionId,
			ChartParentMaster cpmm) {
		try {
			
			String stringQuery ="select c from ChartAccountChildMaster c where c.id.companyId =:companyId and c.id.productId=:productId "
					+ "and c.id.sectionId=:sectionId and c.id.chartId=:chartId and c.status=:status and c.id.amendId=(select max(cc.id.amendId) "
					+ "from ChartAccountChildMaster cc where cc.id.companyId= c.id.companyId and cc.id.productId=c.id.productId and "
					+ "cc.id.sectionId=cc.id.sectionId and cc.id.chartId=c.id.chartId and cc.id.coverId=c.id.coverId and sysdate() between "
					+ "cc.effectiveStartDate and cc.effectiveEndDate)";
			
			@SuppressWarnings("unchecked")
			List<ChartAccountChildMaster> chilldMaster =em.createQuery(stringQuery).setParameter("companyId", companyId).setParameter("productId", productId)
			.setParameter("sectionId", sectionId).setParameter("chartId", cpmm.getChatParentId().getChartId())
			.setParameter("status", "Y").getResultList();
			
			return chilldMaster;
			
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);

		}
		return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PolicyCoverData> getPolicyCoverDataPremium(String quoteNo,List<Integer> coverIds)
	{
		try {
			
			String sqlString ="select pcd from PolicyCoverData pcd where pcd.quoteNo=:quoteNo and pcd.coverId in(:coverId)";
			List<PolicyCoverData> coverDatas =(List<PolicyCoverData>) em.createQuery(sqlString)
					.setParameter("quoteNo", quoteNo).setParameter("coverId", coverIds).getResultList();
			return coverDatas;
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);

		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<PolicyCoverData> getPolicyCoverDataTax(String quoteNo,List<Integer> coverIds)
	{
		try {
			
			String sqlString ="select pcd from PolicyCoverData pcd where pcd.quoteNo=:quoteNo and pcd.taxId in(:taxId)";
			List<PolicyCoverData> coverDatas =(List<PolicyCoverData>) em.createQuery(sqlString)
					.setParameter("quoteNo", quoteNo).setParameter("taxId", coverIds).getResultList();
			return coverDatas;
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);

		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<ChartParentMaster> getChartParentMasterDetails(Integer companyId){
		List<ChartParentMaster> list =null;
		try {
			String sql ="select pm from ChartParentMaster cpm where cpm.chatParentId.companyId=:companyId and cpm.status=:status "
					+ "and sysdate() between cpm.effectiveStartDate and cpm.effectiveEndDate and order by cpm.displayOrder";
			list =(List<ChartParentMaster>) em.createQuery(sql).setParameter("companyId", companyId).setParameter("status", "Y").getResultList();
			
			List<ChartParentMaster> filterList = new ArrayList<ChartParentMaster>();
			
				list.stream().collect(Collectors.groupingBy(p ->p.getChatParentId().getChartId()))
				.forEach((key,value) ->{
					ChartParentMaster cpm =	value.stream().collect(Collectors.maxBy((a,b) ->a.getChatParentId().getAmendId().compareTo(a.getChatParentId().getAmendId())))
					.get();
					filterList.add(cpm);
					});
				
			return filterList;
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);

		}
		return list;
	}
	
	public Integer getChildChartMaxOfAmendId1(ChildChartInfoReq req) {
		Integer amendId=0;
		try {
			
			String queryString ="select ccm from ChartAccountChildMaster ccm  where ccm.id.companyId=:companyId and ccm.id.productId=:productId and ccm.id.sectionId=:sectionId "
					+ "and ccm.id.chartId=:chartId and ccm.id.amendId=(select max(ccmm.id.amendId) from ChartAccountChildMaster ccmm where ccm.id.companyId=ccmm.id.companyId "
					+ "and ccm.id.productId=ccmm.id.productId and ccm.id.sectionId=ccmm.id.sectionId and ccm.id.chartId=ccmm.id.chartId)";
			
			@SuppressWarnings("unchecked")
			List<ChartAccountChildMaster> list =em.createQuery(queryString)
					.setParameter("companyId", Integer.valueOf(req.getCompanyId()))
					.setParameter("productId", Integer.valueOf(req.getProductId()))
					.setParameter("sectionId", Integer.valueOf(req.getSectionId()))
					.setParameter("chartId", Integer.valueOf(req.getChartId()))
					//.setParameter("coverId", Integer.valueOf(req.getCoverId()))
					.getResultList();
			
			if(!list.isEmpty()) {
				
				long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
				
				Date oldEndDate =new Date(sdf.parse(req.getEffectiveStartDate()).getTime() -MILLIS_IN_A_DAY);

				Date todayDate =new Date();
				Date beforeOneDay = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
			
				if(list.get(0).getEffectiveStartDate().before(beforeOneDay)) {
					
					list.stream().map(p ->{
						p.setUpdatedDate(todayDate);
						p.setUpdatedBy(req.getUpdatedBy());
						p.setEffectiveEndDate(oldEndDate);
						return chartChildRepo.save(p);
					}).collect(Collectors.toList());
										
					amendId =list.get(0).getId().getAmendId()+1;
				}else {
					amendId =list.get(0).getId().getAmendId();
					chartChildRepo.deleteAll(list);
				}
								
			}
		   
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);

		}
		
		return amendId;
	}
	
	public void updateErrorRecords(String tranId,List<Integer> sno,String error_desc) {
		try {
			
			String queryText ="update FactorRateRawInsert fri set fri.errorStatus=:error_status,fri.errorDesc"
					+ "=concat(concat(CASE WHEN fri.errorStatus IS NULL THEN '' ELSE fri.errorStatus END,'~'),:error_desc) where fri.tranId=:tranId and fri.sno in(:sno)";

		Integer updateCount=em.createQuery(queryText)
				.setParameter("error_status", "E")
				.setParameter("error_desc", error_desc)
				.setParameter("tranId", tranId)
				.setParameter("sno", sno)
				.executeUpdate();
			
			log.info("updateErrorRecords update rows is "+updateCount+"");
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
	}

	public void updateErrorRecords(String keyName, String factor_id, List<String> filterDuplicateSno, String error_desc) {
		try {
			
			String queryText ="update FactorRateRawInsert fri set fri.errorStatus=:error_status,fri.errorDesc"
					+ "=concat(concat(CASE WHEN fri.errorStatus IS NULL THEN '' ELSE fri.errorStatus END,'~'),:error_desc) where fri.tranId=:tranId and fri."+keyName+" in(:data)";
	
			Integer updateCount=em.createQuery(queryText)
					.setParameter("error_status", "E")
					.setParameter("error_desc", error_desc)
					.setParameter("tranId", factor_id)
					.setParameter("data", filterDuplicateSno)
					.executeUpdate();
			
			log.info("updateErrorRecords update rows is "+updateCount+"");
		}catch (Exception e) {
			e.printStackTrace();
			log.error(e);

		}
	}
		
	public List<Object[]> getRawErrorRecords(String tranId,String queryColumns){
		try {
			
			String queryText ="select errorDesc,sno,xlAgencyCode,"+queryColumns+" from FactorRateRawInsert f where f.tranId = :tranId and f.errorStatus = :errorStatus";
			query = em.createQuery(queryText);
			query.setParameter("tranId", tranId);
			query.setParameter("errorStatus", "E");
			@SuppressWarnings("unchecked")
			List<Object[]> data = query.getResultList();
			return data;
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}
	


}
