package com.maan.eway.uploaddoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.uploaddoc.entity.UploadDocAcExecutiveMaster;
import com.maan.eway.uploaddoc.entity.UploadDocAcExecutiveMasterId;

public interface UploadDocAcExecutiveMasterRepository
		extends JpaRepository<UploadDocAcExecutiveMaster, UploadDocAcExecutiveMasterId> {

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

	@Query("""
			    select c from UploadDocAcExecutiveMaster c
			    where c.branchCode = :branchCode
			    and c.companyId = :companyId
			    and c.bankCode = :bankCode
			    AND s.status = :status
			    and coalesce(c.amendId, 0) = (
			        select max(coalesce(c2.amendId, 0))
			        from UploadDocAcExecutiveMaster c2
			        where c2.acExecutiveId = c.acExecutiveId
			        and c2.branchCode = c.branchCode
			        and c2.companyId = c.companyId
			        and c2.bankCode = c.bankCode
			    )
			""")
	List<UploadDocAcExecutiveMaster> findAllLatest(@Param("branchCode") String branchCode,
			@Param("companyId") String companyId, @Param("bankCode") String bankCode);

	@Query("""
			SELECT COALESCE(MAX(a.acExecutiveId), 0)
			FROM UploadDocAcExecutiveMaster a
			""")
	Integer findMaxAcExecutiveId();

	Optional<UploadDocAcExecutiveMaster> findByAcExecutiveId(Integer acExecutiveId);
	
	 @Query("""
		        SELECT a
		        FROM UploadDocAcExecutiveMaster a
		        WHERE a.companyId = :companyId
		          AND a.branchCode = :branchCode
		          AND a.amendId = (
		              SELECT MAX(b.amendId)
		              FROM UploadDocAcExecutiveMaster b
		              WHERE b.acExecutiveId = a.acExecutiveId
		                AND b.companyId = a.companyId
		                AND b.branchCode = a.branchCode
		          )
		          AND a.status = 'Y'
		        ORDER BY a.acExecutiveId
		    """)
		    List<UploadDocAcExecutiveMaster> findLatestByCompanyIdAndBranchCode(
		            @Param("companyId") String companyId,
		            @Param("branchCode") String branchCode);
}
