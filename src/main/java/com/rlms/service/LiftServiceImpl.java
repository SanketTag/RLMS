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
import com.rlms.constants.SpocRoleConstants;
import com.rlms.constants.Status;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.BranchDao;
import com.rlms.dao.FyaDao;
import com.rlms.dao.LiftDao;
import com.rlms.dao.UserRoleDao;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsFyaTranDtls;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftMaster;
import com.rlms.model.RlmsUserRoles;
import com.rlms.utils.DateUtils;
import com.rlms.utils.PropertyUtils;

@Service("LiftService")
public class LiftServiceImpl implements LiftService{

	@Autowired
	private LiftDao liftDao;
	
	@Autowired
	private BranchDao branchDao;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private FyaDao fyaDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsLiftCustomerMap> getAllLiftsForBranch(Integer companyBranchMapId){
		List<RlmsLiftCustomerMap> liftsForBranch = new ArrayList<RlmsLiftCustomerMap>();
		List<Integer> listOfAllCustmers = new ArrayList<Integer>();
		List<RlmsBranchCustomerMap> listOfCustomersOfBranch = this.companyService.getAllCustomersOfBranch(companyBranchMapId);
		for (RlmsBranchCustomerMap rlmsBranchCustomerMap : listOfCustomersOfBranch) {
			listOfAllCustmers.add(rlmsBranchCustomerMap.getCustomerMaster().getCustomerId());
		}
		if(null != listOfAllCustmers && !listOfAllCustmers.isEmpty()){
			liftsForBranch =  this.liftDao.getAllLiftsForCustomers(listOfAllCustmers);
		}
		return liftsForBranch;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndAddNewLiftDtls(LiftDtlsDto dto, UserMetaInfo metaInfo){
		RlmsLiftMaster liftM = this.constructLiftMaster(dto, metaInfo);
		Integer liftId = this.liftDao.saveLiftM(liftM);
		liftM.setLiftId(liftId);
		
		
		RlmsLiftCustomerMap liftCustomerMap = this.constructLiftCustomerMap(liftM, dto, metaInfo);
		Integer liftCustomerMapID = this.liftDao.saveLiftCustomerMap(liftCustomerMap);
		liftCustomerMap.setLiftCustomerMapId(liftCustomerMapID);
		
		RlmsFyaTranDtls fyaTranDtls = this.constructFyaTranDtls(liftCustomerMap, metaInfo);
		this.fyaDao.saveFyaTranDtls(fyaTranDtls);
		return PropertyUtils.getPrpertyFromContext(RlmsErrorType.LIFT_ADDED_SUCCESSFULLY.getMessage());
		
	}
	
	
	private RlmsFyaTranDtls constructFyaTranDtls(RlmsLiftCustomerMap liftCustomerMap, UserMetaInfo metaInfo){
		RlmsFyaTranDtls fyaTranDtls = new RlmsFyaTranDtls();
		RlmsUserRoles userRole = this.userRoleDao.getUserRoleForCompany(RLMSConstants.INDITECH.getId(), SpocRoleConstants.INDITECH_ADMIN.getSpocRoleId());
		
		fyaTranDtls.setActiveFlag(RLMSConstants.ACTIVE.getId());
		fyaTranDtls.setFyaType(RLMSConstants.LIFT_TYPE.getId());
		fyaTranDtls.setInitiatedBy(metaInfo.getUserId());
		fyaTranDtls.setLiftCustomerMap(liftCustomerMap);
		fyaTranDtls.setPendingWith(userRole.getUserRoleId());
		fyaTranDtls.setStatus(Status.PENDING.getStatusId());
		fyaTranDtls.setCreatedBy(metaInfo.getUserId());
		fyaTranDtls.setCreatedDate(new Date());
		fyaTranDtls.setUpdatedDate(new Date());
		fyaTranDtls.setUdpatedBy(metaInfo.getUserId());
		return fyaTranDtls;
	}
	private RlmsLiftCustomerMap constructLiftCustomerMap(RlmsLiftMaster liftM, LiftDtlsDto dto, UserMetaInfo metaInfo){
		RlmsLiftCustomerMap liftCustomerMap =  new RlmsLiftCustomerMap();
		RlmsBranchCustomerMap branchCustomerMap = this.branchDao.getBranchCustomerMapDtls(dto.getBranchCustomerMapId());
		
		liftCustomerMap.setActiveFlag(RLMSConstants.INACTIVE.getId());
		liftCustomerMap.setBranchCustomerMap(branchCustomerMap);
		liftCustomerMap.setLiftMaster(liftM);
		liftCustomerMap.setUpdatedBy(metaInfo.getUserId());
		liftCustomerMap.setUpdatedDate(new Date());
		liftCustomerMap.setCreatedBy(metaInfo.getUserId());
		liftCustomerMap.setCreatedDate(new Date());
		return liftCustomerMap;	

	}
	
	private RlmsLiftMaster constructLiftMaster(LiftDtlsDto dto, UserMetaInfo metaInfo){
		RlmsLiftMaster liftM = new RlmsLiftMaster();
		liftM.setAccessControl(dto.getAccessControl());
		liftM.setActiveFlag(RLMSConstants.INACTIVE.getId());
		liftM.setAddress(dto.getAddress());
		liftM.setAlarm(dto.getAlarm());
		liftM.setAlarmBattery(dto.getAlarmBattery());
		liftM.setAmcAmount(dto.getAmcAmount());
		liftM.setAmcStartDate(dto.getAmcStartDate());
		liftM.setAmcType(dto.getAmcType());
		liftM.setARD(dto.getArd());
		liftM.setARDPhoto(dto.getArdPhoto());
		liftM.setAutoDoorHeaderPhoto(dto.getAutoDoorHeaderPhoto());
		liftM.setAutoDoorMake(dto.getAutoDoorMake());
		liftM.setBatteryCapacity(dto.getBatteryCapacity());
		liftM.setBatteryMake(dto.getBatteryMake());
		liftM.setBreakVoltage(dto.getBreakVoltage());
		liftM.setCartopPhoto(dto.getCartopPhoto());
		liftM.setCollectiveType(dto.getCollectiveType());
		liftM.setCOPMake(dto.getCopMake());
		liftM.setCOPPhoto(dto.getCopPhoto());		
		liftM.setDateOfInstallation(dto.getDateOfInstallation());
		liftM.setDoorType(dto.getDoorType());
		liftM.setEngineType(dto.getEngineType());
		liftM.setFireMode(dto.getFireMode());
		liftM.setIntercomm(dto.getIntercomm());
		liftM.setLatitude(dto.getLatitude());
		liftM.setLiftNumber(dto.getLiftNumber());
		liftM.setLobbyPhoto(dto.getLobbyPhoto());
		liftM.setLongitude(dto.getLongitude());
		liftM.setLOPMake(dto.getLopMake());
		liftM.setLOPPhoto(dto.getLopPhoto());
		liftM.setMachineCapacity(dto.getMachineCapacity());
		liftM.setMachineCurrent(dto.getMachineCurrent());
		liftM.setMachineMake(dto.getMachineMake());
		liftM.setMachinePhoto(dto.getMachinePhoto());
		liftM.setNoOfBatteries(dto.getNoOfBatteries());
		liftM.setNoOfStops(dto.getNoOfStops());
		liftM.setPanelMake(dto.getPanelMake());
		liftM.setPanelPhoto(dto.getPanelPhoto());
		liftM.setServiceEndDate(dto.getServiceEndDate());
		liftM.setServiceStartDate(dto.getServiceStartDate());
		liftM.setSimplexDuplex(dto.getSimplexDuplex());
		liftM.setUpdatedBy(metaInfo.getUserId());		
		liftM.setWiringShceme(dto.getWiringShceme());
		liftM.setStatus(Status.PENDING_FOR_APPROVAL.getStatusId());
		
		liftM.setUpdatedDate(new Date());
		liftM.setWiringPhoto(dto.getWiringPhoto());
		liftM.setCreatedBy(metaInfo.getUserId());
		liftM.setCreatedDate(new Date());
		
		return liftM;
		
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getLiftsToBeApproved(){
		List<RlmsLiftCustomerMap> listOfAllPendingLifts = this.liftDao.getAllLiftsToBeApproved();
		List<LiftDtlsDto> listOfDtos = new ArrayList<LiftDtlsDto>();
		for (RlmsLiftCustomerMap liftCustomerMap : listOfAllPendingLifts) {
			LiftDtlsDto dto = new LiftDtlsDto();
			RlmsFyaTranDtls fyaTranDtls = this.fyaDao.getFyaForLift(liftCustomerMap.getLiftCustomerMapId(), Status.PENDING.getStatusId());
			dto.setLiftNumber(liftCustomerMap.getLiftMaster().getLiftNumber());
			dto.setAddress(liftCustomerMap.getLiftMaster().getAddress());
			dto.setCustomerName(liftCustomerMap.getBranchCustomerMap().getCustomerMaster().getCustomerName());
			dto.setBranchName(liftCustomerMap.getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			dto.setLiftId(liftCustomerMap.getLiftMaster().getLiftId());
			dto.setLiftCustomerMapId(liftCustomerMap.getLiftCustomerMapId());
			dto.setFyaTranId(fyaTranDtls.getFyaTranId());
			listOfDtos.add(dto);
		}
		
		return listOfDtos;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String approveLift(LiftDtlsDto liftDtlsDto, UserMetaInfo metaInfo){
		RlmsLiftCustomerMap liftCustomerMap = this.liftDao.getLiftCustomerMapByLiftId(liftDtlsDto.getLiftId());
		
		liftCustomerMap.getLiftMaster().setActiveFlag(RLMSConstants.ACTIVE.getId());
		liftCustomerMap.getLiftMaster().setServiceStartDate(new Date());
		liftCustomerMap.setActiveFlag(RLMSConstants.ACTIVE.getId());
		this.liftDao.updateLiftCustomerMap(liftCustomerMap);
		
		RlmsFyaTranDtls fyaTranDtls = this.fyaDao.getFyaByFyaTranIDt(liftDtlsDto.getFyaTranId());
		fyaTranDtls.setStatus(Status.COMPLETED.getStatusId());
		this.fyaDao.updateFyaTranDtls(fyaTranDtls);
		
		return PropertyUtils.getPrpertyFromContext(RlmsErrorType.LIFT_APPROVED.getMessage());
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<LiftDtlsDto> getLiftDetailsForBranch(LiftDtlsDto liftDtlsDto, UserMetaInfo metaInfo){
		List<RlmsLiftCustomerMap> listOFAllLifts = this.liftDao.getAllLiftsForBranchs(liftDtlsDto.getBranchCompanyMapId());
		List<LiftDtlsDto> listOfAllDtos = new ArrayList<LiftDtlsDto>();
		for (RlmsLiftCustomerMap liftCustomerMap : listOFAllLifts) {
			RlmsLiftMaster liftM = liftCustomerMap.getLiftMaster();
			LiftDtlsDto dto = new LiftDtlsDto();
			dto.setAccessControl(liftM.getAccessControl());
			dto.setAddress(liftM.getAddress());
			dto.setAlarm(liftM.getAlarm());
			dto.setAlarmBattery(liftM.getAlarmBattery());
			dto.setAmcAmount(liftM.getAmcAmount());
			dto.setAmcStartDate(liftM.getAmcStartDate());
			if(null != liftM.getAmcStartDate()){
				dto.setAmcStartDateStr(DateUtils.convertDateToStringWithoutTime(liftM.getAmcStartDate()));
			}
			dto.setAmcType(liftM.getAmcType());
			dto.setArd(liftM.getARD());
			dto.setArdPhoto(liftM.getARDPhoto());
			dto.setAutoDoorHeaderPhoto(liftM.getAutoDoorHeaderPhoto());
			dto.setBatteryCapacity(liftM.getBatteryCapacity());
			dto.setBatteryMake(liftM.getBatteryMake());
			dto.setBranchName(liftCustomerMap.getBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());;
			dto.setCustomerName(liftCustomerMap.getBranchCustomerMap().getCustomerMaster().getCustomerName());
			dto.setDateOfInstallation(liftM.getDateOfInstallation());
			if(null != liftM.getDateOfInstallation()){
				dto.setDateOfInstallationStr(DateUtils.convertDateToStringWithoutTime(liftM.getDateOfInstallation()));
			}
			dto.setLiftNumber(liftM.getLiftNumber());
			dto.setServiceStartDate(liftM.getServiceStartDate());
			if(null != liftM.getServiceStartDate()){
				dto.setServiceStartDateStr(DateUtils.convertDateToStringWithoutTime(liftM.getServiceStartDate()));
			}
			dto.setServiceEndDate(liftM.getServiceEndDate());
			if(null != liftM.getServiceEndDate()){
				dto.setServiceEndDateStr(DateUtils.convertDateToStringWithoutTime(liftM.getServiceEndDate()));
			}
			listOfAllDtos.add(dto);
		}
		
		return listOfAllDtos;
		
	}
	
}
