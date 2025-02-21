/**
 * @author : Ashok Kumar S 
 * @since  : 19-02-2025
 */
package com.maan.eway.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.ClausesMasterV1;
import com.maan.eway.repository.ClausesMasterV1Repository;
import com.maan.eway.req.ClausesMasterV1GetAllReq;
import com.maan.eway.req.ClausesMasterV1GetReq;
import com.maan.eway.req.ClausesMasterV1SaveUpReq;
import com.maan.eway.req.ClausesMasterV1StatusChangeReq;
import com.maan.eway.res.ClausesMasterV1Res;
import com.maan.eway.service.ClausesMasterV1Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;

@Service
public class ClausesMasterV1ServiceImpl implements ClausesMasterV1Service {
	
	private static final Logger log = LogManager.getLogger(ClausesMasterV1ServiceImpl.class);
	
	private final Integer STARTING_AMEND_ID = 0;
	private final Integer STARTING_CLAUSES_ID = 1;
	private final Integer DEFAULT_BRANCH_CODE = 99999;
	
	private final String STATUS_ACTIVE = "Y";
	private final String STATUS_INACTIVE = "N";
	
	private final LocalDate DEFAULT_EFFECTIVE_END_DATE = LocalDate.of(2050, 12, 31);
	
	private ClausesMasterV1Repository clausesMasterRepo;
	private EntityManager entityManager;
	private ModelMapper mapper;

	@Autowired
	public ClausesMasterV1ServiceImpl(ClausesMasterV1Repository clausesMasterRepo, EntityManager entityManager,
			ModelMapper mapper) {
		this.clausesMasterRepo = clausesMasterRepo;
		this.entityManager = entityManager;
		this.mapper = mapper;
	}

