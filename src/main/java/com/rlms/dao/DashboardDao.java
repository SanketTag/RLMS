package com.rlms.dao;

import java.util.List;

import com.rlms.model.RlmsLiftAmcDtls;

public interface DashboardDao {

	public List<RlmsLiftAmcDtls> getAMCDetilsForLifts();

}
