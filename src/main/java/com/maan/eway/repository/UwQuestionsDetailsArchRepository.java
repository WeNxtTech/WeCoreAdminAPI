package com.maan.eway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.bean.UwQuestionsDetailsArch;
import com.maan.eway.bean.UwQuestionsDetailsArchId;
public interface UwQuestionsDetailsArchRepository  extends JpaRepository<UwQuestionsDetailsArch,UwQuestionsDetailsArchId > , JpaSpecificationExecutor<UwQuestionsDetailsArch> {

}
