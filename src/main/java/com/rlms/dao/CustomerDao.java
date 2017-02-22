package com.rlms.dao;

import java.util.List;

import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCustomerMaster;
import com.rlms.model.RlmsCustomerMemberMap;
import com.rlms.model.RlmsMemberMaster;

public interface CustomerDao {

	
	public RlmsCustomerMaster getCustomerByEmailId(String emailId);
	public Integer saveCustomerM(RlmsCustomerMaster customerMaster);
	public List<RlmsBranchCustomerMap> getAllCustomersForBranches(
			List<Integer> listOfBranchCompanyMapId);
	public RlmsMemberMaster getMemberByCntNo(String phoneNumber);
	public Integer saveMemberM(RlmsMemberMaster memberMaster);
	public RlmsCustomerMaster getCustomerById(Integer customerId);
	public Integer saveCustomerMemberMap(RlmsCustomerMemberMap customerMemberMap);
	public List<RlmsCustomerMemberMap> getAllCustomersForMember(Integer memberId);
	public RlmsBranchCustomerMap getBranchCustomerMapByCustoId(Integer customerId);
}
