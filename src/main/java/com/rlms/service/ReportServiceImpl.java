package com.rlms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.Status;
import com.rlms.contract.AMCDetailsDto;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.dao.BranchDao;
import com.rlms.dao.LiftDao;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsComplaintMaster;
import com.rlms.model.RlmsLiftAmcDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.predicates.LiftPredicate;
import com.rlms.utils.DateUtils;

@Service("ReportService")
public class ReportServiceImpl implements ReportService {

	@Autowired
	private BranchDao branchDao;
	
	@Audited
	private LiftDao liftDao;
	
	@Audited
	private ComplaintsService complaintService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<AMCDetailsDto> getAMCDetailsForLifts(AMCDetailsDto dto){
		List<AMCDetailsDto> listOFAMCDetails = new ArrayList<AMCDetailsDto>();
		List<Integer> listOFAllCustomersForBranch = new ArrayList<Integer>();
		List<Integer> listOfLiftsForAMCDtls = new ArrayList<Integer>();
		List<RlmsLiftCustomerMap> listOFApplicableLifts = new ArrayList<RlmsLiftCustomerMap>();
		if(null == dto.getListOfBranchCustomerMapId()){
			List<RlmsBranchCustomerMap> listOFAllCustoOfBranch = this.branchDao.getAllCustomersOfBranch(dto.getBranchCompanyMapId());
			for (RlmsBranchCustomerMap rlmsBranchCustomerMap : listOFAllCustoOfBranch) {
				listOFAllCustomersForBranch.add(rlmsBranchCustomerMap.getBranchCustoMapId());
			}			
		}
		
		if(null != dto.getListOfBranchCustomerMapId() && !dto.getListOfBranchCustomerMapId().isEmpty()){
			listOFApplicableLifts = this.liftDao.getAllLiftsForCustomres(dto.getListOfBranchCustomerMapId());
		}else{
			 listOFApplicableLifts = this.liftDao.getAllLiftsForCustomres(listOFAllCustomersForBranch);
		}
		
		for (RlmsLiftCustomerMap rlmsLiftCustomerMap : listOFApplicableLifts) {
			listOfLiftsForAMCDtls.add(rlmsLiftCustomerMap.getLiftCustomerMapId());
		}
		
		List<RlmsLiftAmcDtls> listOfAMCDtls = this.liftDao.getAMCDetilsForLifts(listOfLiftsForAMCDtls, dto);
		Set<Integer> liftIds = new HashSet<Integer>();
		for (RlmsLiftAmcDtls liftAmcDtls : listOfAMCDtls) {
			liftIds.add(liftAmcDtls.getLiftCustomerMap().getLiftMaster().getLiftId());
		}
		
		for (Integer liftId : liftIds) {
			List<RlmsLiftAmcDtls> listForLift = new ArrayList<RlmsLiftAmcDtls>(listOfAMCDtls);
			CollectionUtils.filter(listForLift, new LiftPredicate(liftId));
			listOFAMCDetails.addAll(this.constructListOFAMcDtos(listOfAMCDtls));
		}
		
		
		
		return listOFAMCDetails;
	}
	
