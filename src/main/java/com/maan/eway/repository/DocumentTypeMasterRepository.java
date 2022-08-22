package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.DocumentTypeMaster;
import com.maan.eway.bean.DocumentTypeMasterId;
 
public interface DocumentTypeMasterRepository  extends JpaRepository<DocumentTypeMaster,DocumentTypeMasterId> , JpaSpecificationExecutor<DocumentTypeMaster> {


}
