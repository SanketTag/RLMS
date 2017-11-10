package com.rlms.dao;

import java.util.Date;
import java.util.List;

import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsUserRoles;

public interface DashboardDao {

	public List<RlmsLiftAmcDtls> getAMCDetilsForLifts();

	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(
			Integer branchCompanyMapId, Integer branchCustomerMapId,
			List<Integer> listOfLiftCustoMapId, List<Integer> statusList,
			Date fromDate, Date toDate);

	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(
			Integer complaintId);
	public List<RlmsUserRoles> getAllUserWithRoleFor(List<Integer> commpBranchMapId, Integer spocRoleId);
	public List<RlmsCompanyBranchMapDtls> getAllBranchesForDashboard(Integer companyId);
	public List<RlmsCompanyBranchMapDtls> getAllBranchDtlsForDashboard(List<Integer> ListOfCompanyIds);
	public RlmsCompanyBranchMapDtls getCompanyBranchMapDtlsForDashboard(Integer compBranchMapId);
}
