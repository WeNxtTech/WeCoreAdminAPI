/**
 * @author : Ashok Kumar S 
 * @since  : 14-02-2025
 */
package com.maan.eway.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.maan.eway.common.res.CommonRes;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * Handles validation errors for request parameters when a method argument is invalid.
	 *
	 * @param ex The exception thrown when validation fails for a request parameter.
	 * @return ResponseEntity containing a {@link CommonRes} object with validation error details 
	 *         and an HTTP status of 422 (Unprocessable Entity).
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonRes> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<Error> errors = new ArrayList<>();
		
		for(FieldError fe : fieldErrors) {
			errors.add(new Error (String.valueOf(errors.size() + 1), fe.getField(), fe.getDefaultMessage()));
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Validation failed.");
		response.setIsError(true);
		response.setErrorMessage(errors);
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
	}
	
	/**
	 * Handles ConstraintViolationException and returns a structured error response.
	 * <p>
	 * This method captures validation errors, extracts relevant details such as field names and messages,  
	 * and formats them into a standardized {@link CommonRes} response.
	 * </p>
	 *
	 * @param ex the {@link ConstraintViolationException} thrown when validation fails.
	 * @return a {@link ResponseEntity} containing a {@link CommonRes} object with validation error details.
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<CommonRes> handleConstraintViolationException(ConstraintViolationException ex){		
		List<Error> errors = new ArrayList<>();
		
		for(ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			 String field = violation.getPropertyPath().toString(); 
		     String message = violation.getMessage(); 
		     errors.add(new Error (String.valueOf(errors.size() + 1), field, message));
		}
		
		CommonRes response = new CommonRes();
		response.setMessage("Validation failed.");
		response.setIsError(true);
		response.setErrorMessage(errors);
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
	}

}
