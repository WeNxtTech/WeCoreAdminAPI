package com.maan.eway.uploaddoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.uploaddoc.entity.UploadDocRegionMaster;
import com.maan.eway.uploaddoc.entity.UploadDocRegionMasterId;

public interface UploadDocRegionMasterRepository extends JpaRepository<UploadDocRegionMaster, UploadDocRegionMasterId> {

	@Query("select max(r.amendId) from UploadDocRegionMaster r where r.regionCode = :regionCode and r.countryId = :countryId")
	Optional<Integer> findMaxAmendId(@Param("regionCode") String regionCode, @Param("countryId") String countryId);

	Optional<UploadDocRegionMaster> findByRegionCodeAndCountryIdAndAmendId(String regionCode, String countryId,
			Integer amendId);

	@Query("""
			    SELECT r
			    FROM UploadDocRegionMaster r
			    WHERE r.countryId = :countryId
			      AND r.amendId = (
			          SELECT MAX(r2.amendId)
			          FROM UploadDocRegionMaster r2
			          WHERE r2.regionCode = r.regionCode
			            AND r2.countryId = r.countryId
			      )
			""")
	List<UploadDocRegionMaster> findAllLatest(@Param("countryId") String countryId);
}
