/**
 * @author : Ashok Kumar S 
 * @since  : 19-02-2025
 */
package com.maan.eway.bean;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CLAUSES_MASTER")
@IdClass(ClausesMasterV1PK.class)
@NoArgsConstructor
@Getter
@Setter
public class ClausesMasterV1 {
	
	@Id
	@Column(name = "COMPANY_ID")
	private Integer companyId;
	
	@Id
	@Column(name = "PRODUCT_ID")
	private Integer productId;
	
	@Id
	@Column(name = "SECTION_ID")
	private Integer sectionId;
	
	@Id
	@Column(name = "COVER_ID")
	private Integer coverId;
	
	@Id
	@Column(name = "CLAUSES_ID")
	private Integer clausesId;
	
	@Id
	@Column(name = "AMEND_ID")
	private Integer amendId;
	
	@Column(name = "CLAUSES_SHORT_DESC")
	private String clausesShortDesc;
	
	@Column(name = "CLAUSES_DESCRIPTION")
	private String clausesDescription;
	
	@Column(name = "STATUS", length = 1)
    private String status;
	
	@Column(name = "EFFECTIVE_DATE_START")
	private LocalDate effectiveDateStart;
	
	@Column(name = "EFFECTIVE_DATE_END")
	private LocalDate effectiveDateEnd;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "UPDATED_BY")
	private String updatedBy;

	//Not needed for clause master V1
	@Column(name = "BRANCH_CODE")
	private Integer branchCode;
}
