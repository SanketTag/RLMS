package com.rlms.controller;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlms.constants.RlmsErrorType;
import com.rlms.contract.ComplaintsDtlsDto;
import com.rlms.contract.ComplaintsDto;
import com.rlms.contract.LiftDtlsDto;
import com.rlms.contract.ResponseDto;
import com.rlms.contract.UserRoleDtlsDTO;
import com.rlms.exception.ExceptionCode;
import com.rlms.exception.RunTimeException;
import com.rlms.service.ComplaintsService;
import com.rlms.utils.PropertyUtils;

@Controller
@RequestMapping("/complaint")
public class ComplaintController extends BaseController{

	@Autowired
	private ComplaintsService complaintsService;
	
	
	private static final Logger logger = Logger.getLogger(ComplaintController.class);
	
	@RequestMapping(value = "/getListOfComplaints", method = RequestMethod.POST)
	 public  @ResponseBody List<ComplaintsDto> getListOfComplaints(@RequestBody ComplaintsDtlsDto dto) throws RunTimeException{
		 List<ComplaintsDto> listOfComplaints = null;
		 
		 try{
	        	logger.info("Method :: getListOfComplaints");
	        	listOfComplaints = this.complaintsService.getListOfComplaintsBy(dto);
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return listOfComplaints;
	 }
	
	@RequestMapping(value = "/validateAndRegisterNewComplaint", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto validateAndRegisterNewComplaint(@RequestBody ComplaintsDtlsDto dto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
		 
		 try{
	        	logger.info("Method :: validateAndRegisterNewComplaint");
	        	reponseDto.setResponseMessage(this.complaintsService.validateAndRegisterNewComplaint(dto, this.getMetaInfo()));
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	 }
	
	@RequestMapping(value = "/assignComplaint", method = RequestMethod.POST)
	 public @ResponseBody ResponseDto assignComplaint(@RequestBody ComplaintsDto dto) throws RunTimeException{
		 ResponseDto reponseDto = new ResponseDto();
		 
		 try{
	        	logger.info("Method :: assignComplaint");
	        	reponseDto.setResponseMessage(this.complaintsService.assignComplaint(dto, this.getMetaInfo()));
	        	
	        }
	        catch(Exception e){
	        	logger.error(ExceptionUtils.getFullStackTrace(e));
	        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
	        }
	 
	        return reponseDto;
	 }
	
	@RequestMapping(value = "/getAllApplicableLifts", method = RequestMethod.POST)
	public @ResponseBody List<LiftDtlsDto> getAllApplicableLifts(@RequestBody LiftDtlsDto dto) throws RunTimeException{
		List<LiftDtlsDto> listOfLifts = null;
		try{
        	logger.info("Method :: assignComplaint");
        	listOfLifts = this.complaintsService.getAllLiftsForBranchsOrCustomer(dto);
        	
        }
        catch(Exception e){
        	logger.error(ExceptionUtils.getFullStackTrace(e));
        	throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
		return listOfLifts;
	}
	
	@RequestMapping(value = "/getAllTechniciansToAssignComplaint", method = RequestMethod.POST)
	public @ResponseBody List<UserRoleDtlsDTO> getAllTechniciansToAssignComplaint(@RequestBody ComplaintsDtlsDto complaintsDtlsDto){
		List<UserRoleDtlsDTO> listOftechnicians = null;
		try{
        	logger.info("Method :: assignComplaint");
        	listOftechnicians = this.complaintsService.getAllTechniciansToAssignComplaint(complaintsDtlsDto);
        	
        }
        catch(Exception e){
        	logger.error(ExceptionUtils.getFullStackTrace(e));
        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RlmsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
        }
		return listOftechnicians;
	}
	
}
