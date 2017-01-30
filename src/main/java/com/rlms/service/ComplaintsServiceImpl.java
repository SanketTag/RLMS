package com.rlms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.Status;
import com.rlms.contract.ComplaintsDto;
import com.rlms.dao.ComplaintsDao;
import com.rlms.model.RlmsComplaintTechMapDtls;

@Service("complaaintsService")
public class ComplaintsServiceImpl implements ComplaintsService{

	@Autowired
	private ComplaintsDao complaintsDao;
	
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
}
