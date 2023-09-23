package com.maan.eway.excelconfig;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UploadTypeRepo extends JpaRepository<UploadType, UploadTypePK>{

	@Query(nativeQuery = true,value="select max(typeid) from eway_upload_type_master where company_id=?1 group by company_id")
	public Integer getLastNo(String companyId);

	@Query(nativeQuery = true, value="select * from eway_upload_type_master where company_id=?1 and product_id=?2 and typeid=?3 and section_id=?4")
	public UploadType findBy(String companyId, String productId, String typeId, String sectionId);
	
	@Query(nativeQuery = true, value="select * from eway_upload_type_master where company_id=?1")
	public List<UploadType> findByCompanyId(String companyId);
	
}
