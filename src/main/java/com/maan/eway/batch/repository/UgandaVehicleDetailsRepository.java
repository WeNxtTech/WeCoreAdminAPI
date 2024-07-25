package com.maan.eway.batch.repository;

import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maan.eway.batch.entity.UgandaVehicleDetailsRaw;
import com.maan.eway.batch.entity.UgandaVehicleDetailsRawId;

@Repository
public interface UgandaVehicleDetailsRepository extends JpaRepository<UgandaVehicleDetailsRaw, UgandaVehicleDetailsRawId>{

	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="UPDATE UGANDA_VEHICLE_DETAILS_RAW SET error_desc ='duplicate registration number has found' , STATUS='E' WHERE request_reference_no=?1 AND registration_no IN((SELECT registration_no FROM(SELECT registration_no FROM UGANDA_VEHICLE_DETAILS_RAW WHERE request_reference_no=?1 AND STATUS='Y' GROUP BY registration_no HAVING COUNT(registration_no)>1) temp))")
	Integer updateDuplicateRegistrationNo(String requestReferenceNo);

	List<UgandaVehicleDetailsRaw> findByRequestReferenceNo(String requestReferenceNo);
	
	List<UgandaVehicleDetailsRaw> findByRequestReferenceNoAndEwayReferenceNoNotNull(String requestReferenceNo);

	
	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="UPDATE `eservice_motor_details_raw` mot SET motor_usage_id=(SELECT DISTINCT vehicle_usage_id FROM motor_vehicleusage_master WHERE UPPER(TRIM(vehicle_usage_desc))=UPPER(TRIM(mot.motor_usage_desc)) AND company_id=mot.company_id AND STATUS='Y' AND SYSDATE() BETWEEN effective_date_start AND effective_date_end) WHERE request_reference_no=?1")
	Integer updateMotorUsageId(String requestReferenceNo);

	List<UgandaVehicleDetailsRaw> findByEwayReferenceNo(String requestReferenceNo);

	
	@Query(nativeQuery=true,value ="SELECT registration_number,chassis_number,tira_motor_usage,tira_body_type,color_desc,sum_insured, actual_premium_lc AS premium,vat_premium AS VAT ,overall_premium_lc AS total_premium , policy_start_date AS inception_date , policy_end_date AS expiry_date, request_reference_no FROM eservice_motor_details WHERE registration_number=?1 AND company_id=?2 ORDER BY entry_date DESC LIMIT 1")
	public List<Map<String,Object>> getrenewalData(String regNo,String companyId);
	

}
