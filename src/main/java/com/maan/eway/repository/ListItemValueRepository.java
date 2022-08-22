package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.maan.eway.bean.ListITemValue;

@Repository
public interface ListItemValueRepository  extends JpaRepository<ListITemValue, Integer> , JpaSpecificationExecutor<ListITemValue>{

	List<ListITemValue> findByItemTypeAndStatusOrderByItemCodeAsc(String string, String string2);


}
