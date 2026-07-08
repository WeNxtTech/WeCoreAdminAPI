package com.maan.eway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.maan.eway.bean.WecoreLobMaster;

public interface WecoreLobMasterRepository extends JpaRepository<WecoreLobMaster, Long> {
	Optional<WecoreLobMaster> findTopByClassCodeOrderByAmendIdDesc(String classCode);

	Optional<WecoreLobMaster> findTopByClassNameOrderByAmendIdDesc(String className);

	Integer findMaxAmendIdByClassCode(String classCode);

	@Query("select coalesce(max(w.amendId),0) from WecoreLobMaster w where w.classCode=:classCode")
	Integer getMaxAmendId(@Param("classCode") String classCode);

	@Query("""
			    SELECT w
			    FROM WecoreLobMaster w
			    WHERE w.status <> 'N'
			    AND w.amendId = (
			        SELECT MAX(x.amendId)
			        FROM WecoreLobMaster x
			        WHERE x.className = w.className
			    )
			    ORDER BY w.className
			""")
	List<WecoreLobMaster> findLatestLobConfigs();
}
