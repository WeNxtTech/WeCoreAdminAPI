package com.maan.eway.springbatch;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactorRateRawMasterRepository extends JpaRepository<FactorRateRawInsert, FactorRateRawMasterId> {

	List<FactorRateRawInsert> findByTranId(String tranId);

	List<FactorRateRawInsert> findByTranIdAndErrorDescIsNull(String tranId);

	List<FactorRateRawInsert> findByTranIdAndErrorDescIsNotNull(String tranId);

	List<FactorRateRawInsert> findByTranIdAndErrorStatus(String tranId, String status);

	Long countByTranIdAndErrorStatus(String tranId, String status);

}
