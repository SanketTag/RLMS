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
import com.rlms.constants.RLMSConstants;
import com.rlms.constants.Status;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.dao.DashboardDao;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsMemberMaster;
import com.rlms.model.RlmsUserRoles;
import com.rlms.predicates.LiftPredicate;
import com.rlms.utils.DateUtils;

@Service
public class DashboardServiceImpl implements DashboardService {
	@Autowired
	private DashboardDao dashboardDao;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;

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
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getListOfComplaintsBy(ComplaintsDtlsDto dto){
		List<ComplaintsDto> listOfAllComplaints = new ArrayList<ComplaintsDto>();
		List<RlmsComplaintMaster> listOfComplaints = this.dashboardDao.getAllComplaintsForGivenCriteria(dto.getBranchCompanyMapId(), dto.getBranchCustomerMapId(), dto.getListOfLiftCustoMapId(), dto.getStatusList(),dto.getFromDate(), dto.getToDate());
		for (RlmsComplaintMaster rlmsComplaintMaster : listOfComplaints) {
			ComplaintsDto complaintsDto = this.constructComplaintDto(rlmsComplaintMaster);
			listOfAllComplaints.add(complaintsDto);
		}
		
		return listOfAllComplaints;
	}

	private ComplaintsDto constructComplaintDto(RlmsComplaintMaster complaintMaster){
		ComplaintsDto dto = new ComplaintsDto();
		if(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getId().equals(complaintMaster.getRegistrationType())){
			dto.setRegistrationTypeStr(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getName());
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getId().equals(complaintMaster.getRegistrationType())){
			dto.setRegistrationTypeStr(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getName());
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getId().equals(complaintMaster.getRegistrationType())){
			dto.setRegistrationTypeStr(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getName());
		}
		dto.setComplaintId(complaintMaster.getComplaintId());
		dto.setComplaintNumber(complaintMaster.getComplaintNumber());
		dto.setCustomerName(complaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
		dto.setLiftAddress(complaintMaster.getLiftCustomerMap().getLiftMaster().getAddress());
		dto.setRegistrationDate(complaintMaster.getRegistrationDate());
		if(null != complaintMaster.getRegistrationDate()){
			dto.setRegistrationDateStr(DateUtils.convertDateToStringWithoutTime(complaintMaster.getRegistrationDate()));
		}
		dto.setActualServiceEndDate(complaintMaster.getActualServiceEndDate());
		if(null != complaintMaster.getActualServiceEndDate()){
			dto.setActualServiceEndDateStr(DateUtils.convertDateToStringWithoutTime(complaintMaster.getActualServiceEndDate()));
		}
		dto.setRemark(complaintMaster.getRemark());
		dto.setTitle(complaintMaster.getTitle());
		dto.setServiceStartDate(complaintMaster.getServiceStartDate());
		if(null != complaintMaster.getServiceStartDate()){
			dto.setServiceStartDateStr(DateUtils.convertDateToStringWithoutTime(complaintMaster.getServiceStartDate()));
		}
		if(!Status.PENDING.getStatusId().equals(complaintMaster.getStatus())){
			RlmsComplaintTechMapDtls complaintTechMapDtls = this.dashboardDao.getComplTechMapObjByComplaintId(complaintMaster.getComplaintId());
			if(null != complaintTechMapDtls){
				String techDtls = complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() + " " + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName() + " (" + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber() + ")";			
				dto.setTechnicianDtls(techDtls);
			}
		}else{
			dto.setTechnicianDtls("-");
		}
		if(Status.PENDING.getStatusId().equals(complaintMaster.getStatus())){
			dto.setStatus(Status.PENDING.getStatusMsg());
		}else if(Status.ASSIGNED.getStatusId().equals(complaintMaster.getStatus())){
			dto.setStatus(Status.ASSIGNED.getStatusMsg());
		}else if(Status.INPROGESS.getStatusId().equals(complaintMaster.getStatus())){
			dto.setStatus(Status.INPROGESS.getStatusMsg());
		}else if(Status.COMPLETED.getStatusId().equals(complaintMaster.getStatus())){
			dto.setStatus(Status.COMPLETED.getStatusMsg());
		}
		String complaintent = null;
		if(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getId() == complaintMaster.getRegistrationType()){
			dto.setRegType(RLMSConstants.COMPLAINT_REG_TYPE_ADMIN.getName());
			RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(complaintMaster.getCreatedBy());
			complaintent = userRoles.getRlmsUserMaster().getFirstName() + " " + userRoles.getRlmsUserMaster().getLastName() + " (" + userRoles.getRlmsUserMaster().getContactNumber() + ")";
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getId() == complaintMaster.getRegistrationType()){
			dto.setRegType(RLMSConstants.COMPLAINT_REG_TYPE_END_USER.getName());
			RlmsMemberMaster memberMaster = this.customerService.getMemberById(complaintMaster.getCreatedBy());
			complaintent = memberMaster.getFirstName() + " " + memberMaster.getLastName() + " (" + memberMaster.getContactNumber() + ")";
		}else if(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getId() == complaintMaster.getRegistrationType()){
			dto.setRegType(RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getName());
			complaintent = RLMSConstants.COMPLAINT_REG_TYPE_LIFT_EVENT.getName();
		}
		dto.setComplaintent(complaintent);
		
		
		return dto;
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
