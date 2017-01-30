package com.rlms.service;

import java.util.List;

import com.rlms.contract.ComplaintsDto;
import com.rlms.model.RlmsComplaintTechMapDtls;

public interface ComplaintsService {
	public List<ComplaintsDto> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList);
}
