package com.rlms.dao;

import java.util.List;

import com.rlms.model.RlmsComplaintTechMapDtls;

public interface ComplaintsDao {
	public List<RlmsComplaintTechMapDtls> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList);
}
