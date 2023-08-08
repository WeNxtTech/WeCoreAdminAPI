package com.maan.eway.batch.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;

@Repository
public interface EwayEmplyeeDetailRawRepository extends JpaRepository<EwayEmplyeeDetailRaw, Integer> {

	List<EwayEmplyeeDetailRaw> findByRequestReferenceNo(String requestReferenceNo);

	List<EwayEmplyeeDetailRaw> findByCompanyIdAndProductIdAndRequestReferenceNo(Integer companyId, Integer productId,
			String requestRefNo);

	@Query(value=" SELECT ROW_NUMBER() OVER(PARTITION BY request_reference_no ORDER BY request_reference_no DESC) AS EMPLOYEE_ID,r.* FROM EWAY_EMPLOYEE_DETAILS_RAW r WHERE company_Id=?1 AND product_id=?2 AND request_reference_no=?3 AND STATUS ='Y' AND (API_STATUS IS NULL or API_STATUS='') ORDER BY 1 ASC",nativeQuery=true)
	List<Map<String,Object>> getEmployeRawDetails(Object companyId,Object productId,Object refNo );
	
	List<EwayEmplyeeDetailRaw> findByCompanyIdAndProductIdAndQuoteNoAndRiskIdAndRequestReferenceNo(Integer companyId,Integer productId,
			String quoteNo,Integer riskId,String requestReferenceNo);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw raw SET Occupation_id =( SELECT occupation_id FROM Eway_Occupation_master WHERE STATUS = 'Y' AND Product_id = raw.Product_id AND Company_id = raw.Company_id AND TRIM( UPPER(occupation_name))= TRIM( UPPER(raw.occupation_desc) ) AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND amend_id =( SELECT MAX(amend_id) FROM Eway_Occupation_master WHERE STATUS = 'Y' AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND Product_id = raw.Product_id AND TRIM( UPPER(occupation_name) )= TRIM( UPPER(raw.occupation_desc) ) AND Company_id = raw.Company_id ) ), error_desc = CONCAT( error_desc, CASE WHEN TRIM( UPPER(raw.occupation_desc) ) NOT IN ( SELECT TRIM( UPPER(occupation_name) ) FROM Eway_Occupation_master WHERE STATUS = 'Y' AND Product_id = raw.Product_id AND Company_id = raw.Company_id AND SYSDATE() BETWEEN effective_date_start AND effective_date_end ) THEN '~Occupation id is not found' END ) , STATUS= CASE WHEN error_desc IS NOT NULL OR error_desc!='' THEN 'E' ELSE 'Y' END WHERE raw.company_id=?1 AND raw.product_id =?2 AND raw.risk_id=?3 AND raw.request_reference_no =?4 AND raw.quote_no =?5 and raw.status='Y' and raw.api_status is null",nativeQuery=true)
	Integer updateOccupationId(Integer companyId,Integer productId,Integer riskId,String refNo,String quoteNo);

	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET STATUS='E',error_desc=CONCAT(COALESCE(error_desc,'~'),'Duplicate nationalityId found',nationality_id) WHERE(company_id,product_id,request_reference_no,quote_no,nationality_id) IN (SELECT company_id,product_id,request_reference_no,quote_no,nationality_id FROM(SELECT company_id,product_id,request_reference_no,quote_no,nationality_id FROM eway_employee_details_raw WHERE company_id=?1 AND product_id=?2 AND request_reference_no=?3 AND quote_no=?4 AND STATUS ='Y' AND (api_status IS NULL OR api_status='') GROUP BY company_id,product_id,request_reference_no,quote_no,nationality_id HAVING COUNT(*)>1)X)",nativeQuery=true)
	Integer updateDuplicateNationalityId(Integer companyId,Integer productId,String refNo,String quoteNo);

	List<EwayEmplyeeDetailRaw> findByCompanyIdAndProductIdAndRequestReferenceNoAndQuoteNoAndRiskIdAndStatusIgnoreCase(
			Integer companyId, Integer productId, String requestRefNo, String quoteNo, Integer riskId, String status);

	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET STATUS='E',error_desc=CONCAT(COALESCE(error_desc,'~'),'Suminsured limit exceed..!',salary) WHERE(company_id,product_id,request_reference_no,quote_no) IN (SELECT company_id,product_id,request_reference_no,quote_no FROM (SELECT r.company_id,r.product_id,r.request_reference_no,r.quote_no FROM Common_Data_Details c INNER JOIN eway_employee_details_raw r ON c.company_id=r.company_id AND c.product_id=r.product_id AND c.request_reference_no=r.request_reference_no AND c.quote_no=r.quote_no WHERE r.company_id=?1 AND r.product_id=?2 AND r.request_reference_no=?3 AND r.quote_no=?4 AND (r.api_status IS NULL OR r.api_status='') AND r.status='Y' GROUP BY r.company_id,r.product_id,r.request_reference_no,r.quote_no,c.sum_insured HAVING SUM(r.salary)<>c.sum_insured)X) and status='Y' and api_status is null ",nativeQuery=true)
	Integer checkSuminsuredValidation(String compnayId,String productId,String quoteNO, String refNo);

