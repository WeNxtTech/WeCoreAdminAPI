package com.maan.eway.batch.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.eway.bean.TirraErrorHistory;
import com.maan.eway.bean.TirraErrorHistoryId;

public interface TirraErrorHistoryRepository extends JpaRepository<TirraErrorHistory, TirraErrorHistoryId>{

	List<TirraErrorHistory> findByReqRegNumberAndEntryDateBetween(String registrationNumber, Date effectiveStartDate,
			Date effectiveEndDate);

	List<TirraErrorHistory> findByEntryDateBetween(Date effectiveStartDate, Date effectiveEndDate);

}
