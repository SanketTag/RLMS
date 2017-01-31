package com.rlms.dao;

import java.util.List;

import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCustomerMaster;

public interface CustomerDao {

	
	public RlmsCustomerMaster getCustomerByEmailId(String emailId);
	public Integer saveCustomerM(RlmsCustomerMaster customerMaster);
	List<RlmsBranchCustomerMap> getAllCustomersForBranches(
			List<Integer> listOfBranchCompanyMapId);
}