	EwayEmplyeeDetailRaw findByCompanyIdAndProductIdAndQuoteNoAndRiskIdAndRequestReferenceNoAndNationalityIdAndStatusIgnoreCase(
			Integer companyId, Integer productId, String quoteNo, Integer riskId, String requestRefNo,String refNo, String status);

	@Query("select p.sumInsured from CommonDataDetails p where p.quoteNo=:quoteNo")
	BigDecimal getToalPremium(@Param("quoteNo") String quoteNo);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET STATUS=( CASE WHEN DATE_OF_JOINING >= (SELECT YEAR(CURDATE())) THEN 'E' WHEN STR_TO_DATE(DATE_OF_BIRTH,'%d/%m/%Y') >=DATE_SUB(CURDATE(),INTERVAL 18 YEAR) THEN 'E' ELSE 'Y' END), ERROR_DESC=( CASE WHEN DATE_OF_JOINING >= (SELECT YEAR(CURDATE())) THEN '~Date Of Joining should be equal currentYear or pastYear' WHEN STR_TO_DATE(DATE_OF_BIRTH,'%d/%m/%Y') >=DATE_SUB(CURDATE(),INTERVAL 18 YEAR) THEN '~Date of birth should be equal 18 years or greaterthan 18 years' END ) WHERE QUOTE_NO=?1 AND REQUEST_REFERENCE_NO=?2 AND COMPANY_ID=?3 AND RISK_ID=?4 AND PRODUCT_ID=?5 and STATUS ='Y'",nativeQuery=true)
	Integer checkDateOfjoiningAndDateOfBirth(String quoteNo,String refNo,String companyId,String riskId,String productId);

	@Modifying
	@Transactional
	@Query(value="UPDATE EWAY_EMPLOYEE_DETAILS_RAW SET API_STATUS=?1,ERROR_DESC=?2,API_RESPONSE=?3,API_REQUEST=?4 WHERE company_Id=?5 AND product_id =?6 AND request_reference_no=?7 ",nativeQuery=true)
	Integer updateEmployeeStatus(String apiStatus,String error,String response,String request,String companyId,String productId,String refNo);

	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET STATUS='E',ERROR_DESC=CONCAT(COALESCE(error_desc,'~'),'duplicate passport number found for this : ',passport_no) WHERE(request_reference_no,passport_no)IN (SELECT request_reference_no,passport_no FROM(SELECT a.request_reference_no,a.passport_no FROM eway_employee_details_raw a,eway_employee_details_raw b WHERE a.rownum=b.rownum AND a.company_id=b.company_id AND a.product_id=b.product_id AND a.request_reference_no =b.request_reference_no AND a.quote_no=b.quote_no AND a.company_id=?1 AND a.product_id=?2 AND a.request_reference_no=?3 AND a.quote_no=?4 GROUP BY a.request_reference_no ,a.passport_no HAVING COUNT(*) >1)X) and status='Y' and api_status is null",nativeQuery=true)
	Integer updateDuplicatePassportNo(String companyId,String productId,String refno,String quoteNo);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET STATUS='E',ERROR_DESC=CONCAT(COALESCE(error_desc,'~'),'duplicate civilId number found for this : ',civil_id) WHERE(request_reference_no,civil_id)IN (SELECT request_reference_no,civil_id FROM(SELECT a.request_reference_no,a.civil_id FROM eway_employee_details_raw a,eway_employee_details_raw b WHERE a.rownum=b.rownum AND a.company_id=b.company_id AND a.product_id=b.product_id AND a.request_reference_no =b.request_reference_no AND a.quote_no=b.quote_no AND a.company_id=?1 AND a.product_id=?2 AND a.request_reference_no=?3 AND a.quote_no=?4 GROUP BY a.request_reference_no ,a.civil_id HAVING COUNT(*) >1)X) and status='Y' and api_status is null",nativeQuery=true)
	Integer updateDuplicateCivilId(String companyId,String productId,String refno,String quoteNo);

	@Query("select count(emp) from EwayEmplyeeDetailRaw emp where emp.companyId=?1 and emp.productId=?2 and emp.requestReferenceNo=?3 and emp.status='Y'")
	Long getCountRecords(Integer companyId, Integer productId, String requestReferenceNo);

	@Modifying
	@Transactional
	@Query("update EwayEmplyeeDetailRaw emp set emp.status='E',emp.errorDesc=?1 where emp.companyId=?2 and emp.productId=?3 and emp.requestReferenceNo=?4 and emp.status='Y'")
	Integer updateEmployeeExceededCount(String errorMsg,Integer companyId, Integer productId, String requestReferenceNo);

	
}
