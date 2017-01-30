package com.rlms.service;

import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsCustomerMaster;

public interface CustomerService {
	public RlmsCustomerMaster getCustomerByEmailId(String emailId);
	public String validateAndRegisterNewCustomer(CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo) throws ValidationException;
}
