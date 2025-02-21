/**
 * @author : Ashok Kumar S 
 * @since  : 20-02-2025
 */
package com.maan.eway.service;

import java.util.List;

import com.maan.eway.bean.ClausesMasterV1;
import com.maan.eway.req.ClausesMasterV1GetAllReq;
import com.maan.eway.req.ClausesMasterV1GetReq;
import com.maan.eway.req.ClausesMasterV1SaveUpReq;
import com.maan.eway.req.ClausesMasterV1StatusChangeReq;
import com.maan.eway.res.ClausesMasterV1Res;

public interface ClausesMasterV1Service {
	
	public List<ClausesMasterV1Res> getAllClausesMaster(ClausesMasterV1GetAllReq req);
	
	public List<ClausesMasterV1Res> getAllActiveClausesMaster(ClausesMasterV1GetAllReq req);
	
	public ClausesMasterV1Res getSingleClausesMaster(ClausesMasterV1GetReq req);
	
	public ClausesMasterV1Res getSingleActiveClausesMaster(ClausesMasterV1GetReq req);
	
	public Boolean activateOrInactivateClausesMaster(ClausesMasterV1StatusChangeReq req);
	
	public ClausesMasterV1 saveAndUpdateClausesMasterDetails(ClausesMasterV1SaveUpReq req);

}
