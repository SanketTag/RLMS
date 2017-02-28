package com.rlms.service;

import java.util.List;

import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.UserAppDtls;
import com.rlms.contract.UserMetaInfo;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsCustomerMaster;
import com.rlms.model.RlmsCustomerMemberMap;

public interface CustomerService {
	public RlmsCustomerMaster getCustomerByEmailId(String emailId);
	public String validateAndRegisterNewCustomer(CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo) throws ValidationException;
	public List<CustomerDtlsDto> getAllApplicableCustomers(CustomerDtlsDto dto, UserMetaInfo metaInfo);
	public List<CustomerDtlsDto> getAllCustomersForBranch(CustomerDtlsDto dto, UserMetaInfo metaInfo);
	public MemberDtlsDto registerMemeberDeviceByMblNo(MemberDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException;
	public String validateAndRegisterNewMember(MemberDtlsDto memberDtlsDto, UserMetaInfo metaInfo) throws ValidationException;
	public List<MemberDtlsDto> getListOfAllMemberDtls(MemberDtlsDto memberDtlsDto);
	public UserAppDtls getUserAppDtls(Integer userId, Integer userType);
	public List<RlmsCustomerMemberMap> getAllMembersForCustomer(
			Integer branchCustomerMapId);
	public List<LiftDtlsDto> getAllLiftsForMember(Integer memberId);
}
