/**
 * @author : Ashok Kumar S 
 * @since  : 13-02-2025
 */
package com.maan.eway.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.FlowFieldDetails;
import com.maan.eway.error.Error;
import com.maan.eway.req.FlowFieldLOVGetReq;
import com.maan.eway.res.ListOfValuesRes;
import com.maan.eway.service.FlowFieldLOVService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class FlowFieldLOVServiceImpl implements FlowFieldLOVService {	
	private static final Logger log = LogManager.getLogger(FlowFieldLOVServiceImpl.class);
		
	private final String IS_HEADER_TRUE = "Yes";
	private final String STATUS_ACTIVE = "Y";

	private EntityManager entityManager;
	
	@Autowired	
	public FlowFieldLOVServiceImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public List<Error> validateParametersOfFlowFieldLOVGetRequest(FlowFieldLOVGetReq req) {
		List<Error> errors = new ArrayList<>();
		
		if(req.getCompanyId() == null) {
			errors.add(new Error("1", "companyId", "Company ID is required, It should not be null"));
		}
		if(req.getProductId() == null) {
			errors.add(new Error("2", "productId", "Product ID is required, It should not be null"));
		}
		if(StringUtils.isBlank(req.getIntegType())) {
			errors.add(new Error("3", "integType", "Integ Type is required, It should not be blank"));
		}
		
		return errors;
	}
	
	
	/**
	 * Retrieves a list of parent JSON keys for FlowFieldDetails.
	 * <p>
	 * This method fetches header key details from the {@code retrievingHeaderKeyIdAndDescFromParentReference} method
	 * based on the provided company ID, product ID, and integration type.
	 * If no header keys are found, a {@link NoSuchElementException} is thrown.
	 * </p>
	 *
	 * @param req The request object {@link FlowFieldLOVGetReq} containing company ID, product ID, and integration type.
	 * @return A list of {@link ListOfValuesRes} containing header key IDs and JSON keys,
	 *         or {@code null} in case of an exception.
	 * @throws NoSuchElementException if no header keys are found.
	 */
	@Override
	public List<ListOfValuesRes> dropdownToChooseParentJsonKey(FlowFieldLOVGetReq req) {
		try {
			List<FlowFieldDetails> headerKeyList = retrievingHeaderKeyIdAndDescFromParentReference(
					req.getCompanyId(), req.getProductId(), req.getIntegType());
			
			if(headerKeyList.isEmpty()) {
				throw new NoSuchElementException("All FlowFieldDetails header keys were empty");
			}
			
			return headerKeyList.stream()
					.map(header -> new ListOfValuesRes(String.valueOf(header.getKeyId()), header.getJsonKey()))
					.toList();
			
		} catch (Exception e) {
			log.error("Exception : {}", e.getMessage(), e);
			return null;
		}	
	}
	
	
	/**
	 * Retrieves a list of FlowFieldDetails that serve as parent references (header keys).
	 * <p>
	 * This method constructs a dynamic query using the {@link CriteriaBuilder} to fetch
	 * FlowFieldDetails records that match the given company ID, product ID, and integration type.
	 * The records are filtered to include only those that have {@code isHeader} set to true and
	 * {@code status} as active. The results are sorted in ascending order by {@code keyId}.
	 * </p>
	 *
	 * @param companyId The company ID used as a filter in the query.
	 * @param productId The product ID used as a filter in the query.
	 * @param integType The integration type used as a filter in the query.
	 * @return A list of {@link FlowFieldDetails} matching the given criteria.
	 * @throws Exception If an error occurs while executing the query.
	 */
	private List<FlowFieldDetails> retrievingHeaderKeyIdAndDescFromParentReference(
			BigDecimal companyId, BigDecimal productId, String integType) throws Exception {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<FlowFieldDetails> query = cb.createQuery(FlowFieldDetails.class);
		
		Root<FlowFieldDetails> root = query.from(FlowFieldDetails.class);

		// Define filters for the query
		Predicate companyFilter = cb.equal(root.get("companyId"), companyId);
		Predicate productFilter = cb.equal(root.get("productId"), productId);
		Predicate integTypeFilter = cb.equal(root.get("integType"), integType);
		Predicate isHeaderFilter = cb.equal(root.get("isHeader"), IS_HEADER_TRUE);
		Predicate statusFilter = cb.equal(root.get("status"), STATUS_ACTIVE);
		
	    // Sort by keyId in ascending order
		Order keyIdSorting = cb.asc(root.get("keyId"));
		
	    // Build and execute the query
		query.select(root)
			.where(cb.and(companyFilter, productFilter, integTypeFilter, isHeaderFilter, statusFilter))
			.orderBy(keyIdSorting);
		
		return entityManager.createQuery(query).getResultList();		
	}
	

}
