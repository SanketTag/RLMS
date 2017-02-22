package com.rlms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.fabric.xmlrpc.base.Array;
import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.AddNewUserDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.BranchDao;
import com.rlms.dao.CustomerDao;
import com.rlms.dao.LiftDao;
import com.rlms.dao.UserRoleDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsCustomerMaster;
import com.rlms.model.RlmsCustomerMemberMap;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftMaster;
import com.rlms.model.RlmsMemberMaster;
import com.rlms.model.RlmsUserApplicationMapDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.model.RlmsUsersMaster;
import com.rlms.utils.PropertyUtils;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private BranchDao branchDao;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private LiftDao liftDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	
	public RlmsCustomerMaster getCustomerByEmailId(String emailId){
		return this.customerDao.getCustomerByEmailId(emailId);
	}
	
	
	private boolean validateCustomerDtls(CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo) throws ValidationException{
		boolean isValidUser = true;
		
		RlmsCustomerMaster customerMaster = this.customerDao.getCustomerByEmailId(customerDtlsDto.getEmailID());
		if(null != customerMaster){
			isValidUser = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.CUSTOMER_ALREADY_ADDED.getMessage()));
		}else{
		
			RlmsCompanyBranchMapDtls companyBranchMapDtls = this.branchDao.getCompanyBranchMapDtls(customerDtlsDto.getBranchCompanyMapId());
			
			if(null == companyBranchMapDtls){
				isValidUser = false;
				throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.INVALID_BRANCH_NAME.getMessage()));
			}
		}
		
		return isValidUser;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndRegisterNewCustomer(CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo) throws ValidationException{
		String statusMessage = "";
		if(this.validateCustomerDtls(customerDtlsDto, metaInfo)){
			RlmsCustomerMaster  customerMaster = this.constructCustomerObj(customerDtlsDto, metaInfo);
			Integer customerId = this.customerDao.saveCustomerM(customerMaster);
			customerMaster.setCustomerId(customerId);
			
			RlmsBranchCustomerMap branchCustomerMap = this.constructBranchCustMap(customerMaster, customerDtlsDto, metaInfo);
			this.branchDao.saveBranchCustomerMapDtls(branchCustomerMap);
			
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.USER_REG_SUCCESFUL.getMessage());
		}
		return statusMessage;
		
	}
	
	private RlmsBranchCustomerMap constructBranchCustMap(RlmsCustomerMaster customerMaster, CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo){
		RlmsBranchCustomerMap branchCustomerMap =  new RlmsBranchCustomerMap();
		RlmsCompanyBranchMapDtls companyBranchMapDtls = this.companyService.getCompanyBranchMapById(customerDtlsDto.getBranchCompanyMapId());
		branchCustomerMap.setActiveFlag(RLMSConstants.ACTIVE.getId());
		branchCustomerMap.setCompanyBranchMapDtls(companyBranchMapDtls);
		branchCustomerMap.setCustomerMaster(customerMaster);
		branchCustomerMap.setCreatedBy(metaInfo.getUserId());
		branchCustomerMap.setCreatedDate(new Date());
		branchCustomerMap.setUpdatedBy(metaInfo.getUserId());
		branchCustomerMap.setUpdatedDate(new Date());
		return branchCustomerMap;

	}
	private RlmsCustomerMaster constructCustomerObj(CustomerDtlsDto customerDtlsDto, UserMetaInfo metaInfo){
		RlmsCustomerMaster customerMaster = new RlmsCustomerMaster();
		customerMaster.setActiveFlag(RLMSConstants.ACTIVE.getId());
		customerMaster.setAddress(customerDtlsDto.getAddress());
		customerMaster.setCntNumber(customerDtlsDto.getCntNumber());
		customerMaster.setCustomerName(customerDtlsDto.getFirstName());
		customerMaster.setCustomerType(customerDtlsDto.getCustomerType());
		customerMaster.setEmailID(customerDtlsDto.getEmailID());
		customerMaster.setPanNumber(customerDtlsDto.getPanNumber());
		customerMaster.setTinNumber(customerDtlsDto.getTinNumber());
		customerMaster.setVatNumber(customerDtlsDto.getVatNumber());
		customerMaster.setCreatedBy(metaInfo.getUserId());
		customerMaster.setCreatedDate(new Date());
		customerMaster.setUpdatedBy(metaInfo.getUserId());
		customerMaster.setUpdatedDate(new Date());
		return customerMaster;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDtlsDto> getAllApplicableCustomers(CustomerDtlsDto dto, UserMetaInfo metaInfo){
		//List<Integer> listOfApplicableBranchIds = this.companyService.getListOfApplicableBranch(metaInfo.getUserRole().getUserRoleId(), metaInfo);
		List<Integer> listOfApplicableBranchIds = new ArrayList<Integer>();
		listOfApplicableBranchIds.add(dto.getBranchCompanyMapId());
		List<RlmsBranchCustomerMap> listOfAllCustomers = this.customerDao.getAllCustomersForBranches(listOfApplicableBranchIds);
		return this.constructListOfCustomerDtlsDto(listOfAllCustomers);
	}
	
	private List<CustomerDtlsDto> constructListOfCustomerDtlsDto(List<RlmsBranchCustomerMap> listOfCustomers){
		List<CustomerDtlsDto> listOFDtos = new ArrayList<CustomerDtlsDto>();
		for (RlmsBranchCustomerMap branchCustomerMap : listOfCustomers) {
			List<Integer> listOfCustomer = new ArrayList<Integer>();
			listOfCustomer.add(branchCustomerMap.getCustomerMaster().getCustomerId());
			CustomerDtlsDto dto = new CustomerDtlsDto();
			List<RlmsLiftCustomerMap> listOfLifts = this.liftDao.getAllLiftsForCustomers(listOfCustomer);
			dto.setAddress(branchCustomerMap.getCustomerMaster().getAddress());
			dto.setCntNumber(branchCustomerMap.getCustomerMaster().getCntNumber());
			dto.setFirstName(branchCustomerMap.getCustomerMaster().getCustomerName());
			dto.setEmailID(branchCustomerMap.getCustomerMaster().getEmailID());
			dto.setPanNumber(branchCustomerMap.getCustomerMaster().getPanNumber());
			if(null != listOfLifts && !listOfLifts.isEmpty()){
				dto.setTotalNumberOfLifts(listOfLifts.size());
			}
			dto.setBranchName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			dto.setCompanyName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			listOFDtos.add(dto);
		}
		return listOFDtos;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<CustomerDtlsDto> getAllCustomersForBranch(CustomerDtlsDto dto, UserMetaInfo metaInfo){
		List<CustomerDtlsDto> listOFDtos = new ArrayList<CustomerDtlsDto>();
		List<Integer> listOfApplicableBranchIds = new ArrayList<Integer>();
		listOfApplicableBranchIds.add(dto.getBranchCompanyMapId());
		List<RlmsBranchCustomerMap> listOfAllCustomers = this.customerDao.getAllCustomersForBranches(listOfApplicableBranchIds);
		for (RlmsBranchCustomerMap rlmsBranchCustomerMap : listOfAllCustomers) {
			CustomerDtlsDto customerDtlsDto = new CustomerDtlsDto();
			customerDtlsDto.setBranchCustomerMapId(rlmsBranchCustomerMap.getBranchCustoMapId());
			customerDtlsDto.setFirstName(rlmsBranchCustomerMap.getCustomerMaster().getCustomerName());
			listOFDtos.add(customerDtlsDto);
		}
		return listOFDtos;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndRegisterNewMember(MemberDtlsDto memberDtlsDto, UserMetaInfo metaInfo) throws ValidationException{
		String statusMessage = "";
		if(this.validateMemberDtls(memberDtlsDto)){
			RlmsMemberMaster  memberMaster = this.constructMemberMaster(memberDtlsDto, metaInfo);
			Integer memberID = this.customerDao.saveMemberM(memberMaster);
			memberMaster.setMemberId(memberID);
			
			RlmsCustomerMemberMap customerMemberMap = this.constructCustoMemberMap(memberMaster, memberDtlsDto, metaInfo);
			this.customerDao.saveCustomerMemberMap(customerMemberMap);
			
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.MEMBER_REG_SUCCESSFUL.getMessage());
		}
		return statusMessage;
		
	}
	
	private boolean validateMemberDtls(MemberDtlsDto memberDtlsDto) throws ValidationException{
		boolean isValidMember = true;
		RlmsMemberMaster memberMaster = this.customerDao.getMemberByCntNo(memberDtlsDto.getContactNumber());
		if(null != memberMaster){
			isValidMember = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.MEMBER_WITH_SAME_CONTACT_NO.getMessage()));
		}
		
		return isValidMember;
	}
	
	private RlmsMemberMaster constructMemberMaster(MemberDtlsDto memberDtlsDto, UserMetaInfo metaInfo){
	
		RlmsMemberMaster memberMaster = new RlmsMemberMaster();
		memberMaster.setActiveFlag(RLMSConstants.ACTIVE.getId());
		memberMaster.setAddress(memberDtlsDto.getAddress());
		memberMaster.setContactNumber(memberDtlsDto.getContactNumber());
		memberMaster.setEmailId(memberDtlsDto.getEmailId());
		memberMaster.setFirstName(memberDtlsDto.getFirstName());
		memberMaster.setLastName(memberDtlsDto.getLastName());
		memberMaster.setUpdatedBy(metaInfo.getUserId());
		memberMaster.setUpdatedDate(new Date());
		memberMaster.setCreatedBy(metaInfo.getUserId());
		memberMaster.setCreatedDate(new Date());
		memberMaster.setRegistrationDate(new Date());
		return memberMaster;
	}
	
	private RlmsCustomerMemberMap constructCustoMemberMap(RlmsMemberMaster memberMaster, MemberDtlsDto memberDtlsDto, UserMetaInfo metaInfo){
		RlmsCustomerMemberMap customerMemberMap =  new RlmsCustomerMemberMap();
		RlmsBranchCustomerMap branchCustomerMap = this.branchDao.getBranchCustomerMapDtls(memberDtlsDto.getBranchCustoMapId());
		customerMemberMap.setActiveFlag(RLMSConstants.ACTIVE.getId());
		customerMemberMap.setRlmsBranchCustomerMap(branchCustomerMap);
		customerMemberMap.setRlmsMemberMaster(memberMaster);
		customerMemberMap.setCreatedBy(metaInfo.getUserId());
		customerMemberMap.setCreatedDate(new Date());
		customerMemberMap.setUpdatedBy(metaInfo.getUserId());
		customerMemberMap.setUpdatedDate(new Date());
		return customerMemberMap;

	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public MemberDtlsDto registerMemeberDeviceByMblNo(MemberDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException{
		RlmsMemberMaster memberMaster = this.customerDao.getMemberByCntNo(dto.getContactNumber());
		if(null == memberMaster){
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.INVALID_CONTACT_NUMBER.getMessage()));
		}
		this.registerUserDevice(dto, memberMaster, metaInfo);
		return this.constructMemberDltsSto(memberMaster);
		
	}
	
	
	private void registerUserDevice(MemberDtlsDto dto, RlmsMemberMaster memberMaster, UserMetaInfo metaInfo){
		RlmsUserApplicationMapDtls userApplicationMapDtls = this.constructUserAppMapDtls(dto, memberMaster, metaInfo);
		this.userRoleDao.saveUserAppDlts(userApplicationMapDtls);
	}
	
	private RlmsUserApplicationMapDtls constructUserAppMapDtls(MemberDtlsDto dto, RlmsMemberMaster memberMaster, UserMetaInfo metaInfo){
		RlmsUserApplicationMapDtls userApplicationMapDtls = new RlmsUserApplicationMapDtls();
		userApplicationMapDtls.setActiveFlag(RLMSConstants.ACTIVE.getId());
		userApplicationMapDtls.setAppRegId(dto.getAppRegId());
		userApplicationMapDtls.setLatitude(dto.getLatitude());
		userApplicationMapDtls.setLongitude(dto.getLongitude());
		userApplicationMapDtls.setUserId(memberMaster.getMemberId());
		userApplicationMapDtls.setUserRefType(RLMSConstants.MEMBER_TYPE.getId());
		userApplicationMapDtls.setCreatedBy(metaInfo.getUserId());
		userApplicationMapDtls.setCreatedDate(new Date());
		userApplicationMapDtls.setUpdatedDate(new Date());
		userApplicationMapDtls.setUpdatedBy(metaInfo.getUserId());
		return userApplicationMapDtls;
	}
	
	private MemberDtlsDto constructMemberDltsSto(RlmsMemberMaster memberMaster){
		
		List<RlmsCustomerMemberMap> listOFAllCustomers = this.customerDao.getAllCustomersForMember(memberMaster.getMemberId());
		MemberDtlsDto memberDtlsDto = new MemberDtlsDto();
		memberDtlsDto.setFirstName(memberMaster.getFirstName());
		memberDtlsDto.setLastName(memberMaster.getLastName());
		memberDtlsDto.setContactNumber(memberMaster.getContactNumber());
		memberDtlsDto.setListOfCustomerDtls(this.constructCustoDtlsDto(listOFAllCustomers));
		return memberDtlsDto;
		
	}
	
	private List<CustomerDtlsDto> constructCustoDtlsDto(List<RlmsCustomerMemberMap> listOfAllCustomers){
		List<CustomerDtlsDto> listOfCusoDtls = new ArrayList<CustomerDtlsDto>();
		for (RlmsCustomerMemberMap customerMemberMap : listOfAllCustomers) {
			RlmsBranchCustomerMap branchCustomerMap = this.customerDao.getBranchCustomerMapByCustoId(customerMemberMap.getRlmsBranchCustomerMap().getCustomerMaster().getCustomerId());
			CustomerDtlsDto customerDtlsDto =  new CustomerDtlsDto();
			customerDtlsDto.setBranchCompanyMapId(branchCustomerMap.getBranchCustoMapId());
			customerDtlsDto.setBranchName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			customerDtlsDto.setCustomerName(branchCustomerMap.getCustomerMaster().getCustomerName());
			customerDtlsDto.setCompanyName(branchCustomerMap.getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			listOfCusoDtls.add(customerDtlsDto);
		}
		return listOfCusoDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<MemberDtlsDto> getListOfAllMemberDtls(MemberDtlsDto memberDtlsDto){
		List<RlmsCustomerMemberMap> listOfAllMembers = this.customerDao.getAllMembersForCustomer(memberDtlsDto.getBranchCustoMapId());
		List<MemberDtlsDto> listOfMemberDtls= new ArrayList<MemberDtlsDto>();
		for (RlmsCustomerMemberMap rlmsCustomerMemberMap : listOfAllMembers) {
			MemberDtlsDto dto = new MemberDtlsDto();
			dto.setBranchName(rlmsCustomerMemberMap.getRlmsBranchCustomerMap().getCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
			dto.setCompanyName(rlmsCustomerMemberMap.getRlmsBranchCustomerMap().getCompanyBranchMapDtls().getRlmsCompanyMaster().getCompanyName());
			dto.setCustomerName(rlmsCustomerMemberMap.getRlmsBranchCustomerMap().getCustomerMaster().getCustomerName());
			dto.setContactNumber(rlmsCustomerMemberMap.getRlmsMemberMaster().getContactNumber());
			dto.setFirstName(rlmsCustomerMemberMap.getRlmsMemberMaster().getFirstName());
			dto.setLastName(rlmsCustomerMemberMap.getRlmsMemberMaster().getLastName());
			dto.setRegistrationDate(rlmsCustomerMemberMap.getRlmsMemberMaster().getRegistrationDate());
			listOfMemberDtls.add(dto);
		}
		return listOfMemberDtls;
	}
}

