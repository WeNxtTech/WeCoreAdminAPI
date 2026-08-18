package com.maan.eway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.LocationMaster;

@Repository
public interface LocationMasterRepository extends JpaRepository<LocationMaster, Long> {

	// Fetch locations matching specific branchId or 'ALL'
	@Query("SELECT MAX(l.amendId) FROM LocationMaster l WHERE l.companyId = :companyId AND l.coreAppCode = :coreAppCode")
	Optional<Integer> findMaxAmendId(@Param("companyId") String companyId, @Param("coreAppCode") String coreAppCode);

	// Find current active record by coreAppCode
	Optional<LocationMaster> findByCompanyIdAndCoreAppCodeAndStatus(String companyId, String coreAppCode,
			String status);

	// Fetch active records by Company ID
	List<LocationMaster> findByCompanyIdAndStatus(String companyId, String status);

	// Fetch active records for Global Branch ('99999') + specific Branch
	@Query("SELECT l FROM LocationMaster l WHERE l.companyId = :companyId AND l.status = 'Y' AND l.branchId IN ('99999', :branchId)")
	List<LocationMaster> findByCompanyIdAndBranchWithGlobal(@Param("companyId") String companyId,
			@Param("branchId") String branchId);
}