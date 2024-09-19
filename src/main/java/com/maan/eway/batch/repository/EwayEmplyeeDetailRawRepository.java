package com.maan.eway.batch.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;

@Repository
public interface EwayEmplyeeDetailRawRepository extends JpaRepository<EwayEmplyeeDetailRaw, Integer> {

	
	Page<EwayEmplyeeDetailRaw> findByRequestReferenceNoAndStatusNotAndSnoBetween(String tranId ,String error_status,Integer start, Integer end,PageRequest page);

	List<EwayEmplyeeDetailRaw> findByRequestReferenceNo(String requestReferenceNo);

	List<EwayEmplyeeDetailRaw> findByCompanyIdAndProductIdAndRequestReferenceNo(Integer companyId, Integer productId,
			String requestRefNo);

	@Query(value=" SELECT ROW_NUMBER() OVER(PARTITION BY request_reference_no ORDER BY request_reference_no DESC) AS EMPLOYEE_ID,r.* FROM EWAY_EMPLOYEE_DETAILS_RAW r WHERE company_Id=?1 AND product_id=?2 AND request_reference_no=?3 AND STATUS ='Y' AND (API_STATUS IS NULL or API_STATUS='N') ORDER BY 1 ASC",nativeQuery=true)
	List<Map<String,Object>> getEmployeRawDetails(Object companyId,Object productId,Object refNo );
	
	List<EwayEmplyeeDetailRaw> findByCompanyIdAndProductIdAndQuoteNoAndRiskIdAndRequestReferenceNo(Integer companyId,Integer productId,
			String quoteNo,Integer riskId,String requestReferenceNo);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw raw SET Occupation_id =( SELECT occupation_id FROM Eway_Occupation_master WHERE STATUS = 'Y' AND Product_id = raw.Product_id AND Company_id = raw.Company_id AND TRIM( UPPER(occupation_name))= TRIM( UPPER(raw.occupation_desc) ) AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND amend_id =( SELECT MAX(amend_id) FROM Eway_Occupation_master WHERE STATUS = 'Y' AND SYSDATE() BETWEEN effective_date_start AND effective_date_end AND Product_id = raw.Product_id AND TRIM( UPPER(occupation_name) )= TRIM( UPPER(raw.occupation_desc) ) AND Company_id = raw.Company_id ) ), error_desc = CONCAT( error_desc, CASE WHEN TRIM( UPPER(raw.occupation_desc) ) NOT IN ( SELECT TRIM( UPPER(occupation_name) ) FROM Eway_Occupation_master WHERE STATUS = 'Y' AND Product_id = raw.Product_id AND Company_id = raw.Company_id AND SYSDATE() BETWEEN effective_date_start AND effective_date_end ) THEN '~Occupation id is not found' END ) , STATUS= CASE WHEN error_desc IS NOT NULL OR error_desc!='' THEN 'E' ELSE 'Y' END WHERE raw.company_id=?1 AND raw.product_id =?2 AND raw.risk_id=?3 AND raw.request_reference_no =?4 AND raw.quote_no =?5 and raw.status='Y' and raw.api_status is null",nativeQuery=true)
	Integer updateOccupationId(Integer companyId,Integer productId,Integer riskId,String refNo,String quoteNo);

	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET STATUS='E',error_desc=CONCAT(COALESCE(error_desc,'~'),'Duplicate nationalityId found') WHERE(company_id,product_id,request_reference_no,quote_no,nationality_id,section_id) IN (SELECT company_id,product_id,request_reference_no,quote_no,nationality_id,section_id FROM(SELECT company_id,product_id,request_reference_no,quote_no,nationality_id,section_id FROM eway_employee_details_raw WHERE company_id=?1 AND product_id=?2 AND request_reference_no=?3 AND quote_no=?4 and section_id=?5 AND STATUS ='Y' AND (api_status IS NULL OR api_status='') GROUP BY company_id,product_id,request_reference_no,quote_no,nationality_id,section_id HAVING COUNT(*)>1)X)",nativeQuery=true)
	Integer updateDuplicateNationalityId(Integer companyId,Integer productId,String refNo,String quoteNo,String sectionId);

