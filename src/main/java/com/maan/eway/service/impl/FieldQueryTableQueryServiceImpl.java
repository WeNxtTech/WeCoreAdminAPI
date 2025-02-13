/**
 * @author : Ashok Kumar S 
 * @since  : 12-02-2025
 */
package com.maan.eway.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.eway.bean.FieldQueryTablequery;
import com.maan.eway.bean.FieldQueryTablequeryId;
import com.maan.eway.error.Error;
import com.maan.eway.repository.FieldQueryTableQueryRepository;
import com.maan.eway.req.FieldQueryTableQuerySaveUpReq;
import com.maan.eway.res.FieldQueryTableQueryRes;
import com.maan.eway.service.FieldQueryTableQueryService;

@Service
public class FieldQueryTableQueryServiceImpl implements FieldQueryTableQueryService{	
	
	private static final Logger log = LogManager.getLogger(FieldQueryTableQueryServiceImpl.class);
	
	private FieldQueryTableQueryRepository fieldQueryRepo;
	private ModelMapper mapper;
	
	@Autowired
	public FieldQueryTableQueryServiceImpl(FieldQueryTableQueryRepository fieldQueryRepo, ModelMapper mapper) {
		this.fieldQueryRepo = fieldQueryRepo;
		this.mapper = mapper;
	}

	@Override
	public List<Error> validateParametersOfFieldQueryTablequerySaveRequest(FieldQueryTableQuerySaveUpReq req) {
		List<Error> errors = new ArrayList<>();
		
		if(StringUtils.isBlank(req.getQueryName())) {
			errors.add(new Error("1", "queryName", "Please provide query name, It Should not be blank."));
		}
		if(StringUtils.isBlank(req.getSqlQuery())) {
			errors.add(new Error("2", "sqlQuery", "Please provide SQL query, It should not be blank."));
		}
		
		return errors;
	}
	
	
	/**
	 * Saves or updates a {@link FieldQueryTablequery} entity based on the provided request data.
	 * <p>
	 * If the request contains a null {@code queryId}, a new record is created.
	 * Otherwise, an update operation is performed after validating the existence of the record.
	 * </p>
	 *
	 * @param req The {@link FieldQueryTableQuerySaveUpReq} object containing the field query details.
	 * @return The saved or updated {@link FieldQueryTablequery} entity.
	 *         Returns {@code null} if an exception occurs during the operation.
	 * @throws NoSuchElementException If the requested query ID for update does not exist.
	 */
	@Override
	public FieldQueryTablequery saveUpdateFieldQueryTablequeryDetails(FieldQueryTableQuerySaveUpReq req) {
		try {
	        // Save new entity
			if(req.getQueryId() == null) {
				FieldQueryTablequery saveFieldQuery = mapper.map(req, FieldQueryTablequery.class);
				saveFieldQuery.setQueryId(queryIdGenerator());
				
				return fieldQueryRepo.saveAndFlush(saveFieldQuery);
			}
	        // Update existing entity
			else {
				Optional<FieldQueryTablequery> optFieldQuery = fieldQueryRepo.findById(
						new FieldQueryTablequeryId(req.getQueryId()));
				
				if(optFieldQuery.isEmpty()) {
					throw new NoSuchElementException("FieldQueryTableQuery you are trying to update was not found");
				}
				
				FieldQueryTablequery updateFieldQuery = mapper.map(req, FieldQueryTablequery.class);
				return fieldQueryRepo.saveAndFlush(updateFieldQuery);
			}			
		} catch (Exception e) {
			log.error("Exception : {}", e.getMessage(), e);
			return null;
		}		
	}
	
	
	/**
	 * Retrieves a list of all {@link FieldQueryTableQueryRes} records from the database.
	 * <p>
	 * This method fetches all records from the {@link FieldQueryTablequery} repository,
	 * maps them to their response DTOs, and returns the result.
	 * If no records are found, a {@link NoSuchElementException} is thrown.
	 * </p>
	 *
	 * @return A list of {@link FieldQueryTableQueryRes} containing all field query table query details.
	 *         Returns {@code null} if an exception occurs during the operation.
	 * @throws NoSuchElementException If no field query table query records exist.
	 */
	@Override
	public List<FieldQueryTableQueryRes> getAllFieldQueryTablequeryDetails() {
		try {
			List<FieldQueryTablequery> allFieldQuery = fieldQueryRepo.findAllByOrderByQueryIdAsc();
			
			if(allFieldQuery.isEmpty()) {
				throw new NoSuchElementException("All FieldQueryTableQuery details were empty.");
			}
			
			return allFieldQuery.stream()
					.map(fieldQuery -> mapper.map(fieldQuery, FieldQueryTableQueryRes.class))
					.toList();
		} catch (Exception e) {
			log.error("Exception : {}", e.getMessage(), e);
			return null;
		}
	}

	
	/**
	 * Retrieves the details of a specific {@link FieldQueryTableQueryRes} record based on the provided query ID.
	 * <p>
	 * This method fetches the corresponding record from the {@link FieldQueryTablequery} repository
	 * and maps it to its response DTO. If no record is found, a {@link NoSuchElementException} is thrown.
	 * </p>
	 *
	 * @param queryId The unique identifier of the field query table query.
	 * @return The {@link FieldQueryTableQueryRes} containing the details of the requested field query table query.
	 *         Returns {@code null} if an exception occurs during the operation.
	 * @throws NoSuchElementException If the specified field query table query does not exist.
	 */
	@Override
	public FieldQueryTableQueryRes getFieldQueryTablequeryDetails(BigDecimal queryId) {
		try {
			Optional<FieldQueryTablequery> optFieldQuery = fieldQueryRepo.findById(
					new FieldQueryTablequeryId(queryId));
			
			if(optFieldQuery.isEmpty()) {
				throw new NoSuchElementException("FieldQueryTableQuery you are looking for was not found");
			}
			
			return mapper.map(optFieldQuery.get(), FieldQueryTableQueryRes.class);
		} catch (Exception e) {
			log.error("Exception : {}", e.getMessage(), e);
			return null;
		}
	}

	
	/**
	 * Generates a new unique query ID for {@link FieldQueryTablequery}.
	 * <p>
	 * This method retrieves the highest existing query ID from the database and increments it by one.
	 * If no records are found, it initializes the query ID with {@code BigDecimal.ONE}.
	 * </p>
	 *
	 * @return A newly generated {@link BigDecimal} query ID.
	 */
	private BigDecimal queryIdGenerator() {
		FieldQueryTablequery topByQueryId = fieldQueryRepo.findTopByOrderByQueryIdDesc();
		
		if(topByQueryId == null) {
			return BigDecimal.ONE;
		}
		
		return topByQueryId.getQueryId().add(BigDecimal.ONE);		
	}



}
