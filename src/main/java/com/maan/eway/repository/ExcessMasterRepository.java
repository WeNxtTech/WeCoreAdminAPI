package com.maan.eway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.ExcessMaster;
import com.maan.eway.bean.ExcessMasterId;

public interface ExcessMasterRepository extends JpaRepository<ExcessMaster, ExcessMasterId>,
JpaSpecificationExecutor<ExcessMaster> {

	
    Optional<ExcessMaster> findTopByOrderByExcessIdDesc();
    
    List<ExcessMaster> findByCompanyIdAndSectionIdAndCoverId(String com,String pid,String CoverId);

}
