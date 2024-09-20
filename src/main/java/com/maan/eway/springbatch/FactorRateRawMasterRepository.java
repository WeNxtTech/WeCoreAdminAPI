package com.maan.eway.springbatch;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface FactorRateRawMasterRepository extends JpaRepository<FactorRateRawInsert, FactorRateRawMasterId> {

	Page<FactorRateRawInsert> findByTranIdAndSnoBetween(String tranId ,Integer start, Integer end,PageRequest page);

	List<FactorRateRawInsert> findByTranIdAndErrorDescIsNull(String tranId);

	List<FactorRateRawInsert> findByTranIdAndErrorDescIsNotNull(String tranId);

	List<FactorRateRawInsert> findByTranIdAndErrorStatus(String tranId, String status);

	Long countByTranIdAndErrorStatus(String tranId, String status);

	Long countByTranIdAndStatus(String tranId, String status);
	
	List<FactorRateRawInsert> findByTranId(String tranId);

	@Transactional
	@Modifying
	@Query(value="UPDATE factor_rate_raw_master r SET STATUS ='E', error_desc='You have entered agencycode not matched in excel sheet' WHERE r.tran_id=?1 AND r.company_id=?2 AND r.product_id =?3 AND r.agency_code NOT IN(xl_agency_Code) ",nativeQuery=true)
	Integer updateAgencyCodeValidation(String tranId, String companyId, String productId);

	List<FactorRateRawInsert> findByTranIdAndStatus(String tranId, String string);

	Long countByTranId(String tranId);

	@Query(nativeQuery=true,value="select * from factor_rate_raw_master where TRAN_ID=?1 and SNO=?2 and ERROR_STATUS IS NULL")
	FactorRateRawInsert findByTranIdAndSnoAndErrorStatusIsNull(String tran_id, int i);

	Long countByTranIdAndErrorStatusIsNull(String tran_id);

	FactorRateRawInsert findByTranIdAndSno(String tran_id,Integer sno);

	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from factor_rate_raw_master where TRAN_ID=?1")
	void deleteFactorData(String referenceNo);

	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="DELETE FROM eservice_motor_details_raw WHERE request_reference_no=?1")
	void deleteMotorRawData(String referenceNo);
	
	@Query(nativeQuery=true,value ="SELECT xl_agency_code FROM factor_rate_raw_master WHERE tran_id=?1 AND xl_agency_code IS NOT NULL GROUP BY xl_agency_code")
	List<String> getXlAgencyCode(String request_ref);

}
