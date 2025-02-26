/**
 * @author : Ashok Kumar S 
 * @since  : 13-02-2025
 */
package com.maan.eway.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.FlowFieldDetails;
import com.maan.eway.bean.ListItemValue;
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
import jakarta.persistence.criteria.Subquery;

@Service
public class FlowFieldLOVServiceImpl implements FlowFieldLOVService {	
	private static final Logger log = LogManager.getLogger(FlowFieldLOVServiceImpl.class);
		
	private static final String IS_HEADER_TRUE = "Yes";
	private static final String STATUS_ACTIVE = "Y";
	
	private static final String DEFAULT_COMPANY_ID = "99999";
	private static final String DEFAULT_BRANCH_CODE = "99999";
	private static final String ITEM_TYPE = "FLOW_FIELD_DATATYPE";
	
	private static final String ROOT_HEADER_KEY_ID = "99999";
	
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
	 * Retrieves a dropdown list of parent JSON keys based on the provided request parameters.
	 * <p>
	 * This method fetches header key details from a parent reference and converts them into  
	 * a list of dropdown options. If no data is found, it returns a default root JSON key.
	 * </p>
	 *
	 * @param req the {@link FlowFieldLOVGetReq} containing company ID, product ID, and integration type.
	 * @return a {@link List} of {@link ListOfValuesRes} representing the available parent JSON keys,  
	 *         or {@code null} if an exception occurs.
	 */
	@Override
	public List<ListOfValuesRes> dropdownToChooseParentJsonKey(FlowFieldLOVGetReq req) {
		try {
			List<FlowFieldDetails> headerKeyList = retrievingHeaderKeyIdAndDescFromParentReference(
					req.getCompanyId(), req.getProductId(), req.getIntegType());
			
			if(headerKeyList.isEmpty()) {
				return List.of(new ListOfValuesRes(ROOT_HEADER_KEY_ID, ROOT_HEADER_KEY_ID));
			}
			
			return headerKeyList.stream()
					.map(header -> new ListOfValuesRes(String.valueOf(header.getKeyId()), header.getJsonKey()))
					.toList();
			
		} catch (Exception e) {
			log.error("Exception : {}", e.getMessage(), e);
			return null;
		}	
	}
	
	@Override
	public List<ListOfValuesRes> dropdownToChooseDatatypes() {
		try {
			List<ListItemValue> flowFieldDatatypes = retrieveFlowFieldDatatypes();
			
			return flowFieldDatatypes.stream()
					.map(ffd -> new ListOfValuesRes(ffd.getItemCode(), ffd.getItemValue()))
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
	
	
	/**
	 * Retrieves a list of flow field data types based on predefined criteria.
	 * 
	 * <p>This method constructs a criteria query using {@link CriteriaBuilder} to fetch 
	 * the latest amendments of active {@link ListItemValue} records for a specific company and branch.</p>
	 *
	 * @return a {@code List} of {@link ListItemValue} objects that match the defined criteria.
	 * @throws Exception if an error occurs while executing the query.
	 */
	private List<ListItemValue> retrieveFlowFieldDatatypes() throws Exception {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ListItemValue> query = cb.createQuery(ListItemValue.class);
		
		Root<ListItemValue> listItemRoot = query.from(ListItemValue.class);
		
	    // Subquery to find the maximum amendment ID
		Subquery<Integer> maxAmendId = query.subquery(Integer.class);		
		Root<ListItemValue> subRoot = query.from(ListItemValue.class);
		
		maxAmendId.select(cb.max(subRoot.get("amendId")))
			.where(
				cb.equal(listItemRoot.get("companyId"), subRoot.get("companyId")),
				cb.equal(listItemRoot.get("branchCode"), subRoot.get("branchCode")),
				cb.equal(listItemRoot.get("itemId"), subRoot.get("itemId"))
			);
			
		Instant instant = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date today = Date.from(instant);
		
		// Define filters for the query
		Predicate[] filters = new Predicate[] {
				cb.equal(listItemRoot.get("companyId"), DEFAULT_COMPANY_ID),
				cb.equal(listItemRoot.get("branchCode"), DEFAULT_BRANCH_CODE),
				cb.equal(listItemRoot.get("itemType"), ITEM_TYPE),
				cb.equal(listItemRoot.get("status"), STATUS_ACTIVE),
				cb.equal(listItemRoot.get("amendId"), maxAmendId),
				cb.lessThanOrEqualTo(listItemRoot.get("effectiveDateStart"), today),
				cb.greaterThanOrEqualTo(listItemRoot.get("effectiveDateEnd"), today)			
		};
		
	    // Build and execute the query
		query.select(listItemRoot)
			.where(cb.and(filters))
			.orderBy(cb.asc(listItemRoot.get("itemId")));
		
		return entityManager.createQuery(query).getResultList();		
	}

}
