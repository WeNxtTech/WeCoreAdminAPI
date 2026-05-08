package com.maan.eway.repository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;


	@Repository
	public class MasterLookupRepository {

	    @PersistenceContext
	    private EntityManager em;

	    public String findSectionIdByDesc(Integer insuranceId, Integer productId,
	                                      String branchCode, String desc) {
	        try {
	            String sql = """
	                SELECT CAST(section_id AS CHAR)
	                FROM product_section_master
	                WHERE company_id  = :insuranceId
	                  AND product_id  = :productId
	                  AND LOWER(section_name) = LOWER(:desc)
	                  AND status = 'Y'
	                  AND effective_date_start <= CURRENT_TIMESTAMP
	                  AND effective_date_end   >= CURRENT_TIMESTAMP
	                ORDER BY amend_id DESC
	                LIMIT 1
	                """;
	            return (String) em.createNativeQuery(sql)
	                .setParameter("insuranceId", insuranceId.toString())
	                .setParameter("productId",   productId)
	                .setParameter("desc",        desc)
	                .getSingleResult();
	        } catch (NoResultException e) {
	            return null;
	        }
	    }

	    public String findInsuranceClassIdByDesc(Integer insuranceId, Integer productId,
	                                             String branchCode, String desc) {
	        try {
	            String sql = """
	                SELECT CAST(policy_type_id AS CHAR)
	                FROM policy_type_master
	                WHERE company_id  = :insuranceId
	                  AND product_id  = :productId
	                  AND LOWER(policy_type_name) = LOWER(:desc)
	                  AND status = 'Y'
	                  AND effective_date_start <= CURRENT_TIMESTAMP
	                  AND effective_date_end   >= CURRENT_TIMESTAMP
	                ORDER BY amend_id DESC
	                LIMIT 1
	                """;
	            return (String) em.createNativeQuery(sql)
	                .setParameter("insuranceId", insuranceId.toString())
	                .setParameter("productId",   productId)
	                .setParameter("desc",        desc)
	                .getSingleResult();
	        } catch (NoResultException e) {
	            return null;
	        }
	    }

	    public String findMotorUsageIdByDesc(Integer insuranceId, String sectionId,
	                                         String branchCode, String desc) {
	        try {
	            String sql = """
	                SELECT CAST(vehicle_usage_id AS CHAR)
	                FROM motor_vehicleusage_master
	                WHERE company_id  = :insuranceId
	                  AND section_id  = :sectionId
	                  AND LOWER(vehicle_usage_desc) = LOWER(:desc)
	                  AND status = 'Y'
	                  AND effective_date_start <= CURRENT_TIMESTAMP
	                  AND effective_date_end   >= CURRENT_TIMESTAMP
	                ORDER BY amend_id DESC
	                LIMIT 1
	                """;
	            return (String) em.createNativeQuery(sql)
	                .setParameter("insuranceId", insuranceId.toString())
	                .setParameter("sectionId",   sectionId)
	                .setParameter("desc",        desc)
	                .getSingleResult();
	        } catch (NoResultException e) {
	            return null;
	        }
	    }

	    public String findPolicyTypeByDesc(Integer insuranceId, Integer productId,
	                                       String branchCode, String desc) {
	        try {
	            String sql = """
	                SELECT CAST(section_id AS CHAR)
	                FROM product_section_master
	                WHERE company_id  = :insuranceId
	                  AND product_id  = :productId
	                  AND LOWER(section_name) = LOWER(:desc)
	                  AND status = 'Y'
	                  AND effective_date_start <= CURRENT_TIMESTAMP
	                  AND effective_date_end   >= CURRENT_TIMESTAMP
	                ORDER BY amend_id DESC
	                LIMIT 1
	                """;
	            return (String) em.createNativeQuery(sql)
	                .setParameter("insuranceId", insuranceId.toString())
	                .setParameter("productId",   productId)
	                .setParameter("desc",        desc)
	                .getSingleResult();
	        } catch (NoResultException e) {
	            return null;
	        }
	    }
	 // In MasterLookupRepository — fetch resolved IDs from eservice_motor_details
	 // after save1 persists the record with descriptions
	 public Map<String, Object> findVehicleIdsFromMotorDetails(String requestReferenceNo, String vehicleId) {
	     try {
	         String sql = """
	             SELECT 
	                 CAST(VEHICLE_MAKE_ID   AS CHAR) AS vehicleMakeId,
	                 CAST(VEHICLE_MODEL_ID  AS CHAR) AS vehicleModelId,
	                 CAST(VEHICLE_TYPE      AS CHAR) AS vehicleTypeId,
	                 CAST(FUEL_TYPE_ID      AS CHAR) AS fuelTypeId,
	                 CAST(COLOR             AS CHAR) AS colorId,
	                 CAST(OWNER_CATEGORY_ID AS CHAR) AS ownerCategoryId,
	                 CAST(MOTOR_USAGE       AS CHAR) AS motorUsageId,
	                 CAST(INSURANCE_TYPE    AS CHAR) AS insuranceTypeId,
	                 CAST(INSURANCE_CLASS   AS CHAR) AS insuranceClassId,
	                 CAST(MOTOR_CATEGORY    AS CHAR) AS motorCategoryId,
	                 VEHICLE_MAKE_DESC      AS vehicleMakeDesc,
	                 VEHCILE_MODEL_DESC     AS vehicleModelDesc,
	                 VEHICLE_TYPE_DESC      AS vehicleTypeDesc,
	                 FUEL_TYPE_DESC         AS fuelTypeDesc,
	                 COLOR_DESC             AS colorDesc,
	                 MOTOR_USAGE_DESC       AS motorUsageDesc,
	                 INSURANCE_TYPE_DESC    AS insuranceTypeDesc,
	                 INSURANCE_CLASS_DESC   AS insuranceClassDesc,
	                 CAST(SECTION_ID        AS CHAR) AS sectionId,
	                 CUBIC_CAPACITY         AS cubicCapacity,
	                 CAST(SEATING_CAPACITY  AS CHAR) AS seatingCapacity,
	                 CAST(GROSS_WEIGHT      AS CHAR) AS grossWeight,
	                 CAST(TARE_WEIGHT       AS CHAR) AS tareWeight,
	                 CAST(NUMBER_OF_AXELS   AS CHAR) AS numberOfAxels,
	                 CAST(AXEL_DISTANCE     AS CHAR) AS axelDistance
	             FROM eservice_motor_details
	             WHERE request_reference_no = :rrn
	               AND risk_id = :vehicleId
	             ORDER BY updated_date DESC
	             LIMIT 1
	             """;

	         Object[] row = (Object[]) em.createNativeQuery(sql)
	             .setParameter("rrn",       requestReferenceNo)
	             .setParameter("vehicleId", vehicleId)
	             .getSingleResult();

	         Map<String, Object> result = new LinkedHashMap<>();
	         result.put("vehicleMakeId",    row[0]);
	         result.put("vehicleModelId",   row[1]);
	         result.put("vehicleTypeId",    row[2]);
	         result.put("fuelTypeId",       row[3]);
	         result.put("colorId",          row[4]);
	         result.put("ownerCategoryId",  row[5]);
	         result.put("motorUsageId",     row[6]);
	         result.put("insuranceTypeId",  row[7]);
	         result.put("insuranceClassId", row[8]);
	         result.put("motorCategoryId",  row[9]);
	         result.put("vehicleMakeDesc",  row[10]);
	         result.put("vehicleModelDesc", row[11]);
	         result.put("vehicleTypeDesc",  row[12]);
	         result.put("fuelTypeDesc",     row[13]);
	         result.put("colorDesc",        row[14]);
	         result.put("motorUsageDesc",   row[15]);
	         result.put("insuranceTypeDesc",row[16]);
	         result.put("insuranceClassDesc",row[17]);
	         result.put("sectionId",        row[18]);
	         result.put("cubicCapacity",    row[19]);
	         result.put("seatingCapacity",  row[20]);
	         result.put("grossWeight",      row[21]);
	         result.put("tareWeight",       row[22]);
	         result.put("numberOfAxels",    row[23]);
	         result.put("axelDistance",     row[24]);
	         return result;

	     } catch (NoResultException e) {
	         return Collections.emptyMap();
	     }
	 }
	}