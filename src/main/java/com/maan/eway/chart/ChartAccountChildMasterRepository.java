package com.maan.eway.chart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartAccountChildMasterRepository extends JpaRepository<ChartAccountChildMaster, ChartAccountChildMasterId> {

	List<ChartAccountChildMaster> findByIdCompanyIdAndIdProductIdAndIdSectionIdAndIdChartId(Integer companyId,
			Integer productId, Integer sectionId, Integer chartId);

	List<ChartAccountChildMaster> findByIdCompanyIdAndIdProductIdAndIdSectionIdAndIdChartIdAndIdCoverId(Integer valueOf,
			Integer valueOf2, Integer valueOf3, Integer valueOf4, Integer valueOf5);


	@Query("select cam  from ChartAccountChildMaster cam where cam.id.companyId=?1 and cam.id.productId=?2 and cam.id.sectionId=?3 and cam.id.chartId=?4 and current_date between cam.effectiveStartDate and cam.effectiveEndDate ")
	List<ChartAccountChildMaster> findChildData(Integer companyId,
			Integer productId, Integer sectionId, Integer chartId);


}
