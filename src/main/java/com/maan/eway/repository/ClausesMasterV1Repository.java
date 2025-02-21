/**
 * @author : Ashok Kumar S 
 * @since  : 19-02-2025
 */
package com.maan.eway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.ClausesMasterV1;
import com.maan.eway.bean.ClausesMasterV1PK;

@Repository
public interface ClausesMasterV1Repository extends JpaRepository<ClausesMasterV1, ClausesMasterV1PK>{
	
	public Optional<ClausesMasterV1> findTopByCompanyIdAndProductIdAndSectionIdAndCoverIdOrderByClausesIdDescAmendIdDesc(
			Integer companyId, Integer productId, Integer sectionId, Integer coverId);

	public Optional<ClausesMasterV1> findTopByCompanyIdAndProductIdAndSectionIdAndCoverIdAndClausesIdOrderByAmendIdDesc(
			Integer companyId, Integer productId, Integer sectionId, Integer coverId, Integer clauseId);
}
