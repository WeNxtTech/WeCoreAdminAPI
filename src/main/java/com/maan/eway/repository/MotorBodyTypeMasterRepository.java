package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.bean.MotorBodyTypeMaster;
import com.maan.eway.bean.MotorBodyTypeMasterId;

public interface MotorBodyTypeMasterRepository extends JpaRepository<MotorBodyTypeMaster, MotorBodyTypeMasterId> , JpaSpecificationExecutor<MotorBodyTypeMaster>{
	// Body Type dropdown — per section_id for cascading
	@Query(value = """
	    SELECT * FROM eway_motor_bodytype_master
	    WHERE company_id = :companyId AND section_id = :sectionId
	    AND status = 'Y'
	    AND effective_date_start <= NOW()
	    AND effective_date_end > NOW()
	    """, nativeQuery = true)
	List<MotorBodyTypeMaster> findActiveBodyTypesBySectionId(
	    @Param("companyId") Integer companyId,
	    @Param("sectionId") Integer sectionId);

}
