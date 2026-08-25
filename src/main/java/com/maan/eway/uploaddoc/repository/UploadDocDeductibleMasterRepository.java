package com.maan.eway.uploaddoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.uploaddoc.entity.UploadDocDeductibleMaster;

public interface UploadDocDeductibleMasterRepository extends JpaRepository<UploadDocDeductibleMaster, Long> {

	@Query("select max(d.amendId) from UploadDocDeductibleMaster d where d.deductId = :deductId and d.companyId = :companyId "
			+ "and d.productId = :productId and d.sectionId = :sectionId and d.branchCode = :branchCode")
	Optional<Integer> findMaxAmendId(@Param("deductId") Integer deductId, @Param("companyId") String companyId,
			@Param("productId") Integer productId, @Param("sectionId") Integer sectionId,
			@Param("branchCode") String branchCode);

	Optional<UploadDocDeductibleMaster> findByDeductIdAndCompanyIdAndProductIdAndSectionIdAndBranchCodeAndAmendId(
			Integer deductId, String companyId, Integer productId, Integer sectionId, String branchCode,
			Integer amendId);

	@Query("select d from UploadDocDeductibleMaster d where d.amendId = (select max(d2.amendId) from UploadDocDeductibleMaster d2 "
			+ "where d2.deductId = d.deductId and d2.companyId = d.companyId and d2.productId = d.productId "
			+ "and d2.sectionId = d.sectionId and d2.branchCode = d.branchCode)")
	List<UploadDocDeductibleMaster> findAllLatest();
}
