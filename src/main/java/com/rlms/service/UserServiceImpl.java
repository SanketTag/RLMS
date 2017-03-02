package com.rlms.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RlmsErrorType;
import com.rlms.constants.SpocRoleConstants;
import com.rlms.contract.AddNewUserDto;
import com.rlms.contract.MemberDtlsDto;
import com.rlms.contract.RegisterDto;
import com.rlms.contract.UserDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.dao.UserMasterDao;
import com.rlms.dao.UserRoleDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsCompanyRoleMap;
import com.rlms.model.RlmsCustomerMemberMap;
import com.rlms.model.RlmsMemberMaster;
import com.rlms.model.RlmsSpocRoleMaster;
import com.rlms.model.RlmsUserApplicationMapDtls;
import com.rlms.model.RlmsUserRoles;
import com.rlms.model.RlmsUsersMaster;
import com.rlms.model.User;
import com.rlms.utils.PropertyUtils;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
	
	private static final AtomicLong counter = new AtomicLong();
	
	private static List<User> users;
	
	@Autowired
	private UserMasterDao userMasterDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private MessagingService messagingService;
	
	static{
		users= populateDummyUsers();
	}

	public List<User> findAllUsers() {
		return users;
	}
	
	public User findById(long id) {
		for(User user : users){
			if(user.getId() == id){
				return user;
			}
		}
		return null;
	}
	
	public User findByName(String name) {
		for(User user : users){
			if(user.getUsername().equalsIgnoreCase(name)){
				return user;
			}
		}
		return null;
	}
	
	public void saveUser(User user) {
		user.setId(counter.incrementAndGet());
		users.add(user);
	}

	public void updateUser(User user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}

	public void deleteUserById(long id) {
		
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext(); ) {
		    User user = iterator.next();
		    if (user.getId() == id) {
		        iterator.remove();
		    }
		}
	}

	public boolean isUserExist(User user) {
		return findByName(user.getUsername())!=null;
	}
	
	public void deleteAllUsers(){
		users.clear();
	}

	private static List<User> populateDummyUsers(){
		List<User> users = new ArrayList<User>();
		users.add(new User(counter.incrementAndGet(),"Sam", "NY", "sam@abc.com"));
		users.add(new User(counter.incrementAndGet(),"Tomy", "ALBAMA", "tomy@abc.com"));
		users.add(new User(counter.incrementAndGet(),"Kelly", "NEBRASKA", "kelly@abc.com"));
		return users;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RlmsUserRoles getUserByUserName(String userName){
		return this.userRoleDao.getUserByUserName(userName);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RlmsUserRoles getUserRoleObj(Integer userID, String userName, String password){
		return this.userRoleDao.getUserRoleObj(userID, userName, password);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsSpocRoleMaster> getAllRoles(UserMetaInfo metaInfo){
		return this.userRoleDao.getAllRoles(metaInfo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserDtlsDto> getAllUsersForCompany(Integer companyId){
		List<UserDtlsDto> listOfUserDtls = new ArrayList<UserDtlsDto>();
		 List<RlmsUsersMaster> listOfAllUsers = this.userMasterDao.getAllUsersForCompany(companyId);
		 Iterator<RlmsUsersMaster> it = listOfAllUsers.iterator();
		 while(it.hasNext()){
			 RlmsUsersMaster userMaster = it.next();
			 RlmsUserRoles role = this.userRoleDao.getUserRoleByUserId(userMaster.getUserId());
			 if(null != role){
				 it.remove();
			 }
		 }
		 for (RlmsUsersMaster rlmsUsersMaster : listOfAllUsers) {
			 UserDtlsDto dto = new UserDtlsDto();
			 dto.setAddress(rlmsUsersMaster.getAddress());
			 dto.setArea(rlmsUsersMaster.getArea());
			 dto.setCity(rlmsUsersMaster.getCity());
			 dto.setPinCode(rlmsUsersMaster.getPincode());
			 dto.setCompanyName(rlmsUsersMaster.getRlmsCompanyMaster().getCompanyName());
			 dto.setContactNumber(rlmsUsersMaster.getContactNumber());
			 dto.setEmailId(rlmsUsersMaster.getEmailId());
			 dto.setUserName(rlmsUsersMaster.getFirstName() + " " + rlmsUsersMaster.getLastName());
			 dto.setUserId(rlmsUsersMaster.getUserId());
			 listOfUserDtls.add(dto);
		}
		 return listOfUserDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserDtlsDto> getAllRegisteredUsers(Integer compamyId, UserMetaInfo metaInfo){
		List<UserDtlsDto> listOfUserDtls = new ArrayList<UserDtlsDto>();
			List<RlmsUsersMaster> listOfAllUsers = this.userMasterDao.getAllUsersForCompany(compamyId);
			 
			 for (RlmsUsersMaster rlmsUsersMaster : listOfAllUsers) {
				 UserDtlsDto dto = new UserDtlsDto();
				 dto.setAddress(rlmsUsersMaster.getAddress());
				 dto.setCompanyName(rlmsUsersMaster.getRlmsCompanyMaster().getCompanyName());
				 dto.setContactNumber(rlmsUsersMaster.getContactNumber());
				 dto.setEmailId(rlmsUsersMaster.getEmailId());
				 dto.setUserName(rlmsUsersMaster.getFirstName() + " " + rlmsUsersMaster.getLastName());
				 dto.setCity(rlmsUsersMaster.getCity());
				 dto.setArea(rlmsUsersMaster.getArea());
				 dto.setPinCode(rlmsUsersMaster.getPincode());
				 
				 RlmsUserRoles userRoles = this.userRoleDao.getUserRoleByUserId(rlmsUsersMaster.getUserId());
				 if(null != userRoles){
					 dto.setUserRoleName(userRoles.getRole());
					 if(null != userRoles.getRlmsCompanyBranchMapDtls()){
						dto.setBranchName(userRoles.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName()); 
					 }else{
						 dto.setBranchName(RLMSConstants.NA.getName());
					 }
				 }else{
					 dto.setUserRoleName(RLMSConstants.NA.getName());
					 dto.setBranchName(RLMSConstants.NA.getName());
				 }
				 
				 
				 listOfUserDtls.add(dto);
			}
		 return listOfUserDtls;
	}
	
	private RlmsUserRoles constructUserRole(UserRoleDtlsDTO userRoleDtlsDTO, UserMetaInfo metaInfo){
		
		RlmsCompanyMaster companyMaster = this.companyService.getCompanyById(userRoleDtlsDTO.getCompanyId());
		
		RlmsSpocRoleMaster spocRoleMaster = this.userRoleDao.getSpocRoleById(userRoleDtlsDTO.getSpocRoleId());
		
		RlmsUsersMaster user = this.userMasterDao.getUserByUserId(userRoleDtlsDTO.getUserId());
		
		//RlmsCompanyRoleMap companyRoleMap = this.companyService.getCompanyRole(userRoleDtlsDTO.getCompanyId(), userRoleDtlsDTO.getSpocRoleId());
		
		RlmsCompanyBranchMapDtls companyBranchMapDtls = null;
		if(null != userRoleDtlsDTO.getCompanyBranchMapId()){
			companyBranchMapDtls = this.companyService.getCompanyBranchMapById(userRoleDtlsDTO.getCompanyBranchMapId());
		}
		
		RlmsUserRoles userRole = new RlmsUserRoles();
		userRole.setActiveFlag(RLMSConstants.INACTIVE.getId());
		userRole.setRlmsCompanyMaster(companyMaster);
		userRole.setRlmsSpocRoleMaster(spocRoleMaster);
	//	userRole.setRlmsCompanyRolMap(companyRoleMap);
		userRole.setRlmsUserMaster(user);
		userRole.setRole(spocRoleMaster.getRoleName());
		if(null != companyBranchMapDtls){
			userRole.setRlmsCompanyBranchMapDtls(companyBranchMapDtls);
		}
		userRole.setCreatedBy(metaInfo.getUserId());
		userRole.setCreatedDate(new Date());
		userRole.setUpdatedBy(metaInfo.getUserId());
		userRole.setUpdatedDate(new Date());
		
		return userRole;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndAssignRole(UserRoleDtlsDTO userRoleDtlsDTO, UserMetaInfo metaInfo) throws InvalidKeyException, Exception{
		String statusMessage = "";
		
		if(!this.checkIfActiveRoleIsAlreadyAssigned(userRoleDtlsDTO.getUserId())){
			RlmsUserRoles userRole = this.constructUserRole(userRoleDtlsDTO, metaInfo);
			Integer userRoleId = this.userRoleDao.saveUserRole(userRole);
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.ROLE_SUCCESSFULLY_ASSIGNED.getMessage());
			this.sendAssignedRoleMail(userRoleId, userRole);
		}else{
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.ROLE_ALREADY_GIVEN.getMessage());
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.ROLE_ALREADY_GIVEN.getMessage()));
		}
		
		return statusMessage;
	}
	
	private boolean checkIfActiveRoleIsAlreadyAssigned(Integer userId){
		boolean isRoleAlreadyPresent = false;
		RlmsUserRoles userRole = this.userRoleDao.getUserRoleByUserId(userId);
		if(null != userRole){
			isRoleAlreadyPresent = true;
		}
		
		return isRoleAlreadyPresent;
	}

	private void sendAssignedRoleMail(Integer userRoleId, RlmsUserRoles userRole) throws InvalidKeyException, Exception{
		String encryptedUserRoleId = this.messagingService.encrypt(userRoleId.toString());
		
		this.messagingService.sendAssgnRoleEmail(encryptedUserRoleId, userRole);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String registerUser(RegisterDto dto) throws InvalidKeyException, Exception{
		String statusMessage = "";
		String userRoleIdbyte = dto.getRegistrationId();
		Integer userroleId = Integer.valueOf(this.messagingService.decrypt(userRoleIdbyte));
		RlmsUserRoles userRole = this.userRoleDao.getUserRoleToRegister(userroleId);
		if(null != userRole && !this.isAlreadyHasActiveAccount(userRole)){
			RlmsUsersMaster userMaster = userRole.getRlmsUserMaster();
			userMaster.setUsername(dto.getUserName());
			userMaster.setPassword(dto.getPassword());
			this.userMasterDao.mergerUser(userMaster);
			
			//Make the user role Active
			userRole.setUsername(dto.getUserName());
			userRole.setActiveFlag(RLMSConstants.ACTIVE.getId());
			this.userMasterDao.mergerUserRole(userRole);
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.USER_REG_SUCCESFUL.getMessage());
		}else{
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.REGISTRATION_ID_INCORRECT.getMessage());
		}
		return statusMessage;
	}
	
	private boolean isAlreadyHasActiveAccount(RlmsUserRoles userRole){
		boolean isAlreadyHasActiveAccount = false;
		if(RLMSConstants.ACTIVE.getId().equals(userRole.getActiveFlag())){
			isAlreadyHasActiveAccount = true;
		}
		return isAlreadyHasActiveAccount;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String validateAndRegisterNewUser(AddNewUserDto userDto, UserMetaInfo metaInfo) throws ValidationException{
		String statusMessage = "";
		if(this.validateUserDtls(userDto, metaInfo)){
			RlmsUsersMaster userMaster = this.constructUserObj(userDto, metaInfo);
			this.userMasterDao.saveUser(userMaster);
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.USER_REG_SUCCESFUL.getMessage());
		}
		return statusMessage;
		
	}
	
	private boolean validateUserDtls(AddNewUserDto userDto, UserMetaInfo metaInfo) throws ValidationException{
		boolean isValidUser = true;
		
		RlmsUsersMaster user = this.userMasterDao.getUserByEmailID(userDto.getEmailId());
		if(null != user){
			isValidUser = false;
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.USER_ALREADY_REGISTERED.getMessage()));
		}else{
		
			RlmsCompanyMaster companyMaster = this.companyService.getCompanyById(userDto.getCompanyId());
			
			if(null == companyMaster){
				isValidUser = false;
				throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.INVALID_COMPANY_NAME.getMessage()));
			}
		}
		
		return isValidUser;
	}
	
	
	
	private RlmsUsersMaster constructUserObj(AddNewUserDto dto, UserMetaInfo metaInfo){
		RlmsUsersMaster user = new RlmsUsersMaster();
		user.setActiveFlag(RLMSConstants.ACTIVE.getId());
		user.setAddress(dto.getAddress());
		user.setContactNumber(dto.getContactNumber());
		user.setEmailId(dto.getEmailId());
		user.setEnabled(RLMSConstants.ACTIVE.getId());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setCity(dto.getCity());
		user.setArea(dto.getArea());
		user.setPincode(dto.getPinCode());
		user.setCreatedBy(metaInfo.getUserId());
		user.setCreatedDate(new Date());
		user.setUpdatedBy(metaInfo.getUserId());
		user.setUpdatedDate(new Date());
		RlmsCompanyMaster companyMaster = this.companyService.getCompanyById(dto.getCompanyId());
		if(null != companyMaster){
			user.setRlmsCompanyMaster(companyMaster);
		}
		return user;
	}
	
	/*@Transactional(propagation = Propagation.REQUIRED)
	public RlmsCompanyRoleMap getTechnicianRoleForBranch(Integer commpBranchMapId){
		return this.userRoleDao.getTechnicianRoleForBranch(commpBranchMapId);
	}*/
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsUserRoles> getAllUserWithRoleForBranch(Integer commpBranchMapId, Integer spocRoleId){
		return this.userRoleDao.getAllUserWithRoleForBranch(commpBranchMapId, spocRoleId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RlmsUserRoles> getListOfTechniciansForBranch(Integer compBranchMapId){
		//RlmsCompanyRoleMap companyRoleMap = this.getTechnicianRoleForBranch(compBranchMapId);
		List<RlmsUserRoles> listOfUserRoles = new ArrayList<RlmsUserRoles>();
		
		listOfUserRoles = this.getAllUserWithRoleForBranch(compBranchMapId, SpocRoleConstants.TECHNICIAN.getSpocRoleId());
		
		return listOfUserRoles;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UserDtlsDto registerTechnicianDeviceByMblNo(UserDtlsDto dto, UserMetaInfo metaInfo) throws ValidationException{
		RlmsUserRoles userRole = this.userRoleDao.getTechnicianRoleObjByMblNo(dto.getContactNumber(), SpocRoleConstants.TECHNICIAN.getSpocRoleId());
		if(null == userRole){
			throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.INVALID_CONTACT_NUMBER.getMessage()));
		}
		this.registerUserDevice(dto, userRole, metaInfo);
		return this.constructMemberDltsSto(userRole);
		
	}
	
	private void registerUserDevice(UserDtlsDto dto, RlmsUserRoles userRole, UserMetaInfo metaInfo){
		RlmsUserApplicationMapDtls userApplicationMapDtls = this.constructUserAppMapDtls(dto, userRole, metaInfo);
		this.userRoleDao.saveUserAppDlts(userApplicationMapDtls);
	}
	
	private RlmsUserApplicationMapDtls constructUserAppMapDtls(UserDtlsDto dto, RlmsUserRoles userRole, UserMetaInfo metaInfo){
		RlmsUserApplicationMapDtls userApplicationMapDtls = new RlmsUserApplicationMapDtls();
		userApplicationMapDtls.setActiveFlag(RLMSConstants.ACTIVE.getId());
		userApplicationMapDtls.setAppRegId(dto.getAppRegId());
		userApplicationMapDtls.setLatitude(dto.getLatitude());
		userApplicationMapDtls.setLongitude(dto.getLongitude());
		userApplicationMapDtls.setAddress(dto.getAddress());
		userApplicationMapDtls.setUserId(userRole.getUserRoleId());
		userApplicationMapDtls.setUserRefType(RLMSConstants.USER_ROLE_TYPE.getId());
		userApplicationMapDtls.setCreatedBy(metaInfo.getUserId());
		userApplicationMapDtls.setCreatedDate(new Date());
		userApplicationMapDtls.setUpdatedDate(new Date());
		userApplicationMapDtls.setUpdatedBy(metaInfo.getUserId());
		return userApplicationMapDtls;
	}
	private UserDtlsDto constructMemberDltsSto(RlmsUserRoles userRole){
		
		UserDtlsDto userDtlsDto = new UserDtlsDto();
		userDtlsDto.setBranchCompanyMapId(userRole.getRlmsCompanyBranchMapDtls().getCompanyBranchMapId());
		userDtlsDto.setBranchName(userRole.getRlmsCompanyBranchMapDtls().getRlmsBranchMaster().getBranchName());
		userDtlsDto.setCompanyName(userRole.getRlmsCompanyMaster().getCompanyName());
		userDtlsDto.setContactNumber(userRole.getRlmsUserMaster().getContactNumber());
		userDtlsDto.setFirstName(userRole.getRlmsUserMaster().getFirstName());
		userDtlsDto.setLastName(userRole.getRlmsUserMaster().getLastName());
		userDtlsDto.setUserId(userRole.getRlmsUserMaster().getUserId());
		userDtlsDto.setUserRoleId(userRole.getUserRoleId());
		return userDtlsDto;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RlmsUserRoles getUserRoleObjhById(Integer userRoleId){
		return this.userRoleDao.getUserRole(userRoleId);
	}
	
	
	
}