	List<EwayEmplyeeDetailRaw> findByCompanyIdAndProductIdAndRequestReferenceNoAndQuoteNoAndRiskIdAndStatusIgnoreCase(
			Integer companyId, Integer productId, String requestRefNo, String quoteNo, Integer riskId, String status);

	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET STATUS='E',error_desc=CONCAT(CONCAT(COALESCE(error_desc,'~'),'Suminsured limit exceed..!'),salary) WHERE(company_id,product_id,request_reference_no,quote_no) IN (SELECT company_id,product_id,request_reference_no,quote_no FROM (SELECT r.company_id,r.product_id,r.request_reference_no,r.quote_no FROM Common_Data_Details c INNER JOIN eway_employee_details_raw r ON c.company_id=r.company_id AND c.product_id=r.product_id AND c.request_reference_no=r.request_reference_no AND c.quote_no=r.quote_no WHERE r.company_id=?1 AND r.product_id=?2 AND r.request_reference_no=?3 AND r.quote_no=?4 AND (r.api_status IS NULL OR r.api_status='') AND r.status='Y' GROUP BY r.company_id,r.product_id,r.request_reference_no,r.quote_no,c.sum_insured HAVING SUM(r.salary)<>c.sum_insured)X) and status='Y' and api_status is null ",nativeQuery=true)
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
	@Query(value="UPDATE eway_employee_details_raw SET STATUS='E',ERROR_DESC=CONCAT(CONCAT(COALESCE(error_desc,'~'),'duplicate passport number found for this : '),passport_no) WHERE(request_reference_no,passport_no)IN (SELECT request_reference_no,passport_no FROM(SELECT a.request_reference_no,a.passport_no FROM eway_employee_details_raw a,eway_employee_details_raw b WHERE a.rownum_=b.rownum_ AND a.company_id=b.company_id AND a.product_id=b.product_id AND a.request_reference_no =b.request_reference_no AND a.quote_no=b.quote_no AND a.company_id=?1 AND a.product_id=?2 AND a.request_reference_no=?3 AND a.quote_no=?4 GROUP BY a.request_reference_no ,a.passport_no HAVING COUNT(*) >1)X) and status='Y' and api_status is null",nativeQuery=true)
	Integer updateDuplicatePassportNo(String companyId,String productId,String refno,String quoteNo);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET STATUS='E',ERROR_DESC=CONCAT(CONCAT(COALESCE(error_desc,'~'),'duplicate civilId number found for this : '),civil_id) WHERE(request_reference_no,civil_id)IN (SELECT request_reference_no,civil_id FROM(SELECT a.request_reference_no,a.civil_id FROM eway_employee_details_raw a,eway_employee_details_raw b WHERE a.rownum=b.rownum AND a.company_id=b.company_id AND a.product_id=b.product_id AND a.request_reference_no =b.request_reference_no AND a.quote_no=b.quote_no AND a.company_id=?1 AND a.product_id=?2 AND a.request_reference_no=?3 AND a.quote_no=?4 GROUP BY a.request_reference_no ,a.civil_id HAVING COUNT(*) >1)X) and status='Y' and api_status is null",nativeQuery=true)
	Integer updateDuplicateCivilId(String companyId,String productId,String refno,String quoteNo);

	@Query(value ="select count(*) from Eway_Employee_Details_Raw emp where emp.company_Id=?1 and emp.product_Id=?2 and emp.request_Reference_No=?3 and section_id=?4 and emp.status='Y' and emp.api_Status is null",nativeQuery=true)
	Long getCountRecords(Integer companyId, Integer productId, String requestReferenceNo,String sectionId);

	@Modifying
	@Transactional
	@Query("update EwayEmplyeeDetailRaw emp set emp.status='E',emp.errorDesc=?1 where emp.companyId=?2 and emp.productId=?3 and emp.requestReferenceNo=?4 and sectionId=?5 and emp.status='Y'")
	Integer updateEmployeeExceededCount(String errorMsg,Integer companyId, Integer productId, String requestReferenceNo,String sectionId);

