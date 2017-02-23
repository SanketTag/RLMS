package com.rlms.dao;

import java.util.List;

import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;

public interface ComplaintsDao {
	public List<RlmsComplaintTechMapDtls> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList);
	public List<RlmsComplaintMaster> getAllComplaintsForBranchOrCustomer(Integer branchCompanyMapId, Integer branchCustomerMapId, List<Integer> statusList);
	public void saveComplaintM(RlmsComplaintMaster complaintMaster);
	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(Integer branchCompanyMapId, Integer branchCustomerMapId,List<Integer> listOfLioftIds,  List<Integer> statusList);
	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(Integer complaintId);
	public RlmsComplaintMaster getComplaintMasterObj(Integer complaintId);
	public void saveComplaintTechMapDtls(RlmsComplaintTechMapDtls complaintTechMapDtls);
}
