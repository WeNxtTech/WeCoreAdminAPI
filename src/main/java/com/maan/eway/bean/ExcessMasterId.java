package com.maan.eway.bean;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcessMasterId implements Serializable {
	private static final long serialVersionUId = 5L;
	
	private Integer excessId;
	private String companyId;
	private String productId;
	private String sectionId;
	private String coverId;
	private Integer amendId;

}