	@Query(value ="SELECT COMPANY_ID,PRODUCT_ID,QUOTE_NO,REQUEST_REFERENCE_NO,PASSPORT_NO,FIRST_NAME,LAST_NAME,DATE_OF_BIRTH,PASS_RELATION_ID,CREATED_BY,NATIONALITY_ID,GENDER FROM eway_employee_details_raw WHERE COMPANY_ID=?1 AND PRODUCT_ID=?2 AND REQUEST_REFERENCE_NO=?3 AND STATUS='Y' AND (api_status IS NULL or api_status='N') ",nativeQuery=true)
	List<Map<String,Object>> getPassengersList(String companyId,String productId,String refno);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw REL SET NATIONALITY_ID =( SELECT COUNTRY_ID FROM EWAY_COUNTRY_MASTER WHERE TRIM( UPPER(COUNTRY_NAME)) = TRIM( UPPER(REL.PASSENGER_NATIONALITY) ) AND STATUS = 'Y' AND ( REL.COMPANY_ID = COMPANY_ID OR COMPANY_ID = '99999' ) AND ( SELECT CURRENT_DATE FROM DUAL ) BETWEEN EFFECTIVE_DATE_START AND EFFECTIVE_DATE_END AND AMEND_ID = ( SELECT MAX(AMEND_ID) FROM EWAY_COUNTRY_MASTER WHERE STATUS = 'Y' AND TRIM( UPPER(COUNTRY_NAME) ) = TRIM( UPPER(REL.PASSENGER_NATIONALITY) ) ) ) WHERE COMPANY_ID=?1 AND PRODUCT_ID=?2 AND REQUEST_REFERENCE_NO =?3",nativeQuery=true)
	Integer updateNationlityId(Integer companyId,Integer productId,String refNo);

	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET GENDER_ID =(CASE WHEN UPPER(gender)=UPPER('Male') THEN 'M' WHEN UPPER(gender)=UPPER('female') THEN 'F' ELSE 'T' END) WHERE REQUEST_REFERENCE_NO=?1",nativeQuery=true)
	Integer updateGender(String refNo);
	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw EMP SET MONTH_OF_JOINING =( SELECT ITEM_CODE FROM EWAY_LIST_ITEM_VALUE WHERE REPLACE(UPPER(EMP.MONTH_OF_JOINING_DESC),' ','')=REPLACE(UPPER(ITEM_VALUE),' ','') AND STATUS = 'Y' AND AMEND_ID =( SELECT MAX(AMEND_ID) FROM EWAY_LIST_ITEM_VALUE WHERE REPLACE(UPPER(ITEM_VALUE),' ','')= REPLACE(UPPER(EMP.MONTH_OF_JOINING_DESC),' ','') AND STATUS = 'Y') ) WHERE COMPANY_ID =?1 AND PRODUCT_ID =?2 AND QUOTE_NO =?3 AND REQUEST_REFERENCE_NO =?4 AND SECTION_ID=?5 ",nativeQuery=true)
	Integer updateDateOfMonth(Integer companyId, Integer productId, String quoteNo, String requestReferenceNo,String sectionId);

	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw EMP SET LOCATION_ID =( SELECT RISK_ID FROM building_details WHERE TRIM(UPPER(EMP.LOCATION_DESC)) = TRIM(UPPER(LOCATION_NAME)) AND STATUS='Y' AND EMP.QUOTE_NO=QUOTE_NO AND EMP.REQUEST_REFERENCE_NO=REQUEST_REFERENCE_NO) WHERE REQUEST_REFERENCE_NO=?1 AND PRODUCT_ID=?2 AND SECTION_ID=?3",nativeQuery=true)
	Integer updateLocationId(String requestReferenceNo,String productId,String sectionId);
	
	
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET error_desc='duplicate serial number has found' ,STATUS ='E' WHERE request_reference_no=?1 AND serial_number IN(SELECT * FROM(SELECT serial_number FROM eway_employee_details_raw WHERE request_reference_no=?1 GROUP BY serial_number HAVING COUNT(*)>1)temp)",nativeQuery=true)
	Integer updateDuplicateSerialNo(String requestReferenceNo);
	
    default String get() {
    	
    	return ""
;    }
	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw emp SET STATUS='E',error_desc= CONCAT(COALESCE(error_desc,''),'~Suminsured is not matched') WHERE(SELECT * FROM(SELECT SUM(sum_insured) FROM eway_employee_details_raw WHERE request_reference_no=emp.request_reference_no AND section_id=emp.section_id)X) !=(SELECT CASE WHEN section_id='3' THEN allrisk_suminsured_lc ELSE content_suminsured_lc END FROM eservice_building_details WHERE section_id=emp.section_id AND request_reference_no=emp.request_reference_no) AND request_reference_no=?1",nativeQuery=true)
	Integer updateContentAndAllriskSumInsured(String requestReferenceNo);

	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw emp SET content_type_id=(SELECT item_code FROM eway_list_item_value WHERE item_type=(CASE WHEN emp.section_id='3' THEN 'All Risk' ELSE 'Content' END) AND company_id=emp.company_id AND STATUS='Y' AND TRIM(UPPER(item_value))=TRIM(UPPER(emp.content_type_desc))) WHERE STATUS='Y' AND request_reference_no=?1",nativeQuery=true)
	Integer updateContentTypeId(String requestReferenceNo);

