package com.maan.eway.chart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ChartParentMasterRepository extends JpaRepository<ChartParentMaster, ChartParentMasterId> {

	List<ChartParentMaster> findByChatParentIdCompanyIdAndStatusIgnoreCase(Integer companyId, String status);

	List<ChartParentMaster> findByChatParentIdCompanyIdAndStatusIgnoreCaseOrderByDisplayOrderAsc(Integer companyId,
			String status);
	
	List<ChartParentMaster> findByChatParentIdCompanyId(Integer companyId);
	
	@Query("select cam  from ChartParentMaster cam where cam.chatParentId.companyId=?1 and cam.status=?2 "
			+ "and (current_date between cam.effectiveStartDate and cam.effectiveEndDate or cam.effectiveStartDate>=current_date)")
	List<ChartParentMaster> findParentData(Integer companyId,String status);
		

}
