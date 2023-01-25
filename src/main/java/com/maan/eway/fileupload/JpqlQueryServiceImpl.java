package com.maan.eway.fileupload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.maan.eway.bean.FactorRateMaster;
import com.maan.eway.bean.FactorTypeDetails;
import com.maan.eway.bean.SectionCoverMaster;

@Component
public class JpqlQueryServiceImpl {
	
	Logger log =LogManager.getLogger(getClass());

	private  Query query =null;
	
	@PersistenceContext
	private EntityManager em;
	
	private static Gson json = new Gson();

	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getFactorXlColumns(FileDownloadRequest req) {
		StringJoiner select_columns = new StringJoiner(",");
		StringJoiner display_columns = new StringJoiner(",");
		Map<String,Object> res =new HashMap<String,Object>();
		String factorId ="";
		try {
			query =em.createQuery("select s from SectionCoverMaster s where s.companyId=:companyId and s.productId=:productId and s.coverId=:coverId and s.sectionId=:sectionId and s.subCoverId=:subCoverId and sysdate() between effectiveDateStart and effectiveDateEnd and s.amendId=(SELECT MAX(amendId) FROM SectionCoverMaster WHERE"
					+ " companyId=s.companyId AND productId=s.productId AND coverId=s.coverId AND "
					+ " sectionId=s.sectionId AND subCoverId=s.subCoverId and sysdate() between effectiveDateStart and effectiveDateEnd )");
			query.setParameter("companyId", req.getCompanyId());
			query.setParameter("productId", Integer.valueOf(req.getProductId()));
			query.setParameter("coverId", Integer.valueOf(req.getCoverId()));
			query.setParameter("sectionId", Integer.valueOf(req.getSectionId()));
			query.setParameter("subCoverId", Integer.valueOf(req.getSubCoverId()));
			List<SectionCoverMaster> sectionMaster=query.getResultList(); 
			if(!CollectionUtils.isEmpty(sectionMaster)) {
				factorId =StringUtils.isBlank(sectionMaster.get(0).getFactorTypeId().toString())?"":sectionMaster.get(0).getFactorTypeId().toString();
				log.info("FactorTypeId : "+factorId);
				query=em.createQuery("select f from FactorTypeDetails f where f.companyId=:companyId and f.productId=:productId and f.factorTypeId=:factorTypeId and sysdate() between effectiveDateStart and effectiveDateEnd"
						+ " and f.amendId =(select max(amendId) from FactorTypeDetails where companyId=f.companyId and productId=f.productId and factorTypeId=f.factorTypeId and  sysdate() between effectiveDateStart and effectiveDateEnd ) order by columnsId desc");
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
			query=em.createQuery("select " +columns+ ",rate,calcType,minPremium,regulatoryCode,status from FactorRateMaster where  companyId=:companyId "
					+ "and productId=:productId and factorTypeId=:factorTypeId and coverId=:coverId and sectionId=:sectionId and agencyCode=:agencyCode and branchCode=:branchCode and"
					+ " sysdate() between effectiveDateStart and effectiveDateEnd and subCoverId=:subCoverId");
			
			query.setParameter("companyId", req.getCompanyId());
			query.setParameter("productId", Integer.valueOf(req.getProductId()));
			query.setParameter("factorTypeId", Integer.valueOf(factorId));
			query.setParameter("coverId", Integer.valueOf(req.getCoverId()));
			query.setParameter("sectionId", Integer.valueOf(req.getSectionId()));
			query.setParameter("agencyCode", req.getAgencyCode());
			query.setParameter("branchCode", req.getBranchCode());
			query.setParameter("subCoverId", Integer.valueOf(req.getSubCoverId()));
			object =query.getResultList();
			log.info("getFactorRateDetails Response || "+json.toJson(object));
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
			query=em.createQuery("select f from FactorTypeDetails f where f.companyId=:companyId and f.productId=:productId and f.factorTypeId=:factorTypeId"
					+ " and sysdate() between f.effectiveDateStart and f.effectiveDateEnd and f.amendId=(select max(amendId) from FactorTypeDetails where "
					+ " companyId=f.companyId and productId=f.productId and factorTypeId=f.factorTypeId and ratingFieldId=f.ratingFieldId) order by f.columnsId desc");
		
			query.setParameter("companyId", req.getInsuranceId());
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
			
			query =em.createQuery("select f  from FactorRateMaster f where f.companyId=:companyId and f.productId=:productId and f.coverId=:coverId and f.sectionId=:sectionId"
					+ " and sysdate() between f.effectiveDateStart and f.effectiveDateEnd and f.subCoverId=:subCoverId and f.amendId=(select max(amendId) from FactorRateMaster where companyId=f.companyId and productId=f.productId"
					+ " and coverId=f.coverId and sectionId=f.sectionId and sysdate() between effectiveDateStart and effectiveDateEnd and subCoverId=f.subCoverId)");

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
			query =em.createQuery("select s from SectionCoverMaster s where s.companyId=:companyId and s.productId=:productId and s.coverId=:coverId and s.sectionId=:sectionId and s.subCoverId=:subCoverId and sysdate() between effectiveDateStart and effectiveDateEnd and s.amendId=(SELECT MAX(amendId) FROM SectionCoverMaster WHERE"
					+ " companyId=s.companyId AND productId=s.productId AND coverId=s.coverId AND "
					+ " sectionId=s.sectionId AND subCoverId=s.subCoverId and sysdate() between effectiveDateStart and effectiveDateEnd )");
			query.setParameter("companyId", req.getInsuranceId());
			query.setParameter("productId", Integer.valueOf(req.getProductId()));
			query.setParameter("coverId", Integer.valueOf(req.getCoverId()));
			query.setParameter("sectionId", Integer.valueOf(req.getSectionId()));
			query.setParameter("subCoverId", StringUtils.isBlank(req.getSubCoverId())?Integer.valueOf("0"):Integer.valueOf(req.getSubCoverId()));
			sectionMaster=query.getResultList(); 
			
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return sectionMaster;
	}
	
	
	
	
	
	
	
	
	
}
