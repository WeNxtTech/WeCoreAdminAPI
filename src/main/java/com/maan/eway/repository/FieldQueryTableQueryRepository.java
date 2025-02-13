/**
 * @author : Ashok Kumar S 
 * @since  : 10-02-2025
 */
package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.FieldQueryTablequery;
import com.maan.eway.bean.FieldQueryTablequeryId;

@Repository
public interface FieldQueryTableQueryRepository extends JpaRepository<FieldQueryTablequery, FieldQueryTablequeryId>{
	
	public FieldQueryTablequery findTopByOrderByQueryIdDesc();
	
	public List<FieldQueryTablequery> findAllByOrderByQueryIdAsc();

}
