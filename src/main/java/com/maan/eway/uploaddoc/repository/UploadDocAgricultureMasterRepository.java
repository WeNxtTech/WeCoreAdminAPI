package com.maan.eway.uploaddoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.uploaddoc.entity.UploadDocAgricultureMaster;
import com.maan.eway.uploaddoc.entity.UploadDocAgricultureMasterId;

public interface UploadDocAgricultureMasterRepository extends JpaRepository<UploadDocAgricultureMaster, UploadDocAgricultureMasterId> {

	@Query("select max(a.amendId) from UploadDocAgricultureMaster a where a.sno = :sno and a.companyId = :companyId and a.productId = :productId")
	Optional<Integer> findMaxAmendId(@Param("sno") Integer sno, @Param("companyId") Integer companyId,
			@Param("productId") Integer productId);

	Optional<UploadDocAgricultureMaster> findBySnoAndCompanyIdAndProductIdAndAmendId(Integer sno, Integer companyId,
			Integer productId, Integer amendId);

	@Query("select a from UploadDocAgricultureMaster a where a.amendId = (select max(a2.amendId) from UploadDocAgricultureMaster a2 "
			+ "where a2.sno = a.sno and a2.companyId = a.companyId and a2.productId = a.productId)")
	List<UploadDocAgricultureMaster> findAllLatest();
	
	@Query("""
		    select c
		    from UploadDocAgricultureMaster c
		    where c.companyId = :companyId
		    and c.productId = :productId
		    and c.amendId = (
		        select max(c2.amendId)
		        from UploadDocAgricultureMaster c2
		        where c2.sno = c.sno
		        and c2.companyId = c.companyId
		        and c2.productId = c.productId
		    )
		""")
		List<UploadDocAgricultureMaster> findAllLatest(
		        @Param("companyId") Integer companyId,
		        @Param("productId") Integer productId
		);
}
