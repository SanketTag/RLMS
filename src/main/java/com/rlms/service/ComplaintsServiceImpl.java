package com.rlms.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.Messages;
import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RLMSMessages;
//import com.rlms.constants.RLMSMessages;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.Status;
//import com.rlms.constants.XMPPServerDetails;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.UserAppDtls;
//import com.rlms.contract.UserAppDtls;
import com.rlms.contract.UserMetaInfo;
import com.rlms.controller.RestControllerController;
import com.rlms.dao.ComplaintsDao;
import com.rlms.dao.CustomerDao;
import com.rlms.dao.LiftDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsCustomerMemberMap;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsMemberMaster;
import com.rlms.model.RlmsUserApplicationMapDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.utils.DateUtils;
import com.rlms.utils.PropertyUtils;
//import com.telesist.xmpp.AndroidNotificationService;
//import com.telesist.xmpp.AndroidNotificationServiceImpl;
import com.telesist.xmpp.util.Util;

@Service("ComplaintsService")
public class ComplaintsServiceImpl implements ComplaintsService{

	@Autowired
	private ComplaintsDao complaintsDao;
	
	@Autowired
	private LiftDao liftDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private MessagingService messagingService;
	
	private static final Logger log = Logger.getLogger(ComplaintsServiceImpl.class);
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getAllComplaintsAssigned(Integer userRoleId, List<Integer> statusList){
		
		List<RlmsComplaintTechMapDtls> listOFAssignedComplaints =  this.complaintsDao.getAllComplaintsAssigned(userRoleId, statusList);
		List<ComplaintsDto> listOFComplaints = new ArrayList<ComplaintsDto>();
		for (RlmsComplaintTechMapDtls complaintTechMapDtls : listOFAssignedComplaints) {
			ComplaintsDto dto = new ComplaintsDto();
			dto.setActualServiceEndDate(complaintTechMapDtls.getComplaintMaster().getActualServiceEndDate());
			dto.setComplaintNumber(complaintTechMapDtls.getComplaintMaster().getComplaintNumber());
			dto.setCustomerName(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
			dto.setLatitude(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getLatitude());
			dto.setLongitude(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getLongitude());
			dto.setLiftNumber(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftMaster().getLiftNumber());
			dto.setRegistrationDate(complaintTechMapDtls.getComplaintMaster().getRegistrationDate());
			dto.setRemark(complaintTechMapDtls.getComplaintMaster().getRemark());
			dto.setServiceStartDate(complaintTechMapDtls.getComplaintMaster().getServiceStartDate());
			if(Status.PENDING.getStatusId().equals(complaintTechMapDtls.getComplaintMaster().getStatus())){
				dto.setStatus(Status.PENDING.getStatusMsg());
			}else if(Status.ASSIGNED.getStatusId().equals(complaintTechMapDtls.getComplaintMaster().getStatus())){
				dto.setStatus(Status.ASSIGNED.getStatusMsg());
			}else if(Status.INPROGESS.getStatusId().equals(complaintTechMapDtls.getComplaintMaster().getStatus())){
				dto.setStatus(Status.INPROGESS.getStatusMsg());
			}else if(Status.RESOLVED.getStatusId().equals(complaintTechMapDtls.getComplaintMaster().getStatus())){
				dto.setStatus(Status.RESOLVED.getStatusMsg());
			}
			listOFComplaints.add(dto);
		}
		return listOFComplaints;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndRegisterNewComplaint(ComplaintsDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException{
		String result = null;
		if(this.validateComplaintDetails(dto)){
			RlmsComplaintMaster complaintMaster = this.counstructComplaintMaster(dto, metaInfo);	
			this.complaintsDao.saveComplaintM(complaintMaster);
			result = PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_REG_SUCCESSFUL.getMessage());
			this.sendNotificationsAboutComplaintRegistration(complaintMaster);
		}
	  return result;
	}
	
	
	private boolean validateComplaintDetails(ComplaintsDtlsDto dto) throws ValidationException{
		boolean isValidaDetails = true;
		if(null == dto.getComplaintsRemark()){
			isValidaDetails = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_REMARK_BLANK.getMessage()));
		}
		
		if(null == dto.getComplaintsTitle()){
			isValidaDetails = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_TITLE_BLANK.getMessage()));
		}
		
		if(null == dto.getLiftCustomerMapId()){
			isValidaDetails = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.LIFT_CUSTOMER_BLANK.getMessage()));
		}
		
		return isValidaDetails;
	}
	private RlmsComplaintMaster counstructComplaintMaster(ComplaintsDtlsDto dto, UserMetaInfo metaInfo){
		RlmsComplaintMaster complaintMaster = new RlmsComplaintMaster();
		RlmsLiftCustomerMap liftCustomerMap = this.liftDao.getLiftCustomerMapById(dto.getLiftCustomerMapId());
		
		complaintMaster.setActiveFlag(RLMSConstants.ACTIVE.getId());
		
		complaintMaster.setLiftCustomerMap(liftCustomerMap);
		complaintMaster.setRegistrationDate(new Date());
		complaintMaster.setRegistrationType(dto.getRegistrationType());
		complaintMaster.setRemark(dto.getComplaintsRemark());
		complaintMaster.setStatus(Status.PENDING.getStatusId());
		complaintMaster.setTitle(dto.getComplaintsTitle());
		complaintMaster.setCreatedBy(metaInfo.getUserId());
		complaintMaster.setCreatedDate(new Date());
		complaintMaster.setUpdatedBy(metaInfo.getUserId());
		complaintMaster.setUpdatedDate(new Date());
		return complaintMaster;
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
			RlmsComplaintTechMapDtls complaintTechMapDtls = this.complaintsDao.getComplTechMapObjByComplaintId(complaintMaster.getComplaintId());
			String techDtls = complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() + " " + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName() + " (" + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber() + ")";			
			dto.setTechnicianDtls(techDtls);
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
		return dto;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getListOfComplaintsBy(ComplaintsDtlsDto dto){
		List<ComplaintsDto> listOfAllComplaints = new ArrayList<ComplaintsDto>();
		List<RlmsComplaintMaster> listOfComplaints = this.complaintsDao.getAllComplaintsForGivenCriteria(dto.getBranchCompanyMapId(), dto.getBranchCustomerMapId(), dto.getListOfLiftCustoMapId(), dto.getStatusList(),dto.getFromDate(), dto.getToDate());
		for (RlmsComplaintMaster rlmsComplaintMaster : listOfComplaints) {
			ComplaintsDto complaintsDto = this.constructComplaintDto(rlmsComplaintMaster);
			listOfAllComplaints.add(complaintsDto);
		}
		
		return listOfAllComplaints;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String assignComplaint(ComplaintsDto complaintsDto, UserMetaInfo metaInfo){
		RlmsComplaintTechMapDtls complaintTechMapDtls = this.constructComplaintTechMapDtlsDto(complaintsDto, metaInfo);
		this.complaintsDao.saveComplaintTechMapDtls(complaintTechMapDtls);
		String techName = complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() +  " " +complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName() + " (" + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber() + ")";
		String statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.COMPLAINT_ASSIGNED_SUUCESSFULLY.getMessage()) + " " + techName;
		this.sendNotificationsAboutComplaintAssign(complaintTechMapDtls);
		return statusMessage;
		
	}
	
	private RlmsComplaintTechMapDtls constructComplaintTechMapDtlsDto(ComplaintsDto complaintsDto, UserMetaInfo metaInfo){
		RlmsComplaintTechMapDtls complaintTechMapDtls = new RlmsComplaintTechMapDtls();
		RlmsComplaintMaster complaintMaster = this.complaintsDao.getComplaintMasterObj(complaintsDto.getComplaintId());
		RlmsUserRoles userRoles = this.userService.getUserRoleObjhById(complaintsDto.getUserRoleId());
		complaintTechMapDtls.setActiveFlag(RLMSConstants.ACTIVE.getId());
		complaintTechMapDtls.setAssignedDate(new Date());
		complaintTechMapDtls.setComplaintMaster(complaintMaster);
		complaintTechMapDtls.setCreatedBy(metaInfo.getUserId());
		complaintTechMapDtls.setCreatedDate(new Date());
		complaintTechMapDtls.setRemark(complaintsDto.getRemark());
		complaintTechMapDtls.setStatus(Status.PENDING_FOR_APPROVAL.getStatusId());
		complaintTechMapDtls.setUpdatedBy(metaInfo.getUserId());
		complaintTechMapDtls.setUpdatedDate(new Date());
		complaintTechMapDtls.setUserRoles(userRoles);
		return complaintTechMapDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getAllLiftsForBranchsOrCustomer(LiftDtlsDto dto){
		List<LiftDtlsDto> listOfLiftDtls = new ArrayList<LiftDtlsDto>();
		List<RlmsLiftCustomerMap> listOfLifts = this.liftDao.getAllLiftsForBranchsOrCustomer(dto);
		for (RlmsLiftCustomerMap rlmsLiftCustomerMap : listOfLifts) {
			LiftDtlsDto lift = new LiftDtlsDto();
			lift.setLiftId(rlmsLiftCustomerMap.getLiftCustomerMapId());
			lift.setLiftNumber(rlmsLiftCustomerMap.getLiftMaster().getLiftNumber());
			listOfLiftDtls.add(lift);
		}
		return listOfLiftDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserAppDtls> getRegIdsOfAllApplicableUsers(Integer liftCustomerMapId){
		List<UserAppDtls> listOfAllApplicableDtls = new ArrayList<UserAppDtls>();
		RlmsLiftCustomerMap liftCustomerMap = this.liftDao.getLiftCustomerMapById(liftCustomerMapId);
		List<RlmsCustomerMemberMap> listOfAllMembers = this.customerService.getAllMembersForCustomer(liftCustomerMap.getBranchCustomerMap().getBranchCustoMapId());
		for (RlmsCustomerMemberMap rlmsCustomerMemberMap : listOfAllMembers) {
			listOfAllApplicableDtls.add(this.customerService.getUserAppDtls(rlmsCustomerMemberMap.getRlmsMemberMaster().getMemberId(), RLMSConstants.MEMBER_TYPE.getId()));
		}
		return listOfAllApplicableDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendNotificationsAboutComplaintRegistration(RlmsComplaintMaster complaintMaster){
		Map<String, String> dataPayload = new HashMap<String, String>();
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_TITLE, PropertyUtils.getPrpertyFromContext(RLMSMessages.COMPLAINT_REGISTERED.getMessage()) + " - " + complaintMaster.getTitle());
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, complaintMaster.getRemark());
		
		List<UserAppDtls> listOfUsers = this.getRegIdsOfAllApplicableUsers(complaintMaster.getLiftCustomerMap().getLiftCustomerMapId());
		
		for (UserAppDtls userAppDtls : listOfUsers) {
			try{
				log.debug("sendNotificationsAboutComplaintRegistration :: Sending notification");
				this.messagingService.sendNotification(userAppDtls.getAppRegId(), dataPayload);
				log.debug("sendNotificationsAboutComplaintRegistration :: Notification sent to Id :" + userAppDtls.getAppRegId());
			}catch(Exception e){
				log.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendNotificationsAboutComplaintAssign(RlmsComplaintTechMapDtls complaintTechMapDtls){
		this.notifyTechnician(complaintTechMapDtls);
		this.sendNotificationsToMembers(complaintTechMapDtls);
	}
	
	private void notifyTechnician(RlmsComplaintTechMapDtls complaintTechMapDtls){
		UserAppDtls userAppDtls = this.customerService.getUserAppDtls(complaintTechMapDtls.getUserRoles().getUserRoleId(), RLMSConstants.USER_ROLE_TYPE.getId());
		Map<String, String> dataPayload = new HashMap<String, String>();
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_TITLE, PropertyUtils.getPrpertyFromContext(RLMSMessages.COMPLAINT_REGISTERED.getMessage()) + " - " + complaintTechMapDtls.getComplaintMaster().getTitle());
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, complaintTechMapDtls.getComplaintMaster().getRemark());	
		
		try{
			log.debug("notifyTechnician :: Sending notification");
			this.messagingService.sendNotification(userAppDtls.getAppRegId(), dataPayload);
			log.debug("notifyTechnician :: Notification sent to Id :" + userAppDtls.getAppRegId());
		}catch(Exception e){
			log.error(ExceptionUtils.getFullStackTrace(e));
		}
		
	}
	private void sendNotificationsToMembers(RlmsComplaintTechMapDtls complaintTechMapDtls){
		Map<String, String> dataPayload = new HashMap<String, String>();
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_TITLE, PropertyUtils.getPrpertyFromContext(RLMSMessages.COMPLAINT_REGISTERED.getMessage()) + " - " + complaintTechMapDtls.getComplaintMaster().getTitle());
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_MESSAGE, complaintTechMapDtls.getComplaintMaster().getRemark());
		String techDtls = complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getFirstName() + " " + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getLastName() + " (" + complaintTechMapDtls.getUserRoles().getRlmsUserMaster().getContactNumber() + ")";
		dataPayload.put(Util.PAYLOAD_ATTRIBUTE_TECHNICIAN, techDtls);
		
		List<UserAppDtls> listOfUsers = this.getRegIdsOfAllApplicableUsers(complaintTechMapDtls.getComplaintMaster().getLiftCustomerMap().getLiftCustomerMapId());
		
		for (UserAppDtls userAppDtls : listOfUsers) {
			try{
				log.debug("sendNotificationsAboutComplaintRegistration :: Sending notification");
				this.messagingService.sendNotification(userAppDtls.getAppRegId(), dataPayload);
				log.debug("sendNotificationsAboutComplaintRegistration :: Notification sent to Id :" + userAppDtls.getAppRegId());
			}catch(Exception e){
				log.error(ExceptionUtils.getFullStackTrace(e));
			}
		}
	}
	
}
