package com.maan.eway.ttrncloseing.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.eway.ttrncloseing.bean.TTrnClosing;

public interface TTrnClosingRepo extends JpaRepository<TTrnClosing, Integer>{

//	TTrnClosing findByBranchCodeAndProductCoreCode(String branchCode, String productCoreCode);

//	TTrnClosing findByBranchCodeAndProductCoreCodeAndCompanyid(String branchCode, String productCoreCode,
//			String companyId);

	List<TTrnClosing> findByBranchCodeAndProductIdAndCompanyid(String branchCode, String productId, String companyId);

	TTrnClosing findByBranchCodeAndProductIdAndCompanyidAndSetUpMonth(String branchCode, String productId,
			String companyId, Date setUpMonth);

	TTrnClosing findByBranchCodeAndProductIdAndCompanyidAndTranCode(String branchCode, String productCoreCode,
			String companyId, Integer tranCode);

}
