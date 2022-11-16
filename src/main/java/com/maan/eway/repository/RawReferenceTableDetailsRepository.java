package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.RawReferenceTableDetails;

public interface RawReferenceTableDetailsRepository extends JpaRepository<RawReferenceTableDetails, Integer>, JpaSpecificationExecutor<RawReferenceTableDetails> {



	List<RawReferenceTableDetails> findByTableIdAndStatusOrderByColumnIdAsc(Integer valueOf, String string);

	List<RawReferenceTableDetails> findByTableNameAndStatusOrderByTableIdAsc(String string, String string2);

}
