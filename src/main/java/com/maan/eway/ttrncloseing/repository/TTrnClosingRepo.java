package com.maan.eway.ttrncloseing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.eway.ttrncloseing.bean.TTrnClosing;

public interface TTrnClosingRepo extends JpaRepository<TTrnClosing, Integer>{

	TTrnClosing findByBranchCodeAndProductCoreCode(String branchCode, String productCoreCode);

}
