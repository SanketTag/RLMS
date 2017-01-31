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
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.BranchDao;
import com.rlms.dao.CustomerDao;
import com.rlms.dao.LiftDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsBranchCustomerMap;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsCustomerMaster;
import com.rlms.model.RlmsLiftCustomerMap;
import com.rlms.model.RlmsLiftMaster;
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
		List<Integer> listOfApplicableBranchIds = this.companyService.getListOfApplicableBranch(metaInfo.getUserRole().getUserRoleId(), metaInfo);
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
}