	/**
	 * Retrieves all {@link ClausesMasterV1} records based on the provided request parameters.
	 * <p>
	 * This method fetches all clauses using the given company, product, section, 
	 * and cover identifiers, and maps them to a list of {@link ClausesMasterV1Res} 
	 * response objects.
	 * </p>
	 *
	 * @param req the request object containing clause identifiers; must not be null.
	 * @return a list of {@link ClausesMasterV1Res} objects representing the retrieved clauses, 
	 *         or {@code null} if an exception occurs.
	 */
	@Override
	public List<ClausesMasterV1Res> getAllClausesMaster(ClausesMasterV1GetAllReq req) {
		try {
			List<ClausesMasterV1> clauses = retrieveAllClausesMaster(
					req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getCoverId());
			
			return clauses
					.stream()
					.map(clause -> mapper.map(clause, ClausesMasterV1Res.class))
					.toList();
		} catch (Exception e) {
	        log.error("Exception occurred while retrieving all clauses: {}", e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Retrieves all active {@link ClausesMasterV1} records based on the provided request.
	 * <p>
	 * The method fetches all active clauses using the given identifiers and maps them 
	 * to a list of {@link ClausesMasterV1Res} response objects.
	 * </p>
	 *
	 * @param req the request object containing clause identifiers; must not be null.
	 * @return a list of {@link ClausesMasterV1Res} objects representing the retrieved clauses, 
	 *         or {@code null} if an exception occurs.
	 */
	@Override
	public List<ClausesMasterV1Res> getAllActiveClausesMaster(ClausesMasterV1GetAllReq req) {
		try {
			List<ClausesMasterV1> activeClauses = retrieveAllActiveClausesMaster(
					req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getCoverId());
			
			return activeClauses
					.stream()
					.map(clause -> mapper.map(clause, ClausesMasterV1Res.class))
					.toList();
		} catch (Exception e) {
	        log.error("Exception occurred while retrieving active clauses: {}", e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Retrieves a single {@link ClausesMasterV1} record based on the provided request.
	 * <p>
	 * The method fetches the clause using the given identifiers. If the clause is found, 
	 * it is mapped to a {@link ClausesMasterV1Res} response object. If the clause is not 
	 * found, a {@link NoSuchElementException} is thrown.
	 * </p>
	 *
	 * @param req the request object containing clause identifiers; must not be null.
	 * @return the {@link ClausesMasterV1Res} object representing the retrieved clause, 
	 *         or {@code null} if an exception occurs.
	 * @throws NoSuchElementException if the specified clause does not exist.
	 */
	@Override
	public ClausesMasterV1Res getSingleClausesMaster(ClausesMasterV1GetReq req) {
		try {
			List<ClausesMasterV1> clause = retrieveParticularClausesMaster(
					req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getCoverId(),req.getClausesId());
			
			if(clause.isEmpty()) {
				throw new NoSuchElementException("Clauses master you are looking for was not found.");
			}
			
			return mapper.map(clause.get(0), ClausesMasterV1Res.class);			
		} catch (Exception e) {
	        log.error("Exception occurred while retrieving single clause: {}", e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * Retrieves a single {@link ClausesMasterV1} record based on the provided request.
	 * <p>
	 * The method fetches the clause using the given identifiers. If the clause is found, 
	 * it is mapped to a {@link ClausesMasterV1Res} response object. If the clause is not 
	 * found, a {@link NoSuchElementException} is thrown.
	 * </p>
	 *
	 * @param req the request object containing clause identifiers; must not be null.
	 * @return the {@link ClausesMasterV1Res} object representing the retrieved clause, 
	 *         or {@code null} if an exception occurs.
	 * @throws NoSuchElementException if the specified clause does not exist.
	 */
	@Override
	public ClausesMasterV1Res getSingleActiveClausesMaster(ClausesMasterV1GetReq req) {
		try {
			List<ClausesMasterV1> clause = retrieveParticularActiveClausesMaster(
					req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getCoverId(),req.getClausesId());
			
			if(clause.isEmpty()) {
				throw new NoSuchElementException("Clauses master you are looking for was not found.");
			}
			
			return mapper.map(clause.get(0), ClausesMasterV1Res.class);			
		} catch (Exception e) {
	        log.error("Exception occurred while retrieving single clause: {}", e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Activates or inactivates a clause in the Clauses Master based on the provided request.
	 *
	 * <p>This method retrieves the specified clause and updates its status if the requested
	 * change is valid. Activation updates the start date, while deactivation updates the end date.</p>
	 *
	 * @param req the request object containing company, product, section, cover, clause ID, and status details
	 * @return {@code true} if the status was successfully updated, {@code false} if no change was needed,
	 *         or {@code null} if an exception occurred
	 * @throws NoSuchElementException if the specified clause is not found
	 */
	@Override
	public Boolean activateOrInactivateClausesMaster(ClausesMasterV1StatusChangeReq req) {
		try {
			List<ClausesMasterV1> clausesMaster = retrieveParticularClausesMaster(
					req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getCoverId(),req.getClausesId());
			
			if(clausesMaster.isEmpty()) {
				throw new NoSuchElementException("Clauses master you are looking for was not found.");
			}
			
			ClausesMasterV1 clause = clausesMaster.get(0);
			//Activate clause
			if(STATUS_INACTIVE.equals(clause.getStatus()) && STATUS_ACTIVE.equals(req.getStatus())) {
				clause.setStatus(req.getStatus());
				clause.setUpdatedBy(req.getUpdatedBy());
				clause.setEffectiveDateStart(req.getEffectiveOn());
				clause.setEffectiveDateEnd(DEFAULT_EFFECTIVE_END_DATE);
				
				clausesMasterRepo.save(clause);
				return true;
			}
	        // Inactivate clause
			if(STATUS_ACTIVE.equals(clause.getStatus()) && STATUS_INACTIVE.equals(req.getStatus())) {
				clause.setStatus(req.getStatus());
				clause.setUpdatedBy(req.getUpdatedBy());
				clause.setEffectiveDateEnd(req.getEffectiveOn());
				
				clausesMasterRepo.save(clause);
				return true;
			}

			// failed due to setting same status			
			return false;
		} catch (Exception e) {
	        log.error("Exception occurred while updating clause status: {}", e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * Saves or updates a {@link ClausesMasterV1} record based on the provided request. 
	 * <p>
	 * If the request does not contain a clause ID, a new clause record is created and saved. 
	 * Otherwise, the existing clause record is retrieved, marked as inactive, and a new updated record is created.
	 * If the clause to be updated is not found, an exception is thrown.
	 * </p>
	 *
	 * @param req the request object containing clause details; must not be null.
	 * @return the saved or updated {@link ClausesMasterV1} instance, or {@code null} if an exception occurs.
	 * @throws NoSuchElementException if the clause to be updated does not exist.
	 */
	@Override
	@Transactional(rollbackOn = Exception.class)
	public ClausesMasterV1 saveAndUpdateClausesMasterDetails(ClausesMasterV1SaveUpReq req) {
		try {
	        // If clause ID is null, create and save a new clause
			if(req.getClausesId() == null) {
				ClausesMasterV1 saveClauseMaster = createClausesMaster(req);
				return clausesMasterRepo.save(saveClauseMaster);
			}			
			else {
	            // Retrieve the latest amendment for the given clause
				Optional<ClausesMasterV1> optOldClause = clausesMasterRepo.
						findTopByCompanyIdAndProductIdAndSectionIdAndCoverIdAndClausesIdOrderByAmendIdDesc(
								req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getCoverId(), req.getClausesId());
				
				if(optOldClause.isEmpty()) {
					throw new NoSuchElementException("Clauses master you are trying to update was not found.");
				}

	            // Disable the existing clause by setting its status to inactive and updating end date				
				deactivateClause(optOldClause.get(), req.getCreatedBy(), req.getEffectiveDateStart());
				
	            // Create and save a new updated clause
				ClausesMasterV1 updateClause = createClausesMaster(req);
				return clausesMasterRepo.save(updateClause);				
			}
		} catch (Exception e) {
	        log.error("Exception occurred while saving/updating clause: {}", e.getMessage(), e);
			return null;		
		}		
	}

	
	/**
	 * Creates a new instance of {@link ClausesMasterV1} based on the provided request data. 
	 * The clause ID and amendment ID are generated dynamically if not provided in the request. 
	 * The newly created clause is set to active status and assigned a default effective end date.
	 *
	 * @param req the request object containing clause details; must not be null.
	 * @return a new {@link ClausesMasterV1} instance populated with the provided details.
	 */
	private ClausesMasterV1 createClausesMaster(ClausesMasterV1SaveUpReq req) {
		ClausesMasterV1 clausesMaster = new ClausesMasterV1();
		
        clausesMaster.setCompanyId(req.getCompanyId());
        clausesMaster.setProductId(req.getProductId());
        clausesMaster.setSectionId(req.getSectionId());
        clausesMaster.setCoverId(req.getCoverId());
        clausesMaster.setBranchCode(DEFAULT_BRANCH_CODE);
        
        // Generate and assign clause ID and amendment ID
        clausesMaster.setClausesId(generateClausesId(req));
        clausesMaster.setAmendId(generateAmendId(req));
        
        clausesMaster.setClausesShortDesc(req.getClausesShortDesc());
        clausesMaster.setClausesDescription(req.getClausesDescription());
        
        clausesMaster.setStatus(STATUS_ACTIVE);
        clausesMaster.setCreatedBy(req.getCreatedBy());
        
        clausesMaster.setEffectiveDateStart(req.getEffectiveDateStart());
        clausesMaster.setEffectiveDateEnd(DEFAULT_EFFECTIVE_END_DATE);
		
		return clausesMaster;
	}
	
	/**
	 * Marks the given clause as inactive by updating its status and end date.
	 *
	 * @param clause       the clause to deactivate; must not be null.
	 * @param updatedBy    the user performing the update; must not be null.
	 * @param endDate      the new effective end date; must not be null.
	 */
	private ClausesMasterV1 deactivateClause(ClausesMasterV1 clause, String updatedBy, LocalDate endDate) {
	    clause.setStatus(STATUS_INACTIVE);
	    clause.setUpdatedBy(updatedBy);
	    clause.setEffectiveDateEnd(endDate);
	    return clausesMasterRepo.save(clause);
	}

	/**
	 * Generates a new clause ID based on the provided request. 
	 * If the request already contains a clause ID, it is returned as is. 
	 * Otherwise, retrieves the highest existing clause ID for the given company, product, section, and cover, 
	 * then increments it by one. If no existing clause is found, returns the starting clause ID.
	 *
	 * @param req the request object containing clause details; must not be null.
	 * @return the new clause ID, either from the request, incremented from the latest existing clause, 
	 *         or the default starting clause ID if no prior clause exists.
	 */
	private Integer generateClausesId(ClausesMasterV1SaveUpReq req) {
		// For modifying existing clause
		if(req.getClausesId() != null) {
			return req.getClausesId();
		}
		// For newly create clause
		Optional<ClausesMasterV1> topClause = clausesMasterRepo.
				findTopByCompanyIdAndProductIdAndSectionIdAndCoverIdOrderByClausesIdDescAmendIdDesc(
						req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getCoverId());
		
		return topClause
				.map(clause -> clause.getClausesId()+1)
				.orElse(STARTING_CLAUSES_ID);
	}
	
	
	/**
	 * Generates a new amendment ID for a given clause. 
	 * If the clause ID is null, returns the default amendment ID. 
	 * Otherwise, retrieves the latest amendment ID for the clause and increments it by one. 
	 * If no existing amendment is found, returns -1. (never occurs).
	 * 
	 * @param req the request object containing clause details; must not be null.
	 * @return the new amendment ID, either incremented from the latest existing amendment or -1 if no prior amendment exists.
	 */
	private Integer generateAmendId(ClausesMasterV1SaveUpReq req) {
		// For newly create clause
		if(req.getClausesId() == null) {
			return STARTING_AMEND_ID;
		}
		
		// For modifying existing clause
		Optional<ClausesMasterV1> oldClause = clausesMasterRepo.
				findTopByCompanyIdAndProductIdAndSectionIdAndCoverIdAndClausesIdOrderByAmendIdDesc(
						req.getCompanyId(), req.getProductId(), req.getSectionId(), req.getCoverId(), req.getClausesId());
		
		return oldClause
				.map(clause -> clause.getAmendId() + 1)
				.orElse(-1);
	}
		
	
	/**
	 * Retrieves a list of {@link ClausesMasterV1} entities based on the specified company, product, section, and cover IDs.
	 * It ensures that only the records with the latest amendment ID are fetched.
	 *
	 * @param companyId the ID of the company for which clauses are retrieved; must not be null.
	 * @param productId the ID of the product associated with the clauses; must not be null.
	 * @param sectionId the ID of the section under the product; must not be null.
	 * @param coverId the ID of the cover linked to the section; must not be null.
	 * @return a list of {@link ClausesMasterV1} objects matching the given criteria.
	 * @throws Exception if any error occurs during query execution.
	 */
	private List<ClausesMasterV1> retrieveAllClausesMaster(
	        Integer companyId, Integer productId, Integer sectionId, Integer coverId) throws Exception {
	    
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<ClausesMasterV1> query = cb.createQuery(ClausesMasterV1.class);
	    
	    // Define the root entity from which the query will be executed
	    Root<ClausesMasterV1> clausesRoot = query.from(ClausesMasterV1.class);
	    
	    // Subquery to find the maximum amendment ID for each clause
	    Subquery<Integer> maxAmendId = query.subquery(Integer.class);
	    Root<ClausesMasterV1> subRoot = maxAmendId.from(ClausesMasterV1.class);
	    
	    // Select the maximum amendment ID where company, product, section, cover, and clause IDs match
	    maxAmendId.select(cb.max(subRoot.get("amendId")))
	        .where(
	            cb.equal(clausesRoot.get("companyId"), subRoot.get("companyId")),
	            cb.equal(clausesRoot.get("productId"), subRoot.get("productId")),
	            cb.equal(clausesRoot.get("sectionId"), subRoot.get("sectionId")),
	            cb.equal(clausesRoot.get("coverId"), subRoot.get("coverId")),
	            cb.equal(clausesRoot.get("clausesId"), subRoot.get("clausesId")),
	            cb.equal(clausesRoot.get("branchCode"), subRoot.get("branchCode"))
	        );
	    
	    // Define the predicates (filter conditions) for the main query
	    Predicate[] filters = new Predicate[] {
	        cb.equal(clausesRoot.get("companyId"), companyId),
	        cb.equal(clausesRoot.get("productId"), productId),
	        cb.equal(clausesRoot.get("sectionId"), sectionId),
	        cb.equal(clausesRoot.get("coverId"), coverId),
	        cb.equal(clausesRoot.get("amendId"), maxAmendId)
	    };
	    
	    // Construct the query with selected filters and Execute the query and return the result list
	    query.select(clausesRoot)
	        .where(cb.and(filters))
	        .orderBy(cb.asc(clausesRoot.get("clausesId")));
	    
	    return entityManager.createQuery(query).getResultList();
	}

	
	/**
	 * Retrieves a list of active {@link ClausesMasterV1} entities based on the specified company, product, section, and cover IDs.
	 * This method ensures that only the latest amendment records within the effective date range and with an active status are fetched.
	 *
	 * @param companyId the ID of the company for which active clauses are retrieved; must not be null.
	 * @param productId the ID of the product associated with the clauses; must not be null.
	 * @param sectionId the ID of the section under the product; must not be null.
	 * @param coverId the ID of the cover linked to the section; must not be null.
	 * @return a list of {@link ClausesMasterV1} objects that match the given criteria and are currently active.
	 * @throws Exception if any error occurs during query execution.
	 */
	private List<ClausesMasterV1> retrieveAllActiveClausesMaster(
	        Integer companyId, Integer productId, Integer sectionId, Integer coverId) throws Exception {
	    
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<ClausesMasterV1> query = cb.createQuery(ClausesMasterV1.class);
	    
	    // Define the root entity from which the query will be executed
	    Root<ClausesMasterV1> clausesRoot = query.from(ClausesMasterV1.class);
	    
	    // Subquery to find the maximum amendment ID for each clause
	    Subquery<Integer> maxAmendId = query.subquery(Integer.class);
	    Root<ClausesMasterV1> subRoot = maxAmendId.from(ClausesMasterV1.class);
	    
	    // Select the maximum amendment ID where company, product, section, cover, and clause IDs match
	    maxAmendId.select(cb.max(subRoot.get("amendId")))
	        .where(
	            cb.equal(clausesRoot.get("companyId"), subRoot.get("companyId")),
	            cb.equal(clausesRoot.get("productId"), subRoot.get("productId")),
	            cb.equal(clausesRoot.get("sectionId"), subRoot.get("sectionId")),
	            cb.equal(clausesRoot.get("coverId"), subRoot.get("coverId")),
	            cb.equal(clausesRoot.get("clausesId"), subRoot.get("clausesId")),
	            cb.equal(clausesRoot.get("branchCode"), subRoot.get("branchCode"))
	        );
	    
	    // Define the predicates (filter conditions) for the main query
	    Predicate[] filters = new Predicate[] {
	        cb.equal(clausesRoot.get("companyId"), companyId),
	        cb.equal(clausesRoot.get("productId"), productId),
	        cb.equal(clausesRoot.get("sectionId"), sectionId),
	        cb.equal(clausesRoot.get("coverId"), coverId),
	        cb.lessThanOrEqualTo(clausesRoot.get("effectiveDateStart"), LocalDate.now()),
	        cb.greaterThanOrEqualTo(clausesRoot.get("effectiveDateEnd"), LocalDate.now()),
	        cb.equal(clausesRoot.get("status"), STATUS_ACTIVE),
	        cb.equal(clausesRoot.get("amendId"), maxAmendId)
	    };
	    
	    // Construct the query with selected filters and Execute the query and return the result list
	    query.select(clausesRoot)
	        .where(cb.and(filters))
	        .orderBy(cb.asc(clausesRoot.get("clausesId")));
	    
	    return entityManager.createQuery(query).getResultList();
	}

	
	/**
	 * Retrieves a specific {@link ClausesMasterV1} entity based on the provided company, product, section, cover, and clause IDs.
	 * This method ensures that only the latest amendment is fetched.
	 *
	 * @param companyId the ID of the company for which the clause is retrieved; must not be null.
	 * @param productId the ID of the product associated with the clause; must not be null.
	 * @param sectionId the ID of the section under the product; must not be null.
	 * @param coverId the ID of the cover linked to the section; must not be null.
	 * @param clausesId the ID of the specific clause to retrieve; must not be null.
	 * @return a list containing the matching {@link ClausesMasterV1} entity, or an empty list if no match is found.
	 * @throws Exception if any error occurs during query execution.
	 */
	private List<ClausesMasterV1> retrieveParticularClausesMaster(
	        Integer companyId, Integer productId, Integer sectionId, 
	        Integer coverId, Integer clausesId) throws Exception {
	    
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<ClausesMasterV1> query = cb.createQuery(ClausesMasterV1.class);
	    
	    // Define the root entity from which the query will be executed
	    Root<ClausesMasterV1> clausesRoot = query.from(ClausesMasterV1.class);

	    // Subquery to find the maximum amendment ID for each clause
	    Subquery<Integer> maxAmendId = query.subquery(Integer.class);
	    Root<ClausesMasterV1> subRoot = maxAmendId.from(ClausesMasterV1.class);
	    
	    // Select the maximum amendment ID where company, product, section, cover, and clause IDs match
	    maxAmendId.select(cb.max(subRoot.get("amendId")))
	        .where(
	            cb.equal(clausesRoot.get("companyId"), subRoot.get("companyId")),
	            cb.equal(clausesRoot.get("productId"), subRoot.get("productId")),
	            cb.equal(clausesRoot.get("sectionId"), subRoot.get("sectionId")),
	            cb.equal(clausesRoot.get("coverId"), subRoot.get("coverId")),
	            cb.equal(clausesRoot.get("clausesId"), subRoot.get("clausesId")),
	            cb.equal(clausesRoot.get("branchCode"), subRoot.get("branchCode"))
	        );
	    
	    // Define the predicates (filter conditions) for the main query
	    Predicate[] filters = new Predicate[] {
	        cb.equal(clausesRoot.get("companyId"), companyId),
	        cb.equal(clausesRoot.get("productId"), productId),
	        cb.equal(clausesRoot.get("sectionId"), sectionId),
	        cb.equal(clausesRoot.get("coverId"), coverId),
	        cb.equal(clausesRoot.get("clausesId"), clausesId),
	        cb.equal(clausesRoot.get("amendId"), maxAmendId)
	    };
	    
	    // Construct the query with selected filters and Execute the query and return the result list
	    query.select(clausesRoot)
	        .where(cb.and(filters));
	    
	    return entityManager.createQuery(query).getResultList();
	}	
	

	/**
	 * Retrieves a specific {@link ClausesMasterV1} entity based on the provided company, product, section, cover, and clause IDs.
	 * This method ensures that only the latest amendment record within the effective date range is fetched.
	 *
	 * @param companyId the ID of the company for which the clause is retrieved; must not be null.
	 * @param productId the ID of the product associated with the clause; must not be null.
	 * @param sectionId the ID of the section under the product; must not be null.
	 * @param coverId the ID of the cover linked to the section; must not be null.
	 * @param clausesId the ID of the specific clause to retrieve; must not be null.
	 * @return a list containing the matching {@link ClausesMasterV1} entity, or an empty list if no match is found.
	 * @throws Exception if any error occurs during query execution.
	 */
	private List<ClausesMasterV1> retrieveParticularActiveClausesMaster(
	        Integer companyId, Integer productId, Integer sectionId, 
	        Integer coverId, Integer clausesId) throws Exception {
	    
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<ClausesMasterV1> query = cb.createQuery(ClausesMasterV1.class);
	    
	    // Define the root entity from which the query will be executed
	    Root<ClausesMasterV1> clausesRoot = query.from(ClausesMasterV1.class);

	    // Subquery to find the maximum amendment ID for each clause
	    Subquery<Integer> maxAmendId = query.subquery(Integer.class);
	    Root<ClausesMasterV1> subRoot = maxAmendId.from(ClausesMasterV1.class);
	    
	    // Select the maximum amendment ID where company, product, section, cover, and clause IDs match
	    maxAmendId.select(cb.max(subRoot.get("amendId")))
	        .where(
	            cb.equal(clausesRoot.get("companyId"), subRoot.get("companyId")),
	            cb.equal(clausesRoot.get("productId"), subRoot.get("productId")),
	            cb.equal(clausesRoot.get("sectionId"), subRoot.get("sectionId")),
	            cb.equal(clausesRoot.get("coverId"), subRoot.get("coverId")),
	            cb.equal(clausesRoot.get("clausesId"), subRoot.get("clausesId")),
	            cb.equal(clausesRoot.get("branchCode"), subRoot.get("branchCode"))
	        );
	    
	    // Define the predicates (filter conditions) for the main query
	    Predicate[] filters = new Predicate[] {
	        cb.equal(clausesRoot.get("companyId"), companyId),
	        cb.equal(clausesRoot.get("productId"), productId),
	        cb.equal(clausesRoot.get("sectionId"), sectionId),
	        cb.equal(clausesRoot.get("coverId"), coverId),
	        cb.equal(clausesRoot.get("clausesId"), clausesId),
	        cb.lessThanOrEqualTo(clausesRoot.get("effectiveDateStart"), LocalDate.now()),
	        cb.greaterThanOrEqualTo(clausesRoot.get("effectiveDateEnd"), LocalDate.now()),
	        cb.equal(clausesRoot.get("status"), STATUS_ACTIVE),
	        cb.equal(clausesRoot.get("amendId"), maxAmendId)
	    };
	    
	    // Construct the query with selected filters and Execute the query and return the result list
	    query.select(clausesRoot)
	        .where(cb.and(filters));
	    
	    return entityManager.createQuery(query).getResultList();
	}
	
}
