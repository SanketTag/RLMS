package com.rlms.dao;

import java.util.Date;
import java.util.List;

import com.rlms.contract.SiteVisitReportDto;
import com.rlms.contract.TechnicianWiseReportDTO;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsSiteVisitDtls;

public interface ComplaintsDao {
	public List<RlmsComplaintTechMapDtls> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList);
	public List<RlmsComplaintMaster> getAllComplaintsForBranchOrCustomer(Integer branchCompanyMapId, Integer branchCustomerMapId, List<Integer> statusList);
	public Integer saveComplaintM(RlmsComplaintMaster complaintMaster);
	public List<RlmsComplaintMaster> getAllComplaintsForGivenCriteria(Integer branchCompanyMapId, Integer branchCustomerMapId,List<Integer> listOfLioftIds,  List<Integer> statusList,  Date fromDate, Date toDate);
	public RlmsComplaintTechMapDtls getComplTechMapObjByComplaintId(Integer complaintId);
	public RlmsComplaintMaster getComplaintMasterObj(Integer complaintId);
	public void saveComplaintTechMapDtls(RlmsComplaintTechMapDtls complaintTechMapDtls);
	public List<RlmsComplaintMaster> getAllComplaintsByMemberId(Integer memberId);
	public void mergeComplaintM(RlmsComplaintMaster complaintMaster);
	public List<RlmsComplaintTechMapDtls> getListOfComplaintDtlsForTechies(SiteVisitReportDto dto);
	public void saveComplaintSiteVisitDtls(RlmsSiteVisitDtls siteVisitDtls);
	public RlmsComplaintTechMapDtls getComplTechMapByComplaintTechMapId(Integer complaintTechMapId);
	public List<RlmsSiteVisitDtls> getAllVisitsForComnplaints(Integer complaintTechMapId);
	public List<RlmsComplaintTechMapDtls> getListOfComplaintDtlsForTechies(TechnicianWiseReportDTO dto);
}
