package com.maan.eway.vehicleupload;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.maan.eway.batch.entity.EserviceMotorDetailsRaw;
import com.maan.eway.batch.entity.EwayEmplyeeDetailRaw;
import com.maan.eway.batch.repository.EwayEmplyeeDetailRawRepository;
import com.maan.eway.batch.req.EmployeeUpdateReq;
import com.maan.eway.batch.req.MotorUpdateReq;
import com.maan.eway.batch.req.UpdateRecordReq;
import com.maan.eway.error.Error;

@Component
@Transactional
public class VehicleInputValidation {

	@Autowired
	private EwayEmplyeeDetailRawRepository empRepo;
	
	@PersistenceContext
	EntityManager em;
	
	
	public List<Error> validateEmployee(UpdateRecordReq empReq){
		List<Error> list = new ArrayList<Error>();
		EmployeeUpdateReq req =empReq.getEmployeeUpdateReq();
		try {
			if(StringUtils.isBlank(req.getDateOfBirth())) {
				list.add(new Error("1","DateOfBirth","Please enter dateOfBirth"));
			}if(StringUtils.isBlank(req.getDateOfJoiningYear())) {
				list.add(new Error("1","DateOfJoiningYear","Please enter DateOfJoiningYear"));
			}if(StringUtils.isBlank(req.getDateOfJoiningMonth())) {
				list.add(new Error("1","DateOfJoiningMonth","Please enter DateOfJoiningMonth"));
			}
			if(StringUtils.isBlank(req.getEmployeeName())) {
				list.add(new Error("1","EmployeeName","Please enter EmployeeName"));
			}if(StringUtils.isBlank(req.getNationalityId())) {
				list.add(new Error("1","NationalityId","Please enter NationalityId"));
			}if(StringUtils.isBlank(req.getOccupationDesc())) {
				list.add(new Error("1","OccupationDesc","Please enter OccupationDesc"));
			}if(StringUtils.isBlank(req.getOccupationId())) {
				list.add(new Error("1","OccupationId","Please enter OccupationId"));
			}if(StringUtils.isBlank(req.getSalary())) {
				list.add(new Error("1","Salary","Please enter Salary"));
			}
			EwayEmplyeeDetailRaw emp =empRepo.findByCompanyIdAndProductIdAndQuoteNoAndRiskIdAndRequestReferenceNoAndNationalityIdAndStatusIgnoreCase(Integer.valueOf(empReq.getCompanyId()),
					Integer.valueOf(empReq.getProductId()),empReq.getQuoteNo(),Integer.valueOf(empReq.getRiskId()),empReq.getRequestRefNo(),req.getNationalityId(),"Y");
			if(emp!=null) {
				list.add(new Error("1","NationalityId","NationalityId already exists"));
			}
			
			List<EwayEmplyeeDetailRaw> empList =empRepo.findByCompanyIdAndProductIdAndQuoteNoAndRiskIdAndRequestReferenceNo(Integer.valueOf(empReq.getCompanyId()),
					Integer.valueOf(empReq.getProductId()),empReq.getQuoteNo(),Integer.valueOf(empReq.getRiskId()),empReq.getRequestRefNo());
			
			Double upoadedSumInsured =empList.stream()
					.filter(e ->e.getRowNum()!=Integer.valueOf(req.getRowNum()))
					.collect(Collectors.summingDouble(e ->Double.valueOf(e.getSalary())));
			
			Double totalUploadSumIn =upoadedSumInsured + Double.valueOf(req.getSalary());
			
			Double totalSumInsured =empRepo.getToalPremium(empReq.getQuoteNo()).doubleValue();
			
			
			if(Double.compare(totalUploadSumIn, totalSumInsured)>0) {
				list.add(new Error("1","Suminured","Suminured limit has exceeded ..Please change your employee salary limit lesser than actual suminured "+totalSumInsured+""));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list ;
	}
	
	
	public List<Error> validateVehicle(UpdateRecordReq motorReq){
		List<Error> list = new ArrayList<Error>();
		try {
			MotorUpdateReq req =motorReq.getMotorRequest();
			if(StringUtils.isBlank(req.getSearchByData())) {
				list.add(new Error("1","SearchByData","Please enter SearchByData"));
			}if(StringUtils.isBlank(req.getBodyType())) {
				list.add(new Error("1","BodyType","Please enter BodyType"));
			}if(StringUtils.isBlank(req.getInsuranceType())) {
				list.add(new Error("1","InsuranceType","Please enter InsuranceType"));
			}if(StringUtils.isBlank(req.getAccessoriesSuminured())) {
				list.add(new Error("1","AccessoriesSuminured","Please enter AccessoriesSuminured"));
			}if(StringUtils.isBlank(req.getWinShieldSuminsured())) {
				list.add(new Error("1","WinShieldSuminsured","Please enter WinShieldSuminsured"));
			}if(StringUtils.isBlank(req.getVehcileSuminsured())) {
				list.add(new Error("1","VehcileSuminsured","Please enter VehcileSuminsured"));
			}if(StringUtils.isBlank(req.getMotorUsage())) {
				list.add(new Error("1","MotorUsage","Please enter MotorUsage"));
			}if(StringUtils.isBlank(req.getInsuranceClass())) {
				list.add(new Error("1","InsuranceClass","Please enter InsuranceClass"));
			}if(StringUtils.isBlank(req.getGpsYn())) {
				list.add(new Error("1","GpsYn","Please enter GpsYn"));
			}if(StringUtils.isBlank(req.getClaimYn())) {
				list.add(new Error("1","ClaimYn","Please enter ClaimYn"));
			}if(StringUtils.isBlank(req.getExtendedTPPDSuminsured())) {
				list.add(new Error("1","ExtendedTPPDSuminsured","Please enter ExtendedTPPDSuminsured"));
			}if(StringUtils.isBlank(req.getCollateralYn())) {
				list.add(new Error("1","CollateralYn","Please enter CollateralYn"));
			}else if("Y".equalsIgnoreCase(req.getCollateralYn())) {			
				if(StringUtils.isBlank(req.getBorrowType())) {
					list.add(new Error("1","BorrowType","Please enter BorrowType"));
				}if(StringUtils.isBlank(req.getFirstLossPayee())) {
					list.add(new Error("1","FirstLossPayee","Please enter FirstLossPayee"));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list ;
	}

	public void statusUpdate(Object companyId,Object productId,Object typeId,Object refNo) {
		try {
			 CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		        CriteriaUpdate<EserviceMotorDetailsRaw> updateQuery = criteriaBuilder.createCriteriaUpdate(EserviceMotorDetailsRaw.class);
		        Root<EserviceMotorDetailsRaw> root = updateQuery.from(EserviceMotorDetailsRaw.class);

		        // Define the case condition using a Predicate
		        Predicate caseCondition = criteriaBuilder.or(
		        	criteriaBuilder.notEqual(root.get("errorDesc"),""),
		            criteriaBuilder.isNotNull(root.get("errorDesc"))
		           
		        );

		        // Set the propertyToUpdate based on the case condition
		        updateQuery.set(root.<String>get("status"),
		            criteriaBuilder.selectCase()
		                .when(caseCondition, "E")
		                .otherwise("Y").as(String.class)
		        );

		        // Apply the conditions to restrict the update
		        Predicate whereCondition = criteriaBuilder.and(
		            criteriaBuilder.equal(root.get("companyId"), companyId),
		            criteriaBuilder.equal(root.get("productId"), productId),
		            criteriaBuilder.equal(root.get("typeid"), typeId),
		            criteriaBuilder.equal(root.get("requestReferenceNo"), refNo)
		        );

		        updateQuery.where(whereCondition);

		        // Perform the update
		        em.createQuery(updateQuery).executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
