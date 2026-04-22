package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.bean.PolicyTypeMaster;
import com.maan.eway.bean.PolicyTypeMasterId;

public interface PolicyTypeMasterRepository extends JpaRepository<PolicyTypeMaster,PolicyTypeMasterId>, JpaSpecificationExecutor<PolicyTypeMaster>{

	// Insurance Class dropdown
	@Query(value = """
	    SELECT * FROM policy_type_master
	    WHERE company_id = :companyId AND product_id = :productId
	    AND status = 'Y'
	    AND effective_date_start <= NOW()
	    AND effective_date_end > NOW()
	    """, nativeQuery = true)
	List<PolicyTypeMaster> findActiveInsuranceClasses(
	    @Param("companyId") Integer companyId,
	    @Param("productId") Integer productId);
}
