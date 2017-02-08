package com.rlms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.Status;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.ComplaintsDao;
import com.rlms.dao.LiftDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsComplaintTechMapDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.utils.PropertyUtils;

@Service("complaaintsService")
public class ComplaintsServiceImpl implements ComplaintsService{

	@Autowired
	private ComplaintsDao complaintsDao;
	
	@Autowired
	private LiftDao liftDao;
	
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
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDto> getListOfComplaints(ComplaintsDtlsDto dto){
		List<ComplaintsDto> listOfAllComplaints = new ArrayList<ComplaintsDto>();
		List<RlmsComplaintMaster> listOfComplaints = this.complaintsDao.getAllComplaintsForBranch(dto.getBranchCompanyMapId(), dto.getLiftCustomerMapId(), dto.getStatusList());
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
		
		dto.setComplaintNumber(complaintMaster.getComplaintNumber());
		dto.setCustomerName(complaintMaster.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
		dto.setLiftAddress(complaintMaster.getLiftCustomerMap().getLiftMaster().getAddress());
		dto.setRegistrationDate(complaintMaster.getRegistrationDate());
		dto.setActualServiceEndDate(complaintMaster.getActualServiceEndDate());
		dto.setRemark(complaintMaster.getRemark());
		dto.setTitle(complaintMaster.getTitle());
		dto.setServiceStartDate(complaintMaster.getServiceStartDate());
		
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
}
