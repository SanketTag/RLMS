package com.rlms.service;

import java.util.List;

import com.rlms.contract.AMCDetailsDto;

public interface DashboardService {

	public List<AMCDetailsDto> getAMCDetailsForDashboard();
}
