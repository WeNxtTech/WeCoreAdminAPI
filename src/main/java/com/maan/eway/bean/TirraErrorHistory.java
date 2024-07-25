package com.maan.eway.bean;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@IdClass(TirraErrorHistoryId.class)
@Table(name = "tira_error_history")
public class TirraErrorHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REQ_REG_NUMBER", length = 20, nullable = false)
	private String reqRegNumber;

	@Id
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE",  nullable = false)
	private Date entryDate;

	// --- ENTITY DATA FIELDS
	@Column(name = "REQ_CHASSIS_NUMBER", length = 20)
	private String reqChassisNumber;

	@Column(name = "API_DESCRIPTION", length = 100)
	private String apiDescription;

	@Column(name = "REQUEST_METHOD", length = 20)
	private String requestmethod;

	@Column(name = "REQUEST_URL", length = 300)
	private String requestUrl;

	@Column(name = "REQUEST_HEADERS", length = 300)
	private String requestHeaders;
	
	@Column(name = "REQUEST_STRING")
	private String requestString;

	@Column(name = "RESPONSE_STATUS", length = 10)
	private String responseStatus;
	
	@Column(name = "RESPONSE_STRING")
	private String responseString;

}
