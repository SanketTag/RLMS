package com.rlms.service;

import java.util.List;

import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsCustomerMaster;

public interface CustomerService {
	public RlmsCustomerMaster getCustomerByEmailId(String emailId);
	public String validateAndRegisterNewCustomer(CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo) throws ValidationException;
	public List<CustomerDtlsDto> getAllApplicableCustomers(CustomerDtlsDto dto, UserMetaInfo metaInfo);
	public List<CustomerDtlsDto> getAllCustomersForBranch(CustomerDtlsDto dto, UserMetaInfo metaInfo);
	public MemberDtlsDto registerMemeberDeviceByMblNo(MemberDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException;
	public String validateAndRegisterNewMember(MemberDtlsDto memberDtlsDto, UserMetaInfo metaInfo) throws ValidationException;
	public List<MemberDtlsDto> getListOfAllMemberDtls(MemberDtlsDto memberDtlsDto);
}
