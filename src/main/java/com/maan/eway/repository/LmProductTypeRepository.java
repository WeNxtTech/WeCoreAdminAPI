package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.eway.bean.LmProductType;
import com.maan.eway.bean.LmProductTypeId;

public interface LmProductTypeRepository extends JpaRepository<LmProductType, LmProductTypeId> {

}
