package com.maan.eway.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maan.eway.batch.entity.EwayUploadTypeMasterIdV1;
import com.maan.eway.batch.entity.EwayUploadTypeMasterV1;

@Repository
public interface EwayUploadTypeMasterRepositoryV2  extends JpaRepository<EwayUploadTypeMasterV1, EwayUploadTypeMasterIdV1>{

	
	@Query("select max(u.typeid) from EwayUploadTypeMasterV1 u where u.companyId=:companyId and u.productId=:productId")
	Integer getTypeIdByCompanyIdAndProduyctId(@Param("companyId") Integer companyId,@Param("productId") Integer productId);
	
}
