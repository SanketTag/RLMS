package com.rlms.service;

import java.text.ParseException;
import java.util.List;

import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.TechnicianWiseReportDto;
import com.rlms.contract.UserMetaInfo;

public interface ReportService {

	public List<AMCDetailsDto> getAMCDetailsForLifts(AMCDetailsDto dto);
	public String addAMCDetailsForLift(AMCDetailsDto dto, UserMetaInfo metaInfo) throws ParseException;
	public List<TechnicianWiseReportDto> getSiteVisitReport(TechnicianWiseReportDto dto);
}
