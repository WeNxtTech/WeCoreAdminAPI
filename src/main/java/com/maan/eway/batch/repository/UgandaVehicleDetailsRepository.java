package com.maan.eway.batch.repository;

import java.util.List;

import javax.transaction.Transactional;

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
	@Query(nativeQuery=true,value="UPDATE uganda_vehicle_details_raw ug SET motor_category_id=(SELECT item_Code FROM eway_list_item_value WHERE item_type='MOTOR_CATEGORY' AND company_id=ug.company_id AND UPPER(TRIM(item_value))=UPPER(TRIM(ug.motor_category_desc)) AND SYSDATE() BETWEEN effective_Date_start AND effective_Date_end LIMIT 1) WHERE request_reference_no=?1")
	Integer updateMotorCategory(String requestReferenceNo);

	

}
