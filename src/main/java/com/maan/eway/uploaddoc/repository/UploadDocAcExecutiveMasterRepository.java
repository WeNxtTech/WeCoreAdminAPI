package com.maan.eway.uploaddoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.uploaddoc.entity.UploadDocAcExecutiveMaster;
import com.maan.eway.uploaddoc.entity.UploadDocAcExecutiveMasterId;

public interface UploadDocAcExecutiveMasterRepository extends JpaRepository<UploadDocAcExecutiveMaster, UploadDocAcExecutiveMasterId> {

	@Query("select max(a.amendId) from UploadDocAcExecutiveMaster a where a.acExecutiveId = :acExecutiveId "
			+ "and a.branchCode = :branchCode and a.companyId = :companyId and a.bankCode = :bankCode")
	Optional<Integer> findMaxAmendId(@Param("acExecutiveId") Integer acExecutiveId,
			@Param("branchCode") String branchCode, @Param("companyId") String companyId,
			@Param("bankCode") String bankCode);

	@Query("select a from UploadDocAcExecutiveMaster a where a.acExecutiveId = :acExecutiveId and a.branchCode = :branchCode "
			+ "and a.companyId = :companyId and a.bankCode = :bankCode and a.amendId = :amendId")
	Optional<UploadDocAcExecutiveMaster> findByBusinessKeyAndAmendId(@Param("acExecutiveId") Integer acExecutiveId,
			@Param("branchCode") String branchCode, @Param("companyId") String companyId,
			@Param("bankCode") String bankCode, @Param("amendId") Integer amendId);

	@Query("select a from UploadDocAcExecutiveMaster a where a.amendId = (select max(a2.amendId) from UploadDocAcExecutiveMaster a2 "
			+ "where a2.acExecutiveId = a.acExecutiveId and a2.branchCode = a.branchCode and a2.companyId = a.companyId "
			+ "and a2.bankCode = a.bankCode)")
	List<UploadDocAcExecutiveMaster> findAllLatest();
}
