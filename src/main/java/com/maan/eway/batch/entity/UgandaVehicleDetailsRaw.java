package com.maan.eway.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="UGANDA_VEHICLE_DETAILS_RAW")
@Builder	
@IdClass(UgandaVehicleDetailsRawId.class)
public class UgandaVehicleDetailsRaw {
	
	@Id
	@Column(name="ROW_NUM")
	private Long rowNum;
	
	@Id
	@Column(name="REQUEST_REFERENCE_NO")
	private String requestReferenceNo;

	@Column(name="COMPANY_ID")
	private String companyId;

	@Column(name="PRODUCT_ID")
	private String productId;
	
	@Column(name="TYPE_ID")
	private String typeId;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="ERROR_DESC")
	private String errorDesc;
	
	@Column(name="REGISTRATION_NO")
	private String registrationNo;
	
	@Column(name="BODYTYPE_DESC")
	private String bodytypeDesc;
	
	@Column(name="BODYTYPE_ID")
	private String bodytypeId;
	
	@Column(name="MAKE_DESC")
	private String makeDesc;
	
	@Column(name="MAKE_ID")
	private String makeId;
	
	@Column(name="MODEL_DESC")
	private String modelDesc;
	
	@Column(name="MODEL_ID")
	private String modelId;
	
	@Column(name="CHASSIS_NO")
	private String chassisNo;
	
	@Column(name="ENGINE_NO")
	private String engineNo;
	
	@Column(name="ENGINE_CAPACITY")
	private String engineCapacity;
	
	@Column(name="MANUFACTURE_YEAR")
	private String manufacutreYear;
	
	@Column(name="COLOR_DESC")
	private String colorDesc;
	
	@Column(name="COLOR_ID")
	private String colorId;
	
	@Column(name="MOTOR_CATEGORY_DESC")
	private String motorCategoryDesc;
	
	@Column(name="MOTOR_CATEGORY_ID")
	private String motorCategoryId;
	
	@Column(name="SEATING_CAPACITY")
	private String seatingCapacity;
	
	@Column(name="MOTOR_USEAGE_DESC")
	private String motorUseageDesc;
	
	@Column(name="MOTOR_USEAGE_ID")
	private String motorUseageId;
	
	@Column(name="FUELTYPE_DESC")
	private String fuletpeDesc;
	
	@Column(name="FUELTYPE_ID")
	private String fuetTypeId;
	
	@Column(name="TARE_WEIGHT")
	private String tareWeight;
	
	@Column(name="GROSS_WEIGHT")
	private String grossWeight;
	
	@Column(name="NOOF_AXELS")
	private String noofAxels;
	
	@Column(name="AXLE_DISTANCE")
	private String axleDistance;
	
	@Column(name="UPLOAD_TYPE")
	private String uploadType;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="BRANCH_CODE")
	private String branchCode;
	
	@Column(name="API_STATUS")
	private String apiStatus;
	
	@Column(name="EWAY_REFERENCE_NO")
	private String ewayReferenceNo;
	
	
}

