package com.maan.eway.uploaddoc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.uploaddoc.entity.UploadDocCityMaster;
import com.maan.eway.uploaddoc.entity.UploadDocCityMasterId;

public interface UploadDocCityMasterRepository extends JpaRepository<UploadDocCityMaster, UploadDocCityMasterId> {

	@Query("select max(c.amendId) from UploadDocCityMaster c where c.cityId = :cityId and c.countryId = :countryId and c.stateId = :stateId")
	Optional<Integer> findMaxAmendId(@Param("cityId") Integer cityId, @Param("countryId") String countryId,
			@Param("stateId") String stateId);

	Optional<List<UploadDocCityMaster>> findByCityIdAndCountryIdAndStateIdAndAmendId(
	        Integer cityId, String countryId, String stateId, Integer amendId);

	@Query("select c from UploadDocCityMaster c where c.amendId = (select max(c2.amendId) from UploadDocCityMaster c2 "
			+ "where c2.cityId = c.cityId and c2.countryId = c.countryId and c2.stateId = c.stateId)")
	List<UploadDocCityMaster> findAllLatest();

	Optional<List<UploadDocCityMaster>>  findByCountryIdAndStateIdAndAmendId(String countryId, String stateId, Integer integer);
	
	@Query("""
		    select c from UploadDocCityMaster c
		    where c.countryId = :countryId
		    and c.stateId = :stateId
		    and c.amendId = (
		        select max(c2.amendId) from UploadDocCityMaster c2
		        where c2.cityId = c.cityId
		        and c2.countryId = c.countryId
		        and c2.stateId = c.stateId
		    )
		""")
		List<UploadDocCityMaster> findLatestAmendmentPerCity(
		    @Param("countryId") String countryId,
		    @Param("stateId") String stateId
		);
}
