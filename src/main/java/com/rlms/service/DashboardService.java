package com.rlms.service;

import java.util.List;

import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.model.RlmsLiftCustomerMap;

public interface DashboardService {

	public List<AMCDetailsDto> getAMCDetailsForDashboard(
			List<Integer> listOfApplicableBranchIds);

	public List<ComplaintsDto> getListOfComplaintsBy(ComplaintsDtlsDto dto);

	public List<CustomerDtlsDto> getAllCustomersForBranch(
			List<Integer> listOfApplicableBranchIds);

	public List<RlmsLiftCustomerMap> getAllLiftsForBranchsOrCustomer(
			LiftDtlsDto dto);
}
