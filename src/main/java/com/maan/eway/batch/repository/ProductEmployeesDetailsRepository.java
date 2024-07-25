package com.maan.eway.batch.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.ProductEmployeeDetails;
import com.maan.eway.bean.ProductEmployeeDetailsId;

@Repository
public interface ProductEmployeesDetailsRepository extends JpaRepository<ProductEmployeeDetails, ProductEmployeeDetailsId>, JpaSpecificationExecutor<ProductEmployeeDetails> {

	Long countByQuoteNoAndRiskId(String quoteNo, Integer valueOf);

	@Transactional
	void deleteByQuoteNoAndRiskId(String quoteNo, Integer valueOf);

	List<ProductEmployeeDetails> findByQuoteNo(String quoteNo);

	List<ProductEmployeeDetails> findByQuoteNoAndRiskIdOrderByEmployeeIdAsc(String quoteNo, Integer valueOf);

	Long countByQuoteNoAndRiskIdAndEmployeeId(String quoteNo, Integer valueOf, Long valueOf2);

	@Transactional
	void deleteByQuoteNoAndRiskIdAndEmployeeId(String quoteNo, Integer valueOf, Long valueOf2);

	List<ProductEmployeeDetails> findByQuoteNoAndRiskIdAndSectionIdOrderByEmployeeIdAsc(String quoteNo, Integer valueOf,
			String sectionId);

	Long countByQuoteNoAndRiskIdAndEmployeeIdAndSectionId(String quoteNo, Integer valueOf, Long valueOf2,
			String sectionId);

	void deleteByQuoteNoAndRiskIdAndEmployeeIdAndSectionId(String quoteNo, Integer valueOf, Long valueOf2,
			String sectionId);

	Long countByQuoteNoAndRiskIdAndSectionId(String quoteNo, Integer riskId, String sectionId);

	void deleteByQuoteNoAndRiskIdAndSectionId(String quoteNo, Integer riskId, String sectionId);

	List<ProductEmployeeDetails> findByQuoteNoAndRiskIdAndSectionIdAndEmployeeId(String quoteNo, Integer valueOf,
			String sectionId, Long valueOf2);

	List<ProductEmployeeDetails> findByQuoteNoAndRiskIdAndSectionId(String quoteNo, Integer valueOf, String sectionId);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionId(String quoteNo, String sectionId);

	void deleteByQuoteNoAndSectionId(String quoteNo, String sectionId);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionIdOrderByEmployeeIdAsc(String quoteNo, String sectionId);

	List<ProductEmployeeDetails> findByQuoteNoAndSectionIdAndEmployeeId(String quoteNo, String sectionId, Long valueOf);

	void deleteByQuoteNoAndEmployeeIdAndSectionId(String quoteNo, Long valueOf, String sectionId);

}
