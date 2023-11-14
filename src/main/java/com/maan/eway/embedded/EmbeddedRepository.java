package com.maan.eway.embedded;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbeddedRepository extends JpaRepository<GroupMedicalDetails, GroupMedicalDetailsId>{

	
	@Query(value="SELECT SUM(overAll_premium) AS total_premium,COUNT(*) AS total_policy,"
			+ "SUM(tax_premium) total_tax_premium,SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS "
			+ "total_amount_paid FROM group_medical_details WHERE company_id=:companyId and product_id=:productId AND STATUS='Y' "
			+ "AND DATE(expiry_date) BETWEEN :startDate AND :endDate "
			+ "GROUP BY company_id,login_id",nativeQuery=true)
	public Map<String,Object> getProductDashBoard(@Param("companyId") String companyId,@Param("productId")String productId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Query(value="SELECT SUM(overall_premium) AS active_premium FROM group_medical_details WHERE "
			+ "company_id =:companyId and product_id=:productId and "
			+ " DATE(expiry_date) BETWEEN :startDate AND :endDate "
			+ "GROUP BY company_id, login_id", nativeQuery = true)
	public Map<String,Object> getActivePremium(@Param("companyId") String companyId, @Param("productId")String productId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);
	
	@Query(value="SELECT SUM(overAll_premium) AS expiry_premium,COUNT(*) AS expiry_count, "
			+ "SUM(tax_premium) total_tax_premium,SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS "
			+ "total_amount_paid FROM group_medical_details WHERE company_id=:companyId and product_id =:productId AND STATUS='Y' "
			+ "AND DATE(expiry_date)BETWEEN :startDate AND :endDate "
			+ "GROUP BY company_id,login_id ",nativeQuery=true)
	public Map<String,Object> getExpiryPremium(@Param("companyId") String companyId,@Param("productId")String productId,@Param("startDate") Date startDate,@Param("endDate") Date endDate);


	@Query(value=" SELECT SUM(overAll_premium) AS total_premium,COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium,SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid,(SELECT item_value FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND item_code=plan_opted AND STATUS='Y') AS plan_type ,company_id,login_id FROM group_medical_details WHERE company_id=:companyId AND STATUS='Y' GROUP BY company_id,login_id,plan_opted",nativeQuery=true)
	public Map<String,Object> getPlanBasedOverAllPre(@Param("companyId") String companyId);

	/*@Query(value=" SELECT SUM(overAll_premium) AS total_premium,COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium,SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid,(SELECT item_value FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND item_code=plan_opted AND STATUS='Y') AS plan_type ,company_id,login_id FROM group_medical_details WHERE company_id=:companyId and expiry_date<: AND STATUS='Y' GROUP BY company_id,login_id,plan_opted",nativeQuery=true)
	public Map<String,Object> getPlanBasedExpiryPre(@Param("companyId") String companyId,@Param("systemDate") Date date);*/

	@Query(value=" SELECT SUM(overAll_premium) AS total_premium,COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium,SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid,(SELECT item_value FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND item_code=plan_opted AND STATUS='Y') AS plan_type ,company_id,login_id FROM group_medical_details WHERE company_id=:companyId and :systemDate BETWEEN inception_date AND expiry_date AND STATUS='Y' GROUP BY company_id,login_id,plan_opted",nativeQuery=true)
	public Map<String,Object> getPlanBasedActivePre(@Param("companyId") String companyId,@Param("systemDate") Date date);

	
	@Query(value ="SELECT PLAN_OPTED, SUM(overAll_premium) AS total_premium,COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium,SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid,plan_opted FROM group_medical_details WHERE login_id=:loginId AND company_id=:companyId AND product_id=:productId and status='Y' AND DATE(expiry_date) BETWEEN :startDate AND :endDate GROUP BY login_id ,company_id,product_id,plan_opted ",nativeQuery=true)
	public List<Map<String,Object>> getProductPlanDashBoard(@Param("loginId") String loginId,@Param("companyId") String companyId,@Param("productId") String productId, @Param("startDate") Date startDate,@Param("endDate") Date endDate);
	
	
	@Query(value ="SELECT SUM(overall_premium) as active_premium FROM group_medical_details WHERE login_id=:loginId AND company_id =:companyId AND product_id=:productId AND plan_opted =:planOpted AND DATE(expiry_date) BETWEEN :startDate AND :endDate GROUP BY company_id,product_id,plan_opted",nativeQuery=true)
	public String getActivePremiumBasedPlan(@Param("loginId") String loginId,@Param("companyId") String companyId,@Param("productId") String productId,@Param("planOpted") String planOpted,@Param("startDate") Date startDate, @Param("endDate") Date endDate);


	
	@Query(value="SELECT SUM(overAll_premium) AS total_premium, COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium, SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid , 'Overall' as typ FROM group_medical_details WHERE company_id=?1 AND product_id=?2 AND STATUS='Y' AND DATE(inception_date) BETWEEN ?3 AND ?4 GROUP BY company_id UNION ALL SELECT SUM(overAll_premium) AS total_premium, COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium, SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid , 'Active' as typ FROM group_medical_details WHERE company_id=?1 AND product_id=?2 AND STATUS='Y' AND DATE(inception_date) BETWEEN ?3 AND ?4 AND DATE(expiry_date)>SYSDATE() GROUP BY company_id UNION ALL SELECT SUM(overAll_premium) AS total_premium, COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium, SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid , 'Expiry' as typ FROM group_medical_details WHERE company_id=?1 AND product_id=?2 AND STATUS='Y' AND DATE(inception_date) BETWEEN ?3 AND ?4 AND DATE(expiry_date)<SYSDATE() GROUP BY company_id",nativeQuery=true)
	public List<Map<String,Object>> getCompanyBasedDashBoard(String companyId,String productId,Date staDate,Date endDate);
	
	
	@Query(value ="SELECT company_id, login_id, plan_opted,(SELECT item_value FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND STATUS='Y' AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND item_code=gmd.plan_opted AND (company_id=gmd.company_id OR company_id='99999') AND amend_id=(SELECT MAX(amend_id) FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND STATUS='Y' AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND item_code=gmd.plan_opted AND (company_id=gmd.company_id OR company_id='99999'))) AS plan_desc, SUM(overAll_premium) AS total_premium, COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium, SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid , 'Overall' AS typ FROM group_medical_details gmd WHERE company_id=?1 AND product_id=?2 AND STATUS='Y' GROUP BY company_id,login_id,plan_opted UNION ALL SELECT company_id, login_id, plan_opted, (SELECT item_value FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND STATUS='Y' AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND item_code=gmd.plan_opted AND (company_id=gmd.company_id OR company_id='99999') AND amend_id=(SELECT MAX(amend_id) FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND STATUS='Y' AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND item_code=gmd.plan_opted AND (company_id=gmd.company_id OR company_id='99999'))) AS plan_desc, SUM(overAll_premium) AS total_premium, COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium, SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid , 'Active' AS typ FROM group_medical_details gmd WHERE company_id=?1 AND product_id=?2 AND STATUS='Y' AND DATE(inception_date) BETWEEN ?3 AND ?4 AND DATE(expiry_date)>SYSDATE() GROUP BY company_id,login_id,plan_opted UNION ALL SELECT company_id, login_id, plan_opted, (SELECT item_value FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND STATUS='Y' AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND item_code=gmd.plan_opted AND (company_id=gmd.company_id OR company_id='99999') AND amend_id=(SELECT MAX(amend_id) FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND STATUS='Y' AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND item_code=gmd.plan_opted AND (company_id=gmd.company_id OR company_id='99999'))) AS plan_desc, SUM(overAll_premium) AS total_premium, COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium, SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid , 'Expiry' AS typ FROM group_medical_details gmd WHERE company_id=?1 AND product_id=?2 AND STATUS='Y' AND DATE(inception_date) BETWEEN ?3 AND ?4 AND DATE(expiry_date)<SYSDATE() GROUP BY company_id,login_id,plan_opted",nativeQuery=true)
	public List<Map<String,Object>> getPlanBasedDashBoard(String companyId,String productId,Date staDate,Date endDate);
	

}
