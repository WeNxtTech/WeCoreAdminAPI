package com.maan.eway.springbatch;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FactorRateRawMasterRepository extends JpaRepository<FactorRateRawInsert, FactorRateRawMasterId> {

	List<FactorRateRawInsert> findByTranId(String tranId);

	List<FactorRateRawInsert> findByTranIdAndErrorDescIsNull(String tranId);

	List<FactorRateRawInsert> findByTranIdAndErrorDescIsNotNull(String tranId);

	List<FactorRateRawInsert> findByTranIdAndErrorStatus(String tranId, String status);

	Long countByTranIdAndErrorStatus(String tranId, String status);

	Long countByTranIdAndStatus(String tranId, String status);

	@Transactional
	@Modifying
	@Query(value="UPDATE factor_rate_raw_master r SET STATUS ='E', error_desc='You have entered agencycode not matched in excel sheet' WHERE r.tran_id=?1 AND r.company_id=?2 AND r.product_id =?3 AND r.agency_code NOT IN(xl_agency_Code) ",nativeQuery=true)
	Integer updateAgencyCodeValidation(String tranId, String companyId, String productId);

	List<FactorRateRawInsert> findByTranIdAndStatus(String tranId, String string);

	Long countByTranId(String tranId);

}
