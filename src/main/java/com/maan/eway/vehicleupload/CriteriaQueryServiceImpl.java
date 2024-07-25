package com.maan.eway.vehicleupload;


import java.sql.Date;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;
import com.maan.eway.bean.ListItemValue;
import com.maan.eway.bean.MotorBodyTypeMaster;
import com.maan.eway.bean.MotorVehicleUsageMaster;
import com.maan.eway.bean.OccupationMaster;
import com.maan.eway.bean.PolicyTypeMaster;
import com.maan.eway.bean.ProductSectionMaster;


@Component
@Transactional
public class CriteriaQueryServiceImpl {
	
	Logger log =LogManager.getLogger(CriteriaQueryServiceImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void updateInsuranceType(Object companyId, Object productId, Object typeId, Object referenceNo) {
       try {
		
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
	        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);
	
	        // Create the Subquery
	        Subquery<String> subquery = updateQuery.subquery(String.class);
	        Root<ProductSectionMaster> subqueryRoot = subquery.from(ProductSectionMaster.class);
	
	        // Define the correlated conditions for the Subquery
	        Predicate subqueryConditions = criteriaBuilder.and(
	            criteriaBuilder.equal(subqueryRoot.get("productId"), root.get("productId")),
	            criteriaBuilder.equal(subqueryRoot.get("companyId"), root.get("companyId")),
	            criteriaBuilder.equal(subqueryRoot.get("status"), "Y"),
	            criteriaBuilder.between(criteriaBuilder.currentDate(), subqueryRoot.get("effectiveDateStart"), subqueryRoot.get("effectiveDateEnd")),
	            criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.upper(subqueryRoot.get("sectionName"))), criteriaBuilder.trim(criteriaBuilder.upper(root.get("insuranceTypeDesc"))))
	        );
	
	        // Set the SELECT clause in the Subquery
	        subquery.select(subqueryRoot.get("sectionId")).distinct(true);
	
	        // Add the correlated conditions to the Subquery
	        subquery.where(subqueryConditions);
	
	        // Create the Subquery for amend_id
	        Subquery<Integer> amendSubquery = updateQuery.subquery(Integer.class);
	        Root<ProductSectionMaster> amendSubqueryRoot = amendSubquery.from(ProductSectionMaster.class);
	
