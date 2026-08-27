package com.maan.eway.uploaddoc.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.uploaddoc.entity.UploadDocStateMaster;
import com.maan.eway.uploaddoc.entity.UploadDocStateMasterId;

public interface UploadDocStateMasterRepository extends JpaRepository<UploadDocStateMaster, UploadDocStateMasterId> {

	@Query("select max(s.amendId) from UploadDocStateMaster s where s.stateId = :stateId and s.stateShortCode = :stateShortCode "
			+ "and s.countryId = :countryId and s.regionCode = :regionCode and s.cityId = :cityId and s.suburbId = :suburbId")
	Optional<Integer> findMaxAmendId(@Param("stateId") Integer stateId, @Param("stateShortCode") String stateShortCode,
			@Param("countryId") String countryId, @Param("regionCode") String regionCode,
			@Param("cityId") Integer cityId, @Param("suburbId") Integer suburbId);

	Optional<UploadDocStateMaster> findByStateIdAndStateShortCodeAndCountryIdAndRegionCodeAndCityIdAndSuburbIdAndAmendId(
			Integer stateId, String stateShortCode, String countryId, String regionCode, Integer cityId,
			Integer suburbId, Integer amendId);

	@Query("""
			    SELECT s
			    FROM UploadDocStateMaster s
			    WHERE s.countryId = :countryId
			     AND s.amendId = (
			          SELECT MAX(s2.amendId)
			          FROM UploadDocStateMaster s2
			          WHERE s2.stateId = s.stateId
			            AND s2.stateShortCode = s.stateShortCode
			            AND s2.countryId = s.countryId
			            AND s2.regionCode = s.regionCode
			            AND s2.cityId = s.cityId
			            AND s2.suburbId = s.suburbId
			      )
			""")
	List<UploadDocStateMaster> findAllLatest(@Param("countryId") String countryId);
	
	@Query("""
		    SELECT s
		    FROM UploadDocStateMaster s
		    WHERE s.countryId = :countryId
		     AND  s.regionCode = :regionCode
		     AND s.amendId = (
		          SELECT MAX(s2.amendId)
		          FROM UploadDocStateMaster s2
		          WHERE s2.stateId = s.stateId
		            AND s2.stateShortCode = s.stateShortCode
		            AND s2.countryId = s.countryId
		            AND s2.regionCode = s.regionCode
		            AND s2.cityId = s.cityId
		            AND s2.suburbId = s.suburbId
		      )
		""")

	Collection<UploadDocStateMaster> findAllLatest(String countryId, String regionCode);
}
