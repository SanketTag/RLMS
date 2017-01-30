package com.rlms.dao;

import com.rlms.model.RlmsCustomerMaster;

public interface CustomerDao {

	
	public RlmsCustomerMaster getCustomerByEmailId(String emailId);
	public void saveCustomerM(RlmsCustomerMaster customerMaster);
}
