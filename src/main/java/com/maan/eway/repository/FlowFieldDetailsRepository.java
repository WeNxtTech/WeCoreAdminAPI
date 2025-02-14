/**
 * @author : Ashok Kumar S 
 * @since  : 10-02-2025
 */
package com.maan.eway.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.FlowFieldDetails;
import com.maan.eway.bean.FlowFieldDetailsId;

@Repository
public interface FlowFieldDetailsRepository extends JpaRepository<FlowFieldDetails, FlowFieldDetailsId> {
	
	public List<FlowFieldDetails> findAllByCompanyIdAndProductIdAndIntegTypeOrderByKeyId(
			BigDecimal companyId, BigDecimal productId, String integType);
	
	public FlowFieldDetails findTopByCompanyIdAndProductIdOrderByKeyIdDesc(
			BigDecimal companyId, BigDecimal productId);
	
	public FlowFieldDetails findTopByCompanyIdAndProductIdAndIntegTypeOrderByKeyIdDesc(
			BigDecimal companyId, BigDecimal productId, String integType);
	
	
	public List<FlowFieldDetails> findAllByCompanyIdAndProductIdAndIntegTypeAndStatus(
			BigDecimal companyId, BigDecimal productId, String integType, String status);
	
	public Optional<FlowFieldDetails> findByCompanyIdAndProductIdAndIntegTypeAndKeyIdAndStatus(
			BigDecimal companyId, BigDecimal productId, String integType, BigDecimal keyId, String status);

}
