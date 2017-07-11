package com.rlms.service;

import java.util.List;

import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;

public interface DashboardService {

	public List<AMCDetailsDto> getAMCDetailsForDashboard();

	public List<ComplaintsDto> getListOfComplaintsBy(ComplaintsDtlsDto dto);
}
