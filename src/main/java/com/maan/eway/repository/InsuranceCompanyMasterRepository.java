package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.InsuranceCompanyMaster;
import com.maan.eway.bean.InsuranceCompanyMasterId;

public interface InsuranceCompanyMasterRepository extends JpaRepository<InsuranceCompanyMaster, InsuranceCompanyMasterId>,JpaSpecificationExecutor<InsuranceCompanyMaster> {

	List<InsuranceCompanyMaster> findByStatusOrderByInsNameAsc(String string);
	
}
