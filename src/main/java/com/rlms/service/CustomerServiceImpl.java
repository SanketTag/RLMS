package com.rlms.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rlms.constants.RLMSConstants;
import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.AddNewUserDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.UserMetaInfo;
import com.rlms.dao.BranchDao;
import com.rlms.dao.CustomerDao;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsCustomerMaster;
import com.rlms.model.RlmsUsersMaster;
import com.rlms.utils.PropertyUtils;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private BranchDao branchDao;
	
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
			this.customerDao.saveCustomerM(customerMaster);
			statusMessage = PropertyUtils.getPrpertyFromContext(RlmsErrorType.USER_REG_SUCCESFUL.getMessage());
		}
		return statusMessage;
		
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
}