	        // Define the correlated conditions for the amend_id Subquery
	        Predicate amendSubqueryConditions = criteriaBuilder.and(
	            criteriaBuilder.equal(amendSubqueryRoot.get("productId"), root.get("productId")),
	            criteriaBuilder.equal(amendSubqueryRoot.get("companyId"), root.get("companyId")),
	            criteriaBuilder.equal(amendSubqueryRoot.get("status"), "Y"),
	            criteriaBuilder.between(criteriaBuilder.currentDate(), amendSubqueryRoot.get("effectiveDateStart"), amendSubqueryRoot.get("effectiveDateEnd")),
	            criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.upper(amendSubqueryRoot.get("sectionName"))), criteriaBuilder.trim(criteriaBuilder.upper(root.get("insuranceTypeDesc"))))
	        );
	
	        // Set the SELECT clause in the amend_id Subquery
	        amendSubquery.select(criteriaBuilder.max(amendSubqueryRoot.get("amendId"))).distinct(true);
	
	        // Add the correlated conditions to the amend_id Subquery
	        amendSubquery.where(amendSubqueryConditions);
	
	        // Set the insurance_type_id property based on the Subquery result
	        updateQuery.set(root.<Integer>get("insuranceTypeId"), subquery.as(Integer.class));
	        
	        updateQuery.set(root.<Integer>get("sectionId"), subquery.as(Integer.class));

	
	        // Apply the amend_id condition
	        updateQuery.where(criteriaBuilder.equal(subquery, amendSubquery));
	
	        // Apply the conditions to restrict the update
	        Predicate whereCondition = criteriaBuilder.and(
	            criteriaBuilder.equal(root.get("companyId"), companyId),
	            criteriaBuilder.equal(root.get("productId"), productId),
	            criteriaBuilder.equal(root.get("typeid"), typeId),
	            criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo)
	        );
	
	        updateQuery.where(whereCondition);
	
	        // Perform the update
	       Integer count= entityManager.createQuery(updateQuery).executeUpdate();
	       log.info("updateInsuranceType query result :" +count);
	    }catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

	}
	
	
	public void updateSectionId(Object companyId, Object productId, Object typeId, Object referenceNo) {
       try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaUpdate<EserviceMotorDetailsRaw> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);

			// Set the target entity
			Root<EserviceMotorDetailsRaw> root = criteriaUpdate.from(EserviceMotorDetailsRaw.class);

			// Subquery
			Subquery<Integer> subquery = criteriaUpdate.subquery(Integer.class);
			Root<EserviceMotorDetailsRaw> subqueryRoot = subquery.from(EserviceMotorDetailsRaw.class);
			subquery.select(subqueryRoot.get("insuranceTypeId")).distinct(true);
			subquery.where(
			    criteriaBuilder.and(
			        criteriaBuilder.equal(subqueryRoot.get("companyId"), root.get("companyId")),
			        criteriaBuilder.equal(subqueryRoot.get("productId"), root.get("productId")),
			        criteriaBuilder.equal(subqueryRoot.get("typeid"), root.get("typeid")),
			        criteriaBuilder.equal(subqueryRoot.get("requestReferenceNo"), root.get("requestReferenceNo")),
			        criteriaBuilder.equal(subqueryRoot.get("rowNum"), root.get("rowNum"))
			    )
			);

			// Set the update clause
			criteriaUpdate.set(root.<Integer>get("sectionId"), subquery);

			// Set the WHERE clause
			criteriaUpdate.where(
			    criteriaBuilder.and(
			        criteriaBuilder.equal(root.get("companyId"), companyId),
			        criteriaBuilder.equal(root.get("productId"), productId),
			        criteriaBuilder.equal(root.get("typeid"), typeId),
			        criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo)
			    )
			);

			// Perform the update
			entityManager.createQuery(criteriaUpdate).executeUpdate();
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	}
	
	
	public void updateBodyTypeId(Object companyId, Object productId, Object typeId, Object referenceNo) {
        try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
	        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);
	
	        
	        
	        // Create the Subquery for body_id
	        Subquery<String> bodyIdSubquery = updateQuery.subquery(String.class);
	        Root<MotorBodyTypeMaster> bodyIdSubqueryRoot = bodyIdSubquery.from(MotorBodyTypeMaster.class);
	
	     // Create the Subquery for amend_id
	        Subquery<Integer> amendIdSubquery = bodyIdSubquery.subquery(Integer.class);
	        Root<MotorBodyTypeMaster> amendIdSubqueryRoot = amendIdSubquery.from(MotorBodyTypeMaster.class);
	
	        // Define the correlated conditions for the amend_id Subquery
	        Predicate amendIdSubqueryConditions = criteriaBuilder.and(
	            criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.upper(amendIdSubqueryRoot.get("bodyNameEn"))),
	                                  criteriaBuilder.trim(criteriaBuilder.upper(root.get("bodyTypeDesc")))),
	            criteriaBuilder.equal(amendIdSubqueryRoot.get("status"), "Y"),
	            criteriaBuilder.or(criteriaBuilder.equal(amendIdSubqueryRoot.get("branchCode"), root.get("branchCode")),
	                               criteriaBuilder.equal(amendIdSubqueryRoot.get("branchCode"), "99999")),
	            criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.currentDate(), amendIdSubqueryRoot.get("effectiveDateEnd")),
	            criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.currentDate(), amendIdSubqueryRoot.get("effectiveDateStart")),
	            criteriaBuilder.equal(amendIdSubqueryRoot.get("companyId"), root.get("companyId"))
	        );
	
	        // Set the SELECT clause in the amend_id Subquery
	        amendIdSubquery.select(criteriaBuilder.max(amendIdSubqueryRoot.get("amendId")));
	
	        // Add the correlated conditions to the amend_id Subquery
	        amendIdSubquery.where(amendIdSubqueryConditions);
	
	        
	        // Define the correlated conditions for the body_id Subquery
	        Predicate bodyIdSubqueryConditions = criteriaBuilder.and(
	            criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.upper(bodyIdSubqueryRoot.get("bodyNameEn"))),
	                                  criteriaBuilder.trim(criteriaBuilder.upper(root.get("bodyTypeDesc")))),
	            criteriaBuilder.equal(bodyIdSubqueryRoot.get("status"), "Y"),
	            criteriaBuilder.or(criteriaBuilder.equal(bodyIdSubqueryRoot.get("branchCode"), root.get("branchCode")),
	                               criteriaBuilder.equal(bodyIdSubqueryRoot.get("branchCode"), "99999")),
	            criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.currentDate(), bodyIdSubqueryRoot.get("effectiveDateEnd")),
	            criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.currentDate(), bodyIdSubqueryRoot.get("effectiveDateStart")),
	            criteriaBuilder.equal(bodyIdSubqueryRoot.get("companyId"), root.get("companyId")),
	            criteriaBuilder.equal(bodyIdSubqueryRoot.get("amendId"), amendIdSubquery)

	        );
	
	        // Set the SELECT clause in the body_id Subquery
	        bodyIdSubquery.select(bodyIdSubqueryRoot.get("bodyId")).distinct(true);
	
	        // Add the correlated conditions to the body_id Subquery
	        bodyIdSubquery.where(bodyIdSubqueryConditions);
	
	        
	        // Set the body_type_id property based on the body_id Subquery result
	        updateQuery.set(root.<Integer>get("bodyTypeId"), bodyIdSubquery.as(Integer.class));
	
	        // Apply the conditions to restrict the update
	        Predicate whereCondition = criteriaBuilder.and(
	            criteriaBuilder.equal(root.get("companyId"), companyId),
	            criteriaBuilder.equal(root.get("productId"), productId),
	            criteriaBuilder.equal(root.get("typeid"), typeId),
	            criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo)
	        );
	
	        updateQuery.where(whereCondition);
	
	        // Perform the update
	        entityManager.createQuery(updateQuery).executeUpdate();
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	}


	 public void updateInsuranceClassId(Object companyId, Object productId, Object typeId, Object referenceNo) {
	     try {  
		    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
	        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);

	        // Create the Subquery for policy_type_id
	        Subquery<String> policyTypeIdSubquery = updateQuery.subquery(String.class);
	        Root<PolicyTypeMaster> policyTypeIdSubqueryRoot = policyTypeIdSubquery.from(PolicyTypeMaster.class);

	        Subquery<Integer> amendIdSubquery = policyTypeIdSubquery.subquery(Integer.class);
	        Root<PolicyTypeMaster> amendIdSubqueryRoot = amendIdSubquery.from(PolicyTypeMaster.class);

	        // Define the correlated conditions for the amend_id Subquery
	        Predicate amendIdSubqueryConditions = criteriaBuilder.and(
	            criteriaBuilder.equal(criteriaBuilder.upper(amendIdSubqueryRoot.get("policyTypeName")),
	                                  criteriaBuilder.upper(root.get("insuranceClassDesc"))),
	            criteriaBuilder.equal(amendIdSubqueryRoot.get("productId"), root.get("productId")),
	            criteriaBuilder.equal(amendIdSubqueryRoot.get("status"), "Y"),
	            criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.currentDate(), amendIdSubqueryRoot.get("effectiveDateEnd")),
	            criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.currentDate(), amendIdSubqueryRoot.get("effectiveDateStart")),
	            criteriaBuilder.equal(amendIdSubqueryRoot.get("companyId"), root.get("companyId"))
	        );

	        // Set the SELECT clause in the amend_id Subquery
	        amendIdSubquery.select(criteriaBuilder.max(amendIdSubqueryRoot.get("amendId")));

	        // Add the correlated conditions to the amend_id Subquery
	        amendIdSubquery.where(amendIdSubqueryConditions);
	        
	        // Define the correlated conditions for the policy_type_id Subquery
	        Predicate policyTypeIdSubqueryConditions = criteriaBuilder.and(
	            criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.upper(policyTypeIdSubqueryRoot.get("policyTypeName"))),
	                                  criteriaBuilder.trim(criteriaBuilder.upper(root.get("insuranceClassDesc")))),
	            criteriaBuilder.equal(policyTypeIdSubqueryRoot.get("productId"), root.get("productId")),
	            criteriaBuilder.equal(policyTypeIdSubqueryRoot.get("status"), "Y"),
	            criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.currentDate(), policyTypeIdSubqueryRoot.get("effectiveDateEnd")),
	            criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.currentDate(), policyTypeIdSubqueryRoot.get("effectiveDateStart")),
	            criteriaBuilder.equal(policyTypeIdSubqueryRoot.get("companyId"), root.get("companyId")),
	            criteriaBuilder.equal(policyTypeIdSubqueryRoot.get("amendId"), amendIdSubquery)

	        		);

	        // Set the SELECT clause in the policy_type_id Subquery
	        policyTypeIdSubquery.select(policyTypeIdSubqueryRoot.get("policyTypeId")).distinct(true);

	        // Add the correlated conditions to the policy_type_id Subquery
	        policyTypeIdSubquery.where(policyTypeIdSubqueryConditions);

	        // Create the Subquery for amend_id
	       

	        // Set the insurance_class_id property based on the policy_type_id Subquery result
	        updateQuery.set(root.<Integer>get("insuranceClassId"), policyTypeIdSubquery.as(Integer.class));

	        // Apply the amend_id condition

	        // Apply the conditions to restrict the update
	        Predicate whereCondition = criteriaBuilder.and(
	            criteriaBuilder.equal(root.get("companyId"), companyId),
	            criteriaBuilder.equal(root.get("productId"), productId),
	            criteriaBuilder.equal(root.get("typeid"), typeId),
	            criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo)
	        );

	        updateQuery.where(whereCondition);

	        // Perform the update
	        entityManager.createQuery(updateQuery).executeUpdate();
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	 }
	     
	  
	 
	
	 public void updateSuminsuredValidation(Object companyId, Object productId, Object typeId, Object referenceNo) {
	     try {  
	    	 CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		 	CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
	        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);

	        // Concatenate the error_desc field with the new error message conditionally
	        String errorConditionMessage = "~vehicle or winshield or accessories or tppd suminsured is mandatory for comprehensive or TPFT insurance";
	        updateQuery.set(root.<String>get("errorDesc"),
	            criteriaBuilder.concat(
	                criteriaBuilder.coalesce(root.get("errorDesc"), ""),
	                criteriaBuilder.selectCase()
	                    .when(
	                        criteriaBuilder.and(
	                            criteriaBuilder.or(
	                                criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.upper(root.get("insuranceTypeDesc"))), "COMPREHENSIVE"),
	                                criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.upper(root.get("insuranceTypeDesc"))), "TPFT")
	                            ),
	                            criteriaBuilder.or(
	                                criteriaBuilder.equal(root.get("vehicleSuminsured"), ""),
	                                criteriaBuilder.isNull(root.get("vehicleSuminsured")),
	                                criteriaBuilder.equal(root.get("accessoriesSuminsured"), ""),
	                                criteriaBuilder.isNull(root.get("accessoriesSuminsured")),
	                                criteriaBuilder.equal(root.get("windshieldSuminsured"), ""),
	                                criteriaBuilder.isNull(root.get("windshieldSuminsured")),
	                                criteriaBuilder.equal(root.get("extendedSuminsured"), ""),
	                                criteriaBuilder.isNull(root.get("extendedSuminsured"))
	                            )
	                        ),
	                        errorConditionMessage
	                    )
	                    .otherwise(root.get("errorDesc")).as(String.class)
	            )
	        );

	        // Set the STATUS column based on the error_desc
	        updateQuery.set(root.<String>get("status"),
	            criteriaBuilder.selectCase()
	                .when(
	                    criteriaBuilder.and(
	                        criteriaBuilder.isNotNull(root.get("errorDesc")),
	                        criteriaBuilder.notEqual(root.get("errorDesc"), "")
	                    ),
	                    "E"
	                )
	                .otherwise("Y").as(String.class)
	        );

	        // Apply the conditions to restrict the update
	        updateQuery.where(
	            criteriaBuilder.and(
	                criteriaBuilder.equal(root.get("companyId"), companyId),
	                criteriaBuilder.equal(root.get("productId"), productId),
	                criteriaBuilder.equal(root.get("typeid"), typeId),
	                criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo)
	            )
	        );

	        // Perform the update
	        entityManager.createQuery(updateQuery).executeUpdate();
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	 }

	
	   public void updateColleteralValidation(Object companyId, Object productId, Object typeId, Object referenceNo) {
	       try { 
		    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
	        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);

	        // Concatenate the error_desc field with the new error message conditionally
	        String errorConditionMessage = "~Borrower Type and First loss payee is mandatory if you selected collateral is YES";
	        updateQuery.set(root.<String>get("errorDesc"),
	            criteriaBuilder.concat(
	                criteriaBuilder.coalesce(root.get("errorDesc"), ""),
	                criteriaBuilder.selectCase()
	                    .when(
	                        criteriaBuilder.and(
	                            criteriaBuilder.equal(criteriaBuilder.trim(criteriaBuilder.upper(root.get("collateral"))), "YES"),
	                            criteriaBuilder.or(
	                                criteriaBuilder.isNull(root.get("borrowerType")),
	                                criteriaBuilder.equal(criteriaBuilder.trim(root.get("borrowerType")), ""),
	                                criteriaBuilder.isNull(root.get("firstLossPayee")),
	                                criteriaBuilder.equal(criteriaBuilder.trim(root.get("firstLossPayee")), "")
	                            )
	                        ),
	                        errorConditionMessage
	                    )
	                    .otherwise(root.get("errorDesc")).as(String.class)
	            )
	        );

	        // Set the STATUS column based on the error_desc
	        updateQuery.set(root.<String>get("status"),
	            criteriaBuilder.selectCase()
	                .when(
	                    criteriaBuilder.and(
	                        criteriaBuilder.isNotNull(root.get("errorDesc")),
	                        criteriaBuilder.notEqual(root.get("errorDesc"), "")
	                    ),
	                    "E"
	                )
	                .otherwise("Y").as(String.class)
	        );

	        // Apply the conditions to restrict the update
	        updateQuery.where(
	            criteriaBuilder.and(
	                criteriaBuilder.equal(root.get("companyId"), companyId),
	                criteriaBuilder.equal(root.get("productId"), productId),
	                criteriaBuilder.equal(root.get("typeid"), typeId),
	                criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo)
	            )
	        );

	        // Perform the update
	        entityManager.createQuery(updateQuery).executeUpdate();
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	   }

	   public void updateEmptyDataError(Object companyId, Object productId, Object typeId, Object referenceNo) {
	       try { 
		    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
	        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);

	        // Set the error_desc column based on the given conditions using CASE expressions
	        updateQuery.set(
	            root.<String>get("errorDesc"),
	            criteriaBuilder.selectCase()
	                .when(
	                    criteriaBuilder.or(
	                        criteriaBuilder.isNull(root.get("sectionId"))
	                    ),
	                    "~Sectionid not found"
	                )
	                .when(
	                    criteriaBuilder.or(
	                        criteriaBuilder.isNull(root.get("insuranceTypeId"))
	                    ),
	                    "~InsuranceType id not found"
	                )
	                .when(
	                    criteriaBuilder.or(
	                        criteriaBuilder.isNull(root.get("insuranceClassId"))
	                    ),
	                    "~InsuranceClass id not found"
	                )
	                .when(
	                    criteriaBuilder.or(
	                        criteriaBuilder.isNull(root.get("bodyTypeId"))
	                    ),
	                    "~BodyType id not found"
	                )
	                .when(
	                    criteriaBuilder.or(
	                        criteriaBuilder.isNull(root.get("motorUsageId"))
	                    ),
	                    "~MotorUsageId not found"
	                )
	                .otherwise(root.get("errorDesc")).as(String.class)
	        );

	     

	        // Set the WHERE clause conditions
	        Predicate condition1 = criteriaBuilder.equal(root.get("companyId"), companyId);
	        Predicate condition2 = criteriaBuilder.equal(root.get("productId"), productId);
	        Predicate condition3 = criteriaBuilder.equal(root.get("typeid"), typeId);
	        Predicate condition4 = criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo);

	        updateQuery.where(criteriaBuilder.and(condition1, condition2, condition3, condition4));

	        // Perform the update operation
	        entityManager.createQuery(updateQuery).executeUpdate();
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	
	  }
	   public void updateDuplicateData(Object companyId, Object productId, Object typeId, Object referenceNo) {
	       try {
	    	    String query ="UPDATE EserviceMotorDetailsRaw ra SET ra.status = 'E', ra.errorDesc = concat( ra.errorDesc, '~DUPLICATE SEARCH BY DATA IS FOUND' ) "
	    	    		+ "WHERE ra.searchByData IN ( SELECT x.searchByData FROM ( SELECT e.searchByData FROM EserviceMotorDetailsRaw e WHERE e.companyId = :1 "
	    	    		+ "AND e.productId = :2 AND e.typeid = :3 AND e.requestReferenceNo = :4 AND e.status = 'Y' AND e.tiraStatus = 'Y' "
	    	    		+ "AND ( e.apiStatus IN ('Y', '') OR e.apiStatus IS NULL ) GROUP BY e.searchByData HAVING COUNT(*) > 1 ) x ) "
	    	    		+ "AND ra.companyId = :1 AND ra.productId = :2 AND ra.typeid = :3 AND ra.requestReferenceNo = :4 AND ra.status = 'Y' "
	    	    		+ "AND ra.tiraStatus = 'Y' AND ( ra.apiStatus IN ('Y', '') OR ra.apiStatus IS NULL )";
	    	    Integer count= entityManager.createQuery(query)
	    	   .setParameter("1", companyId)
	    	   .setParameter("2", productId)
	    	   .setParameter("3", typeId)
	    	   .setParameter("4", referenceNo)
	    	   .executeUpdate();
	    	  System.out.println(count);
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	   }
	   
	   public void updateStatusAndErrorDesc(Object typeId, Object referenceNo, Object companyId, Object productId) {
	        try {
		    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
	        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);

	        // Create a subquery to retrieve the search_by_data values meeting the specified conditions
	        Subquery<String> subquery = updateQuery.subquery(String.class);
	        Root<EserviceMotorDetailsRaw> subqueryRoot = subquery.from(EserviceMotorDetailsRaw.class);
	        subquery.select(subqueryRoot.get("searchByData"));
	        subquery.where(
	            criteriaBuilder.and(
	                criteriaBuilder.equal(subqueryRoot.get("typeid"), typeId),
	                criteriaBuilder.equal(subqueryRoot.get("requestReferenceNo"), referenceNo),
	                criteriaBuilder.equal(subqueryRoot.get("companyId"), companyId),
	                criteriaBuilder.equal(subqueryRoot.get("productId"), productId),
	                criteriaBuilder.equal(subqueryRoot.get("status"), "Y")
	            )
	        );
	        subquery.groupBy(subqueryRoot.get("searchByData"));

	        // Set the status and error_desc columns based on the subquery results
	        updateQuery.set(root.get("status"), "X");
	        updateQuery.set(root.<String>get("errorDesc"), criteriaBuilder.concat(criteriaBuilder.coalesce(root.get("errorDesc"), ""), "~Data has been overridden"));

	        // Apply the conditions to restrict the update
	        updateQuery.where(
	            criteriaBuilder.and(
	                criteriaBuilder.in(root.get("searchByData")).value(subquery),
	                criteriaBuilder.equal(root.get("typeid"), typeId),
	                criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo),
	                criteriaBuilder.equal(root.get("companyId"), companyId),
	                criteriaBuilder.equal(root.get("productId"), productId),
	                criteriaBuilder.equal(root.get("status"), "E")
	            )
	        );

	        // Perform the update
	        entityManager.createQuery(updateQuery).executeUpdate();
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	  }

	   
	   public void updateMotorUsageId(Object companyId, Object productId, Object typeId, Object referenceNo) {
	       try { 
		    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
	        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);

	        // Create a subquery to retrieve the vehicle_usage_id based on the specified conditions
	        Subquery<String> subquery = updateQuery.subquery(String.class);
	        Subquery<Integer> amendIdSubQuery=updateQuery.subquery(Integer.class);
	        Root<MotorVehicleUsageMaster> amendIdSubQueryRoot = amendIdSubQuery.from(MotorVehicleUsageMaster.class);

	        Root<MotorVehicleUsageMaster> subqueryRoot = subquery.from(MotorVehicleUsageMaster.class);
	        subquery.select(subqueryRoot.get("vehicleUsageId"));
	        subquery.where(
	            criteriaBuilder.and(
	                criteriaBuilder.equal(
	                    criteriaBuilder.function("replace", String.class, 
	                        criteriaBuilder.upper(criteriaBuilder.trim(subqueryRoot.get("vehicleUsageDesc"))),
	                        criteriaBuilder.literal("\r\n"),
	                        criteriaBuilder.literal("")
	                    ),
	                    criteriaBuilder.function("replace", String.class, 
	                        criteriaBuilder.upper(criteriaBuilder.trim(root.get("motorUsageDesc"))),
	                        criteriaBuilder.literal("\r\n"),
	                        criteriaBuilder.literal("")
	                    ) 
	                ),
	                criteriaBuilder.or(
	                    criteriaBuilder.equal(subqueryRoot.get("sectionId"), root.get("sectionId")),
	                    criteriaBuilder.equal(subqueryRoot.get("sectionId"), "99999")
	                ),
	                criteriaBuilder.or(
	                    criteriaBuilder.equal(subqueryRoot.get("branchCode"), root.get("branchCode")),
	                    criteriaBuilder.equal(subqueryRoot.get("branchCode"), "99999")
	                ),
	                criteriaBuilder.equal(subqueryRoot.get("status"), "Y"),
	                criteriaBuilder.between(
	                    criteriaBuilder.currentDate(),
	                    subqueryRoot.get("effectiveDateStart"),
	                    subqueryRoot.get("effectiveDateEnd")
	                ),
	                criteriaBuilder.equal(subqueryRoot.get("companyId"), root.get("companyId")),
	                criteriaBuilder.equal(
	                    subqueryRoot.get("amendId"),
	                    amendIdSubQuery.select(criteriaBuilder.max(amendIdSubQueryRoot.get("amendId")))
	                    .where(criteriaBuilder.equal(
	    	                    criteriaBuilder.function("replace", String.class, 
	    	                        criteriaBuilder.upper(criteriaBuilder.trim(amendIdSubQueryRoot.get("vehicleUsageDesc"))),
	    	                        criteriaBuilder.literal("\r\n"),
	    	                        criteriaBuilder.literal("")
	    	                    ),
	    	                    criteriaBuilder.function("replace", String.class, 
	    	                        criteriaBuilder.upper(criteriaBuilder.trim(root.get("motorUsageDesc"))),
	    	                        criteriaBuilder.literal("\r\n"),
	    	                        criteriaBuilder.literal("")
	    	                    ) 
	    	                ))
	                )
	            )
	        );

	        // Set the motor_usage_id column based on the subquery result
	        updateQuery.set(root.<Integer>get("motorUsageId"), subquery.as(Integer.class));

	        // Apply the conditions to restrict the update
	        updateQuery.where(
	            criteriaBuilder.and(
	                criteriaBuilder.equal(root.get("companyId"), companyId),
	                criteriaBuilder.equal(root.get("productId"), productId),
	                criteriaBuilder.equal(root.get("typeid"), typeId),
	                criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo)
	            )
	        );

	        // Perform the update
	        entityManager.createQuery(updateQuery).executeUpdate();
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	  }
	   
	   public void overirdeExistingErrorRecord(Object typeId, Object referenceNo, Object companyId, Object productId) {
	       try {
		   CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
	        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);

	        // Create a subquery to retrieve the search_by_data values based on the specified conditions
	        Subquery<String> subquery = updateQuery.subquery(String.class);
	        Root<EserviceMotorDetailsRaw> subqueryRoot = subquery.from(EserviceMotorDetailsRaw.class);
	        subquery.select(subqueryRoot.get("searchByData"));
	        subquery.where(
	            criteriaBuilder.and(
	                criteriaBuilder.equal(subqueryRoot.get("typeid"), typeId),
	                criteriaBuilder.equal(subqueryRoot.get("requestReferenceNo"), referenceNo),
	                criteriaBuilder.equal(subqueryRoot.get("companyId"), companyId),
	                criteriaBuilder.equal(subqueryRoot.get("productId"), productId),
	                criteriaBuilder.equal(subqueryRoot.get("status"), "Y")
	            )
	        );
	        subquery.groupBy(subqueryRoot.get("searchByData"));

	        // Set the status and error_desc columns based on the subquery results
	        updateQuery.set(root.get("status"), "X");
	        updateQuery.set(root.<String>get("errorDesc"), criteriaBuilder.concat(criteriaBuilder.coalesce(root.get("errorDesc"), ""), "~Data has been overridden"));

	        // Apply the conditions to restrict the update
	        updateQuery.where(
	            criteriaBuilder.and(
	                criteriaBuilder.in(root.get("searchByData")).value(subquery),
	                criteriaBuilder.equal(root.get("typeid"), typeId),
	                criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo),
	                criteriaBuilder.equal(root.get("companyId"), companyId),
	                criteriaBuilder.equal(root.get("productId"), productId),
	                criteriaBuilder.equal(root.get("status"), "E")
	            )
	        );

	        // Perform the update
	        entityManager.createQuery(updateQuery).executeUpdate();
	    }catch (Exception e) {
	    	e.printStackTrace();
			log.error(e);
		}
	       
	   }
	   
	   public void updateErrorStatus(Object companyId, Object productId, Object typeId, Object referenceNo,Object sectionId) {
		   try {
			   CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		        CriteriaUpdate<EwayEmplyeeDetailRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EwayEmplyeeDetailRaw.class);
		        Root<EwayEmplyeeDetailRaw> root = updateQuery.from(EwayEmplyeeDetailRaw.class);
		        //Set the status column based on the error_desc value using another CASE expression
		        updateQuery.set(
		            root.<String>get("status"),
		            criteriaBuilder.selectCase()
		                .when(
		                      criteriaBuilder.isNotNull(root.get("errorDesc")),
		                    "E"
		                )
		                .otherwise("Y").as(String.class)
		        );
		        // Set the WHERE clause conditions
		        Predicate condition1 = criteriaBuilder.equal(root.get("companyId"), companyId);
		        Predicate condition2 = criteriaBuilder.equal(root.get("productId"), productId);
		        Predicate condition3 = criteriaBuilder.equal(root.get("typeid"), typeId);
		        Predicate condition4 = criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo);
		        //Predicate condition5 = criteriaBuilder.equal(root.get("sectionId"), sectionId);
		        
		        updateQuery.where(criteriaBuilder.and(condition1, condition2, condition3, condition4));

		        // Perform the update operation
		        entityManager.createQuery(updateQuery).executeUpdate();
		   }catch (Exception e) {
			   e.printStackTrace();
				log.error(e);
		}
	   
	  }
	   
	   
	   public void updateMotorErrorStatus(Object companyId, Object productId, Object typeId, Object referenceNo,Object sectionId) {
		   try {
			   CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
		        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);
		        //Set the status column based on the error_desc value using another CASE expression
		        updateQuery.set(
		            root.<String>get("status"),
		            criteriaBuilder.selectCase()
		                .when(
		                      criteriaBuilder.isNotNull(root.get("errorDesc")),
		                    "E"
		                )
		                .otherwise("Y").as(String.class)
		        );
		        // Set the WHERE clause conditions
		        Predicate condition1 = criteriaBuilder.equal(root.get("companyId"), companyId);
		        Predicate condition2 = criteriaBuilder.equal(root.get("productId"), productId);
		        Predicate condition3 = criteriaBuilder.equal(root.get("typeid"), typeId);
		        Predicate condition4 = criteriaBuilder.equal(root.get("requestReferenceNo"), referenceNo);
		        //Predicate condition5 = criteriaBuilder.equal(root.get("sectionId"), sectionId);
		        
		        updateQuery.where(criteriaBuilder.and(condition1, condition2, condition3, condition4));

		        // Perform the update operation
		        entityManager.createQuery(updateQuery).executeUpdate();
		   }catch (Exception e) {
			   e.printStackTrace();
				log.error(e);
		}
	   
	  }
	   
	   
	  public Integer updateOccupationId(Object companyId,Object productId,Object quoteNo,Object refNo,Object sectionId) {
		  try {
			  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			  CriteriaUpdate<EwayEmplyeeDetailRaw> criteriaUpdate =cb.createCriteriaUpdate(EwayEmplyeeDetailRaw.class);
			  Root<EwayEmplyeeDetailRaw> root =criteriaUpdate.from(EwayEmplyeeDetailRaw.class);
			
			  Subquery<Integer> occupationId= criteriaUpdate.subquery(Integer.class);
			  Root<OccupationMaster> occuRoot =occupationId.from(OccupationMaster.class);
			  Subquery<Integer> amendId =occupationId.subquery(Integer.class);
			  Root<OccupationMaster> amendRoot =amendId.from(OccupationMaster.class);
			  
			  amendId.select(cb.max(amendRoot.get("amendId"))).where(
					  cb.equal(occuRoot.get("status"), "Y"),cb.equal(occuRoot.get("companyId"), amendRoot.get("companyId")),
					  cb.equal(occuRoot.get("productId"), amendRoot.get("productId")),
					  cb.equal(cb.function("replace", String.class,(cb.upper(occuRoot.get("occupationName"))),cb.literal(" "),cb.literal(""))
							  ,cb.function("replace", String.class,cb.upper(amendRoot.get("occupationName")),cb.literal(" "),cb.literal(""))),
					  cb.between(cb.currentDate(), amendRoot.get("effectiveDateStart"), amendRoot.get("effectiveDateEnd"))
					  ).distinct(true);
			  occupationId.select(occuRoot.get("occupationId")).where(
					  cb.equal(occuRoot.get("status"), "Y"),cb.equal(occuRoot.get("companyId"), root.get("companyId")),
					  cb.equal(occuRoot.get("productId"), root.get("productId")),
					  cb.equal(cb.function("replace", String.class,(cb.upper(root.get("occupationDesc"))),cb.literal(" "),cb.literal(""))
							  ,cb.function("replace", String.class,cb.upper(occuRoot.get("occupationName")),cb.literal(" "),cb.literal(""))),
					  cb.between(cb.currentDate(), occuRoot.get("effectiveDateStart"), occuRoot.get("effectiveDateEnd")),
					  cb.equal(occuRoot.get("amendId"), amendId)
					  ).distinct(true);
			  criteriaUpdate.set(root.<Integer>get("occupationId"), occupationId)
			  .where(
					  cb.equal(root.get("companyId"), companyId), cb.equal(root.get("productId"), productId), cb.equal(root.get("requestReferenceNo"), refNo),
					  cb.equal(root.get("status"), "Y"),cb.isNull(root.get("apiStatus")),cb.equal(root.get("quoteNo"), quoteNo)
					  ,cb.equal(root.get("sectionId"), sectionId));
			  
			  entityManager.createQuery(criteriaUpdate).executeUpdate();
			  
			  
		  }catch (Exception e) {
			  e.printStackTrace();
				log.error(e);
		 }
		  return null;
	  }
	   
	  public Integer updateEmpErrorDesc(Object companyId,Object productId,Object quoteNo,Object refNo,Object sectionId) {
		  try {
			  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			  CriteriaUpdate<EwayEmplyeeDetailRaw> criteriaUpdate =cb.createCriteriaUpdate(EwayEmplyeeDetailRaw.class);
			  Root<EwayEmplyeeDetailRaw> root =criteriaUpdate.from(EwayEmplyeeDetailRaw.class);
			  Expression<String> caseCon =cb.selectCase()
					  .when(cb.isNull(root.<Integer>get("occupationId")), "Please enter valid Occupation")
					  .when(cb.isNull(root.get("locationId")), "Please enter valid Location")
					  .when(cb.isNull(root.get("monthOfJoining")), "Pleae enter valid Joining Period")
					  .otherwise(root.get("errorDesc")).as(String.class);
			  criteriaUpdate.set(root.<String>get("errorDesc"), caseCon)
			  .where(
					  cb.equal(root.get("companyId"), companyId), cb.equal(root.get("productId"), productId), cb.equal(root.get("requestReferenceNo"), refNo),
					  cb.equal(root.get("status"), "Y"), cb.or(cb.isNull(root.get("apiStatus")),cb.isNull(root.get("apiStatus"))),cb.equal(root.get("quoteNo"), quoteNo),
					  cb.equal(root.get("sectionId"), sectionId)
					);
			  entityManager.createQuery(criteriaUpdate).executeUpdate();
		  }catch (Exception e) {
			  e.printStackTrace();
				log.error(e);
		}
		 
		  return null;
		  
	  }
	   
	  public Integer updateEmpErrorStatus(Object companyId,Object productId,Object quoteNo,Object refNo) {
		  try {
			  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			  CriteriaUpdate<EwayEmplyeeDetailRaw> criteriaUpdate =cb.createCriteriaUpdate(EwayEmplyeeDetailRaw.class);
			  Root<EwayEmplyeeDetailRaw> root =criteriaUpdate.from(EwayEmplyeeDetailRaw.class);
			  Expression<String> caseCon =cb.selectCase().when(cb.isNotNull(root.get("errorDesc")), "E").otherwise("Y").as(String.class);
			  criteriaUpdate.set(root.<String>get("status"), caseCon)
			  .where(
					  cb.equal(root.get("companyId"), companyId), cb.equal(root.get("productId"), productId), cb.equal(root.get("requestReferenceNo"), refNo),
					  cb.equal(root.get("status"), "Y"), cb.isNull(root.get("apiStatus")),cb.equal(root.get("quoteNo"), quoteNo)
					 
					);
			  entityManager.createQuery(criteriaUpdate).executeUpdate();
		  }catch (Exception e) {
			  e.printStackTrace();
				log.error(e);
		}
		 
		  return null;
		  
	  }
	   
	  public Integer updateRelationId(Object companyId,Object productId,Object quoteNo,Object refNo) {
		  Integer count=0;
		  try {
			  CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			  CriteriaUpdate<EwayEmplyeeDetailRaw> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(EwayEmplyeeDetailRaw.class);

			  Root<EwayEmplyeeDetailRaw> root = criteriaUpdate.from(EwayEmplyeeDetailRaw.class);

			  // Subquery to select ITEM_CODE from EWAY_LIST_ITEM_VALUE
			  Subquery<String> subquery = criteriaUpdate.subquery(String.class);
			  Root<ListItemValue> subqueryRoot = subquery.from(ListItemValue.class);

			  Expression<String> trimmedItemValue = criteriaBuilder.function("replace", String.class,(criteriaBuilder.upper(subqueryRoot.get("itemValue"))),criteriaBuilder.literal(" "),criteriaBuilder.literal(""));
			  Expression<String> trimmedRelationDesc = criteriaBuilder.function("replace", String.class,(criteriaBuilder.upper(root.get("relationDesc"))),criteriaBuilder.literal(" "),criteriaBuilder.literal(""));
			  Expression<String> trimmedParam1 = criteriaBuilder.function("replace", String.class,(criteriaBuilder.upper(subqueryRoot.get("param1"))),criteriaBuilder.literal(" "),criteriaBuilder.literal(""));
			  Expression<String> trimmedGender = criteriaBuilder.function("replace", String.class,(criteriaBuilder.upper(criteriaBuilder.substring(root.get("gender"), 1, 1))),criteriaBuilder.literal(" "),criteriaBuilder.literal(""));

			  subquery.select(subqueryRoot.get("itemCode"))
			          .where(
			              criteriaBuilder.and(
			                  criteriaBuilder.equal(trimmedItemValue, trimmedRelationDesc),
			                  criteriaBuilder.equal(trimmedParam1, trimmedGender),
			                  criteriaBuilder.or(
			                      criteriaBuilder.equal(subqueryRoot.get("companyId"), root.get("companyId")),
			                      criteriaBuilder.equal(subqueryRoot.get("companyId"), "99999")
			                  )
			              )
			          );

			  // Set the value of PASS_RELATION_ID using the subquery
			  criteriaUpdate.set(root.<Integer>get("passRelationId"), subquery.as(Integer.class));

			  // Apply the WHERE clause
			  Predicate whereClause =criteriaBuilder.and(criteriaBuilder.equal(root.get("companyId"), companyId),
					  criteriaBuilder.equal(root.get("productId"), productId),
					  criteriaBuilder.equal(root.get("requestReferenceNo"), refNo),
					  criteriaBuilder.equal(root.get("quoteNo"), quoteNo)
					  );
			  criteriaUpdate.where(whereClause);

			  // Execute the update query
			   count = entityManager.createQuery(criteriaUpdate).executeUpdate();
		  }catch (Exception e) {
			  e.printStackTrace();
				log.error(e);
		}
		  return count;
	  }
	  
	  public Integer updateTravelErrorDesc(Object companyId,Object productId,Object quoteNo,Object refNo) {
		  try {
			  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			  CriteriaUpdate<EwayEmplyeeDetailRaw> criteriaUpdate =cb.createCriteriaUpdate(EwayEmplyeeDetailRaw.class);
			  Root<EwayEmplyeeDetailRaw> root =criteriaUpdate.from(EwayEmplyeeDetailRaw.class);
			  Expression<String> caseCon =cb.selectCase()
					  .when(cb.isNull(root.get("nationalityId")), "Please enter valid Nationality")
					  .when(cb.isNull(root.get("passRelationId")), "Please enter valid RelationShip")
					  .otherwise(root.get("errorDesc")).as(String.class);
			  criteriaUpdate.set(root.<String>get("errorDesc"), caseCon)
			  .where(
					  cb.equal(root.get("companyId"), companyId), cb.equal(root.get("productId"), productId), cb.equal(root.get("requestReferenceNo"), refNo),
					  cb.equal(root.get("status"), "Y"), cb.or(cb.isNull(root.get("apiStatus")),cb.isNull(root.get("apiStatus"))),cb.equal(root.get("quoteNo"), quoteNo)
					 
					);
			  entityManager.createQuery(criteriaUpdate).executeUpdate();
		  }catch (Exception e) {
			  e.printStackTrace();
				log.error(e);
		}
		 
		  return null;
		  
	  }
}
