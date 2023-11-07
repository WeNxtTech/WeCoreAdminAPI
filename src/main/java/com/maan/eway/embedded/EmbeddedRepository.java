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

	
	@Query(value ="SELECT SUM(overAll_premium) AS total_premium,COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium,SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid FROM group_medical_details WHERE login_id=:loginId AND company_id=:companyId AND product_id=:productId and status='Y' GROUP BY login_id ,company_id,product_id ",nativeQuery=true)
	public Map<String,Object> getProductDashBoard(@Param("loginId") String loginId,@Param("companyId") String companyId,@Param("productId") String productId);
	
	
	@Query(value ="SELECT SUM(overAll_premium) AS total_premium,COUNT(*) AS total_policy, SUM(tax_premium) total_tax_premium,SUM(commission_amount) total_commission_amount, SUM(Amount_paid) AS total_amount_paid,plan_opted FROM group_medical_details WHERE login_id=:loginId AND company_id=:companyId AND product_id=:productId and status='Y' GROUP BY login_id ,company_id,product_id,plan_opted ",nativeQuery=true)
	public List<Map<String,Object>> getProductPlanDashBoard(@Param("loginId") String loginId,@Param("companyId") String companyId,@Param("productId") String productId);
	
	
	@Query(value ="SELECT SUM(overAll_premium) AS active_premium FROM group_medical_details WHERE login_id=:loginId AND company_id=:companyId AND product_id=:productId AND :systemDate BETWEEN inception_date AND expiry_date and status='Y' GROUP BY login_id,company_id,product_id ",nativeQuery=true)
	public Map<String,Object> getActivePremium(@Param("loginId") String loginId,@Param("companyId") String companyId,@Param("productId") String productId,@Param("systemDate") Date date);
	
	@Query(value ="SELECT SUM(overall_premium) as active_premium FROM group_medical_details WHERE login_id=:loginId AND company_id =:companyId AND product_id=:productId AND plan_opted =:planOpted AND :todaydate BETWEEN inception_date AND expiry_date GROUP BY company_id,product_id,plan_opted",nativeQuery=true)
	public String getActivePremiumBasedPlan(@Param("loginId") String loginId,@Param("companyId") String companyId,@Param("productId") String productId,@Param("planOpted") String planOptd,@Param("todaydate") Date date);

	
	@Query(value="SELECT item_value FROM eway_list_item_value WHERE item_type='PLAN_OPTED' AND item_code=:planId and status ='Y'",nativeQuery=true)
	public String getPlanName(@Param("planId")String planOptedId);
}
