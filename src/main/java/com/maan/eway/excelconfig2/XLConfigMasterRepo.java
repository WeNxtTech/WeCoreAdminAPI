package com.maan.eway.excelconfig2;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.maan.eway.bean.OneTimeTableDetails;

public interface XLConfigMasterRepo extends JpaRepository<XLConfigMaster, XLConfigMasterPK> {

	@Query(nativeQuery = true, value = "select * from eway_xlconfig_master where COMPANY_ID=?1 and PRODUCT_ID=?2 and TYPEID=?3")
	public List<XLConfigMaster> findXLConfigMasterByPk(String companyId, String productId, String typeId);

	@Query(nativeQuery = true, value = "select * from eway_xlconfig_master where COMPANY_ID=?1 and PRODUCT_ID=?2 and TYPEID=?3 AND FIELDID=?4")
	public XLConfigMaster findByPK(String companyId, String productId, String typeId,String fieldId);

//	@Query(nativeQuery = true, value = "SELECT MAX(fieldid) FROM eway_xlconfig_master WHERE company_id=?1 AND product_id=?2 AND typeid=?3 GROUP BY typeid")
//	public Integer findMaxOfFieldId(String companyId, String productId, String typeId);

//	@Query(nativeQuery = true, value = "DELETE FROM eway_xlconfig_master WHERE company_id IN(1,2)")
//	public void delete();
	
	@Query(nativeQuery = true, value="SELECT ITEM_CODE,ITEM_VALUE FROM eway_list_item_value WHERE ITEM_TYPE='DATA_TYPES' AND STATUS='Y'")
	public List<Map<String,String>> getDataType();
	
	@Query(nativeQuery = true, value="SELECT * FROM one_time_table_details WHERE Item_id=?1 AND STATUS=?2 ORDER BY Item_Code ASC")
	List<OneTimeTableDetails> findColumn(Integer valueOf, String string);

}
