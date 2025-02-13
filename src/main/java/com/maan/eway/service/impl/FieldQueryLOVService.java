/**
 * @author : Ashok Kumar S 
 * @since  : 13-02-2025
 */
package com.maan.eway.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.FieldQueryTablequery;
import com.maan.eway.repository.FieldQueryTableQueryRepository;
import com.maan.eway.res.ListOfValuesRes;

@Service
public class FieldQueryLOVService {
	private static final Logger log = LogManager.getLogger(FieldQueryLOVService.class);
	
	private FieldQueryTableQueryRepository fieldQueryRepo;

	@Autowired
	public FieldQueryLOVService(FieldQueryTableQueryRepository fieldQueryRepo) {
		this.fieldQueryRepo = fieldQueryRepo;
	}

	/**
	 * Retrieves a list of available Query IDs and their corresponding Query Names.
	 * <p>
	 * This method fetches all records from the {@code fieldQueryRepo} and maps them 
	 * into a list of {@link ListOfValuesRes} objects containing Query ID and Query Name.
	 * If an exception occurs, it logs the error and returns {@code null}.
	 * </p>
	 *
	 * @return A list of {@link ListOfValuesRes} containing Query IDs and Query Names,
	 *         or {@code null} in case of an exception.
	 */
	public List<ListOfValuesRes> dropdownToChooseQueryId(){
		try {
			List<FieldQueryTablequery> allFieldQueries = fieldQueryRepo.findAllByOrderByQueryIdAsc();
			
			return allFieldQueries.stream()
					.map(fieldQuery -> new ListOfValuesRes(
							String.valueOf(fieldQuery.getQueryId()), fieldQuery.getQueryName()))
					.toList();
		} catch (Exception e) {
			log.error("Exception : {}", e.getMessage(), e);
			return null;
		}		
	}
	
	
}
