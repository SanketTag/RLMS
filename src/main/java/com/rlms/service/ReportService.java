package com.rlms.service;

import java.util.List;

import com.rlms.contract.AMCDetailsDto;

public interface ReportService {

	public List<AMCDetailsDto> getAMCDetailsForLifts(AMCDetailsDto dto);
}
