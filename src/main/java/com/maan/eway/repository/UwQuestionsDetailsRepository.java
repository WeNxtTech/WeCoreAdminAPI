package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.UwQuestionsDetails;
import com.maan.eway.bean.UwQuestionsDetailsId;
public interface UwQuestionsDetailsRepository  extends JpaRepository<UwQuestionsDetails,UwQuestionsDetailsId > , JpaSpecificationExecutor<UwQuestionsDetails> {

	UwQuestionsDetails findByCompanyIdAndProductIdAndRequestReferenceNoAndVehicleIdAndUwQuestionId(String companyId,
			Integer valueOf, String requestReferenceNo, Integer valueOf2, Integer valueOf3);



}
