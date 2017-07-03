package com.rlms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.AMCType;
import com.rlms.constants.Status;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.dao.DashboardDao;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.predicates.LiftPredicate;
import com.rlms.utils.DateUtils;

@Service
public class DashboardServiceImpl implements DashboardService {
	@Autowired
	private DashboardDao dashboardDao;

	@Transactional(propagation = Propagation.REQUIRED)
	public List<AMCDetailsDto> getAMCDetailsForDashboard() {
		List<AMCDetailsDto> listOFAMCDetails = new ArrayList<AMCDetailsDto>();
		List<Integer> listOFAllCustomersForBranch = new ArrayList<Integer>();
		List<Integer> listOfLiftsForAMCDtls = new ArrayList<Integer>();
		List<RlmsLiftCustomerMap> listOFApplicableLifts = new ArrayList<RlmsLiftCustomerMap>();

		List<RlmsLiftAmcDtls> listOfAMCDtls = this.dashboardDao
				.getAMCDetilsForLifts();
		Set<Integer> liftIds = new HashSet<Integer>();
		for (RlmsLiftAmcDtls liftAmcDtls : listOfAMCDtls) {
			liftIds.add(liftAmcDtls.getLiftCustomerMap().getLiftMaster()
					.getLiftId());
		}

		for (Integer liftId : liftIds) {
			List<RlmsLiftAmcDtls> listForLift = new ArrayList<RlmsLiftAmcDtls>(
					listOfAMCDtls);
			CollectionUtils.filter(listForLift, new LiftPredicate(liftId));
			listOFAMCDetails.addAll(this.constructListOFAMcDtos(listOfAMCDtls));
		}

		return listOFAMCDetails;
	}

	private List<AMCDetailsDto> constructListOFAMcDtos(
			List<RlmsLiftAmcDtls> listOFAMCs) {
		List<AMCDetailsDto> listOFDtos = new ArrayList<AMCDetailsDto>();
		int i = 0;
		for (RlmsLiftAmcDtls liftAmcDtls : listOFAMCs) {
			AMCDetailsDto dto = new AMCDetailsDto();
			if (null != liftAmcDtls.getAmcEndDate()) {
				dto.setAmcEndDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcEndDate()));
			}
			if (null != liftAmcDtls.getAmcStartDate()) {
				dto.setAmcStartDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcStartDate()));
			}

			dto.setCustomerName(liftAmcDtls.getLiftCustomerMap()
					.getBranchCustomerMap().getCustomerMaster()
					.getCustomerName());
			if (null != liftAmcDtls.getAmcDueDate()) {
				dto.setDueDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcDueDate()));
			}

			if (null != liftAmcDtls.getAmcSlackStartDate()) {
				dto.setLackEndDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcSlackStartDate()));
			}

			if (null != liftAmcDtls.getAmcSlackEndDate()) {
				dto.setLackEndDate(DateUtils
						.convertDateToStringWithoutTime(liftAmcDtls
								.getAmcSlackEndDate()));
			}

			dto.setLiftNumber(liftAmcDtls.getLiftCustomerMap().getLiftMaster()
					.getLiftNumber());
			dto.setCity(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap()
					.getCustomerMaster().getCity());
			dto.setArea(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap()
					.getCustomerMaster().getArea());
			Date tempStartDate = listOFAMCs.get(listOFAMCs.size() - 1)
					.getLiftCustomerMap().getLiftMaster().getAmcStartDate();
			Date tempEndDate = listOFAMCs.get(listOFAMCs.size() - 1)
					.getLiftCustomerMap().getLiftMaster().getAmcEndDate();
			Date tempDateOfInstallation = listOFAMCs.get(listOFAMCs.size() - 1)
					.getLiftCustomerMap().getLiftMaster()
					.getDateOfInstallation();
			dto.setStatus(this.calculateAMCStatus(tempStartDate, tempEndDate,
					tempDateOfInstallation).getStatusMsg());
			dto.setAmcAmount(liftAmcDtls.getLiftCustomerMap().getLiftMaster()
					.getAmcAmount());

			if (i > 0) {

				Integer diffInDays = DateUtils.daysBetween(listOFAMCs.get(i)
						.getAmcStartDate(), listOFAMCs.get(i - 1)
						.getAmcEndDate());
				if (diffInDays > 0) {
					Date slackStartDate = DateUtils.addDaysToDate(listOFAMCs
							.get(i - 1).getAmcEndDate(), 1);
					Date slackEndDate = DateUtils.addDaysToDate(
							listOFAMCs.get(i).getAmcStartDate(), -1);
					if (null != slackStartDate && null != slackEndDate) {
						{
							dto.setSlackStartDate(DateUtils
									.convertDateToStringWithoutTime(slackStartDate));
							dto.setSlackEndDate(DateUtils
									.convertDateToStringWithoutTime(slackEndDate));
							dto.setSlackperiod(diffInDays);
						}
					}
				}
			}

			if (AMCType.COMPREHENSIVE.getId() == liftAmcDtls.getAmcType()) {
				dto.setAmcTypeStr(AMCType.COMPREHENSIVE.getType());
			} else if (AMCType.NON_COMPREHENSIVE.getId() == liftAmcDtls
					.getAmcType()) {
				dto.setAmcTypeStr(AMCType.NON_COMPREHENSIVE.getType());
			} else if (AMCType.ON_DEMAND.getId() == liftAmcDtls.getAmcType()) {
				dto.setAmcTypeStr(AMCType.ON_DEMAND.getType());
			} else if (AMCType.OTHER.getId() == liftAmcDtls.getAmcType()) {
				dto.setAmcTypeStr(AMCType.OTHER.getType());
			}
			listOFDtos.add(dto);
			i++;
		}
		return listOFDtos;
	}

	private Status calculateAMCStatus(Date amcStartDate, Date amcEndDate,
			Date dateOfInstallation) {
		Status amcStatus = null;
		Date today = new Date();
		Date warrantyexpiryDate = DateUtils.addDaysToDate(dateOfInstallation,
				365);
		Date renewalDate = DateUtils.addDaysToDate(amcEndDate, -30);
		if (DateUtils.isBeforeOrEqualToDate(amcEndDate, warrantyexpiryDate)) {
			amcStatus = Status.UNDER_WARRANTY;
		} else if (DateUtils.isAfterOrEqualTo(renewalDate, today)
				&& DateUtils.isBeforeOrEqualToDate(today, amcEndDate)) {
			amcStatus = Status.RENEWAL_DUE;
		} else if (DateUtils.isAfterToDate(amcEndDate, today)) {
			amcStatus = Status.AMC_PENDING;
		} else if (DateUtils.isBeforeOrEqualToDate(amcStartDate, today)
				&& DateUtils.isAfterOrEqualTo(today, amcEndDate)) {
			amcStatus = Status.UNDER_AMC;
		}
		return amcStatus;

	}
}
