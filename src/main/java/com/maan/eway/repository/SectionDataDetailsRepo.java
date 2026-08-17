package com.maan.eway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.eway.bean.SectionDataDetails;
import com.maan.eway.bean.SectionDataDetailsId;

public interface SectionDataDetailsRepo extends JpaRepository<SectionDataDetails, SectionDataDetailsId> {

	List<SectionDataDetails> findByStickerNumberAndCompanyId(String stickerno, String companyId);

	List<SectionDataDetails> findByCoverNoteReferenceNoAndCompanyId(String coverNoteNo, String companyId);

	List<SectionDataDetails> findByQuoteNo(String qu);

	List<SectionDataDetails> findByQuoteNoAndCompanyId(String quoteNo, String companyId);

}