	@Modifying
	@Transactional
	@Query(value="UPDATE eway_employee_details_raw SET STATUS='E', error_desc=CONCAT(COALESCE(error_desc,' '), CASE WHEN content_type_id IS NULL OR content_type_id='' THEN '~Content/Allrisk Id is not updated' ELSE '~Location Id is not updated' END) WHERE(content_type_id IS NULL OR content_type_id='' OR LOCATION_ID IS NULL OR LOCATION_ID='') AND STATUS='Y' AND request_reference_no=?1",nativeQuery=true)
	Integer updateErrorStatusAndErrorDesc(String requestReferenceNo);

	List<EwayEmplyeeDetailRaw> findByCompanyIdAndProductIdAndQuoteNoAndRiskIdAndSectionIdAndRequestReferenceNo(
			Integer companyId, Integer productId, String quoteNo, Integer valueOf, String sectionId,
			String requestReferenceNo);
	
	
	@Modifying
	@Transactional
	@Query(nativeQuery=true,value="UPDATE eway_employee_details_raw emp SET emp.pass_relation_id=(SELECT DISTINCT item_code FROM EWAY_LIST_ITEM_VALUE WHERE item_type='RATING_RELATION_TYPE' AND company_id=?1 AND TRIM(UPPER(item_value))=TRIM(UPPER(emp.relation_desc)) AND STATUS='Y') WHERE request_reference_no=?2")
	Integer updateHealthRelation(String companyId,String refNol);

	Integer countByRequestReferenceNoAndStatusNot(String tranId, String string);

	@Query(nativeQuery=true,value="SELECT OCCUPATION_ID ITEM_CODE,OCCUPATION_NAME ITEM_VALUE FROM eway_occupation_master WHERE company_id=?1 AND product_id=?2 AND STATUS='Y' AND CURRENT_DATE BETWEEN effective_date_start AND effective_date_end")
	List<Map<String, String>> getOccupation(String companyId,String productId);

	
	@Query(nativeQuery=true,value="SELECT RISK_ID ITEM_CODE,LOCATION_NAME ITEM_VALUE FROM eservice_section_details WHERE REQUEST_REFERENCE_NO=?1")
	List<Map<String, String>> getBuildingDet(String refNo);

	@Query(nativeQuery=true,value="SELECT COUNTRY_ID ITEM_CODE,COUNTRY_NAME ITEM_VALUE FROM EWAY_COUNTRY_MASTER WHERE COMPANY_ID =?1 AND CURRENT_DATE BETWEEN EFFECTIVE_DATE_START AND EFFECTIVE_DATE_END AND STATUS ='Y'")
	List<Map<String, String>> getCountry(String companyId);

	@Query(nativeQuery=true,value="SELECT ITEM_CODE,ITEM_VALUE FROM eway_list_item_value WHERE item_type=?2 AND COMPANY_ID=?1 AND CURRENT_DATE BETWEEN EFFECTIVE_DATE_START AND EFFECTIVE_DATE_END AND STATUS='Y'")
	List<Map<String, String>> getContentType(String companyId,String itemType);
	
	@Query(nativeQuery=true,value="SELECT ITEM_CODE,ITEM_VALUE FROM EWAY_LIST_ITEM_VALUE WHERE company_id=?1 and item_type='RATING_RELATION_TYPE' AND CURRENT_DATE BETWEEN EFFECTIVE_DATE_START AND EFFECTIVE_DATE_END AND STATUS='Y'")
	List<Map<String, String>> getRelationType(String companyId);

	@Query(nativeQuery=true,value=" SELECT ITEM_CODE,ITEM_VALUE FROM eway_list_item_value WHERE ITEM_TYPE = 'MONTHS' AND STATUS = 'Y'")
	List<Map<String, String>> getMonthOfYear();


	

	
}
