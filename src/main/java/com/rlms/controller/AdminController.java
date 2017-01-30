package com.rlms.controller;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.AddNewUserDto;
import com.rlms.contract.BranchDtlsDto;
import com.rlms.contract.CompanyDtlsDTO;
import com.rlms.contract.CustomerDtlsDto;
import com.rlms.contract.RegisterDto;
import com.rlms.contract.ResponseDto;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;
import com.rlms.model.RlmsBranchMaster;
import com.rlms.model.RlmsCompanyBranchMapDtls;
import com.rlms.model.RlmsCompanyMaster;
import com.rlms.model.RlmsSpocRoleMaster;
import com.rlms.model.RlmsUsersMaster;
import com.rlms.service.CompanyService;
import com.rlms.service.CustomerService;
import com.rlms.service.UserService;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = Logger.getLogger(AdminController.class);
	
	 @RequestMapping(value = "/addNewCompany", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto addNewCompany(@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException {
	        System.out.println("Adding n ew Company");
	        ResponseDto reponseDto = new ResponseDto();
	        try{
	        	logger.info("In addNewCompany method");
	        	reponseDto.setResponseMessage(this.companyService.validateAndSaveCompanyObj(companyDtlsDTO, this.getMetaInfo()));
	        	
	        }catch(Exception e){
	        	logger.error("In addNewCompany method");	        	
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	    }
	 
	 @RequestMapping(value = "/getApplicableRoles", method = RequestMethod.POST)
	    public @ResponseBody List<RlmsSpocRoleMaster> getApplicableRoles() throws RunTimeException {
	        List<RlmsSpocRoleMaster> listOfAllRoles = null;
	        
	        try{
	        	logger.info("Method :: getAllActiveRoles");
	        	listOfAllRoles =  this.userService.getAllRoles(this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfAllRoles;
	 }
	 
	 @RequestMapping(value = "/getAllUsersForCompany", method = RequestMethod.POST)
	    public @ResponseBody List<RlmsUsersMaster> getAllUsersForCompany(@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException {
	        List<RlmsUsersMaster> listOfAllUsers = null;
	        
	        try{
	        	logger.info("Method :: getAllUsersForCompany");
	        	listOfAllUsers =  this.userService.getAllUsersForCompany(companyDtlsDTO.getCompanyId());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfAllUsers;
	    }
	

	 @RequestMapping(value = "/getAllApplicableCompanies", method = RequestMethod.POST)
	    public @ResponseBody List<RlmsCompanyMaster> getAllApplicableCompanies() throws RunTimeException {
	        List<RlmsCompanyMaster> listOfApplicableCompanies = null;
	        
	        try{
	        	logger.info("Method :: getAllApplicableCompanies");
	        	listOfApplicableCompanies =  this.companyService.getAllCompanies(this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfApplicableCompanies;
	    }
	 
	 @RequestMapping(value = "/assignRole", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto assignRole(@RequestBody UserRoleDtlsDTO userRoleDtlsDTO) throws RunTimeException, ValidationException {
	        
		 	ResponseDto reponseDto = new ResponseDto();
	        try{
	        	logger.info("Method :: assignRole");
	        	reponseDto.setResponseMessage(this.userService.validateAndAssignRole(userRoleDtlsDTO, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));
	        	throw vex;
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	    }
	 
	 @RequestMapping(value = "/getAllBranchesForCompany", method = RequestMethod.POST)
	    public @ResponseBody List<RlmsCompanyBranchMapDtls> getAllBranchesForCompany(@RequestBody CompanyDtlsDTO companyDtlsDTO) throws RunTimeException {
	        List<RlmsCompanyBranchMapDtls> listOfAllBranches = null;
	        
	        try{
	        	logger.info("Method :: getAllBranchesForCompany");
	        	listOfAllBranches =  this.companyService.getAllBranches(companyDtlsDTO.getCompanyId());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfAllBranches;
	    }
	 
	 @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto registerUser(@RequestBody RegisterDto registerDto) throws RunTimeException {
		 	ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: registerUser");
	        	reponseDto.setResponseMessage(this.userService.registerUser(registerDto));
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	  }
	 
	 @RequestMapping(value = "/validateAndRegisterNewUser", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto validateAndRegisterNewUser(@RequestBody AddNewUserDto dto) throws RunTimeException, ValidationException {
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: registerUser");
	        	reponseDto.setResponseMessage(this.userService.validateAndRegisterNewUser(dto, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));
	        	throw vex;
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	  }
	 
	 
	 @RequestMapping(value = "/validateAndRegisterNewCustomer", method = RequestMethod.POST)
	    public @ResponseBody ResponseDto validateAndRegisterNewCustomer(@RequestBody CustomerDtlsDto dto) throws RunTimeException, ValidationException {
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: validateAndRegisterNewCustomer");
	        	reponseDto.setResponseMessage(this.customerService.validateAndRegisterNewCustomer(dto, this.getMetaInfo()));
	        	
	        }catch(ValidationException vex){
	        	logger.error(ExceptionUtils.getFullStackTrace(vex));
	        	throw vex;
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	  }
	 
	 
	 @RequestMapping(value = "/addNewBranchInCompany", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto addNewBranchInCompany(@RequestBody BranchDtlsDto dto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
	        
	        try{
	        	logger.info("Method :: registerUser");
	        	reponseDto.setResponseMessage(this.companyService.validateAndAddNewBranchInCompany(dto, this.getMetaInfo()));
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	 }
	 
	 @RequestMapping(value = "/getListOfBranchDtls", method = RequestMethod.POST)
	 public @ResponseBody List<BranchDtlsDto> getListOfBranchDtls() throws RunTimeException{
		 List<BranchDtlsDto> listOfBranches = null;
	        
	        try{
	        	logger.info("Method :: registerUser");
	        	listOfBranches = this.companyService.getListOfBranchDtls(this.getMetaInfo());
	        	
	        }catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        	
	        }
	 
	        return listOfBranches;
	 }
}
