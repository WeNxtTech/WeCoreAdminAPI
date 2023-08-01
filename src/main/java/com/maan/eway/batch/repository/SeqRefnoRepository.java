package com.maan.eway.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.eway.batch.entity.SqlSeqNumber;

public interface SeqRefnoRepository extends JpaRepository<SqlSeqNumber,Long > , JpaSpecificationExecutor<SqlSeqNumber> {

}


