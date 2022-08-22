package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.BranchMaster;
import com.maan.eway.bean.BranchMasterId;

public interface BranchMasterRepository	extends JpaRepository<BranchMaster, BranchMasterId>, JpaSpecificationExecutor<BranchMaster> {

	List<BranchMaster> findByBranchCodeInOrderByBranchCodeAsc(List<String> branches);

	

}
