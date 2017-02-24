package com.rlms.service;

import java.util.List;

import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.MemberDtlsDto;
//import com.rlms.contract.UserAppDtls;
import com.rlms.contract.UserMetaInfo;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsComplaintTechMapDtls;

public interface ComplaintsService {
	public List<ComplaintsDto> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList);
	public String validateAndRegisterNewComplaint(ComplaintsDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException;
	public List<ComplaintsDto> getListOfComplaintsBy(ComplaintsDtlsDto dto);
	public String assignComplaint(ComplaintsDto complaintsDto, UserMetaInfo metaInfo);
	public List<LiftDtlsDto> getAllLiftsForBranchsOrCustomer(LiftDtlsDto dto);
	//public List<UserAppDtls> getRegIdsOfAllApplicableUsers(Integer complaintId);
}
