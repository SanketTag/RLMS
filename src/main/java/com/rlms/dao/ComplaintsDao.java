package com.rlms.dao;

import java.util.List;

import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;

public interface ComplaintsDao {
	public List<RlmsComplaintTechMapDtls> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList);
	public List<RlmsComplaintMaster> getAllComplaintsForBranch(Integer branchCompanyMapId, Integer branchCustomerMapId, List<Integer> statusList);
	public void saveComplaintM(RlmsComplaintMaster complaintMaster);
}
