package com.maan.eway.uploaddoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.uploaddoc.entity.UploadDocNotifTemplateMaster;
import com.maan.eway.uploaddoc.entity.UploadDocNotifTemplateMasterId;

/**
 * Named with an "UploadDoc" prefix to avoid a Spring bean-name collision with
 * the pre-existing {@code com.maan.eway.repository.NotifTemplateMasterRepository}
 * (Spring Data derives the bean name from the interface's simple name, which
 * is package-independent).
 */
public interface UploadDocNotifTemplateMasterRepository
		extends JpaRepository<UploadDocNotifTemplateMaster, UploadDocNotifTemplateMasterId> {

	@Query("select max(n.amendId) from UploadDocNotifTemplateMaster n where n.notifTemplateCode = :notifTemplateCode "
			+ "and n.companyId = :companyId and n.productId = :productId")
	Optional<Integer> findMaxAmendId(@Param("notifTemplateCode") String notifTemplateCode,
			@Param("companyId") String companyId, @Param("productId") Long productId);

	Optional<UploadDocNotifTemplateMaster> findByNotifTemplateCodeAndCompanyIdAndProductIdAndAmendId(String notifTemplateCode,
			String companyId, Long productId, Integer amendId);

	@Query("select n from UploadDocNotifTemplateMaster n where n.amendId = (select max(n2.amendId) from UploadDocNotifTemplateMaster n2 "
			+ "where n2.notifTemplateCode = n.notifTemplateCode and n2.companyId = n.companyId and n2.productId = n.productId)")
	List<UploadDocNotifTemplateMaster> findAllLatest();
}
