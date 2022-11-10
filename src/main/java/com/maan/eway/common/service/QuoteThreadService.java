package com.maan.eway.common.service;

import com.maan.eway.common.req.NewQuoteReq;
import com.maan.eway.common.service.impl.NewQuoteRes;

public interface QuoteThreadService {

	NewQuoteRes call_OT_Insert(NewQuoteReq req);

}