	private List<AMCDetailsDto> constructListOFAMcDtos(List<RlmsLiftAmcDtls> listOFAMCs){
		List<AMCDetailsDto> listOFDtos = new ArrayList<AMCDetailsDto>();
		int i = 0;
		for (RlmsLiftAmcDtls liftAmcDtls : listOFAMCs) {
			AMCDetailsDto dto  = new AMCDetailsDto();
			if(null != liftAmcDtls.getAmcEndDate()){
				dto.setAmcEndDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcEndDate()));
			}
			if(null != liftAmcDtls.getAmcStartDate()){
				dto.setAmcStartDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcStartDate()));
			}
			
			dto.setCustomerName(liftAmcDtls.getLiftCustomerMap().getBranchCustomerMap().getCustomerMaster().getCustomerName());
			if(null != liftAmcDtls.getAmcDueDate()){
				dto.setDueDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcDueDate()));
			}
			
			if(null != liftAmcDtls.getAmcSlackStartDate()){
				dto.setLackEndDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcSlackStartDate()));
			}
			
			if(null != liftAmcDtls.getAmcSlackEndDate()){
				dto.setLackEndDate(DateUtils.convertDateToStringWithoutTime(liftAmcDtls.getAmcSlackEndDate()));
			}
			
			dto.setLiftNumber(liftAmcDtls.getLiftCustomerMap().getLiftMaster().getLiftNumber());
			dto.setStatus(this.calculateAMCStatus(liftAmcDtls.getLiftCustomerMap().getLiftMaster().getAmcStartDate(), liftAmcDtls.getLiftCustomerMap().getLiftMaster().getAmcEndDate(), liftAmcDtls.getLiftCustomerMap().getLiftMaster().getDateOfInstallation()));
			dto.setAmcAmount(liftAmcDtls.getLiftCustomerMap().getLiftMaster().getAmcAmount());
			
			if(i > 0 ){
				
				Integer diffInDays = DateUtils.daysBetween(listOFAMCs.get(i).getAmcStartDate(), listOFAMCs.get(i - 1).getAmcEndDate());
				if(diffInDays > 0){
					Date slackStartDate = DateUtils.addDaysToDate(listOFAMCs.get(i - 1).getAmcEndDate(), 1);
					Date slackEndDate = DateUtils.addDaysToDate(listOFAMCs.get(i).getAmcStartDate(), -1);
					if(null != slackStartDate &&  null != slackEndDate){
						{
							dto.setSlackStartDate(DateUtils.convertDateToStringWithoutTime(slackStartDate));
							dto.setSlackEndDate(DateUtils.convertDateToStringWithoutTime(slackEndDate));
							dto.setSlackperiod(diffInDays);
						}
					}
				}
			}
			
			listOFDtos.add(dto);
			i++;
		}
		return listOFDtos;
	}
	
	private String calculateAMCStatus(Date amcStartDate, Date amcEndDate, Date dateOfInstallation){
		String amcStatus = null;
		Date today = new Date();
		Date warrantyexpiryDate = DateUtils.addDaysToDate(dateOfInstallation, 365);
		Date renewalDate = DateUtils.addDaysToDate(amcEndDate, -30);
		if(DateUtils.isBeforeOrEqualToDate(amcEndDate, warrantyexpiryDate)){
			amcStatus = Status.UNDER_WARRANTY.getStatusMsg();
		}else if(DateUtils.isAfterOrEqualTo(renewalDate, today) && DateUtils.isBeforeOrEqualToDate(today, amcEndDate)){
			amcStatus = Status.RENEWAL_DUE.getStatusMsg();
		}else if(DateUtils.isAfterToDate(amcEndDate, today)){
			amcStatus = Status.AMC_PENDING.getStatusMsg();
		}else if(DateUtils.isBeforeOrEqualToDate(amcStartDate, today) &&  DateUtils.isAfterOrEqualTo(today, amcEndDate)){
			amcStatus = Status.UNDER_AMC.getStatusMsg();
		}
		return amcStatus;
		
	}
	
	/*@Transactional(propagation = Propagation.REQUIRED)
	public List<ComplaintsDtlsDto> generateComplaintsReport(ComplaintsDtlsDto dto){
		List<ComplaintsDtlsDto> listOfAllComplaints = new ArrayList<ComplaintsDtlsDto>();
		List<RlmsComplaintMaster> listOfComplaints = this.complaintService.getAllComplaintsForGivenCriteria(dto);
		for (RlmsComplaintMaster rlmsComplaintMaster : listOfComplaints) {
			ComplaintsDtlsDto dto =  new ComplaintsDtlsDto();
			
		}
		
	}*/
}
