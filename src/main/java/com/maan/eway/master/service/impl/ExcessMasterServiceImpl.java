/**
 * @author : Ashok Kumar S 
 * @since  : 26-02-2025
 */
package com.maan.eway.master.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.ExcessMaster;
import com.maan.eway.master.req.ExcessMasterGetAllReq;
import com.maan.eway.master.req.ExcessMasterGetReq;
import com.maan.eway.master.req.ExcessMasterSaveUpReq;
import com.maan.eway.master.res.ExcessMasterRes;
import com.maan.eway.master.service.ExcessMasterService;
import com.maan.eway.repository.ExcessMasterRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;

@Service
public class ExcessMasterServiceImpl  implements ExcessMasterService{

	private Logger log = LogManager.getLogger(ExcessMasterServiceImpl.class);
	
	private static final DozerBeanMapper mapper = new DozerBeanMapper();
	private static final Date DEFAULT_END_DATE = Date.from(
			LocalDate.of(2050, 12, 31).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	
	private static final String DEFAULT_COMMON_VALUE = "99999";
	
	@Autowired
	private ExcessMasterRepository excessRepo;
	
	@Autowired
	private EntityManager entityManager;
	
	/**
	 * Saves and updates a list of {@code ExcessMaster} entities.
	 * 
	 * <p>This method processes a list of {@code ExcessMasterSaveUpReq} objects. If an excess ID is null,
	 * a new {@code ExcessMaster} entity is created. If an excess ID exists, the corresponding old entity is deactivated,
	 * and a new one is created. The processed entities are then saved and flushed in the repository.</p>
	 * 
	 * @param req the list of request objects containing excess master details
	 * @return the list of saved {@code ExcessMaster} entities, or {@code null} if an exception occurs
	 */
	@Transactional(rollbackOn = Exception.class)
	public List<ExcessMaster> saveAndUpdateExcessMaster2(List<ExcessMasterSaveUpReq> req) {
		try {
			List<ExcessMaster> list = new ArrayList<>();			
			for(ExcessMasterSaveUpReq excess : req) {
				if(excess.getExcessId() == null) {
					ExcessMaster saved = excessRepo.saveAndFlush(createNewExcessMaster(excess));
					list.add(saved);
				}
				
				else {
					ExcessMaster deactivated = excessRepo.saveAndFlush(deactivateOldExcessMaster(excess));
					ExcessMaster updated = excessRepo.saveAndFlush(createNewExcessMaster(excess));
					list.add(deactivated);
					list.add(updated);
				}				
			}
			return list;
		} catch (Exception e) {
			log.error("Exception occured :{}",e.getMessage(), e);
			return null;
		}
	}
	

	@Transactional(rollbackOn = Exception.class)
	public ExcessMaster saveAndUpdateExcessMaster(ExcessMasterSaveUpReq req) {
		try {
			ExcessMaster excessMaster=new ExcessMaster();
				if(req.getExcessId() == null) {
					ExcessMaster saved = excessRepo.saveAndFlush(createNewExcessMaster(req));
					excessMaster=saved;
				}
				
				else {
					ExcessMaster deactivated = excessRepo.saveAndFlush(deactivateOldExcessMaster(req));
					ExcessMaster updated = excessRepo.saveAndFlush(createNewExcessMaster(req));
					excessMaster=updated;
				}				
			
			return excessMaster;
		} catch (Exception e) {
			log.error("Exception occured :{}",e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Retrieves all active {@code ExcessMaster} entities and maps them to a list of response objects.
	 * 
	 * <p>This method fetches all active {@code ExcessMaster} records based on company ID, product ID,
	 * and section ID. The retrieved records are mapped to {@code ExcessMasterRes} objects
	 * and returned as a list.</p>
	 * 
	 * @param req the request object containing company ID, product ID, and section ID
	 * @return a list of mapped {@code ExcessMasterRes} objects, or {@code null} if an exception occurs
	 */
	@Override
	public List<ExcessMasterRes> getAllActiveExcessMaster (ExcessMasterGetAllReq req) {
		try {
			List<ExcessMaster> allActiveExcessMaster = retrieveAllActiveExcessMaster(
					req.getCompanyId(), req.getProductId(), req.getSectionId());
			
			return allActiveExcessMaster.stream()
					.map(excessMaster -> mapper.map(excessMaster, ExcessMasterRes.class))
					.toList();
		} catch (Exception e) {
			log.error("Exception occured :{}",e.getMessage(), e);
			return null;
		}
	}


	/**
	 * Retrieves all {@code ExcessMaster} entities and maps them to a list of response objects.
	 * 
	 * <p>This method fetches all {@code ExcessMaster} records based on company ID, product ID,
	 * and section ID. The retrieved records are mapped to {@code ExcessMasterRes} objects
	 * and returned as a list.</p>
	 * 
	 * @param req the request object containing company ID, product ID, and section ID
	 * @return a list of mapped {@code ExcessMasterRes} objects, or {@code null} if an exception occurs
	 */
	@Override
	public List<ExcessMasterRes> getallExcessMaster(ExcessMasterGetAllReq req) {
		try {
			List<ExcessMaster> allExcessMaster = retrieveAllExcessMaster(
					req.getCompanyId(), req.getProductId(), req.getSectionId());
			
			return allExcessMaster.stream()
					.map(excessMaster -> mapper.map(excessMaster, ExcessMasterRes.class))
					.toList();
		} catch (Exception e) {
			log.error("Exception occured :{}",e.getMessage(), e);
			return null;
		}		
	}


	/**
	 * Retrieves an {@code ExcessMaster} entity and maps it to a response object.
	 * 
	 * <p>This method fetches a single {@code ExcessMaster} record based on company ID, product ID,
	 * section ID, and excess ID. If exactly one record is found, it is mapped to an {@code ExcessMasterRes} object
	 * and returned. If multiple or no records are found, an exception is thrown.</p>
	 * 
	 * @param req the request object containing company ID, product ID, section ID, and excess ID
	 * @return the mapped {@code ExcessMasterRes} object, or {@code null} if an exception occurs
	 * @throws IllegalArgumentException if multiple or no records are found for the given criteria
	 */
	@Override
	public ExcessMasterRes getExcessMaster (ExcessMasterGetReq req) {
		try {
			List<ExcessMaster> excessMaster = retrieveSingleExcessMaster(
					req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getExcessId());
			
			if(excessMaster.isEmpty() || excessMaster.size() != 1) {
				throw new IllegalArgumentException("For retrieving single entry size of elements must be one." );
			}
			
			return mapper.map(excessMaster.get(0), ExcessMasterRes.class);
		} catch (Exception e) {
			log.error("Exception occured :{}",e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Deactivates an existing {@code ExcessMaster} entity by updating its status and end date.
	 * 
	 * <p>This method retrieves a single {@code ExcessMaster} entry based on company ID, product ID,
	 * section ID, and excess ID. If exactly one record is found, its effective end date is set to
	 * the provided start date, and its status is updated to inactive ("N").</p>
	 * 
	 * @param req the request object containing necessary details to identify the excess master to be deactivated
	 * @return the deactivated {@code ExcessMaster} entity
	 * @throws Exception if an error occurs during the deactivation process
	 * @throws IllegalArgumentException if multiple or no records are found for the given criteria
	 */
	private ExcessMaster deactivateOldExcessMaster(ExcessMasterSaveUpReq req) throws Exception {
		List<ExcessMaster> excessMaster = retrieveSingleExcessMaster(
				req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getExcessId());
		System.out.println(excessMaster);
		if(excessMaster.isEmpty() || excessMaster.size() != 1) {
			throw new IllegalArgumentException("For retrieving single entry size of elements must be one." );
		}
		
		ExcessMaster oldExcessMaster = excessMaster.get(0);		
		oldExcessMaster.setEffectiveDateEnd( Date.from(
		        req.getEffectiveDateStart()
		           .atStartOfDay(ZoneId.systemDefault())
		           .toInstant()
		    ));
		oldExcessMaster.setStatus("N");
		return oldExcessMaster;		
	}
	
	
	/**
	 * Creates a new {@code ExcessMaster} entity based on the provided request data.
	 * 
	 * <p>This method initializes a new {@code ExcessMaster} object by setting various attributes
	 * including company ID, product ID, section ID, excess details, status, and other relevant fields.
	 * It also generates new excess and amendment IDs where applicable.</p>
	 * 
	 * @param req the request object containing necessary details for creating a new excess master
	 * @return the newly created {@code ExcessMaster} entity
	 * @throws Exception if an error occurs during the creation process
	 */
	private ExcessMaster createNewExcessMaster(ExcessMasterSaveUpReq req) throws Exception {
		ExcessMaster excessMaster = new ExcessMaster();
		
		excessMaster.setCompanyId(req.getCompanyId());
		excessMaster.setProductId(req.getProductId());
		excessMaster.setSectionId(req.getSectionId());
				
		excessMaster.setExcessId(excessIdGenerator(req));		
		excessMaster.setAmendId(amendIdGenerator(req));
		
		excessMaster.setExcessAmount(req.getExcessAmount());
		excessMaster.setExcessDescription(req.getExcessDescription());
		excessMaster.setExcessPercentage(req.getExcessPercentage() != null  ? (req.getExcessPercentage()) :null );
		excessMaster.setCurrency(req.getCurrency());
		excessMaster.setCoverName(req.getCoverId());
		excessMaster.setStatus(req.getStatus());
		
		String coverId = StringUtils.isBlank(req.getCoverId()) ? DEFAULT_COMMON_VALUE : req.getCoverId();
		excessMaster.setCoverId(coverId);		
		String branchCode = StringUtils.isBlank(req.getBranchCode()) ? DEFAULT_COMMON_VALUE : req.getBranchCode();
		excessMaster.setBranchCode(branchCode);
		
		String regulatoryCode = StringUtils.isBlank(req.getRegulatoryCode()) ? DEFAULT_COMMON_VALUE : req.getRegulatoryCode();
		excessMaster.setRegulatoryCode(regulatoryCode);		
		String coreAppCode = StringUtils.isBlank(req.getCoreAppCode()) ? DEFAULT_COMMON_VALUE : req.getCoreAppCode();
		excessMaster.setCoreAppCode(coreAppCode);
				
		excessMaster.setCreatedBy(req.getCreatedBy());
		excessMaster.setEffectiveDateStart( Date.from(
		        req.getEffectiveDateStart()
		           .atStartOfDay(ZoneId.systemDefault())
		           .toInstant()
		    ));
		excessMaster.setEffectiveDateEnd(DEFAULT_END_DATE);		
		excessMaster.setEntryDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				
		return excessMaster;
	}
	
	
	/**
	 * Generates a new excess ID for an {@code ExcessMaster} entity.
	 * 
	 * <p>If an excess ID is provided in the request, it is returned as is. Otherwise, the method
	 * retrieves the highest existing excess ID for a given company, product, and section.
	 * If no record is found, it returns 1. Otherwise, it returns the next incremented excess ID.</p>
	 * 
	 * @param req the request object containing company ID, product ID, section ID, and optional excess ID
	 * @return the generated excess ID
	 */
	private Integer excessIdGenerator(ExcessMasterSaveUpReq req) {
		if(req.getExcessId() != null) {
			return req.getExcessId();
		}
		
		ExcessMaster topExcess = excessRepo.findTopByCompanyIdAndProductIdAndSectionIdOrderByExcessIdDesc(
				req.getCompanyId(), req.getProductId(), req.getSectionId());
		
		if(topExcess == null) {	return 1; }
		
		else { return topExcess.getExcessId() + 1; }
	}
	
		
	/**
	 * Generates a new amendment ID for an {@code ExcessMaster} entity.
	 * 
	 * <p>This method retrieves the highest existing amendment ID for a given company, product,
	 * and section. If no record is found, it returns 0. Otherwise, it returns the next incremented
	 * amendment ID.</p>
	 * 
	 * @param req the request object containing company ID, product ID, and section ID
	 * @return the generated amendment ID
	 */
	private Integer amendIdGenerator(ExcessMasterSaveUpReq req) {
		
		ExcessMaster topExcess = excessRepo.findTopByCompanyIdAndProductIdAndSectionIdAndExcessIdOrderByAmendIdDesc(
				req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getExcessId());
		
		if(topExcess == null) {	return 0; }
		
		else { return topExcess.getAmendId() + 1; }		
	}

	
	/**
	 * Retrieves a list of all {@code ExcessMaster} entities based on the specified criteria.
	 * 
	 * <p>This method fetches all excess master records filtered by company ID, product ID,
	 * and section ID. It ensures that the records have the latest amendment ID </p>
	 * 
	 * @param companyId  the company identifier
	 * @param productId  the product identifier
	 * @param sectionId  the section identifier
	 * @return a list of all {@code ExcessMaster} records
	 * @throws Exception if an error occurs during query execution
	 */
	private List<ExcessMaster> retrieveAllExcessMaster(
			String companyId, String productId, String sectionId) throws Exception {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ExcessMaster> query = cb.createQuery(ExcessMaster.class);
		
	    // Define the root entity from which the query will be executed
		Root<ExcessMaster> excessRoot = query.from(ExcessMaster.class);
		
	    // Subquery to find the maximum amendment ID
		Subquery<Integer> maxAmendId = query.subquery(Integer.class);	
		Root<ExcessMaster> subRoot = maxAmendId.from(ExcessMaster.class);
		
		maxAmendId.select(cb.max(subRoot.get("amendId")))
			.where(
					cb.equal(excessRoot.get("companyId"), subRoot.get("companyId")),
					cb.equal(excessRoot.get("productId"), subRoot.get("productId")),
					cb.equal(excessRoot.get("sectionId"), subRoot.get("sectionId")),
					cb.equal(excessRoot.get("coverId"), subRoot.get("coverId")),
					cb.equal(excessRoot.get("excessId"), subRoot.get("excessId"))					
			);
		
		// Define filters for the query
		Predicate [] filters = new Predicate[] {
				cb.equal(excessRoot.get("companyId"), companyId),
				cb.equal(excessRoot.get("productId"), productId),
				cb.equal(excessRoot.get("sectionId"), sectionId),
				cb.equal(excessRoot.get("amendId"), maxAmendId)
		};
				
	    // Build and execute the query
		query.select(excessRoot)
			.where(cb.and(filters))
			.orderBy(cb.asc(excessRoot.get("excessDescription")));
		
		return entityManager.createQuery(query).getResultList();		
	}
	

	/**
	 * Retrieves a list of active {@code ExcessMaster} entities based on the specified criteria.
	 * 
	 * <p>This method fetches all active excess master records filtered by company ID, product ID,
	 * and section ID. It ensures that the records have the latest amendment ID and fall within
	 * the effective date range.</p>
	 * 
	 * @param companyId  the company identifier
	 * @param productId  the product identifier
	 * @param sectionId  the section identifier
	 * @return a list of active {@code ExcessMaster} records
	 * @throws Exception if an error occurs during query execution
	 */
	private List<ExcessMaster> retrieveAllActiveExcessMaster(
			String companyId, String productId, String sectionId) throws Exception {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ExcessMaster> query = cb.createQuery(ExcessMaster.class);
		
	    // Define the root entity from which the query will be executed
		Root<ExcessMaster> excessRoot = query.from(ExcessMaster.class);
		
	    // Subquery to find the maximum amendment ID
		Subquery<Integer> maxAmendId = query.subquery(Integer.class);	
		Root<ExcessMaster> subRoot = maxAmendId.from(ExcessMaster.class);
		
		maxAmendId.select(cb.max(subRoot.get("amendId")))
			.where(
					cb.equal(excessRoot.get("companyId"), subRoot.get("companyId")),
					cb.equal(excessRoot.get("productId"), subRoot.get("productId")),
					cb.equal(excessRoot.get("sectionId"), subRoot.get("sectionId")),
					cb.equal(excessRoot.get("coverId"), subRoot.get("coverId")),
					cb.equal(excessRoot.get("excessId"), subRoot.get("excessId"))					
			);
		
		// Define filters for the query
		Predicate [] filters = new Predicate[] {
				cb.equal(excessRoot.get("companyId"), companyId),
				cb.equal(excessRoot.get("productId"), productId),
				cb.equal(excessRoot.get("sectionId"), sectionId),
				cb.equal(excessRoot.get("status"), "Y"), 	//"Y" means active
				cb.equal(excessRoot.get("amendId"), maxAmendId),
				cb.lessThanOrEqualTo(excessRoot.get("effectiveDateStart"), LocalDateTime.now()),
				cb.greaterThanOrEqualTo(excessRoot.get("effectiveDateEnd"), LocalDateTime.now())
		};
				
	    // Build and execute the query
		query.select(excessRoot)
			.where(cb.and(filters))
			.orderBy(cb.asc(excessRoot.get("excessDescription")));
		
		return entityManager.createQuery(query).getResultList();		
	}
	
	
	/**
	 * Retrieves a list of {@code ExcessMaster} entities that match the specified criteria.
	 * 
	 * <p>This method fetches a single excess master record based on company ID, product ID,
	 * section ID, and excess ID. It ensures that the retrieved record has the latest amendment ID.</p>
	 * 
	 * @param companyId  the company identifier
	 * @param productId  the product identifier
	 * @param sectionId  the section identifier
	 * @param excessId   the excess identifier
	 * @return a list of matching {@code ExcessMaster} records
	 * @throws Exception if an error occurs during query execution
	 */
	private List<ExcessMaster> retrieveSingleExcessMaster(
			String companyId, String productId, String sectionId, Integer excessId) throws Exception {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ExcessMaster> query = cb.createQuery(ExcessMaster.class);
		
	    // Define the root entity from which the query will be executed
		Root<ExcessMaster> excessRoot = query.from(ExcessMaster.class);
		
	    // Subquery to find the maximum amendment ID
		Subquery<Integer> maxAmendId = query.subquery(Integer.class);	
		Root<ExcessMaster> subRoot = maxAmendId.from(ExcessMaster.class);
		
		maxAmendId.select(cb.max(subRoot.get("amendId")))
			.where(
					cb.equal(excessRoot.get("companyId"), subRoot.get("companyId")),
					cb.equal(excessRoot.get("productId"), subRoot.get("productId")),
					cb.equal(excessRoot.get("sectionId"), subRoot.get("sectionId")),
					cb.equal(excessRoot.get("coverId"), subRoot.get("coverId")),
					cb.equal(excessRoot.get("excessId"), subRoot.get("excessId"))					
			);
		
		// Define filters for the query
		Predicate [] filters = new Predicate[] {
				cb.equal(excessRoot.get("companyId"), companyId),
				cb.equal(excessRoot.get("productId"), productId),
				cb.equal(excessRoot.get("sectionId"), sectionId),
				cb.equal(excessRoot.get("excessId"), excessId),
				cb.equal(excessRoot.get("amendId"), maxAmendId)
		};
				
	    // Build and execute the query
		query.select(excessRoot)
			.where(cb.and(filters));
		
		return entityManager.createQuery(query).getResultList();		
	}
	
	
	

}
